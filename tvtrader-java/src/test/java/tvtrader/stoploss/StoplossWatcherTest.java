package tvtrader.stoploss;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import tvtrader.exchange.ExchangeException;
import tvtrader.model.StoplossOrder;
import tvtrader.orders.OrderPlacer;
import tvtrader.services.AccountService;
import tvtrader.services.ExchangeService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
class StoplossWatcherTest {
	private static final double FEE = 0.01;
	private static final String EXCHANGE = "EXCHANGE";
	private static final String ALTCOIN = "ALTCOIN";
	private static final String MAINCOIN = "BTC";
	private static final String ACCOUNT = "ACCOUNT";
	private static final double STOPLOSS = 10;
	private static final double TSSL = 5;
	private static final double BALANCE = 1;
	private static final double EMPTY_BALANCE = 0;

	@Mock
	private AccountService accountService;

	@Mock
	private ExchangeService exchangeService;
	
	@Mock private OrderPlacer orderPlacer;

	@InjectMocks
	private StoplossWatcher watcher;

	@BeforeAll
	synchronized static void startup() {

	}

	@BeforeEach
	void setup() throws ExchangeException {
		MockitoAnnotations.initMocks(this);

		when(accountService.getMainCurrency(EXCHANGE, ACCOUNT)).thenReturn(MAINCOIN);
		when(accountService.getStoploss(EXCHANGE, ACCOUNT)).thenReturn(STOPLOSS);
		when(accountService.getTrailingStoploss(EXCHANGE, ACCOUNT)).thenReturn(TSSL);

		when(exchangeService.getTakerFee(EXCHANGE)).thenReturn(FEE);
	}

	@Test
	void checkStoploss_whenNotVerified_shouldReturnFalse() throws Exception {
		StoplossOrder order = new StoplossOrder(EXCHANGE, ACCOUNT, ALTCOIN);

		when(exchangeService.getBalance(EXCHANGE, ACCOUNT, ALTCOIN)).thenReturn(BALANCE);
		when(exchangeService.getBoughtPrice(EXCHANGE, ACCOUNT, ALTCOIN, BALANCE)).thenReturn(0.0);

		boolean verified = watcher.checkStoploss(order);

		assertFalse(verified);
	}

	@Test
	void checkStoploss_whenNotVerifiedAfterTenTimes_shouldThrowUnverifiedStoplossWatcherException() throws Exception {
		StoplossOrder order = new StoplossOrder(EXCHANGE, ACCOUNT, ALTCOIN);
		for (int i = 0; i < 10; i++) {
			order.incrementVerifyCounter();
		}

		assertThrows(UnverifiedStoplossWatcherException.class, () -> watcher.checkStoploss(order));
	}

	@Test
	void checkStoploss_whenAltCoinBalanceIsZero_shouldReturnTrue() throws Exception {
		StoplossOrder order = new StoplossOrder(EXCHANGE, ACCOUNT, ALTCOIN);

		when(exchangeService.getBalance(EXCHANGE, ACCOUNT, ALTCOIN)).thenReturn(BALANCE).thenReturn(EMPTY_BALANCE);
		when(exchangeService.getBoughtPrice(EXCHANGE, ACCOUNT, ALTCOIN, BALANCE)).thenReturn(1.0);

		assertFalse(watcher.checkStoploss(order));

		boolean triggered = watcher.checkStoploss(order);

		assertTrue(triggered);
	}

	@Test
	void checkStoploss_whenBoughtPriceIsZeroAfterVerifying_shouldNotTriggerStoploss() throws Exception {
		StoplossOrder order = new StoplossOrder(EXCHANGE, ACCOUNT, ALTCOIN);

		when(exchangeService.getBalance(EXCHANGE, ACCOUNT, ALTCOIN)).thenReturn(BALANCE);
		when(exchangeService.getBoughtPrice(EXCHANGE, ACCOUNT, ALTCOIN, BALANCE)).thenReturn(1.0).thenReturn(EMPTY_BALANCE);

		when(exchangeService.getBid(EXCHANGE, MAINCOIN, ALTCOIN)).thenReturn(2.5);

		assertFalse(watcher.checkStoploss(order));

		boolean triggered = watcher.checkStoploss(order);

		assertFalse(triggered);
	}

	@Test
	void checkStoploss_whenStoplossIsTriggered_shouldReturnTrue() throws Exception {
		StoplossOrder order = new StoplossOrder(EXCHANGE, ACCOUNT, ALTCOIN);

		when(exchangeService.getBalance(EXCHANGE, ACCOUNT, ALTCOIN)).thenReturn(BALANCE);
		when(exchangeService.getBoughtPrice(EXCHANGE, ACCOUNT, ALTCOIN, BALANCE)).thenReturn(1.0);

		when(exchangeService.getBid(EXCHANGE, MAINCOIN, ALTCOIN)).thenReturn(0.89);

		assertFalse(watcher.checkStoploss(order));

		boolean triggered = watcher.checkStoploss(order);

		assertTrue(triggered);
	}

