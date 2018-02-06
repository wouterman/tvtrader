package tvtrader.orders;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import test.logger.Logger;
import tvtrader.services.ExchangeService;

class OrderBuilderTest {
	private static final String EXCHANGE = "EXCHANGE";
	private static final String MAINCOIN = "MAINCOIN";
	private static final String ALTCOIN = "ALTCOIN";
	private static final String ACCOUNT = "ACCOUNT";

	@Mock
	private ExchangeService exchangeService;

	@InjectMocks
	private OrderBuilder orderBuilder;

	@BeforeAll
	synchronized static void startup() {
		Logger.turnOffLogging();
	}

	@BeforeEach
	void setup() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void buildOrders_whenValidBuyOrder_shouldCalculateRateAndQuantity() throws Exception {
		MarketOrder order = new MarketOrder();
		order.setExchange(EXCHANGE);
		order.setAccount(ACCOUNT);
		order.setMainCoin(MAINCOIN);
		order.setAltCoin(ALTCOIN);
		order.setOrderType(OrderType.LIMIT_BUY);

		Mockito.when(exchangeService.getAsk(EXCHANGE, MAINCOIN, ALTCOIN)).thenReturn(1.0);
		Mockito.when(exchangeService.getBalance(EXCHANGE, ACCOUNT, ALTCOIN)).thenReturn(0.0);
		Mockito.when(exchangeService.getMinimumOrderAmount(EXCHANGE)).thenReturn(1.0);
		Mockito.when(exchangeService.getTakerFee(EXCHANGE)).thenReturn(0.0);

		orderBuilder.calculateQuantityAndRate(order, 1.0);

		assertAll("order", 
				() -> assertEquals(1.0, order.getRate()), 
				() -> assertEquals(1.0, order.getQuantity()));
	}
	
	@Test
	void buildOrders_whenAccountHasBalance_shouldNotCalculateQuantityForBuyOrder() throws Exception {
		MarketOrder order = new MarketOrder();
		order.setExchange(EXCHANGE);
		order.setAccount(ACCOUNT);
		order.setMainCoin(MAINCOIN);
		order.setAltCoin(ALTCOIN);
		order.setOrderType(OrderType.LIMIT_BUY);

		Mockito.when(exchangeService.getAsk(EXCHANGE, MAINCOIN, ALTCOIN)).thenReturn(1.0);
		Mockito.when(exchangeService.getBalance(EXCHANGE, ACCOUNT, ALTCOIN)).thenReturn(1.0);
		Mockito.when(exchangeService.getMinimumOrderAmount(EXCHANGE)).thenReturn(0.0);
		Mockito.when(exchangeService.getTakerFee(EXCHANGE)).thenReturn(0.0);

		orderBuilder.calculateQuantityAndRate(order, 1.0);

		assertAll("order", 
				() -> assertEquals(1.0, order.getRate()), 
				() -> assertEquals(0.0, order.getQuantity()));
	}
	
	@Test
	void buildOrders_whenValidSellOrder_shouldCalculateRateAndQuantity() throws Exception {
		MarketOrder order = new MarketOrder();
		order.setExchange(EXCHANGE);
		order.setAccount(ACCOUNT);
		order.setMainCoin(MAINCOIN);
		order.setAltCoin(ALTCOIN);
		order.setOrderType(OrderType.LIMIT_SELL);

		Mockito.when(exchangeService.getBid(EXCHANGE, MAINCOIN, ALTCOIN)).thenReturn(1.0);
		Mockito.when(exchangeService.getBalance(EXCHANGE, ACCOUNT, ALTCOIN)).thenReturn(1.0);
		Mockito.when(exchangeService.getMinimumOrderAmount(EXCHANGE)).thenReturn(0.0);
		Mockito.when(exchangeService.getTakerFee(EXCHANGE)).thenReturn(0.0);

		orderBuilder.calculateQuantityAndRate(order, 1.0);

		assertAll("order", 
				() -> assertEquals(1.0, order.getRate()), 
				() -> assertEquals(1.0, order.getQuantity()));
	}
	
	@Test
	void buildOrders_whenAccountHasNoBalance_shouldNotCalculateQuantityForSellOrder() throws Exception {
		MarketOrder order = new MarketOrder();
		order.setExchange(EXCHANGE);
		order.setAccount(ACCOUNT);
		order.setMainCoin(MAINCOIN);
		order.setAltCoin(ALTCOIN);
		order.setOrderType(OrderType.LIMIT_SELL);

		Mockito.when(exchangeService.getBid(EXCHANGE, MAINCOIN, ALTCOIN)).thenReturn(1.0);
		Mockito.when(exchangeService.getBalance(EXCHANGE, ACCOUNT, ALTCOIN)).thenReturn(0.0);
		Mockito.when(exchangeService.getMinimumOrderAmount(EXCHANGE)).thenReturn(0.0);
		Mockito.when(exchangeService.getTakerFee(EXCHANGE)).thenReturn(0.0);

		orderBuilder.calculateQuantityAndRate(order, 1.0);

		assertAll("order", 
				() -> assertEquals(1.0, order.getRate()), 
				() -> assertEquals(0.0, order.getQuantity()));
	}

	@Test
	void buildOrders_whenOrderTypeUnsupported_shouldNotSetRateAndQuantity() throws Exception {
		MarketOrder order = new MarketOrder();
		order.setExchange(EXCHANGE);
		order.setAccount(ACCOUNT);
		order.setMainCoin(MAINCOIN);
		order.setAltCoin(ALTCOIN);
		
		Mockito.when(exchangeService.getAsk(EXCHANGE, MAINCOIN, ALTCOIN)).thenReturn(1.0);
		Mockito.when(exchangeService.getBalance(EXCHANGE, ACCOUNT, ALTCOIN)).thenReturn(1.0);
		Mockito.when(exchangeService.getMinimumOrderAmount(EXCHANGE)).thenReturn(0.0);
		Mockito.when(exchangeService.getTakerFee(EXCHANGE)).thenReturn(0.0);

		orderBuilder.calculateQuantityAndRate(order, 1.0);

		assertAll("order", 
				() -> assertEquals(0.0, order.getRate()), 
				() -> assertEquals(0.0, order.getQuantity()));
	}

	@Test
	void buildOrders_whenRateNotSet_shouldNotCalculateQuantity() throws Exception {
		MarketOrder order = new MarketOrder();
		order.setExchange(EXCHANGE);
		order.setAccount(ACCOUNT);
		order.setMainCoin(MAINCOIN);
		order.setAltCoin(ALTCOIN);
		order.setOrderType(OrderType.LIMIT_BUY);
		
		Mockito.when(exchangeService.getAsk(EXCHANGE, MAINCOIN, ALTCOIN)).thenReturn(0.0);
		Mockito.when(exchangeService.getBalance(EXCHANGE, ACCOUNT, ALTCOIN)).thenReturn(1.0);
		Mockito.when(exchangeService.getMinimumOrderAmount(EXCHANGE)).thenReturn(0.0);
		Mockito.when(exchangeService.getTakerFee(EXCHANGE)).thenReturn(0.0);

		orderBuilder.calculateQuantityAndRate(order, 1.0);

		assertAll("order", 
				() -> assertEquals(0.0, order.getRate()), 
				() -> assertEquals(0.0, order.getQuantity()));
	}

}
