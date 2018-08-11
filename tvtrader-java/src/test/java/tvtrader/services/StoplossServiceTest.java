package tvtrader.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import tvtrader.exchange.ExchangeException;
import tvtrader.model.Account;
import tvtrader.model.ApiCredentials;
import tvtrader.model.StoplossOrder;
import tvtrader.orders.OrderPlacer;
import tvtrader.stoploss.StoplossWatcher;
import tvtrader.stoploss.UnverifiedStoplossWatcherException;

import java.util.*;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
class StoplossServiceTest {
	private static final double MAX_DOUBLE = Double.MAX_VALUE;
	private static final String SUPPORTED_EXCHANGE = "BITTREX";
	private static final String EXCHANGE = "EXCHANGE";
	private static final String ACCOUNT = "ACCOUNT";
	private static final String MAINCOIN = "MAINCOIN";
	private static final String ALTCOIN = "ALTCOIN";
	private static final double STOPLOSS = 10.0;
	private static final double ZERO = 0.0;
	private static final String ALTCOIN_2 = "ALTCOIN_2";
	private static final String KEY = "KEY";
	private static final String SECRET = "SECRET";

	private ApiCredentials credentials;

	@Mock private AccountService accountService;
	@Mock private ExchangeService exchangeService;
	@Mock private StoplossWatcher watcher;
	@Mock private OrderPlacer orderPlacer;
	
	private StoplossService service;
	
	@BeforeEach
	void setup() {
		MockitoAnnotations.initMocks(this);

		credentials = new ApiCredentials(KEY, SECRET);
		service = new StoplossService(accountService, exchangeService, watcher, orderPlacer);
	}
	
	@Test
	void addStoplossProtection_whenOrder_shouldAddStoplossProtection() throws Exception {
		when(accountService.getStoploss(EXCHANGE, ACCOUNT)).thenReturn(STOPLOSS);
		
		StoplossOrder order = new StoplossOrder(EXCHANGE, ACCOUNT, ALTCOIN);
		boolean added = service.addStoplossProtection(order);
		
		assertTrue(added);
	}
	
	@Test
	void addStoplossProtection_whenDuplicateOrder_shouldNotAddStoplossProtection() throws Exception {
		when(accountService.getStoploss(EXCHANGE, ACCOUNT)).thenReturn(STOPLOSS);
		
		StoplossOrder order = new StoplossOrder(EXCHANGE, ACCOUNT, ALTCOIN);
		
		assertTrue(service.addStoplossProtection(order));
		assertFalse(service.addStoplossProtection(order));
	}
	
	@Test
	void addStoplossProtection_whenAccountHasZeroStoploss_shouldNotAddStoplossProtection() throws Exception {
		when(accountService.getStoploss(EXCHANGE, ACCOUNT)).thenReturn(ZERO);
		
		StoplossOrder order = new StoplossOrder(EXCHANGE, ACCOUNT, ALTCOIN);
		
		assertFalse(service.addStoplossProtection(order));
	}
	
	@Test
	void startStoplossProtection_whenExchangeExceptionIsThrown_shouldReturnFalse() throws Exception {
		when(accountService.getStoploss(EXCHANGE, ACCOUNT)).thenThrow(ExchangeException.class);
		
		StoplossOrder order = new StoplossOrder(EXCHANGE, ACCOUNT, ALTCOIN);
		
		assertFalse(service.addStoplossProtection(order));
	}
	
	@Test
	void stopStoplossProtection_whenHasOrder_shouldRemoveOrder() throws Exception {
		when(accountService.getStoploss(EXCHANGE, ACCOUNT)).thenReturn(STOPLOSS);
		
		StoplossOrder order = new StoplossOrder(EXCHANGE, ACCOUNT, ALTCOIN);
		
		boolean added = service.addStoplossProtection(order);
		assertTrue(added);
		
		boolean removed = service.stopStoplossProtection(order);
		assertTrue(removed);
	}
	
	@Test
	void stopStoplossProtection_whenUnknownOrder_shouldReturnFalse() throws Exception {
		when(accountService.getStoploss(EXCHANGE, ACCOUNT)).thenReturn(STOPLOSS);
		
		StoplossOrder order = new StoplossOrder(EXCHANGE, ACCOUNT, ALTCOIN);
		
		boolean removed = service.stopStoplossProtection(order);
		assertFalse(removed);
	}
	
