package tvtrader.binance;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import test.logger.Logger;
import tvtrader.accounts.ApiCredentials;
import tvtrader.orders.MarketOrder;
import tvtrader.orders.OrderType;
import tvtrader.utils.HashingUtility;
import tvtrader.web.Url;

public class BinanceApiTest {
	private static final String ETH = "ETH";
	private static final String BTC = "BTC";
	private static final String ACTUAL = "actual";
	private static final String KEY = "key";
	private static final String SECRET = "secret";
	private static final String API_SIGNATURE = "apisign";
	private static final String MOCK_HASHER_RESPONSE = "hash";

	private MarketOrder order;
	private ApiCredentials credentials;
	
	@Mock private HashingUtility hasher;

	@InjectMocks private BinanceApi api;

	@BeforeAll
	synchronized static void startup() {
		Logger.turnOffLogging();
	}

	@BeforeEach
	void setup() throws Exception {
		MockitoAnnotations.initMocks(this);
		when(hasher.calculateSignature(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(MOCK_HASHER_RESPONSE);
		
		order = new MarketOrder();
		order.setMainCoin(BTC);
		order.setAltCoin(ETH);
	
		credentials = new ApiCredentials(KEY, SECRET);
	}
	
	@Test
	void getMarketSummaries_whenCalled_shouldReturnMarketSummariesUrl() throws Exception {
		String expected = "https://api.binance.com/api/v1/ticker/24hr";
		
		Url actual = api.getMarketSummaries();

		assertEquals(expected, actual.getUrl(), "Expected url string did not match the actual api getMarketSummaries url!");
	}
	
	@Test
	void placeOrder_whenProvidedWithBuyOrder_shouldReturnBuyOrderUrl() throws Exception {
		String expectedPattern = "";
		
		order.setOrderType(OrderType.LIMIT_BUY);
		
		Url actual = api.placeOrder(order, credentials);
		
		assertAll(ACTUAL,
				() -> assertTrue(actual.getUrl().matches(expectedPattern), "placeOrder() didn't return the expected url pattern!"),
				() -> assertTrue(actual.getHeaders().containsKey(API_SIGNATURE), "Header should contain api signature key!"),
				() -> assertEquals(actual.getHeaders().get(API_SIGNATURE), MOCK_HASHER_RESPONSE, "apisign should be equal to the mock hasher response!"));
	}
	
	
}
