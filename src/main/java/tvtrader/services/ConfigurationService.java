package tvtrader.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.NonNull;
import tvtrader.controllers.Listener;
import tvtrader.model.Configuration;
import tvtrader.model.ConfigurationField;
import tvtrader.model.MailConfiguration;
import tvtrader.persistence.ConfigurationDao;

@Component
public class ConfigurationService implements Listener {
	@Autowired
	private Configuration configuration;
	
	@Autowired
	private ConfigurationDao dao;
	
	public ConfigurationService(Configuration configuration) {
		configuration.addChangeListener(this);
	}

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
	public void setMailConfiguration(@NonNull MailConfiguration config) {
		System.out.println(dao.getConfigurationById(1));
		configuration.setMailConfig(config);
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

	@Override
	public void update(ConfigurationField changedField, Configuration configuration) {
		dao.update(configuration);
	}
}
