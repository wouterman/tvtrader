package tvtrader.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.NonNull;
import tvtrader.exceptionlogger.UnverifiedException;
import tvtrader.mail.MailClient;
import tvtrader.model.Account;
import tvtrader.model.Configuration;
import tvtrader.model.MailConfiguration;

@Component
public class ConfigurationController {

	@Autowired
	private Configuration configuration;

	@Autowired
	private MailClient mailClient;
	
	/**
	 * Sets the expected sender for the mailclient.
	 */
	public void setExpectedSender(@NonNull String senderToSet) {
		configuration.setExpectedSender(senderToSet);
	}

	/**
	 * Initializes the mail client with the provided configuration.
	 * 
	 */
	public void setMailConfiguration(@NonNull MailConfiguration config) throws UnverifiedException {
		configuration.setMailConfig(config);		
		mailClient.verify();
	}

	/**
	 * Sets the interval for polling the mail server.<br>
	 * 
	 */
	public void setMailPollingInterval(int interval) {
		configuration.setMailPollingInterval(interval);
	}

	/**
	 * Sets the interval for checking stoploss conditions.<br>
	 * 
	 */
	public void setStoplossInterval(int interval) {
		configuration.setStoplossInterval(interval);
	}

	/**
	 * Sets the interval for checking open orders.<br>
	 * 
	 */
	public void setOpenOrdersInterval(int interval) {
		configuration.setOpenOrdersInterval(interval);
	}

	/**
	 * Sets the open orders expiration time.
	 * 
	 */
	public void setOpenOrdersExpirationTime(int expirationTime) {
		configuration.setOpenOrdersExpirationTime(expirationTime);
	}

	/**
	 * Sets the ticker refresh rate for all exchanges.
	 * 
	 */
	public void setTickerRefreshRate(int refreshRate) {
		configuration.setTickerRefreshRate(refreshRate);
	}

	/**
	 * Sets the asset refresh rate for all exchanges.<br>
	 */
	public void setAssetRefreshRate(int refreshRate) {
		configuration.setAssetRefreshRate(refreshRate);
	}

	/**
	 * Boolean flag. Indicating if we should replace cancelled orders.<br>
	 * 
	 */
	public void setUnfilledOrdersReplaceFlag(boolean flag) {
		configuration.setUnfilledOrdersReplaceFlag(flag);
	}

	/**
	 * Adds the account to the exchange.<br>
	 * 
	 * @throws NullPointerException
	 *             If one of the arguments is null.
	 */
	public void addAccount(@NonNull String exchangeName, @NonNull Account account) {
		configuration.addAccount(exchangeName, account);
	}

	/**
	 * Removes the account from the exchange.<br>
	 * 
	 * @throws NullPointerException
	 *             If one of the arguments is null.
	 */
	public void removeAccount(@NonNull String exchangeName, @NonNull String accountName) {
		configuration.deleteAccount(exchangeName, accountName);
	}
}
