package tvtrader.services;

import java.io.Closeable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;
import tvtrader.checkers.OpenOrdersChecker;
import tvtrader.checkers.OrderChecker;
import tvtrader.checkers.StoplossChecker;
import tvtrader.controllers.Listener;
import tvtrader.model.Configuration;
import tvtrader.model.ConfigurationField;
import tvtrader.orders.OrderPlacer;

@Log4j2
@Component
public class CheckerService implements Runnable, Closeable, Listener {
	private int pollingInterval;
	private int stoplossInterval;
	private int openOrdersInterval;
	private boolean running;

	@Autowired
	private OrderChecker orderChecker;
	@Autowired
	private StoplossChecker stoplossChecker;
	@Autowired
	private OpenOrdersChecker openOrdersChecker;
	@Autowired
	private OrderPlacer orderPlacer;

	@Autowired
	private ScheduledExecutorService executorService;

	private ScheduledFuture<?> orderCheckerFuture;
	private ScheduledFuture<?> stoplossCheckerFuture;
	private ScheduledFuture<?> openOrdersCheckerFuture;
	
	public CheckerService(Configuration configuration) {
		configuration.addChangeListener(this);
	}
	
	@Override
	public void run() {
		try {
			running = true;

			stoplossChecker.startStoplossProtection();

			orderCheckerFuture = executorService.scheduleAtFixedRate(orderChecker, 0, pollingInterval,
					TimeUnit.SECONDS);
			stoplossCheckerFuture = executorService.scheduleAtFixedRate(stoplossChecker, 0, stoplossInterval,
					TimeUnit.SECONDS);
			openOrdersCheckerFuture = executorService.scheduleAtFixedRate(openOrdersChecker, 0, openOrdersInterval,
					TimeUnit.SECONDS);
			ScheduledFuture<?> orderPlacerFuture = executorService.scheduleAtFixedRate(orderPlacer, 0, 5,
					TimeUnit.SECONDS);

			// Checkers don't actually return anything. All exceptions get caught by the
			// checkers.
			// Alternative was busy waiting.
			orderCheckerFuture.get();
			stoplossCheckerFuture.get();
			openOrdersCheckerFuture.get();
			orderPlacerFuture.get();

		} catch (Exception e) {
			log.debug("Received the following exception: ", e);
			log.error("Something went wrong:\n{}", e.getMessage());
		}
	}

	/**
	 * Sets the polling interval.
	 * 
	 */
	public void setPollingInterval(int interval) {
		log.info("Setting the polling interval for mails to {} seconds.", interval);
		pollingInterval = interval;

		if (running) {
			if (orderCheckerFuture != null) {
				orderCheckerFuture.cancel(true);
			}

			orderCheckerFuture = executorService.scheduleAtFixedRate(orderChecker, 0, pollingInterval,
					TimeUnit.SECONDS);
		}
	}

	/**
	 * Sets the stoploss interval.
	 */
	public void setStoplossInterval(int interval) {
		log.info("Setting the polling interval for stoploss watching to {} seconds.", interval);
		stoplossInterval = interval;

		if (running) {
			if (stoplossCheckerFuture != null) {
				stoplossCheckerFuture.cancel(true);
			}

			stoplossCheckerFuture = executorService.scheduleAtFixedRate(stoplossChecker, 0, stoplossInterval,
					TimeUnit.SECONDS);
		}
	}

	/**
	 * Sets the open orders interval.
	 */
	public void setOpenOrdersInterval(int interval) {
		log.info("Setting the polling interval for open orders to {} seconds.", interval);
		openOrdersInterval = interval;

		if (running) {
			if (openOrdersCheckerFuture != null) {
				openOrdersCheckerFuture.cancel(true);
			}

			openOrdersCheckerFuture = executorService.scheduleAtFixedRate(openOrdersChecker, 0, openOrdersInterval,
					TimeUnit.SECONDS);
		}
	}

	/**
	 * Shuts down the internal executorservice, effectively stopping the
	 * application.<br>
	 */
	@Override
	public void close() {
		log.info("Shutting down the application.");
		executorService.shutdown();
		running = false;
	}

	@Override
	public void update(ConfigurationField changedField, Configuration configuration) {
		if (changedField == ConfigurationField.MAILPOLLINGINTERVAL) {
			setPollingInterval(configuration.getMailPollingInterval());
		} else if (changedField == ConfigurationField.STOPLOSSINTERVAL) {
			setStoplossInterval(configuration.getStoplossInterval());
		} else if (changedField == ConfigurationField.OPENORDERSINTERVAL) {
			setOpenOrdersInterval(configuration.getOpenOrdersInterval());
		}
	}

}
