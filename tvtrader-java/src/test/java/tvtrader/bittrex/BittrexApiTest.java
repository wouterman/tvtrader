package tvtrader.bittrex;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tvtrader.exchange.UnsupportedOrderTypeException;
import tvtrader.model.ApiCredentials;
import tvtrader.model.MarketOrder;
import tvtrader.model.OrderType;
import tvtrader.request.Url;
import tvtrader.utils.HashingUtility;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class BittrexApiTest {
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

	@InjectMocks private tvtrader.bittrex.BittrexApi api;

	@BeforeAll
	synchronized static void startup() {

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
	void getMarketSummaries_whenCalled_shouldReturnBittrexMarketSummariesUrl() throws Exception {
		String expected = "https://bittrex.com/api/v1.1/public/getmarketsummaries";
		
		Url actual = api.getMarketSummaries();

		assertEquals(expected, actual.getUrl(), "Expected url string did not match the actual api getMarketSummaries url!");
	}

	@Test
	void placeOrder_whenProvidedWithBuyOrder_shouldReturnBuyOrderUrl() throws Exception {
		String expectedPattern = "https://bittrex.com/api/v1.1/market/buylimit\\?apikey=key&market=BTC-ETH&quantity=\\d+\\.\\d+&rate=\\d+\\.\\d+&nonce=\\d+";
		
		order.setOrderType(OrderType.LIMIT_BUY);
		
		Url actual = api.placeOrder(order, credentials);
		
		assertAll(ACTUAL,
				() -> assertTrue(actual.getUrl().matches(expectedPattern), "placeOrder() didn't return the expected url pattern!"),
				() -> assertTrue(actual.getHeaders().containsKey(API_SIGNATURE), "Header should contain api signature key!"),
				() -> assertEquals(actual.getHeaders().get(API_SIGNATURE), MOCK_HASHER_RESPONSE, "apisign should be equal to the mock hasher response!"));
	}
	
	@Test
	void placeOrder_whenProvidedWithSellorder_shouldReturnSellOrderUrl() throws Exception {
		String expectedPattern = "https://bittrex.com/api/v1.1/market/selllimit\\?apikey=key&market=BTC-ETH&quantity=\\d+\\.\\d+&rate=\\d+\\.\\d+&nonce=\\d+";
		
		order.setOrderType(OrderType.LIMIT_SELL);
		
		Url actual = api.placeOrder(order, credentials);
		
		assertAll(ACTUAL,
				() -> assertTrue(actual.getUrl().matches(expectedPattern), "placeOrder() didn't return the expected url pattern!"),
				() -> assertTrue(actual.getHeaders().containsKey(API_SIGNATURE), "Header should contain api signature key!"),
				() -> assertEquals(actual.getHeaders().get(API_SIGNATURE), MOCK_HASHER_RESPONSE, "apisign should be equal to the mock hasher response!"));
	}
		
	@Test
	void placeOrder_whenProvidedWithUnsupportedOrderType_shouldThrowUnsupportedOrderTypeException() {
		
		order.setOrderType(OrderType.UNSUPPORTED);
				
		assertThrows(UnsupportedOrderTypeException.class, () -> api.placeOrder(order, credentials));
	}
	
	@Test
	void getBalances_whenProvidedWithAccount_shouldReturnGetBalancesUrl() throws Exception {
		String expectedPattern = "https://bittrex.com/api/v1.1/account/getbalances\\?apikey=key&nonce=\\d+";
		
		Url actual = api.getBalances(credentials);
		
		assertAll(ACTUAL,
				() -> assertTrue(actual.getUrl().matches(expectedPattern), "GetBalances didn't return the expected url pattern!"),
				() -> assertTrue(actual.getHeaders().containsKey(API_SIGNATURE), "Header should contain api signature key!"),
				() -> assertEquals(actual.getHeaders().get(API_SIGNATURE), MOCK_HASHER_RESPONSE)); 
	}

	@Test
	void cancelOrder_whenProvidedWithOrderIdAndAccount_shouldReturnCancelOrderUrl() throws Exception {
		String expectedPattern = "https://bittrex.com/api/v1.1/market/cancel\\?apikey=key&uuid=uuid123&nonce=\\d+";

		Url actual = api.cancelOrder("uuid123", credentials);

		assertAll(ACTUAL,
				() -> assertTrue(actual.getUrl().matches(expectedPattern), "cancelOrder didn't return the expected url pattern!"),
				() -> assertTrue(actual.getHeaders().containsKey(API_SIGNATURE), "Header should contain api signature key!"),
				() -> assertEquals(actual.getHeaders().get(API_SIGNATURE), MOCK_HASHER_RESPONSE));
	}
	
	@Test
	void getOrderHistory_whenProvidedWithMarketAndAccount_shouldReturnGetOrderHistoryUrl() throws Exception {
		String expectedPattern = "https://bittrex.com/api/v1.1/account/getorderhistory\\?apikey=key&nonce=\\d+";
		
		Url actual = api.getOrderHistory(credentials);
		
		assertAll(ACTUAL,
				() -> assertTrue(actual.getUrl().matches(expectedPattern), "getOrderHistory didn't return the expected url pattern!"),
				() -> assertTrue(actual.getHeaders().containsKey(API_SIGNATURE), "Header should contain api signature key!"),
				() -> assertEquals(actual.getHeaders().get(API_SIGNATURE), MOCK_HASHER_RESPONSE));
	}
	
	@Test
	void getOpenOrders_whenProvidedWithMarketAndAccount_shouldReturnGetOpenOrdersUrl() throws Exception {
		String expectedPattern = "https://bittrex.com/api/v1.1/market/getopenorders\\?apikey=key&nonce=\\d+";
		
		Url actual = api.getOpenOrders(credentials);
		
		assertAll(ACTUAL,
				() -> assertTrue(actual.getUrl().matches(expectedPattern), "getOpenOrders didn't return the expected url pattern!"),
				() -> assertTrue(actual.getHeaders().containsKey(API_SIGNATURE), "Header should contain api signature key!"),
				() -> assertEquals(actual.getHeaders().get(API_SIGNATURE), MOCK_HASHER_RESPONSE));
	}
}