	@Test
	void startStoplossProtection_whenAccountHasPositiveStoploss_shouldStartStoplossProtectionForAccount() throws Exception {
		Account account = new Account(EXCHANGE, ACCOUNT, MAINCOIN, 0, STOPLOSS, 0, 0, credentials);
		List<Account> accounts = Collections.singletonList(account);
		when(accountService.getAccounts(ArgumentMatchers.notNull())).thenReturn(accounts.iterator());
		
		Map<String, Double> altcoins = new HashMap<>();
		altcoins.put(ALTCOIN, 1.0);

		Iterator<Entry<String, Double>> balances = altcoins.entrySet().iterator();
		when(exchangeService.getBalances(ArgumentMatchers.notNull(), ArgumentMatchers.notNull())).thenReturn(balances);
		when(exchangeService.getMinimumOrderAmount(SUPPORTED_EXCHANGE)).thenReturn(ZERO);
		
		when(exchangeService.getBid(SUPPORTED_EXCHANGE, MAINCOIN, ALTCOIN)).thenReturn(2.0);
		when(accountService.getStoploss(SUPPORTED_EXCHANGE, ACCOUNT)).thenReturn(STOPLOSS);
		
		StoplossOrder order = new StoplossOrder(SUPPORTED_EXCHANGE, ACCOUNT, ALTCOIN);
		
		service.startStoplossProtection();
		
		assertTrue(service.hasProtection(order));
	}
	
	@Test
	void startStoplossProtection_whenAccountHasPositiveStoploss_shouldNotStartStoplossProtectionForMainCurrency() throws Exception {
		Account account = new Account(EXCHANGE, ACCOUNT, MAINCOIN, 0, STOPLOSS, 0, 0, credentials);
		List<Account> accounts = Collections.singletonList(account);
		when(accountService.getAccounts(ArgumentMatchers.notNull())).thenReturn(accounts.iterator());
		
		Map<String, Double> altcoins = new HashMap<>();
		altcoins.put(MAINCOIN, 1.0);

		Iterator<Entry<String, Double>> balances = altcoins.entrySet().iterator();
		when(exchangeService.getBalances(ArgumentMatchers.notNull(), ArgumentMatchers.notNull())).thenReturn(balances);
		when(exchangeService.getMinimumOrderAmount(SUPPORTED_EXCHANGE)).thenReturn(ZERO);
		
		when(exchangeService.getBid(SUPPORTED_EXCHANGE, MAINCOIN, ALTCOIN)).thenReturn(2.0);
		when(accountService.getStoploss(SUPPORTED_EXCHANGE, ACCOUNT)).thenReturn(STOPLOSS);
		
		StoplossOrder order = new StoplossOrder(SUPPORTED_EXCHANGE, ACCOUNT, ALTCOIN);
		
		service.startStoplossProtection();
		
		assertFalse(service.hasProtection(order));
	}
	
	@Test
	void startStoplossProtection_whenStoplossPriceIsBelowMinimumOrderAmount_shouldNotStartStoplossProtection() throws Exception {
		Account account = new Account(EXCHANGE, ACCOUNT, MAINCOIN, 0, STOPLOSS, 0, 0, credentials);
		List<Account> accounts = Collections.singletonList(account);
		when(accountService.getAccounts(ArgumentMatchers.notNull())).thenReturn(accounts.iterator());
		
		Map<String, Double> altcoins = new HashMap<>();
		altcoins.put(ALTCOIN, 1.0);

		Iterator<Entry<String, Double>> balances = altcoins.entrySet().iterator();
		when(exchangeService.getBalances(ArgumentMatchers.notNull(), ArgumentMatchers.notNull())).thenReturn(balances);
		when(exchangeService.getMinimumOrderAmount(SUPPORTED_EXCHANGE)).thenReturn(MAX_DOUBLE);
		
		when(exchangeService.getBid(SUPPORTED_EXCHANGE, MAINCOIN, ALTCOIN)).thenReturn(2.0);
		when(accountService.getStoploss(SUPPORTED_EXCHANGE, ACCOUNT)).thenReturn(STOPLOSS);
		
		StoplossOrder order = new StoplossOrder(SUPPORTED_EXCHANGE, ACCOUNT, ALTCOIN);
		
		service.startStoplossProtection();
		
		assertFalse(service.hasProtection(order));
	}
	
	@Test
	void startStoplossProtection_whenNoAltcoinBalanceIsPresent_shouldNotStartStoplossProtectionForAccount() throws Exception {
		Account account = new Account(EXCHANGE, ACCOUNT, MAINCOIN, 0, STOPLOSS, 0, 0, credentials);
		List<Account> accounts = Collections.singletonList(account);
		when(accountService.getAccounts(ArgumentMatchers.notNull())).thenReturn(accounts.iterator());
		
		Map<String, Double> altcoins = new HashMap<>();
		altcoins.put(ALTCOIN, ZERO);

		Iterator<Entry<String, Double>> balances = altcoins.entrySet().iterator();
		when(exchangeService.getBalances(ArgumentMatchers.notNull(), ArgumentMatchers.notNull())).thenReturn(balances);
		when(exchangeService.getMinimumOrderAmount(SUPPORTED_EXCHANGE)).thenReturn(ZERO);
		
		when(exchangeService.getBid(SUPPORTED_EXCHANGE, MAINCOIN, ALTCOIN)).thenReturn(2.0);
		when(accountService.getStoploss(SUPPORTED_EXCHANGE, ACCOUNT)).thenReturn(STOPLOSS);
		
		StoplossOrder order = new StoplossOrder(SUPPORTED_EXCHANGE, ACCOUNT, ALTCOIN);
		
		service.startStoplossProtection();
		
		assertFalse(service.hasProtection(order));
	}
	
