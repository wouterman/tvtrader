package tvtrader.services;

import java.io.Closeable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;
import tvtrader.controllers.Listener;
import tvtrader.jobs.OpenOrdersJob;
import tvtrader.jobs.OrderCheckJobs;
import tvtrader.jobs.OrderPlacerJob;
import tvtrader.jobs.StoplossCheckJob;
import tvtrader.model.Configuration;
import tvtrader.model.ConfigurationField;

@Log4j2
@Component
public class JobService implements Closeable, Listener {
	private static final int ORDER_PLACER_INTERVAL = 5;
	private int pollingInterval;
	private int stoplossInterval;
	private int openOrdersInterval;
	private boolean running;

	@Autowired
	private OrderCheckJobs orderCheckJob;
	@Autowired
	private StoplossCheckJob stoplossCheckJob;
	@Autowired
	private OpenOrdersJob openOrdersJob;
	@Autowired
	private OrderPlacerJob orderPlacerJob;

	@Autowired
	private ScheduledExecutorService executorService;
	private ScheduledFuture<?> orderCheckerFuture;
	private ScheduledFuture<?> stoplossCheckerFuture;
	private ScheduledFuture<?> openOrdersCheckerFuture;
	
	public JobService(Configuration configuration) {
		configuration.addChangeListener(this);
	}
	
	public void startJobs() {
		try {
			running = true;

			stoplossCheckJob.startStoplossProtection();

			orderCheckerFuture = executorService.scheduleAtFixedRate(orderCheckJob, 0, pollingInterval,
					TimeUnit.SECONDS);
			stoplossCheckerFuture = executorService.scheduleAtFixedRate(stoplossCheckJob, 0, stoplossInterval,
					TimeUnit.SECONDS);
			openOrdersCheckerFuture = executorService.scheduleAtFixedRate(openOrdersJob, 0, openOrdersInterval,
					TimeUnit.SECONDS);
			ScheduledFuture<?> orderPlacerFuture = executorService.scheduleAtFixedRate(orderPlacerJob, 0, ORDER_PLACER_INTERVAL,
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

			orderCheckerFuture = executorService.scheduleAtFixedRate(orderCheckJob, 0, pollingInterval,
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

			stoplossCheckerFuture = executorService.scheduleAtFixedRate(stoplossCheckJob, 0, stoplossInterval,
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

			openOrdersCheckerFuture = executorService.scheduleAtFixedRate(openOrdersJob, 0, openOrdersInterval,
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