	@Test
	void checkStoploss_whenPriceIsAboveStoplossThreshold_shouldSwitchToTrailingStoploss() throws Exception {
		StoplossOrder order = new StoplossOrder(EXCHANGE, ACCOUNT, ALTCOIN);

		when(exchangeService.getBalance(EXCHANGE, ACCOUNT, ALTCOIN)).thenReturn(BALANCE);
		when(exchangeService.getBoughtPrice(EXCHANGE, ACCOUNT, ALTCOIN, BALANCE)).thenReturn(1.0);

		when(exchangeService.getBid(EXCHANGE, MAINCOIN, ALTCOIN)).thenReturn(1.0).thenReturn(2.0).thenReturn(1.80);

		assertFalse(watcher.checkStoploss(order));
		assertFalse(watcher.checkStoploss(order));
		assertFalse(watcher.checkStoploss(order));

		boolean triggered = watcher.checkStoploss(order);

		assertTrue(triggered);
	}

	@Test
	void checkStoploss_whenPriceDropsBelowStoplossThreshold_shouldSwitchBackToStoploss() throws Exception {
		StoplossOrder order = new StoplossOrder(EXCHANGE, ACCOUNT, ALTCOIN);

		when(exchangeService.getBalance(EXCHANGE, ACCOUNT, ALTCOIN)).thenReturn(BALANCE);
		when(exchangeService.getBoughtPrice(EXCHANGE, ACCOUNT, ALTCOIN, BALANCE)).thenReturn(1.0);

		when(exchangeService.getBid(EXCHANGE, MAINCOIN, ALTCOIN)).thenReturn(1.0).thenReturn(2.0).thenReturn(0.95);

		assertFalse(watcher.checkStoploss(order));
		assertFalse(watcher.checkStoploss(order));
		assertFalse(watcher.checkStoploss(order));

		boolean triggered = watcher.checkStoploss(order);

		assertFalse(triggered);
	}

	@Test
	void checkStoploss_whenPriceRises_shouldFollowThatPriceForStoploss() throws Exception {
		StoplossOrder order = new StoplossOrder(EXCHANGE, ACCOUNT, ALTCOIN);

		when(exchangeService.getBalance(EXCHANGE, ACCOUNT, ALTCOIN)).thenReturn(BALANCE);
		when(exchangeService.getBoughtPrice(EXCHANGE, ACCOUNT, ALTCOIN, BALANCE)).thenReturn(1.0);

		when(exchangeService.getBid(EXCHANGE, MAINCOIN, ALTCOIN)).thenReturn(2.5).thenReturn(2.3);

		assertFalse(watcher.checkStoploss(order));

		boolean triggered = watcher.checkStoploss(order);

		assertFalse(triggered);
	}

	@Test
	void checkStoploss_whenPriceRises_shouldTriggerOnTrailingStoploss() throws Exception {
		StoplossOrder order = new StoplossOrder(EXCHANGE, ACCOUNT, ALTCOIN);

		when(exchangeService.getBalance(EXCHANGE, ACCOUNT, ALTCOIN)).thenReturn(BALANCE);
		when(exchangeService.getBoughtPrice(EXCHANGE, ACCOUNT, ALTCOIN, BALANCE)).thenReturn(1.0);

		when(exchangeService.getBid(EXCHANGE, MAINCOIN, ALTCOIN)).thenReturn(2.5).thenReturn(2.49).thenReturn(2.0);

		assertFalse(watcher.checkStoploss(order));
		assertFalse(watcher.checkStoploss(order));
		assertFalse(watcher.checkStoploss(order));

		boolean triggered = watcher.checkStoploss(order);

		assertTrue(triggered);
	}

	@Test
	void checkStoploss_whenTickerIsInvalid_shouldReturnFalse() throws Exception {
		StoplossOrder order = new StoplossOrder(EXCHANGE, ACCOUNT, ALTCOIN);

		when(exchangeService.getBalance(EXCHANGE, ACCOUNT, ALTCOIN)).thenReturn(BALANCE);
		when(exchangeService.getBoughtPrice(EXCHANGE, ACCOUNT, ALTCOIN, BALANCE)).thenReturn(1.0);

		when(exchangeService.getBid(EXCHANGE, MAINCOIN, ALTCOIN)).thenReturn(0.0);

		assertFalse(watcher.checkStoploss(order));

		boolean triggered = watcher.checkStoploss(order);

		assertFalse(triggered);
	}

	@Test
	void checkStoploss_whenExchangeThrowsExchangeException_shouldReturnFalse() throws Exception {
		StoplossOrder order = new StoplossOrder(EXCHANGE, ACCOUNT, ALTCOIN);

		when(exchangeService.getBalance(EXCHANGE, ACCOUNT, ALTCOIN)).thenThrow(ExchangeException.class);
		when(exchangeService.getBoughtPrice(EXCHANGE, ACCOUNT, ALTCOIN, BALANCE)).thenThrow(ExchangeException.class);

		when(exchangeService.getBid(EXCHANGE, MAINCOIN, ALTCOIN)).thenReturn(0.0);
		
		boolean triggered = watcher.checkStoploss(order);

		assertFalse(triggered);
	}

}
