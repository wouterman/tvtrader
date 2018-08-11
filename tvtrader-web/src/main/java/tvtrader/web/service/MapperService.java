package tvtrader.web.service;

import org.springframework.stereotype.Component;
import tvtrader.model.Account;
import tvtrader.model.ApiCredentials;
import tvtrader.model.Configuration;
import tvtrader.model.MailConfiguration;
import tvtrader.web.model.AccountForm;
import tvtrader.web.model.ConfigurationForm;
import tvtrader.web.model.MailConfigurationForm;

/**
 * Maps the frontend forms to the backend models and vice versa.
 *
 */
@Component
public class MapperService {

	public Configuration map(ConfigurationForm form) {
		Configuration configuration = new Configuration();

		configuration.setExpectedSender(form.getExpectedSender());
		configuration.setMailPollingInterval(form.getMailPollingInterval());
		configuration.setStoplossInterval(form.getStoplossInterval());
		configuration.setOpenOrdersInterval(form.getOpenOrdersInterval());
		configuration.setOpenOrdersExpirationTime(form.getOpenOrdersExpirationTime());
		configuration.setRetryOrderFlag(form.isRetryOrderFlag());
		configuration.setTickerRefreshRate(form.getTickerRefreshRate());
		configuration.setAssetRefreshRate(form.getAssetRefreshRate());

		return configuration;
	}

	public ConfigurationForm map(Configuration configuration) {
		ConfigurationForm form = new ConfigurationForm();

		form.setExpectedSender(configuration.getExpectedSender());
		form.setMailPollingInterval(configuration.getMailPollingInterval());
		form.setStoplossInterval(configuration.getStoplossInterval());
		form.setOpenOrdersInterval(configuration.getOpenOrdersInterval());
		form.setOpenOrdersExpirationTime(configuration.getOpenOrdersExpirationTime());
		form.setRetryOrderFlag(configuration.isRetryOrderFlag());
		form.setTickerRefreshRate(configuration.getTickerRefreshRate());
		form.setAssetRefreshRate(configuration.getAssetRefreshRate());

		return form;
	}

	public MailConfiguration map(MailConfigurationForm form) {
		MailConfiguration mailConfiguration = new MailConfiguration();

		mailConfiguration.setProtocol(form.getProtocol());
		mailConfiguration.setHost(form.getHost());
		mailConfiguration.setInbox(form.getInbox());
		mailConfiguration.setPassword(form.getPassword());
		mailConfiguration.setPort(form.getPort());
		mailConfiguration.setUsername(form.getUsername());

		return mailConfiguration;
	}

	public MailConfigurationForm map(MailConfiguration mailConfiguration) {
		MailConfigurationForm form = new MailConfigurationForm();

		form.setProtocol(mailConfiguration.getProtocol());
		form.setHost(mailConfiguration.getHost());
		form.setInbox(mailConfiguration.getInbox());
		form.setPassword(mailConfiguration.getPassword());
		form.setPort(mailConfiguration.getPort());
		form.setUsername(mailConfiguration.getUsername());

		return form;
	}

	public Account map(AccountForm form) {
		String exchangeName = form.getExchangeName();
		String accountName = form.getAccountName();
		String mainCurrency = form.getMainCurrency();
		double buyLimit = form.getBuyLimit();
		double stoploss = form.getStoploss();
		double trailingStoploss = form.getTrailingStoploss();
		double minimumGain = form.getMinimumGain();

		String apiKey = form.getApiKey();
		String apiSecret = form.getApiSecret();

		ApiCredentials credentials = new ApiCredentials(apiKey, apiSecret);

		return new Account(exchangeName, accountName, mainCurrency, buyLimit, stoploss, trailingStoploss, minimumGain, credentials);
	}

	public AccountForm map(Account account) {
		AccountForm form = new AccountForm();

		form.setAccountName(account.getName());
		form.setApiKey(account.getCredentials().getKey());
		form.setApiSecret(account.getCredentials().getSecret());
		form.setBuyLimit(account.getBuyLimit());
		form.setExchangeName(account.getExchange());
		form.setMainCurrency(account.getMainCurrency());
		form.setMinimumGain(account.getMinimumGain());
		form.setStoploss(account.getStoploss());
		form.setTrailingStoploss(account.getTrailingStoploss());

		return form;
	}
}
