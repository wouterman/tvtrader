package tvtrader.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.NonNull;
import tvtrader.exceptionlogger.UnverifiedException;
import tvtrader.mail.MailClient;
import tvtrader.model.Account;
import tvtrader.model.MailConfiguration;
import tvtrader.services.AccountService;
import tvtrader.services.ConfigurationService;

@Component
public class InputController {

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private MailClient mailClient;
	
	@Autowired
	private AccountService accountService;
	
	/**
	 * Sets the expected sender for the mailclient.
	 */
	public void setExpectedSender(@NonNull String senderToSet) {
		configurationService.setExpectedSender(senderToSet);
	}

	/**
	 * Initializes the mail client with the provided configuration.
	 * 
	 */
	public void setMailConfiguration(@NonNull MailConfiguration config) throws UnverifiedException {
		configurationService.setMailConfiguration(config);		
		mailClient.verify();
	}

	/**
	 * Sets the interval for polling the mail server.<br>
	 * 
	 */
	public void setMailPollingInterval(int interval) {
		configurationService.setMailPollingInterval(interval);
	}

	/**
	 * Sets the interval for checking stoploss conditions.<br>
	 * 
	 */
	public void setStoplossInterval(int interval) {
		configurationService.setStoplossInterval(interval);
	}

	/**
	 * Sets the interval for checking open orders.<br>
	 * 
	 */
	public void setOpenOrdersInterval(int interval) {
		configurationService.setOpenOrdersInterval(interval);
	}

	/**
	 * Sets the open orders expiration time.
	 * 
	 */
	public void setOpenOrdersExpirationTime(int expirationTime) {
		configurationService.setOpenOrdersExpirationTime(expirationTime);
	}

	/**
	 * Sets the ticker refresh rate for all exchanges.
	 * 
	 */
	public void setTickerRefreshRate(int refreshRate) {
		configurationService.setTickerRefreshRate(refreshRate);
	}

	/**
	 * Sets the asset refresh rate for all exchanges.<br>
	 */
	public void setAssetRefreshRate(int refreshRate) {
		configurationService.setAssetRefreshRate(refreshRate);
	}

	/**
	 * Boolean flag. Indicating if we should replace cancelled orders.<br>
	 * 
	 */
	public void setUnfilledOrdersReplaceFlag(boolean flag) {
		configurationService.setUnfilledOrdersReplaceFlag(flag);
	}

	/**
	 * Adds the account to the exchange.<br>
	 * 
	 * @throws NullPointerException
	 *             If one of the arguments is null.
	 */
	public void addAccount(@NonNull String exchangeName, @NonNull Account account) {
		accountService.addAccount(exchangeName, account);
	}

	/**
	 * Removes the account from the exchange.<br>
	 * 
	 * @throws NullPointerException
	 *             If one of the arguments is null.
	 */
	public void removeAccount(@NonNull String exchangeName, @NonNull String accountName) {
		accountService.removeAccount(exchangeName, accountName);
	}
}
