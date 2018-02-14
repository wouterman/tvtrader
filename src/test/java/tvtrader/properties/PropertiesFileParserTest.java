package tvtrader.properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import test.logger.Logger;
import tvtrader.accounts.Account;
import tvtrader.accounts.AccountCreator;
import tvtrader.exceptionlogger.GameBreakerException;
import tvtrader.model.MailConfiguration;
import tvtrader.stubs.MailConfigurationStub;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
class PropertiesFileParserTest {
	private static final String INVALID_CONFIGURATIONS = "invalidConfigurations";
	private PropertiesFileParser configParser;
	
	@Mock private AccountCreator accountCreator;
	
	
	@BeforeAll
	synchronized static void startup() {
		Logger.turnOffLogging();
	}
	
	@BeforeEach
	void setup() {
		MockitoAnnotations.initMocks(this);

		configParser = new PropertiesFileParser(accountCreator);
	}
	
	@Test
	void parseMailConfiguration_whenAllParametersAreProvided_shouldCreateMailConfig() throws Exception {
		Properties properties = MailConfigurationStub.getValidPropertiesStub();
		configParser.load(properties);
		MailConfiguration expected = MailConfigurationStub.getExpectedMailConfiguration();
		
		MailConfiguration actual = configParser.parseMailConfiguration();

		assertEquals(expected, actual, "MailProperties should be equal!");
	}

	@Test
	void parseMailConfiguration_unparseablePortNumber_expectInvalidMailConfigException() {
		Properties properties = MailConfigurationStub.getUnparseableportStub();
		configParser.load(properties);
		
		assertThrows(GameBreakerException.class, () -> configParser.parseMailConfiguration());
	}

	@ParameterizedTest
	@MethodSource(value = { INVALID_CONFIGURATIONS })
	void parseMailConfiguration_whenProvidedWithMissingParameters_shouldThrowInvalidMailConfigException(
			Properties config) {
		configParser.load(config);

		assertThrows(GameBreakerException.class, () -> configParser.parseMailConfiguration());
	}
	
	@SuppressWarnings("unused")
	private static Stream<Properties> invalidConfigurations() {
		return Stream.of(MailConfigurationStub.getMissingHostStub(), 
				MailConfigurationStub.getMissingInboxStub(),
				MailConfigurationStub.getMissingPasswordStub(),
				MailConfigurationStub.getMissingPortStub(),
				MailConfigurationStub.getMissingUsernameStub());
	}
	
	@Test
	void getExpectedSender_whenConfigNotLoaded_shouldThrowGameBreakerException() throws Exception {
		assertThrows(GameBreakerException.class, () -> configParser.getExpectedSender());
	}
	
	@Test
	void getExpectedSender_whenConfigLoaded_shouldReturnExpectedSender() throws Exception {
		Properties config = new Properties();
		config.put("expected_sender", "sender");
		
		configParser.load(config);
		
		String sender = configParser.getExpectedSender();
		
		assertEquals("sender", sender);
	}
	
	@Test
	void getPollingInterval_whenConfigNotLoaded_shouldThrowGameBreakerException() throws Exception {
		assertThrows(GameBreakerException.class, () -> configParser.getPollingInterval());
	}
	
	@Test
	void getPollingInterval_whenConfigLoaded_shouldReturnInterval() throws Exception {
		Properties config = new Properties();
		config.put("mail_polling_interval", "1");
		
		configParser.load(config);
		
		int interval = configParser.getPollingInterval();
		
		assertEquals(1, interval);
	}
	
	@Test
	void getPollingInterval_whenIntervalNaN_shouldSetIntervalToZero() throws Exception {
		Properties config = new Properties();
		config.put("mail_polling_interval", "ABC");
		
		configParser.load(config);
		
		int interval = configParser.getPollingInterval();
		
		assertEquals(0, interval);
	}
	
	@Test
	void getStoplossInterval_whenConfigNotLoaded_shouldThrowGameBreakerException() throws Exception {
		assertThrows(GameBreakerException.class, () -> configParser.getStoplossInterval());
	}
	
	@Test
	void getStoplossInterval_whenConfigLoaded_shouldReturnInterval() throws Exception {
		Properties config = new Properties();
		config.put("stoploss_polling_interval", "1");
		
		configParser.load(config);
		
		int interval = configParser.getStoplossInterval();
		
		assertEquals(1, interval);
	}
	
	@Test
	void getStoplossInterval_whenIntervalNaN_shouldSetIntervalToZero() throws Exception {
		Properties config = new Properties();
		config.put("stoploss_polling_interval", "ABC");
		
		configParser.load(config);
		
		int interval = configParser.getStoplossInterval();
		
		assertEquals(0, interval);
	}
	
	@Test
	void getOpenOrdersInterval_whenConfigNotLoaded_shouldThrowGameBreakerException() throws Exception {
		assertThrows(GameBreakerException.class, () -> configParser.getOpenOrdersInterval());
	}
	
