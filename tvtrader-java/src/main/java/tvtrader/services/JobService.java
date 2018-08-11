package tvtrader.services;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tvtrader.jobs.MailFetchJob;
import tvtrader.jobs.OpenOrdersJob;
import tvtrader.jobs.OrderPlacerJob;
import tvtrader.jobs.StoplossCheckJob;

import java.io.Closeable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Log4j2
@Service
public class JobService implements Closeable {
	private static final int ORDER_PLACER_INTERVAL = 5;
	private int pollingInterval;
	private int stoplossInterval;
	private int openOrdersInterval;
	private boolean running;

	private ScheduledFuture<?> orderCheckerFuture;
	private ScheduledFuture<?> stoplossCheckerFuture;
	private ScheduledFuture<?> openOrdersCheckerFuture;

	private MailFetchJob mailFetchJob;
	private StoplossCheckJob stoplossCheckJob;
	private OpenOrdersJob openOrdersJob;
	private OrderPlacerJob orderPlacerJob;
	private ConfigurationService configurationService;
	private ScheduledExecutorService executorService;

	@Autowired
	public JobService(MailFetchJob mailFetchJob, StoplossCheckJob stoplossCheckJob, OpenOrdersJob openOrdersJob, OrderPlacerJob orderPlacerJob, ConfigurationService configurationService,
	                  ScheduledExecutorService executorService) {
		this.mailFetchJob = mailFetchJob;
		this.stoplossCheckJob = stoplossCheckJob;
		this.openOrdersJob = openOrdersJob;
		this.orderPlacerJob = orderPlacerJob;
		this.configurationService = configurationService;
		this.executorService = executorService;
	}
	
	public void startJobs() {
		try {
			running = true;

			stoplossCheckJob.startStoplossProtection();

			orderCheckerFuture = executorService.scheduleAtFixedRate(mailFetchJob, 0, configurationService.getMailPollingInterval(),
			                                                         TimeUnit.SECONDS);
			stoplossCheckerFuture = executorService.scheduleAtFixedRate(stoplossCheckJob, 0, configurationService.getStoplossInterval(),
					TimeUnit.SECONDS);
			openOrdersCheckerFuture = executorService.scheduleAtFixedRate(openOrdersJob, 0, configurationService.getOpenOrdersInterval(),
					TimeUnit.SECONDS);
			ScheduledFuture<?>  orderPlacerFuture = executorService.scheduleAtFixedRate(orderPlacerJob, 0, ORDER_PLACER_INTERVAL,
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

			orderCheckerFuture = executorService.scheduleAtFixedRate(mailFetchJob, 0, pollingInterval,
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
		executorService.shutdownNow();
		running = false;
	}

}
