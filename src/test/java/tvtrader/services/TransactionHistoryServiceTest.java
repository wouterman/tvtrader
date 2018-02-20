package tvtrader.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import test.logger.Logger;
import tvtrader.exchange.Exchange;
import tvtrader.exchange.ExchangeException;
import tvtrader.exchange.ExchangeFactory;
import tvtrader.exchange.apidata.Order;
import tvtrader.model.ApiCredentials;
import tvtrader.model.Configuration;
import tvtrader.orders.OrderType;

@RunWith(MockitoJUnitRunner.StrictStubs.class)

class TransactionHistoryServiceTest {
	private static final int ZERO = 0;
	private static final int ONE_MINUTE = 60;
	private static final double TAKER_FEE = 1.0;
	private static final String EXCHANGE = "EXCHANGE";
	private static final String ACCOUNT = "ACCOUNT";
	private static final String MAINCOIN = "MAINCOIN";
	private static final String ALTCOIN = "ALTCOIN";
	private static final double SINGLE_BUY_ORDER_BALANCE = 5.0;
	private static final double SINGLE_BUY_ORDER_BOUGHTPRICE = 10.0;
	private static final double MIXED_ORDER_BALANCE = 8.0;
	private static final double MIXED_ORDER_BOUGHTPRICE = 16.0;

	@Mock private Order buyOrder;
	@Mock private Order sellOrder;
	@Mock private Exchange exchange;
	@Mock private ExchangeFactory factory;
	@Mock private AccountService accountService;
	@Mock private Configuration configuration;

	private TransactionHistoryService service;

	@BeforeAll
	synchronized static void startup() {
		Logger.turnOffLogging();
	}

