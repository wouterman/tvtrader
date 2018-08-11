package tvtrader.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import tvtrader.exchange.Exchange;
import tvtrader.exchange.ExchangeException;
import tvtrader.exchange.ExchangeFactory;
import tvtrader.exchange.apidata.Order;
import tvtrader.model.ApiCredentials;
import tvtrader.model.OrderType;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

	@Mock
	private Order buyOrder;
	@Mock
	private Order sellOrder;
	@Mock
	private Exchange exchange;
	@Mock
	private ExchangeFactory factory;
	@Mock
	private AccountService accountService;
	@Mock
	private ConfigurationService configurationService;

	@InjectMocks
	private TransactionHistoryService service;

	@BeforeAll
	synchronized static void startup() {

	}

	@BeforeEach
	void init() throws Exception {
		MockitoAnnotations.initMocks(this);

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

		ApiCredentials credentials = new ApiCredentials("", "");
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
		setupSingleBuyOrder();

		List<Order> orders = new ArrayList<>();
		orders.add(buyOrder);

		ApiCredentials credentials = new ApiCredentials("", "");
		Mockito.when(factory.getExchange(EXCHANGE)).thenReturn(exchange);
		Mockito.when(accountService.hasAccount(EXCHANGE, ACCOUNT)).thenReturn(true);
		Mockito.when(accountService.getCredentials(EXCHANGE, ACCOUNT)).thenReturn(credentials);
		Mockito.when(accountService.getMainCurrency(EXCHANGE, ACCOUNT)).thenReturn(MAINCOIN);
		Mockito.when(exchange.getTakerFee()).thenReturn(TAKER_FEE);
		Mockito.when(exchange.getOrderHistory(credentials)).thenReturn(orders).thenThrow(Exception.class);
		Mockito.when(configurationService.getAssetRefreshRate()).thenReturn(ONE_MINUTE);

		service.getBoughtPrice(EXCHANGE, ACCOUNT, ALTCOIN, SINGLE_BUY_ORDER_BALANCE);
		double actual = service.getBoughtPrice(EXCHANGE, ACCOUNT, ALTCOIN, SINGLE_BUY_ORDER_BALANCE);

		assertEquals(SINGLE_BUY_ORDER_BOUGHTPRICE, actual);
	}

	@Test
	void getBoughtPrice_whenQuantityRemainingAfterCheckingHistory_shouldReturnZero() throws Exception {
		setupSingleBuyOrder();

		List<Order> orders = new ArrayList<>();
		orders.add(buyOrder);

		ApiCredentials credentials = new ApiCredentials("", "");
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
		setupSingleSellOrder();

		List<Order> orders = new ArrayList<>();
		orders.add(sellOrder);

		ApiCredentials credentials = new ApiCredentials("", "");
		Mockito.when(factory.getExchange(EXCHANGE)).thenReturn(exchange);
		Mockito.when(accountService.hasAccount(EXCHANGE, ACCOUNT)).thenReturn(true);
		Mockito.when(accountService.getCredentials(EXCHANGE, ACCOUNT)).thenReturn(credentials);
		Mockito.when(accountService.getMainCurrency(EXCHANGE, ACCOUNT)).thenReturn(MAINCOIN);
		Mockito.when(exchange.getTakerFee()).thenReturn(TAKER_FEE);
		Mockito.when(exchange.getOrderHistory(credentials)).thenReturn(orders).thenThrow(Exception.class);
		Mockito.when(configurationService.getAssetRefreshRate()).thenReturn(ONE_MINUTE);

		service.getBoughtPrice(EXCHANGE, ACCOUNT, ALTCOIN, SINGLE_BUY_ORDER_BALANCE);
		double actual = service.getBoughtPrice(EXCHANGE, ACCOUNT, ALTCOIN, SINGLE_BUY_ORDER_BALANCE);

		assertEquals(ZERO, actual);
	}

	@Test
	void getBoughtPrice_whenMixedOrders_shouldCalculateBoughtPrice() throws Exception {
		setupMixedOrders();

		List<Order> orders = new ArrayList<>();
		orders.add(buyOrder);
		orders.add(sellOrder);
		orders.add(buyOrder);

		ApiCredentials credentials = new ApiCredentials("", "");
		Mockito.when(factory.getExchange(EXCHANGE)).thenReturn(exchange);
		Mockito.when(accountService.hasAccount(EXCHANGE, ACCOUNT)).thenReturn(true);
		Mockito.when(accountService.getCredentials(EXCHANGE, ACCOUNT)).thenReturn(credentials);
		Mockito.when(accountService.getMainCurrency(EXCHANGE, ACCOUNT)).thenReturn(MAINCOIN);
		Mockito.when(exchange.getTakerFee()).thenReturn(TAKER_FEE);
		Mockito.when(exchange.getOrderHistory(credentials)).thenReturn(orders).thenThrow(Exception.class);
		Mockito.when(configurationService.getAssetRefreshRate()).thenReturn(ONE_MINUTE);

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
