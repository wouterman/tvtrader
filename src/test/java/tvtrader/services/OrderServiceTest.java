package tvtrader.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import test.logger.Logger;
import tvtrader.bittrex.BittrexFilledOrder;
import tvtrader.exchange.Exchange;
import tvtrader.exchange.ExchangeFactory;
import tvtrader.exchange.apidata.Order;
import tvtrader.model.ApiCredentials;
import tvtrader.orders.MarketOrder;

class OrderServiceTest {
	
	private static final String EXCHANGE = "EXCHANGE";
	@Mock private Exchange exchange;
	@Mock private ExchangeFactory factory;
	
	@InjectMocks private OrderService service;
	
	
	@BeforeAll
	synchronized static void startup() {
		Logger.turnOffLogging();
	}

	@BeforeEach
	void init() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		Mockito.when(factory.getExchange(EXCHANGE)).thenReturn(exchange);
	}
	
	@Test
	void placeOrder_whenCalled_shouldFetchExchangeAndPlaceOrder() throws Exception {
		MarketOrder order = new MarketOrder();
		order.setExchange(EXCHANGE);
		
		ApiCredentials credentials = new ApiCredentials(null, null);
		
		Mockito.when(exchange.placeOrder(order, credentials)).thenReturn(true);
		
		assertTrue(service.placeOrder(order, credentials));
	}

	@Test
	void getMinimumOrderAmount_whenCalled_shouldFetchExchangeAndGetMinimumOrderAmount() throws Exception {
		Mockito.when(exchange.getMinimumOrderAmount()).thenReturn(1.0);
		
		assertEquals(1.0, service.getMinimumOrderAmount(EXCHANGE));
	}

	@Test
	void getTakerFee_whenCalled_shouldFetchExchangeAndGetTakerFee() throws Exception {
		Mockito.when(exchange.getTakerFee()).thenReturn(1.0);
		
		assertEquals(1.0, service.getTakerFee(EXCHANGE));
	}

	@Test
	void getOpenOrders_whenCalled_shouldFetchExchangeAndGetOpenOrders() throws Exception {
		Order order = new BittrexFilledOrder();
		List<Order> orders = Arrays.asList(order);
		
		ApiCredentials credentials = new ApiCredentials(null, null);
		
		Mockito.when(exchange.getOpenOrders(credentials)).thenReturn(orders);
		
		assertEquals(orders, service.getOpenOrders(EXCHANGE, credentials));
	}

	@Test
	void cancelOrder_whenCalled_shouldFetchExchangeAndCancelOrder() throws Exception {
		String orderId = "1";
		ApiCredentials credentials = new ApiCredentials(null, null);
		
		Mockito.when(exchange.cancelOrder(orderId, credentials)).thenReturn(true);
		
		assertTrue(service.cancelOrder(EXCHANGE, credentials, orderId));
	}

}
