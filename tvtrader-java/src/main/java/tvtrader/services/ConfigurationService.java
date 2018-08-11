package tvtrader.services;

import lombok.NonNull;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import tvtrader.model.Configuration;
import tvtrader.model.MailConfiguration;

import java.util.Properties;

/**
 * Service class for the different configuration models.
 *
 * @author Wouter
 */
@Log4j2
@Service
public class ConfigurationService {

	@Setter @NonNull
	private Configuration configuration;

	@Setter @NonNull
	private MailConfiguration mailConfiguration;

	public Properties getMailConfigurationAsProperties() {
		return mailConfiguration.getProperties();
	}

	public void setUnfilledOrdersReplaceFlag(boolean flag) {
		configuration.setRetryOrderFlag(flag);
	}

	public String getExpectedSender() {
		return configuration.getExpectedSender();
	}

	public void setExpectedSender(@NonNull String senderToSet) {
		configuration.setExpectedSender(senderToSet);
	}

	public String getHost() {
		return mailConfiguration.getHost();
	}

	public void setHost(@NonNull String host) {
		mailConfiguration.setHost(host);
	}

	public String getProtocol() {
		return mailConfiguration.getProtocol();
	}

	public void setProtocol(@NonNull String protocol) {
		mailConfiguration.setProtocol(protocol);
	}

	public String getInbox() {
		return mailConfiguration.getInbox();
	}

	public void setInbox(@NonNull String inbox) {
		mailConfiguration.setInbox(inbox);
	}

	public String getUsername() {
		return mailConfiguration.getUsername();
	}

	public void setUsername(@NonNull String username) {
		mailConfiguration.setUsername(username);
	}

	public String getPassword() {
		return mailConfiguration.getPassword();
	}

	public void setPassword(@NonNull String password) {
		mailConfiguration.setPassword(password);
	}

	public int getPort() {
		return mailConfiguration.getPort();
	}

	public void setPort(int port) {
		mailConfiguration.setPort(port);
	}

	public int getMailPollingInterval() {
		return configuration.getMailPollingInterval();
	}

	public void setMailPollingInterval(int interval) {
		configuration.setMailPollingInterval(interval);
	}

	public int getStoplossInterval() { return configuration.getStoplossInterval(); }

	public void setStoplossInterval(int interval) {
		configuration.setStoplossInterval(interval);
	}

	public int getOpenOrdersInterval() {
		return configuration.getOpenOrdersInterval();
	}

	public void setOpenOrdersInterval(int interval) {
		configuration.setOpenOrdersInterval(interval);
	}

	public int getOpenOrdersExpirationTime() {
		return configuration.getOpenOrdersExpirationTime();
	}

	public void setOpenOrdersExpirationTime(int expirationTime) {
		configuration.setOpenOrdersExpirationTime(expirationTime);
	}

	public int getTickerRefreshRate() {
		return configuration.getTickerRefreshRate();
	}

	public void setTickerRefreshRate(int refreshRate) {
		configuration.setTickerRefreshRate(refreshRate);
	}

	public int getAssetRefreshRate() {
		return configuration.getAssetRefreshRate();
	}

	public void setAssetRefreshRate(int refreshRate) {
		configuration.setAssetRefreshRate(refreshRate);
	}

	public boolean getRetryOrderFlag() {
		return configuration.isRetryOrderFlag();
	}

}
