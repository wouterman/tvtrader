package tvtrader.orders;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import tvtrader.exchange.ExchangeException;
import tvtrader.exchange.apidata.Order;
import tvtrader.model.Account;
import tvtrader.model.ApiCredentials;
import tvtrader.model.MarketOrder;
import tvtrader.model.OrderType;
import tvtrader.services.AccountService;
import tvtrader.services.ConfigurationService;
import tvtrader.services.ExchangeService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
class OpenOrdersWatcherTest {
	public static final String BTC = "BTC";
	private static final String SUPPORTED_EXCHANGE = "BITTREX";
	private static final int ONE_MINUTE = 60;
	private static final String ORDER_ID = "1";
	
	private static final String MAINCOIN = "MAINCOIN";
	private static final String ALTCOIN = "ALTCOIN";
	
	private static final String EXCHANGE = "EXCHANGE";
	private static final String ACCOUNT = "ACCOUNT";
	private static final String ACCOUNT_2 = "ACCOUNT_2";
	private static final long ONE_DAY = 1_000 * 60 * 60 * 24;

	private List<Account> accounts;
	private List<Order> orders;
	
	@Mock private Order order;
	@Mock private ExchangeService exchangeService;
	@Mock private AccountService accountService;
	@Mock private OrderBuilder orderBuilder;
	@Mock private OrderPlacer orderPlacer;
	@Mock private ConfigurationService configurationService;

	@InjectMocks
	private OpenOrdersWatcher orderWatcher;
	
	@BeforeEach
	void setup() throws Exception {
		MockitoAnnotations.initMocks(this);
		
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

		Account btcAccount = new Account(EXCHANGE ,ACCOUNT, BTC, 0, 0, 0, 0, new ApiCredentials("", ""));
		Account ethAccount = new Account(EXCHANGE, ACCOUNT_2, BTC, 0, 0, 0, 0, new ApiCredentials("", ""));
		accounts = Arrays.asList(btcAccount, ethAccount);

		when(configurationService.getOpenOrdersExpirationTime()).thenReturn(ONE_MINUTE);
		when(configurationService.getRetryOrderFlag()).thenReturn(true);
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

}
