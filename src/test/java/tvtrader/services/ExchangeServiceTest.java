package tvtrader.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import test.logger.Logger;
import tvtrader.bittrex.BittrexFilledOrder;
import tvtrader.exchange.ExchangeException;
import tvtrader.exchange.apidata.Order;
import tvtrader.model.Account;
import tvtrader.model.ApiCredentials;
import tvtrader.orders.MarketOrder;

class ExchangeServiceTest {
	@Mock private TickerService tickerService;
	@Mock private OrderService orderService;
	@Mock private BalanceService balanceService;
	@Mock private AccountService accountService;
	@Mock private TransactionHistoryService transactionHistoryService;
	
	@InjectMocks private ExchangeService exchangeService;

	@BeforeAll
	synchronized static void startup() {
		Logger.turnOffLogging();
	}

	@BeforeEach
	void init() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void getAsk_whenCalled_shouldDelegateToTickerService() throws Exception {
		String exchange = "exchange";
		String maincoin = "main";
		String altcoin = "alt";
		
		
		Mockito.when(tickerService.getAsk(exchange, maincoin, altcoin)).thenReturn(1.0);
		
		assertEquals(1.0, exchangeService.getAsk(exchange, maincoin, altcoin));
		
	}

	@Test
	void getBid_whenCalled_shouldDelegateToTickerService() throws Exception {
		String exchange = "exchange";
		String maincoin = "main";
		String altcoin = "alt";
		
		Mockito.when(tickerService.getBid(exchange, maincoin, altcoin)).thenReturn(1.0);
		
		assertEquals(1.0, exchangeService.getBid(exchange, maincoin, altcoin));
	}

	@Test
	void getBalance_whenCalled_shouldDelegateToBalanceService() throws Exception {
		String exchange = "exchange";
		String account = "main";
		String altcoin = "alt";
		
		Mockito.when(balanceService.getBalance(exchange, account, altcoin)).thenReturn(1.0);
		
		assertEquals(1.0, exchangeService.getBalance(exchange, account, altcoin));
	}

	@Test
	void getBalances_whenCalled_shouldDelegateToBalanceService() throws Exception {
		String exchange = "exchange";
		String account = "main";
		Map<String, Double> map = new HashMap<>();
		Iterator<Entry<String, Double>> iterator = map.entrySet().iterator();
		
		Mockito.when(balanceService.getBalances(exchange, account)).thenReturn(iterator);
		
		assertEquals(iterator, exchangeService.getBalances(exchange, account));
	}
	
	@Test
	void getMinimumOrderAmount_whenCalled_shouldDelegateToOrderService() throws Exception {
		String exchange = "exchange";
		
		Mockito.when(orderService.getMinimumOrderAmount(exchange)).thenReturn(1.0);
		
		assertEquals(1.0, exchangeService.getMinimumOrderAmount(exchange));
	}

	@Test
	void getTakerFee_whenCalled_shouldDelegateToOrderService() throws Exception {
		String exchange = "exchange";
		
		Mockito.when(orderService.getTakerFee(exchange)).thenReturn(1.0);
		
		assertEquals(1.0, exchangeService.getTakerFee(exchange));
	}
	
	@Test
	void placeOrder_whenCalled_shouldFetchAccountAndDelegateToOrderService() throws Exception {
		String exchange = "exchange";
		String accountName = "account";
		String key = "key";
		String secret = "secret";
		
		MarketOrder order = new MarketOrder();
		order.setAccount(accountName);
		order.setExchange(exchange);
		ApiCredentials credentials = new ApiCredentials(key, secret);
		Account account = new Account(null, accountName, null, 0, 0, 0, 0, credentials);
		
		Mockito.when(accountService.getAccount(exchange, accountName)).thenReturn(account);
		Mockito.when(orderService.placeOrder(order, credentials)).thenReturn(true);
		
		assertTrue(exchangeService.placeOrder(order));
	}
	
	@Test
	void placeOrder_whenExchangeExceptionIsThrown_shouldReturnFalse() throws Exception {
		String exchange = "exchange";
		String accountName = "account";
		String key = "key";
		String secret = "secret";
		
		MarketOrder order = new MarketOrder();
		order.setAccount(accountName);
		order.setExchange(exchange);
		ApiCredentials credentials = new ApiCredentials(key, secret);
		Account account = new Account(null, accountName, null, 0, 0, 0, 0, credentials);
		
		Mockito.when(accountService.getAccount(exchange, accountName)).thenReturn(account);
		Mockito.when(orderService.placeOrder(order, credentials)).thenThrow(ExchangeException.class);
		
		assertFalse(exchangeService.placeOrder(order));
	}

	@Test
	void getBoughtPrice_whenCalled_shouldDelegateToTransactionHistoryService() throws Exception {
		String exchange = "exchange";
		String account = "main";
		String altcoin = "alt";
		double balance = 0.0;
		
		Mockito.when(transactionHistoryService.getBoughtPrice(exchange, account, altcoin, balance)).thenReturn(1.0);
		
		assertEquals(1.0, exchangeService.getBoughtPrice(exchange, account, altcoin, balance));
	}

	@Test
	void getOpenorders_whenCalled_shouldFetchAccountAndDelegateToOrderService() throws Exception {
		String exchange = "exchange";
		String accountName = "main";
		String key = "key";
		String secret = "secret";
		
		Order order = new BittrexFilledOrder();
		List<Order> orders = Arrays.asList(order);
		
		ApiCredentials credentials = new ApiCredentials(key, secret);
		Account account = new Account(null, accountName, null, 0, 0, 0, 0, credentials);
		
		Mockito.when(accountService.getAccount(exchange, accountName)).thenReturn(account);
		Mockito.when(orderService.getOpenOrders(exchange, credentials)).thenReturn(orders);
		
		assertEquals(orders, exchangeService.getOpenOrders(exchange, accountName));
	}

	@Test
	void cancelOrder_whenCalled_shouldFetchAccountAndDelegateToOrderService() throws Exception {
		String exchange = "exchange";
		String accountName = "main";
		String orderId = "1";
		String key = "key";
		String secret = "secret";
		
		ApiCredentials credentials = new ApiCredentials(key, secret);
		Account account = new Account(null, accountName, null, 0, 0, 0, 0, credentials);
		
		Mockito.when(accountService.getAccount(exchange, accountName)).thenReturn(account);
		Mockito.when(orderService.cancelOrder(exchange, credentials, orderId)).thenReturn(true);
		
		assertTrue(exchangeService.cancelOrder(exchange, accountName, orderId));
	}

}
