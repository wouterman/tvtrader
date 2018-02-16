package tvtrader.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.Getter;
import tvtrader.controllers.Listener;
import tvtrader.services.AccountService;

@Component
public class Configuration {
	private List<Listener> listeners = new ArrayList<>();
	private static final String INVALID_INTERVAL_MESSAGE = "Interval has to be > 0!";

	@Getter
	private MailConfiguration mailConfig;
	@Getter
	private String expectedSender;
	@Getter
	private int mailPollingInterval;
	@Getter
	private int stoplossInterval;
	@Getter
	private int openOrdersInterval;
	@Getter
	private int openOrdersExpirationTime;
	@Getter
	private boolean retryOrderFlag;
	@Getter
	private int tickerRefreshRate;
	@Getter
	private int assetRefreshRate;
	
	@Autowired
	private AccountService accountService;

	/**
	 * Initializes the mail client with the provided configuration.
	 * 
	 */
	public void setMailConfig(MailConfiguration mailConfig) {
		if (this.mailConfig == null || !this.mailConfig.equals(mailConfig)) {
			this.mailConfig = mailConfig;
			notifyListeners(ConfigurationField.MAILCONFIG);
		}
	}

	/**
	 * Sets the expected sender for the mailclient.
	 */
	public void setExpectedSender(String expectedSender) {
		if (this.expectedSender == null || !this.expectedSender.equals(expectedSender)) {
			this.expectedSender = expectedSender;
			notifyListeners(ConfigurationField.EXPECTEDSENDER);
		}
	}

	/**
	 * Sets the interval for polling the mail server.<br>
	 * 
	 * @throws IllegalArgumentException
	 *             If the provided interval is <= 0.
	 */
	public void setMailPollingInterval(int mailPollingInterval) {
		if (mailPollingInterval <= 0) {
			throw new IllegalArgumentException(INVALID_INTERVAL_MESSAGE);
		}

		if (this.mailPollingInterval != mailPollingInterval) {
			this.mailPollingInterval = mailPollingInterval;
			notifyListeners(ConfigurationField.MAILPOLLINGINTERVAL);
		}
	}

	/**
	 * Sets the interval for checking stoploss conditions.<br>
	 * 
	 * @throws IllegalArgumentException
	 *             If the provided interval is <= 0.
	 */
	public void setStoplossInterval(int stoplossInterval) {
		if (stoplossInterval <= 0) {
			throw new IllegalArgumentException(INVALID_INTERVAL_MESSAGE);
		}

		if (this.stoplossInterval != stoplossInterval) {
			this.stoplossInterval = stoplossInterval;
			notifyListeners(ConfigurationField.STOPLOSSINTERVAL);
		}
	}

	/**
	 * Sets the interval for checking open orders..<br>
	 * 
	 * @throws IllegalArgumentException
	 *             If the provided interval is <= 0.
	 */
	public void setOpenOrdersInterval(int openOrdersInterval) {
		if (openOrdersInterval <= 0) {
			throw new IllegalArgumentException(INVALID_INTERVAL_MESSAGE);
		}
		if (this.openOrdersInterval != openOrdersInterval) {
			this.openOrdersInterval = openOrdersInterval;
			notifyListeners(ConfigurationField.OPENORDERSINTERVAL);
		}
	}

	/**
	 * Sets the interval for checking open orders.<br>
	 *
	 * @throws IllegalArgumentException
	 *             If the provided interval is <= 0.
	 */
	public void setOpenOrdersExpirationTime(int openOrdersExpirationTime) {
		if (openOrdersExpirationTime <= 0) {
			throw new IllegalArgumentException("Expiration time has to be > 0!");
		}

		if (this.openOrdersExpirationTime != openOrdersExpirationTime) {
			this.openOrdersExpirationTime = openOrdersExpirationTime;
			notifyListeners(ConfigurationField.OPENORDERSEXPIRATIONTIME);
		}
	}

	/**
	 * Boolean flag. Indicating if we should replace cancelled orders.<br>
	 * 
	 */
	public void setUnfilledOrdersReplaceFlag(boolean retryOrderFlag) {
		if (this.retryOrderFlag != retryOrderFlag) {
			this.retryOrderFlag = retryOrderFlag;
			notifyListeners(ConfigurationField.UNFILLEDORDERSREPLACEFLAG);
		}
	}

	/**
	 * Sets the interval for refreshing the ticker cache.<br>
	 *
	 * @throws IllegalArgumentException
	 *             If the provided interval is <= 0.
	 */
	public void setTickerRefreshRate(int tickerRefreshRate) {
		if (tickerRefreshRate <= 0) {
			throw new IllegalArgumentException(INVALID_INTERVAL_MESSAGE);
		}

		if (this.tickerRefreshRate != tickerRefreshRate) {
			notifyListeners(ConfigurationField.TICKERREFRESHRATE);
			this.tickerRefreshRate = tickerRefreshRate;
		}
	}

	/**
	 * Sets the interval for refreshing the asset cache.<br>
	 *
	 * @throws IllegalArgumentException
	 *             If the provided interval is <= 0.
	 */
	public void setAssetRefreshRate(int assetRefreshRate) {
		if (assetRefreshRate <= 0) {
			throw new IllegalArgumentException(INVALID_INTERVAL_MESSAGE);
		}

		if (this.assetRefreshRate != assetRefreshRate) {
			this.assetRefreshRate = assetRefreshRate;
			notifyListeners(ConfigurationField.ASSETREFRESHRATE);
		}
	}

	/**
	 * Adds the account to the repository.<br>
	 * 
	 */
	public void addAccount(String exchange, Account account) {
		accountService.addAccount(exchange, account);
	}

	/**
	 * Removes the account from the repository.<br>
	 * 
	 */
	public void deleteAccount(String exchange, String accountName) {
		accountService.removeAccount(exchange, accountName);

	}

	private void notifyListeners(ConfigurationField field) {
		for (Listener listener : listeners) {
			listener.update(field, this);
		}
	}

	public void addChangeListener(Listener newListener) {
		listeners.add(newListener);
	}

}