	@Test
	void getOpenOrdersInterval_whenConfigLoaded_shouldReturnInterval() throws Exception {
		Properties config = new Properties();
		config.put("open_orders_polling_interval", "1");
		
		configParser.load(config);
		
		int interval = configParser.getOpenOrdersInterval();
		
		assertEquals(1, interval);
	}
	
	@Test
	void getOpenOrdersInterval_whenIntervalNaN_shouldSetIntervalToZero() throws Exception {
		Properties config = new Properties();
		config.put("open_orders_polling_interval", "ABC");
		
		configParser.load(config);
		
		int interval = configParser.getOpenOrdersInterval();
		
		assertEquals(0, interval);
	}
	
	@Test
	void getOpenOrdersExpirationTime_whenConfigNotLoaded_shouldThrowGameBreakerException() throws Exception {
		assertThrows(GameBreakerException.class, () -> configParser.getOpenOrdersExpirationTime());
	}
	
	@Test
	void getOpenOrdersExpirationTime_whenConfigLoaded_shouldReturnInterval() throws Exception {
		Properties config = new Properties();
		config.put("open_orders_expiration_time", "1");
		
		configParser.load(config);
		
		int interval = configParser.getOpenOrdersExpirationTime();
		
		assertEquals(1, interval);
	}
	
	@Test
	void getOpenOrdersExpirationTime_whenIntervalNaN_shouldSetIntervalToZero() throws Exception {
		Properties config = new Properties();
		config.put("open_orders_expiration_time", "ABC");
		
		configParser.load(config);
		
		int interval = configParser.getOpenOrdersExpirationTime();
		
		assertEquals(0, interval);
	}
	
	@Test
	void getTickerRefreshRate_whenConfigNotLoaded_shouldThrowGameBreakerException() throws Exception {
		assertThrows(GameBreakerException.class, () -> configParser.getTickerRefreshRate());
	}
	
	@Test
	void getTickerRefreshRate_whenConfigLoaded_shouldReturnInterval() throws Exception {
		Properties config = new Properties();
		config.put("ticker_refresh_rate", "1");
		
		configParser.load(config);
		
		int interval = configParser.getTickerRefreshRate();
		
		assertEquals(1, interval);
	}
	
	@Test
	void getTickerRefreshRate_whenIntervalNaN_shouldSetIntervalToZero() throws Exception {
		Properties config = new Properties();
		config.put("ticker_refresh_rate", "ABC");
		
		configParser.load(config);
		
		int interval = configParser.getTickerRefreshRate();
		
		assertEquals(0, interval);
	}
	
	@Test
	void getAssetRefreshRate_whenConfigNotLoaded_shouldThrowGameBreakerException() throws Exception {
		assertThrows(GameBreakerException.class, () -> configParser.getAssetRefreshRate());
	}
	
	@Test
	void getAssetRefreshRate_whenConfigLoaded_shouldReturnInterval() throws Exception {
		Properties config = new Properties();
		config.put("asset_refresh_rate", "1");
		
		configParser.load(config);
		
		int interval = configParser.getAssetRefreshRate();
		
		assertEquals(1, interval);
	}
	
	@Test
	void getAssetRefreshRate_whenIntervalNaN_shouldSetIntervalToZero() throws Exception {
		Properties config = new Properties();
		config.put("asset_refresh_rate", "ABC");
		
		configParser.load(config);
		
		int interval = configParser.getAssetRefreshRate();
		
		assertEquals(0, interval);
	}
	
	@Test
	void getCancelledOrdersFlag_whenConfigNotLoaded_shouldThrowGameBreakerException() throws Exception {
		assertThrows(GameBreakerException.class, () -> configParser.getCancelledOrdersFlag());
	}
	
	@Test
	void getCancelledOrdersFlag_whenConfigLoaded_shouldReturnTrue() throws Exception {
		Properties config = new Properties();
		config.put("retry_cancelled_orders", "true");
		
		configParser.load(config);
		
		boolean flag = configParser.getCancelledOrdersFlag();
		
		assertTrue(flag);
	}
	
	@Test
	void getCancelledOrdersFlag_whenIntervalNaN_shouldSetFlagToFalse() throws Exception {
		Properties config = new Properties();
		config.put("retry_cancelled_orders", "ABC");
		
		configParser.load(config);
		
		boolean flag = configParser.getCancelledOrdersFlag();
		
		assertFalse(flag);
	}
	
	@Test
	void parseAccounts_whenConfigNotLoaded_shouldThrowGameBreakerException() throws Exception {
		assertThrows(GameBreakerException.class, () -> configParser.parseAccounts());
	}
	
	@Test
	void parseAccounts_whenConfigLoaded_shouldCallAccountCreatorWithExchange() throws Exception {
		Properties config = new Properties();
		
		configParser.load(config);
		
		String exchange = "BITTREX";
		Account account = new Account(null, null, 0, 0, 0, 0, null);
		Mockito.when(accountCreator.extractAccounts(exchange, config)).thenReturn(Arrays.asList(account));
		
		Map<String, List<Account>> accounts = configParser.parseAccounts();
		
		assertNotNull(accounts.get(exchange));
	}
}
