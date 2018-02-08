package tvtrader.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;
import tvtrader.accounts.Account;
import tvtrader.exceptionlogger.UnverifiedException;
import tvtrader.mail.MailClient;
import tvtrader.model.Configuration;
import tvtrader.model.MailConfiguration;

@Log4j2
@Component
public class InputController {
	private static final String INVALID_INTERVAL_MESSAGE = "Interval has to be > 0!";

	@Autowired
	private Configuration configuration;

	@Autowired
	private MailClient mailClient;


	/**
	 * Sets the expected sender for the mailclient.
	 */
	public void setExpectedSender(String senderToSet) {
		if (senderToSet != null) {
			configuration.setExpectedSender(senderToSet);
		} else {
			log.info("Couldn't set expected sender. Defaulting to <noreply@tradingview.com>.");
		}
	}

	/**
	 * Initializes the mail client with the provided configuration.
	 * 
	 */
	public void setMailConfiguration(MailConfiguration config) throws UnverifiedException {
		configuration.setMailConfig(config);
		mailClient.verify();
	}

	/**
	 * Sets the interval for polling the mail server.<br>
	 * 
	 * @throws IllegalArgumentException
	 *             If the provided interval is <= 0.
	 */
	public void setMailPollingInterval(int interval) {
		if (interval > 0) {
			configuration.setMailPollingInterval(interval);
		} else {
			throw new IllegalArgumentException(INVALID_INTERVAL_MESSAGE);
		}
	}

	/**
	 * Sets the interval for checking stoploss conditions.<br>
	 * 
	 * @throws IllegalArgumentException
	 *             If the provided interval is <= 0.
	 */
	public void setStoplossInterval(int interval) {
		if (interval > 0) {
			configuration.setStoplossInterval(interval);
		} else {
			throw new IllegalArgumentException(INVALID_INTERVAL_MESSAGE);
		}
	}

	/**
	 * Sets the interval for checking open orders..<br>
	 * 
	 * @throws IllegalArgumentException
	 *             If the provided interval is <= 0.
	 */
	public void setOpenOrdersInterval(int interval) {
		if (interval > 0) {
			configuration.setOpenOrdersInterval(interval);
		} else {
			throw new IllegalArgumentException(INVALID_INTERVAL_MESSAGE);
		}
	}

	/**
	 * Sets the open orders expiration time.
	 * 
	 */
	public void setOpenOrdersExpirationTime(int expirationTime) {
		if (expirationTime > 0) {
			configuration.setOpenOrdersExpirationTime(expirationTime);
		} else {
			throw new IllegalArgumentException("Expiration time has to be > 0!");
		}
	}

	/**
	 * Boolean flag. Indicating if we should replace cancelled orders.<br>
	 * 
	 * @param replace
	 */
	public void setCancelledOrderReplaceFlag(boolean replace) {
		configuration.setUnfilledOrdersReplaceFlag(replace);
	}

	/**
	 * Sets the ticker refresh rate for all exchanges.
	 * 
	 */
	public void setTickerRefreshRate(int refreshRate) {
		if (refreshRate > 0) {
			configuration.setTickerRefreshRate(refreshRate);
		} else {
			throw new IllegalArgumentException("Refresh rate has to be > 0!");
		}
	}

	/**
	 * Sets the asset refresh rate for all exchanges.<br>
	 */
	public void setAssetRefreshRate(int refreshRate) {
		if (refreshRate > 0) {
			configuration.setAssetRefreshRate(refreshRate);
		} else {
			throw new IllegalArgumentException("Refresh rate has to be > 0!");
		}
	}
	
	public void setUnfilledOrdersReplaceFlag(boolean flag) {
		configuration.setUnfilledOrdersReplaceFlag(flag);
	}

	/**
	 * Adds the account to the exchange.<br>
	 * 
	 * @throws NullPointerException
	 *             If one of the arguments is null.
	 */
	public void addAccount(String exchangeName, Account account) {
		if (exchangeName == null || account == null) {
			throw new NullPointerException();
		} 
		
		configuration.addAccount(exchangeName, account);
	}

	/**
	 * Removes the account from the exchange.<br>
	 * 
	 * @throws NullPointerException
	 *             If one of the arguments is null.
	 */
	public void removeAccount(String exchangeName, String accountName) {
		if (exchangeName == null || accountName == null) {
			throw new NullPointerException();
		} 
		configuration.deleteAccount(exchangeName, accountName);
	}
}