	@Test
	void startStoplossProtection_whenExchangeExceptionIsThrown_shouldNotStartStoplossProtectionForThatMarket() throws Exception {
		Account account = new Account(EXCHANGE, ACCOUNT, MAINCOIN, 0, STOPLOSS, 0, 0, credentials);
		List<Account> accounts = Collections.singletonList(account);
		when(accountService.getAccounts(ArgumentMatchers.notNull())).thenReturn(accounts.iterator());
		
		Map<String, Double> altcoins = new HashMap<>();
		altcoins.put(ALTCOIN, ZERO);

		Iterator<Entry<String, Double>> balances = altcoins.entrySet().iterator();
		when(exchangeService.getBalances(ArgumentMatchers.notNull(), ArgumentMatchers.notNull())).thenReturn(balances);
		when(exchangeService.getMinimumOrderAmount(SUPPORTED_EXCHANGE)).thenReturn(ZERO);
		
		when(exchangeService.getBid(SUPPORTED_EXCHANGE, MAINCOIN, ALTCOIN)).thenReturn(2.0);
		when(accountService.getStoploss(SUPPORTED_EXCHANGE, ACCOUNT)).thenThrow(ExchangeException.class);
		
		StoplossOrder order = new StoplossOrder(SUPPORTED_EXCHANGE, ACCOUNT, ALTCOIN);
		
		service.startStoplossProtection();
		
		assertFalse(service.hasProtection(order));
	}
	
	@Test
	void startStoplossProtection_whenExchangeExceptionIsThrown_shouldStillStartStoplossProtectionForOtherMarkets() throws Exception {
		Account account = new Account(EXCHANGE, ACCOUNT, MAINCOIN, 0, STOPLOSS, 0, 0, credentials);
		List<Account> accounts = Collections.singletonList(account);
		when(accountService.getAccounts(ArgumentMatchers.notNull())).thenReturn(accounts.iterator());
		
		Map<String, Double> altcoins = new HashMap<>();
		altcoins.put(ALTCOIN, 1.0);
		altcoins.put(ALTCOIN_2, 1.0);

		Iterator<Entry<String, Double>> balances = altcoins.entrySet().iterator();
		when(exchangeService.getBalances(ArgumentMatchers.notNull(), ArgumentMatchers.notNull())).thenReturn(balances);
		when(exchangeService.getMinimumOrderAmount(SUPPORTED_EXCHANGE)).thenReturn(ZERO);
		
		when(exchangeService.getBid(SUPPORTED_EXCHANGE, MAINCOIN, ALTCOIN)).thenThrow(ExchangeException.class);
		when(exchangeService.getBid(SUPPORTED_EXCHANGE, MAINCOIN, ALTCOIN_2)).thenReturn(2.0);
		when(accountService.getStoploss(SUPPORTED_EXCHANGE, ACCOUNT)).thenReturn(STOPLOSS);
		
		StoplossOrder order = new StoplossOrder(SUPPORTED_EXCHANGE, ACCOUNT, ALTCOIN);
		StoplossOrder order_2 = new StoplossOrder(SUPPORTED_EXCHANGE, ACCOUNT, ALTCOIN_2);
		
		service.startStoplossProtection();
		
		assertFalse(service.hasProtection(order));
		assertTrue(service.hasProtection(order_2));
	}
	
	@Test
	void checkStoploss_whenStoplossTriggered_shouldRemoveWatcher() throws Exception {
		StoplossOrder order = new StoplossOrder(SUPPORTED_EXCHANGE, ACCOUNT, ALTCOIN);
		
		when(watcher.checkStoploss(order)).thenReturn(true);
		
		List<StoplossOrder> orders = new ArrayList<>();
		orders.add(order);
		service.addWatchers(orders);
		
		service.checkStoploss();
		
		verify(watcher).checkStoploss(order);
		assertFalse(service.hasProtection(order));
	}
	
	@Test
	void checkStoploss_whenUnverifiedStoplossWatcherExceptionIsThrown_shouldRemoveWatcher() throws Exception {
		StoplossOrder order = new StoplossOrder(SUPPORTED_EXCHANGE, ACCOUNT, ALTCOIN);
		
		when(watcher.checkStoploss(order)).thenThrow(UnverifiedStoplossWatcherException.class);
		
		List<StoplossOrder> orders = new ArrayList<>();
		orders.add(order);
		service.addWatchers(orders);
		
		service.checkStoploss();
		
		verify(watcher).checkStoploss(order);
		assertFalse(service.hasProtection(order));
	}
	
}
