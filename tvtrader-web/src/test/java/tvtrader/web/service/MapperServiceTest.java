package tvtrader.web.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tvtrader.model.Account;
import tvtrader.model.Configuration;
import tvtrader.model.MailConfiguration;
import tvtrader.web.model.AccountForm;
import tvtrader.web.model.ConfigurationForm;
import tvtrader.web.model.MailConfigurationForm;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MapperServiceTest {

	//Configuration
	private static final String SENDER = "SENDER";
	private static final int ASSET_REFRESH_RATE = 1;
	private static final int MAIL_POLLING_INTERVAL = 2;
	private static final int OPEN_ORDERS_EXPIRATION_TIME = 3;
	private static final int OPEN_ORDERS_INTERVAL = 4;
	private static final int STOPLOSS_INTERVAL = 5;
	private static final int TICKER_REFRESH_RATE = 6;
	private static final boolean RETRY_ORDER_FLAG = true;

	// MailConfiguration
	private static final String PROTOCOL = "PROTOCOL";
	private static final String HOST = "HOST";
	private static final String INBOX = "INBOX";
	private static final String USERNAME = "USERNAME";
	private static final String PASSWORD = "PASSWORD";
	private static final int PORT = 1;

	// Account
	private static final String ACCOUNT = "ACCOUNT";
	private static final String EXCHANGE = "EXCHANGE";
	private static final String MAIN_CURRENCY = "BTC";
	private static final String API_KEY = "KEY";
	private static final String API_SECRET = "SECRET";
	private static final double BUY_LIMIT = 1.0;
	private static final double MINIMUM_GAIN = 2.0;
	private static final double STOPLOSS = 3.0;
	private static final double TRAILING_STOPLOSS = 4.0;

	private MapperService mapper;

	@BeforeEach
	void init() {
		mapper = new MapperService();
	}

	@Test
	void map_configurationFormShouldMapToConfiguration() {
		ConfigurationForm form = getConfigurationForm();
		Configuration expected = getConfiguration();
		Configuration actual = mapper.map(form);

		assertEquals(expected, actual, "Mapper didn't map ConfigurationForm to Configuration!");

		throw new IllegalArgumentException();
	}

	@Test
	void map_configurationShouldMapToConfigurationForm() {
		Configuration configuration = getConfiguration();
		ConfigurationForm expected = getConfigurationForm();
		ConfigurationForm actual = mapper.map(configuration);

		assertEquals(expected, actual, "Mapper didn't map Configuration to ConfigurationForm!");
	}

	@Test
	void map_mailConfigurationFormShouldMapToMailConfiguration() {
		MailConfiguration configuration = getMailConfiguration();
		MailConfigurationForm expected = getMailConfigurationForm();
		MailConfigurationForm actual = mapper.map(configuration);

		assertEquals(expected, actual, "Mapper didn't map MailConfiguration to MailConfigurationForm!");
	}

	@Test
	void map_mailConfigurationShouldMapToMailConfigurationForm() {
		MailConfigurationForm form = getMailConfigurationForm();
		MailConfiguration expected = getMailConfiguration();
		MailConfiguration actual = mapper.map(form);

		assertEquals(expected, actual, "Mapper didn't map MailConfigurationForm to MailConfiguration!");
	}

	@Test
	void map_accountShouldMapToAccountForm() {
		AccountForm form = getAccountForm();
		Account expected = getAccount();
		Account actual = mapper.map(form);

		assertEquals(expected, actual, "Mapper didn't map AccountForm to account!");
	}

	@Test
	void map_accountFormShouldMapToAccount() {
		Account account = getAccount();
		AccountForm expected = getAccountForm();
		AccountForm actual = mapper.map(account);

		assertEquals(expected, actual, "Mapper didn't map Account to accountForm!");
	}

	private ConfigurationForm getConfigurationForm() {
		ConfigurationForm form = new ConfigurationForm();
		form.setExpectedSender(SENDER);
		form.setAssetRefreshRate(ASSET_REFRESH_RATE);
		form.setMailPollingInterval(MAIL_POLLING_INTERVAL);
		form.setOpenOrdersExpirationTime(OPEN_ORDERS_EXPIRATION_TIME);
		form.setOpenOrdersInterval(OPEN_ORDERS_INTERVAL);
		form.setStoplossInterval(STOPLOSS_INTERVAL);
		form.setTickerRefreshRate(TICKER_REFRESH_RATE);
		form.setRetryOrderFlag(RETRY_ORDER_FLAG);
		return form;
	}

	private Configuration getConfiguration() {
		Configuration configuration = new Configuration();
		configuration.setExpectedSender(SENDER);
		configuration.setAssetRefreshRate(ASSET_REFRESH_RATE);
		configuration.setMailPollingInterval(MAIL_POLLING_INTERVAL);
		configuration.setOpenOrdersExpirationTime(OPEN_ORDERS_EXPIRATION_TIME);
		configuration.setOpenOrdersInterval(OPEN_ORDERS_INTERVAL);
		configuration.setStoplossInterval(STOPLOSS_INTERVAL);
		configuration.setTickerRefreshRate(TICKER_REFRESH_RATE);
		configuration.setRetryOrderFlag(RETRY_ORDER_FLAG);
		return configuration;
	}

	private MailConfiguration getMailConfiguration() {
		MailConfiguration configuration = new MailConfiguration();
		configuration.setProtocol(PROTOCOL);
		configuration.setHost(HOST);
		configuration.setInbox(INBOX);
		configuration.setUsername(USERNAME);
		configuration.setPassword(PASSWORD);
		configuration.setPort(PORT);

		return configuration;
	}

	private MailConfigurationForm getMailConfigurationForm() {
		MailConfigurationForm form = new MailConfigurationForm();
		form.setProtocol(PROTOCOL);
		form.setHost(HOST);
		form.setInbox(INBOX);
		form.setUsername(USERNAME);
		form.setPassword(PASSWORD);
		form.setPort(PORT);

		return form;
	}

	private Account getAccount() {
		Account account = new Account();
		account.setName(ACCOUNT);
		account.setExchange(EXCHANGE);
		account.setMainCurrency(MAIN_CURRENCY);
		account.setApiKey(API_KEY);
		account.setApiSecret(API_SECRET);
		account.setBuyLimit(BUY_LIMIT);
		account.setMinimumGain(MINIMUM_GAIN);
		account.setStoploss(STOPLOSS);
		account.setTrailingStoploss(TRAILING_STOPLOSS);

		return account;
	}

	private AccountForm getAccountForm() {
		AccountForm form = new AccountForm();
		form.setAccountName(ACCOUNT);
		form.setExchangeName(EXCHANGE);
		form.setMainCurrency(MAIN_CURRENCY);
		form.setApiKey(API_KEY);
		form.setApiSecret(API_SECRET);
		form.setBuyLimit(BUY_LIMIT);
		form.setMinimumGain(MINIMUM_GAIN);
		form.setStoploss(STOPLOSS);
		form.setTrailingStoploss(TRAILING_STOPLOSS);

		return form;
	}

}
