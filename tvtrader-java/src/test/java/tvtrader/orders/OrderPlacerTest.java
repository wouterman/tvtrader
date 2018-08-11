package tvtrader.orders;

import logger.TestStoplossListener;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import tvtrader.model.MarketOrder;
import tvtrader.services.ExchangeService;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderPlacerTest {
	@Mock private ExchangeService exchangeService;
	@InjectMocks private OrderPlacer orderPlacer;
	
	private TestStoplossListener listener;
	
	@BeforeAll
	synchronized static void startup() {

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
		
		orderPlacer.placeOrders();
		
		assertTrue(listener.isNotified());
	}
	
	@Test
	void placeOrders_whenOrderNotPlaced_shouldNotNotifyListeners() {
		MarketOrder order = new MarketOrder();
		
		orderPlacer.addOrder(order);
		
		Mockito.when(exchangeService.placeOrder(order)).thenReturn(false);
		
		orderPlacer.placeOrders();

	assertFalse(listener.isNotified());
}

}
