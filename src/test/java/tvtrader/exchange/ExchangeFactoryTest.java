package tvtrader.exchange;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import test.logger.Logger;
import tvtrader.bittrex.Bittrex;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
class ExchangeFactoryTest {
	
	private static final String BITTREX = "BITTREX";
	private static final String UNSUPPORTED = "UNSUPPORTED";
	
	@Mock private Bittrex bittrex;
	
	private ExchangeFactory factory;
	
	@BeforeAll
	synchronized  static void startup() {
		Logger.turnOffLogging();
	}
	
	@BeforeEach
	void setup() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		factory = new ExchangeFactory();
		Field bittrexField = factory.getClass().getDeclaredField("bittrexExchange");
		bittrexField.setAccessible(true);
		bittrexField.set(factory, bittrex);
	}
	
	@Test
	void getExchanges_whenCalled_shouldReturnListWithAllSupportedExchanges() throws Exception {
		List<Exchange> actual = factory.getExchanges();
		
		assertEquals(1, actual.size(), "Exchange list should only contain one element!");
		assertTrue(actual.contains(bittrex), "List of exchange should've contained bittrex!");
	}
	
	@Test
	void getExchange_whenRequestingBittrex_shouldReturnBittrex() throws Exception {
		Exchange requested = factory.getExchange(BITTREX);
		
		assertEquals(bittrex, requested);
	}
	
	@Test
	void getExchange_whenRequestingUnsupportedExchange_shouldThrowUnsupportedExchangeException() throws Exception {
		assertThrows(UnsupportedExchangeException.class, () -> factory.getExchange(UNSUPPORTED));
	}
	
}
