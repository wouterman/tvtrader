package tvtrader.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import tvtrader.exchange.Exchange;
import tvtrader.exchange.ExchangeException;
import tvtrader.exchange.ExchangeFactory;
import tvtrader.model.ApiCredentials;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BalanceServiceTest {

	private static final double ALTCOIN_BALANCE = 1.0;
	private static final int NOT_SET = 0;
	private static final int ONE_MINUTE = 60;
	private static final String ALTCOIN = "ALTCOIN";
	private static final String UNKNOWN = "UNKNOWN";

	private static final String ACCOUNT = "ACCOUNT";
	private static final String EXCHANGE = "EXCHANGE";

	@Mock private Exchange exchange;

	@Mock private ConfigurationService configurationService;
	@Mock private AccountService accountService;
	@Mock private ExchangeFactory factory;

	@InjectMocks
	private BalanceService service;

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

		ApiCredentials credentials = new ApiCredentials("", "");
		Mockito.when(factory.getExchange(EXCHANGE)).thenReturn(exchange);
		Mockito.when(accountService.hasAccount(EXCHANGE, ACCOUNT)).thenReturn(true);
		Mockito.when(accountService.getCredentials(EXCHANGE, ACCOUNT)).thenReturn(credentials);
		Mockito.when(exchange.getBalances(credentials)).thenReturn(balances);

		double actual = service.getBalance(EXCHANGE, ACCOUNT, ALTCOIN);

		assertEquals(ALTCOIN_BALANCE, actual);
	}

	@Test
	void getBalance_whenNoRefreshIsNeeded_shouldNotRefreshBalances() throws Exception {
		Map<String, Double> balances = new HashMap<>();
		balances.put(ALTCOIN, ALTCOIN_BALANCE);

		ApiCredentials credentials = new ApiCredentials("", "");
		Mockito.when(factory.getExchange(EXCHANGE)).thenReturn(exchange);
		Mockito.when(accountService.hasAccount(EXCHANGE, ACCOUNT)).thenReturn(true);
		Mockito.when(accountService.getCredentials(EXCHANGE, ACCOUNT)).thenReturn(credentials);
		Mockito.when(exchange.getBalances(credentials)).thenReturn(balances).thenThrow(Exception.class);
		Mockito.when(configurationService.getAssetRefreshRate()).thenReturn(ONE_MINUTE);

		service.getBalance(EXCHANGE, ACCOUNT, ALTCOIN);
		double actual = service.getBalance(EXCHANGE, ACCOUNT, ALTCOIN);

		assertEquals(ALTCOIN_BALANCE, actual);
	}

	@Test
	void getBalance_whenCurrencyIsUnknown_shouldReturnZero() throws Exception {
		Map<String, Double> balances = new HashMap<>();

		ApiCredentials credentials = new ApiCredentials("", "");
		Mockito.when(factory.getExchange(EXCHANGE)).thenReturn(exchange);
		Mockito.when(accountService.hasAccount(EXCHANGE, ACCOUNT)).thenReturn(true);
		Mockito.when(accountService.getCredentials(EXCHANGE, ACCOUNT)).thenReturn(credentials);
		Mockito.when(exchange.getBalances(credentials)).thenReturn(balances);

		double actual = service.getBalance(EXCHANGE, ACCOUNT, UNKNOWN);

		assertEquals(NOT_SET, actual);
	}

}