	@BeforeEach
	void init() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		service = new TransactionHistoryService(configuration);
		service.setAccountService(accountService);
		service.setFactory(factory);
	}

	@Test
	void getBoughtPrice_whenExchangeIsUnknown_shouldThrowExchangeException() throws Exception {
		Mockito.when(factory.getExchange(EXCHANGE)).thenThrow(ExchangeException.class);

		assertThrows(ExchangeException.class,
				() -> service.getBoughtPrice(EXCHANGE, ACCOUNT, ALTCOIN, SINGLE_BUY_ORDER_BALANCE));
	}

	@Test
	void getBoughtPrice_whenAccountIsUnknown_shouldThrowExchangeException() throws Exception {
		Mockito.when(accountService.hasAccount(EXCHANGE, ACCOUNT)).thenReturn(false);

		assertThrows(ExchangeException.class,
				() -> service.getBoughtPrice(EXCHANGE, ACCOUNT, ALTCOIN, SINGLE_BUY_ORDER_BALANCE));
	}

	@Test
	void getBoughtPrice_whenRefreshIsNeeded_shouldRefreshCache() throws Exception {
		setupSingleBuyOrder();

		List<Order> orders = new ArrayList<>();
		orders.add(buyOrder);

		ApiCredentials credentials = new ApiCredentials(null, null);
		Mockito.when(factory.getExchange(EXCHANGE)).thenReturn(exchange);
		Mockito.when(accountService.hasAccount(EXCHANGE, ACCOUNT)).thenReturn(true);
		Mockito.when(accountService.getCredentials(EXCHANGE, ACCOUNT)).thenReturn(credentials);
		Mockito.when(accountService.getMainCurrency(EXCHANGE, ACCOUNT)).thenReturn(MAINCOIN);
		Mockito.when(exchange.getOrderHistory(credentials)).thenReturn(orders);
		Mockito.when(exchange.getTakerFee()).thenReturn(TAKER_FEE);

		double actual = service.getBoughtPrice(EXCHANGE, ACCOUNT, ALTCOIN, SINGLE_BUY_ORDER_BALANCE);

		assertEquals(10.0, actual);
	}

	@Test
	void getBoughtPrice_whenRefreshIsNotNeeded_shouldNotRefreshCache() throws Exception {
		service.setBoughtPriceRefreshRate(ONE_MINUTE);
		setupSingleBuyOrder();

		List<Order> orders = new ArrayList<>();
		orders.add(buyOrder);

		ApiCredentials credentials = new ApiCredentials(null, null);
		Mockito.when(factory.getExchange(EXCHANGE)).thenReturn(exchange);
		Mockito.when(accountService.hasAccount(EXCHANGE, ACCOUNT)).thenReturn(true);
		Mockito.when(accountService.getCredentials(EXCHANGE, ACCOUNT)).thenReturn(credentials);
		Mockito.when(accountService.getMainCurrency(EXCHANGE, ACCOUNT)).thenReturn(MAINCOIN);
		Mockito.when(exchange.getTakerFee()).thenReturn(TAKER_FEE);
		Mockito.when(exchange.getOrderHistory(credentials)).thenReturn(orders).thenThrow(Exception.class);

		service.getBoughtPrice(EXCHANGE, ACCOUNT, ALTCOIN, SINGLE_BUY_ORDER_BALANCE);
		double actual = service.getBoughtPrice(EXCHANGE, ACCOUNT, ALTCOIN, SINGLE_BUY_ORDER_BALANCE);

		assertEquals(SINGLE_BUY_ORDER_BOUGHTPRICE, actual);
	}

	@Test
	void getBoughtPrice_whenQuantityRemainingAfterCheckingHistory_shouldReturnZero() throws Exception {
		setupSingleBuyOrder();

		List<Order> orders = new ArrayList<>();
		orders.add(buyOrder);

		ApiCredentials credentials = new ApiCredentials(null, null);
		Mockito.when(factory.getExchange(EXCHANGE)).thenReturn(exchange);
		Mockito.when(accountService.hasAccount(EXCHANGE, ACCOUNT)).thenReturn(true);
		Mockito.when(accountService.getCredentials(EXCHANGE, ACCOUNT)).thenReturn(credentials);
		Mockito.when(accountService.getMainCurrency(EXCHANGE, ACCOUNT)).thenReturn(MAINCOIN);
		Mockito.when(exchange.getTakerFee()).thenReturn(TAKER_FEE);
		Mockito.when(exchange.getOrderHistory(credentials)).thenReturn(orders).thenThrow(Exception.class);

		double actual = service.getBoughtPrice(EXCHANGE, ACCOUNT, ALTCOIN, MIXED_ORDER_BALANCE);

		assertEquals(ZERO, actual);
	}

	@Test
	void getBoughtPrice_whenNoBuyOrders_shouldReturnZero() throws Exception {
		service.setBoughtPriceRefreshRate(ONE_MINUTE);

		setupSingleSellOrder();

		List<Order> orders = new ArrayList<>();
		orders.add(sellOrder);

		ApiCredentials credentials = new ApiCredentials(null, null);
		Mockito.when(factory.getExchange(EXCHANGE)).thenReturn(exchange);
		Mockito.when(accountService.hasAccount(EXCHANGE, ACCOUNT)).thenReturn(true);
		Mockito.when(accountService.getCredentials(EXCHANGE, ACCOUNT)).thenReturn(credentials);
		Mockito.when(accountService.getMainCurrency(EXCHANGE, ACCOUNT)).thenReturn(MAINCOIN);
		Mockito.when(exchange.getTakerFee()).thenReturn(TAKER_FEE);
		Mockito.when(exchange.getOrderHistory(credentials)).thenReturn(orders).thenThrow(Exception.class);

		service.getBoughtPrice(EXCHANGE, ACCOUNT, ALTCOIN, SINGLE_BUY_ORDER_BALANCE);
		double actual = service.getBoughtPrice(EXCHANGE, ACCOUNT, ALTCOIN, SINGLE_BUY_ORDER_BALANCE);

		assertEquals(ZERO, actual);
	}

	@Test
	void getBoughtPrice_whenMixedOrders_shouldCalculateBoughtPrice() throws Exception {
		service.setBoughtPriceRefreshRate(ONE_MINUTE);

		setupMixedOrders();

		List<Order> orders = new ArrayList<>();
		orders.add(buyOrder);
		orders.add(sellOrder);
		orders.add(buyOrder);

		ApiCredentials credentials = new ApiCredentials(null, null);
		Mockito.when(factory.getExchange(EXCHANGE)).thenReturn(exchange);
		Mockito.when(accountService.hasAccount(EXCHANGE, ACCOUNT)).thenReturn(true);
		Mockito.when(accountService.getCredentials(EXCHANGE, ACCOUNT)).thenReturn(credentials);
		Mockito.when(accountService.getMainCurrency(EXCHANGE, ACCOUNT)).thenReturn(MAINCOIN);
		Mockito.when(exchange.getTakerFee()).thenReturn(TAKER_FEE);
		Mockito.when(exchange.getOrderHistory(credentials)).thenReturn(orders).thenThrow(Exception.class);

		service.getBoughtPrice(EXCHANGE, ACCOUNT, ALTCOIN, SINGLE_BUY_ORDER_BALANCE);
		double actual = service.getBoughtPrice(EXCHANGE, ACCOUNT, ALTCOIN, MIXED_ORDER_BALANCE);

		assertEquals(MIXED_ORDER_BOUGHTPRICE, actual);
	}

	private void setupSingleBuyOrder() {
		Mockito.when(buyOrder.getAltCoin()).thenReturn(ALTCOIN);
		Mockito.when(buyOrder.getMainCoin()).thenReturn(MAINCOIN);
		Mockito.when(buyOrder.getOrderType()).thenReturn(OrderType.LIMIT_BUY);
		Mockito.when(buyOrder.getQuantity()).thenReturn(5.0);
		Mockito.when(buyOrder.getRate()).thenReturn(1.0);
	}

	private void setupSecondBuyOrder() {
		Mockito.when(buyOrder.getAltCoin()).thenReturn(ALTCOIN);
		Mockito.when(buyOrder.getMainCoin()).thenReturn(MAINCOIN);
		Mockito.when(buyOrder.getOrderType()).thenReturn(OrderType.LIMIT_BUY);
		Mockito.when(buyOrder.getQuantity()).thenReturn(5.0);
		Mockito.when(buyOrder.getRate()).thenReturn(1.0);
	}

	private void setupSingleSellOrder() {
		Mockito.when(sellOrder.getAltCoin()).thenReturn(ALTCOIN);
		Mockito.when(sellOrder.getMainCoin()).thenReturn(MAINCOIN);
		Mockito.when(sellOrder.getOrderType()).thenReturn(OrderType.LIMIT_SELL);
		Mockito.when(sellOrder.getQuantity()).thenReturn(5.0);
		Mockito.when(sellOrder.getRate()).thenReturn(1.0);
	}

	private void setupMixedOrders() {
		setupSingleSellOrder();
		setupSingleBuyOrder();
		setupSecondBuyOrder();
	}

}
