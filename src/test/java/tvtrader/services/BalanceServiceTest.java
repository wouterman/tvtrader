package tvtrader.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import test.logger.Logger;
import tvtrader.exchange.Exchange;
import tvtrader.exchange.ExchangeException;
import tvtrader.exchange.ExchangeFactory;
import tvtrader.model.ApiCredentials;
import tvtrader.model.Configuration;
import tvtrader.model.ConfigurationField;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
class BalanceServiceTest {

	private static final double ALTCOIN_BALANCE = 1.0;
	private static final int NOT_SET = 0;
	private static final int ONE_MINUTE = 60;
	private static final String ALTCOIN = "ALTCOIN";
	private static final String UNKNOWN = "UNKNOWN";

	private static final String ACCOUNT = "ACCOUNT";
	private static final String EXCHANGE = "EXCHANGE";

	@Mock
	private Exchange exchange;

	@Mock
	private AccountService accountService;
	@Mock
	private ExchangeFactory factory;
	@Mock
	private Configuration configuration;
	@InjectMocks
	private BalanceService service;

	@BeforeAll
	synchronized static void startup() {
		Logger.turnOffLogging();
	}

	@BeforeEach
	void init() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void getBalance_whenExchangeIsUnknown_shouldThrowExchangeException() throws Exception {
		Mockito.when(factory.getExchange(EXCHANGE)).thenThrow(ExchangeException.class);

		assertThrows(ExchangeException.class, () -> service.getBalance(EXCHANGE, ACCOUNT, ALTCOIN));
	}

	@Test
	void getBalance_whenAccountIsUnknown_shouldThrowExchangeException() throws Exception {
		Mockito.when(accountService.hasAccount(EXCHANGE, ACCOUNT)).thenReturn(false);

		assertThrows(ExchangeException.class, () -> service.getBalance(EXCHANGE, ACCOUNT, ALTCOIN));
	}

	@Test
	void getBalance_whenRefreshIsNeeded_shouldRefreshBalances() throws Exception {
		Map<String, Double> balances = new HashMap<>();
		balances.put(ALTCOIN, ALTCOIN_BALANCE);

		ApiCredentials credentials = new ApiCredentials(null, null);
		Mockito.when(factory.getExchange(EXCHANGE)).thenReturn(exchange);
		Mockito.when(accountService.hasAccount(EXCHANGE, ACCOUNT)).thenReturn(true);
		Mockito.when(accountService.getCredentials(EXCHANGE, ACCOUNT)).thenReturn(credentials);
		Mockito.when(exchange.getBalances(credentials)).thenReturn(balances);

		double actual = service.getBalance(EXCHANGE, ACCOUNT, ALTCOIN);

		assertEquals(ALTCOIN_BALANCE, actual);
	}

	@Test
	void getBalance_whenNoRefreshIsNeeded_shouldNotRefreshBalances() throws Exception {
		service.setAssetRefreshRate(ONE_MINUTE);

		Map<String, Double> balances = new HashMap<>();
		balances.put(ALTCOIN, ALTCOIN_BALANCE);

		ApiCredentials credentials = new ApiCredentials(null, null);
		Mockito.when(factory.getExchange(EXCHANGE)).thenReturn(exchange);
		Mockito.when(accountService.hasAccount(EXCHANGE, ACCOUNT)).thenReturn(true);
		Mockito.when(accountService.getCredentials(EXCHANGE, ACCOUNT)).thenReturn(credentials);
		Mockito.when(exchange.getBalances(credentials)).thenReturn(balances).thenThrow(Exception.class);

		service.getBalance(EXCHANGE, ACCOUNT, ALTCOIN);
		double actual = service.getBalance(EXCHANGE, ACCOUNT, ALTCOIN);

		assertEquals(ALTCOIN_BALANCE, actual);
	}

	@Test
	void getBalance_whenCurrencyIsUnknown_shouldReturnZero() throws Exception {
		Map<String, Double> balances = new HashMap<>();

		ApiCredentials credentials = new ApiCredentials(null, null);
		Mockito.when(factory.getExchange(EXCHANGE)).thenReturn(exchange);
		Mockito.when(accountService.hasAccount(EXCHANGE, ACCOUNT)).thenReturn(true);
		Mockito.when(accountService.getCredentials(EXCHANGE, ACCOUNT)).thenReturn(credentials);
		Mockito.when(exchange.getBalances(credentials)).thenReturn(balances);

		double actual = service.getBalance(EXCHANGE, ACCOUNT, UNKNOWN);

		assertEquals(NOT_SET, actual);
	}

	@Test
	void update_shouldUpdateRefreshRate_whenReceivingUpdate() throws Exception {
		Configuration configuration = new Configuration();
		configuration.setAssetRefreshRate(ONE_MINUTE);

		service.update(ConfigurationField.ASSETREFRESHRATE, configuration);

		assertEquals(ONE_MINUTE, service.getAssetRefreshRate());
	}

	@Test
	void update_shouldNotUpdateRefreshRate_whenReceivingIrrelevantUpdate() throws Exception {
		Configuration configuration = new Configuration();
		configuration.setAssetRefreshRate(ONE_MINUTE);

		service.update(ConfigurationField.EXPECTEDSENDER, configuration);

		assertEquals(NOT_SET, service.getAssetRefreshRate());
	}

}
