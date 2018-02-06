package tvtrader.orders;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import test.logger.Logger;
import test.logger.TestStoplossListener;
import tvtrader.services.ExchangeService;

class OrderPlacerTest {
	@Mock private ExchangeService exchangeService;
	@InjectMocks private OrderPlacer orderPlacer;
	
	private TestStoplossListener listener;
	
	@BeforeAll
	synchronized static void startup() {
		Logger.turnOffLogging();
	}

	@BeforeEach
	void init() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		listener = new TestStoplossListener();
		orderPlacer.addChangeListener(listener);
	}

	@Test
	void placeOrders_whenOrderPlaced_shouldNotifyListeners() {
		MarketOrder order = new MarketOrder();
		
		orderPlacer.addOrder(order);
		
		Mockito.when(exchangeService.placeOrder(order)).thenReturn(true);
		
		orderPlacer.run();
		
		assertTrue(listener.isNotified());
	}
	
	@Test
	void placeOrders_whenOrderNotPlaced_shouldNotNotifyListeners() {
		MarketOrder order = new MarketOrder();
		
		orderPlacer.addOrder(order);
		
		Mockito.when(exchangeService.placeOrder(order)).thenReturn(false);
		
		orderPlacer.run();
		
		assertFalse(listener.isNotified());
	}

}
