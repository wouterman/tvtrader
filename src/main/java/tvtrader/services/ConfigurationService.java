package tvtrader.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import tvtrader.model.Configuration;
import tvtrader.model.FormConfiguration;
import tvtrader.model.MailConfiguration;

@Log4j2
@Component
public class ConfigurationService {
	
	@Autowired
	private Configuration configuration;
	
	@Autowired
	private MailConfiguration mailConfiguration;

	/**
	 * Sets the expected sender for the mailclient.
	 */
	public void setExpectedSender(@NonNull String senderToSet) {
		configuration.setExpectedSender(senderToSet);	
	}

	/**
	 * Sets the host for the mailClient.
	 * 
	 */
	public void setHost(@NonNull String host) {
		mailConfiguration.setHost(host);
	}
	
	/**
	 * Sets the protocol for the mailClient.
	 * 
	 */
	public void setProtocol(@NonNull String protocol) {
		mailConfiguration.setProtocol(protocol);
	}
	
	/**
	 * Sets the inbox for the mailClient.
	 * 
	 */
	public void setInbox(@NonNull String inbox) {
		mailConfiguration.setInbox(inbox);
	}
	
	/**
	 * Sets the username for the mailClient.
	 * 
	 */
	public void setUsername(@NonNull String username) {
		mailConfiguration.setUsername(username);
	}
	
	/**
	 * Sets the password for the mailClient.
	 * 
	 */
	public void setPassword(@NonNull String password) {
		mailConfiguration.setPassword(password);
	}
	
	/**
	 * Sets the portnumber for the mailClient.
	 * 
	 */
	public void setPort(int port) {
		mailConfiguration.setPort(port);
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
	 * Copies the provided formConfiguration into the regular configuration.<br>
	 * 
	 * @param formConfig
	 *            The form configuration to use for copying.
	 */
	public void cloneForm(FormConfiguration formConfig) {
		log.debug("Copying configuration. FormConfig: {}", formConfig);
		
		setExpectedSender(formConfig.getExpectedSender());
		setUnfilledOrdersReplaceFlag(formConfig.isRetryOrderFlag());

		setMailPollingInterval(formConfig.getMailPollingInterval());
		setOpenOrdersInterval(formConfig.getOpenOrdersInterval());
		setOpenOrdersExpirationTime(formConfig.getOpenOrdersExpirationTime());
		setStoplossInterval(formConfig.getStoplossInterval());
		setTickerRefreshRate(formConfig.getTickerRefreshRate());
		setAssetRefreshRate(formConfig.getAssetRefreshRate());
		
		setHost(formConfig.getHost());
		setProtocol(formConfig.getProtocol());
		setInbox(formConfig.getInbox());
		setUsername(formConfig.getUsername());
		setPassword(formConfig.getPassword());
		setPort(formConfig.getPort());
	}

	public void cloneConfig(Configuration storedConfig) {
		log.debug("Copying configuration. storedConfig: {}", storedConfig);
		
		setExpectedSender(storedConfig.getExpectedSender());
		setUnfilledOrdersReplaceFlag(storedConfig.isRetryOrderFlag());

		setMailPollingInterval(storedConfig.getMailPollingInterval());
		setOpenOrdersInterval(storedConfig.getOpenOrdersInterval());
		setOpenOrdersExpirationTime(storedConfig.getOpenOrdersExpirationTime());
		setStoplossInterval(storedConfig.getStoplossInterval());
		setTickerRefreshRate(storedConfig.getTickerRefreshRate());
		setAssetRefreshRate(storedConfig.getAssetRefreshRate());
	}
	
	public void cloneMailConfig(MailConfiguration mailConfig) {
		setHost(mailConfig.getHost());
		setProtocol(mailConfig.getProtocol());
		setInbox(mailConfig.getInbox());
		setUsername(mailConfig.getUsername());
		setPassword(mailConfig.getPassword());
		setPort(mailConfig.getPort());
	}
}
