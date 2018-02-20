package tvtrader.orders;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import test.logger.Logger;
import tvtrader.exchange.ExchangeException;
import tvtrader.exchange.apidata.Order;
import tvtrader.model.Account;
import tvtrader.model.Configuration;
import tvtrader.model.ConfigurationField;
import tvtrader.services.AccountService;
import tvtrader.services.ExchangeService;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
class OpenOrdersWatcherTest {
	private static final String SUPPORTED_EXCHANGE = "BITTREX";
	private static final int ONE_MINUTE = 60;
	private static final String ORDER_ID = "1";
	
	private static final String MAINCOIN = "MAINCOIN";
	private static final String ALTCOIN = "ALTCOIN";
	
	private static final String EXCHANGE = "EXCHANGE";
	private static final String ACCOUNT = "ACCOUNT";
	private static final String ACCOUNT_2 = "ACCOUNT_2";
	private static final long ONE_DAY = 1_000 * 60 * 60 * 24;
	private static final int ONE_SECOND = 1;
	
	private List<Account> accounts;
	private List<Order> orders;
	
	@Mock private Order order;
	@Mock private ExchangeService exchangeService;
	@Mock private AccountService accountService;
	@Mock private OrderBuilder orderBuilder;
	@Mock private OrderPlacer orderPlacer;
	@Mock private Configuration configuration;
	
	private OpenOrdersWatcher orderWatcher;
	
	private Account btcAccount;
	private Account ethAccount;

	@BeforeAll
	synchronized static void startup() {
		Logger.turnOffLogging();
	}

	@BeforeEach
	void setup() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		orderWatcher = new OpenOrdersWatcher(configuration);
		orderWatcher.setAccountService(accountService);
		orderWatcher.setExchangeService(exchangeService);
		orderWatcher.setOrderBuilder(orderBuilder);
		orderWatcher.setOrderPlacer(orderPlacer);
		
		when(order.getCommission()).thenReturn(1.0);
		when(order.getMainCoin()).thenReturn(MAINCOIN);
		when(order.getAltCoin()).thenReturn(ALTCOIN);
		when(order.getOrderType()).thenReturn(OrderType.LIMIT_SELL);
		when(order.getPrice()).thenReturn(1.0);
		when(order.getRate()).thenReturn(1.0);
		when(order.getQuantityRemaining()).thenReturn(1.0);
		when(order.getQuantity()).thenReturn(1.0);
		when(order.getOrderId()).thenReturn(ORDER_ID);
		
		orders = new ArrayList<>();
		orders.add(order);
		
		btcAccount = new Account(EXCHANGE ,ACCOUNT, null, 0, 0, 0, 0, null);
		ethAccount = new Account(EXCHANGE, ACCOUNT_2, null, 0, 0, 0, 0, null);
		accounts = Arrays.asList(btcAccount, ethAccount);
		
		orderWatcher.setExpirationTime(ONE_MINUTE);
	}

	@Test
	void checkOrders_whenOrderIsNotExpired_shouldNotCancelOrder() throws Exception {
		when(order.getTimeStamp())
				.thenReturn(System.currentTimeMillis());
		
		when(accountService.getAccounts(SUPPORTED_EXCHANGE)).thenReturn(accounts.iterator());
		when(exchangeService.getOpenOrders(SUPPORTED_EXCHANGE, ACCOUNT)).thenReturn(orders);
		
		orderWatcher.checkOrders();
		
		verify(exchangeService, never()).cancelOrder(SUPPORTED_EXCHANGE, ACCOUNT, ORDER_ID);
	}
	
	@Test
	void checkOrders_whenOrderIsExpired_shouldCancelOrder() throws Exception {
		when(order.getTimeStamp())
		.thenReturn(System.currentTimeMillis() - ONE_DAY);
		
		when(accountService.getAccounts(SUPPORTED_EXCHANGE)).thenReturn(accounts.iterator());
		when(exchangeService.getOpenOrders(SUPPORTED_EXCHANGE, ACCOUNT)).thenReturn(orders);
		
		orderWatcher.checkOrders();
		
		verify(exchangeService, times(1)).cancelOrder(SUPPORTED_EXCHANGE, ACCOUNT, ORDER_ID);
	}
	
	@Test
	void replaceOrder_whenCalled_shouldCalculateCorrectQuantityAndRate() throws Exception {
		MarketOrder expectedOrder = new MarketOrder();
		expectedOrder.setAccount(ACCOUNT);
		expectedOrder.setAltCoin(ALTCOIN);
		expectedOrder.setExchange(SUPPORTED_EXCHANGE);
		expectedOrder.setMainCoin(MAINCOIN);
		expectedOrder.setOrderType(OrderType.LIMIT_SELL);
		expectedOrder.setQuantity(1.0);
		expectedOrder.setRate(1.0);

		when(exchangeService.getTakerFee(SUPPORTED_EXCHANGE)).thenReturn(0.0);
		when(exchangeService.getBid(SUPPORTED_EXCHANGE, MAINCOIN, ALTCOIN)).thenReturn(1.0);
		
		orderWatcher.replaceOrder(expectedOrder, 1.0);
		
		verify(orderBuilder, times(1)).calculateQuantityAndRate(expectedOrder, 1.0);
		verify(orderPlacer, times(1)).addOrder(expectedOrder);
	}
	
	@Test
	void checkOrders_whenExchangeExceptionIsThrown_shouldStillCheckOtherAccounts() throws Exception {
		when(order.getTimeStamp())
		.thenReturn(System.currentTimeMillis() - ONE_DAY);
		
		when(accountService.getAccounts(SUPPORTED_EXCHANGE)).thenReturn(accounts.iterator());
		when(exchangeService.getOpenOrders(SUPPORTED_EXCHANGE, ACCOUNT)).thenThrow(ExchangeException.class);
		when(exchangeService.getOpenOrders(SUPPORTED_EXCHANGE, ACCOUNT_2)).thenReturn(orders);
		
		orderWatcher.checkOrders();
		
		verify(exchangeService, times(1)).cancelOrder(SUPPORTED_EXCHANGE, ACCOUNT_2, ORDER_ID);
	}

	@Test
	void update_shouldUpdateExpirationTime_whenReceivingUpdate() throws Exception {
		Configuration configuration = new Configuration();
		configuration.setOpenOrdersExpirationTime(ONE_SECOND);
		
		orderWatcher.update(ConfigurationField.OPENORDERSEXPIRATIONTIME, configuration);
		
		assertEquals(ONE_SECOND, orderWatcher.getExpirationTime());
	}
	
	@Test
	void update_shouldUpdateOpenOrdersInterval_whenReceivingUpdate() throws Exception {
		Configuration configuration = new Configuration();
		configuration.setUnfilledOrdersReplaceFlag(true);
		
		orderWatcher.update(ConfigurationField.UNFILLEDORDERSREPLACEFLAG, configuration);
		
		assertTrue(orderWatcher.isReplace());
	}
	
	@Test
	void update_shouldNotUpdate_whenReceivingIrrelevantUpdate() throws Exception {
		Configuration configuration = new Configuration();
		configuration.setOpenOrdersExpirationTime(ONE_SECOND);
		configuration.setUnfilledOrdersReplaceFlag(true);
		
		orderWatcher.update(ConfigurationField.EXPECTEDSENDER, configuration);
		
		assertEquals(ONE_MINUTE, orderWatcher.getExpirationTime());
		assertFalse(orderWatcher.isReplace());
	}
}
