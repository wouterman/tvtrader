package tvtrader.exchange.bittrex;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import test.logger.Logger;
import tvtrader.accounts.ApiCredentials;
import tvtrader.exchange.ExchangeException;
import tvtrader.exchange.UnsupportedOrderTypeException;
import tvtrader.exchange.apidata.Order;
import tvtrader.exchange.apidata.Ticker;
import tvtrader.orders.MarketOrder;
import tvtrader.orders.OrderType;
import tvtrader.stubs.BalanceStubs;
import tvtrader.stubs.TickerStubs;
import tvtrader.stubs.bittrex.BittrexResponseStub;
import tvtrader.web.RequestHandler;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
class BittrexTest {
	private static final String ACCOUNTNAME = "ACCOUNTNAME";
	private static final String EXCHANGENAME = "BITTREX";
	private static final String ORDERUUID = "1";
	private static final String KEY = "key";
	private static final String SECRET = "secret";
	private static final String BITTREX_BTC_ETH_MARKET = "BTC-ETH";
	private static final String ETH = "ETH";
	private static final String BTC = "BTC";

	private ApiCredentials credentials;
	
	@Mock
	private MarketOrder order;
	@Mock
	private RequestHandler handler;
	@Mock
	private BittrexApi api;
	@Mock
	private BittrexParser parser;

	@InjectMocks
	private Bittrex bittrex;

	@BeforeAll
	synchronized static void startup() {
		Logger.turnOffLogging();
	}

	@BeforeEach
	void init() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		credentials = new ApiCredentials(KEY, SECRET);
	}

	@Test
	void getTickers_whenProvidedWithMarket_shouldReturnTickerForThatMarket() throws Exception {
		Map<String, Ticker> tickers = TickerStubs.getAllTickers();

		when(handler.sendRequest(ArgumentMatchers.any())).thenReturn(BittrexResponseStub.getSuccessfulTickerResponse());
		when(parser.parseMarketSummaries(ArgumentMatchers.any())).thenReturn(tickers);

		Ticker expected = TickerStubs.getBtcEthTicker();
		
		Ticker actual = bittrex.getTickers().get(BITTREX_BTC_ETH_MARKET);

		assertEquals(expected, actual);
	}

	@Test
	void getTickers_whenJsonIsMalformed_shouldThrowExchangeException() throws Exception {
		when(handler.sendRequest(ArgumentMatchers.any())).thenReturn(BittrexResponseStub.getSuccessfulTickerResponse());
		when(parser.parseMarketSummaries(ArgumentMatchers.any())).thenThrow(ExchangeException.class);

		assertThrows(ExchangeException.class, () -> bittrex.getTickers());
	}

	@Test
	void getTickers_whenResponseIsUnsuccessful_shouldThrowExchangeException() throws Exception {
		when(handler.sendRequest(ArgumentMatchers.any())).thenReturn(BittrexResponseStub.getUnsuccessfulResponse());
		when(parser.parseMarketSummaries(BittrexResponseStub.getUnsuccessfulResponse())).thenThrow(ExchangeException.class);

		assertThrows(ExchangeException.class, () -> bittrex.getTickers());
	}

	@Test
	void getBalances_whenAccountIsRegistered_shouldReturnAllBalances() throws Exception {
		Map<String, Double> expected = new HashMap<>();
		expected.put(BTC, 0.5);
		expected.put(ETH, 1.0);

		when(parser.parseBalances(ArgumentMatchers.any())).thenReturn(BalanceStubs.getValidBalances());
		when(handler.sendRequest(ArgumentMatchers.any()))
				.thenReturn(BittrexResponseStub.getSuccessfulBalancesResponse());

		Map<String, Double> actual = bittrex.getBalances(credentials);

		assertEquals(expected, actual);
	}

	@Test
	void getBalances_whenAvailableBalanceIsNull_shouldReturnZero() throws Exception {
		Map<String, Double> expected = new HashMap<>();
		expected.put(BTC, 0.0);
		expected.put(ETH, 0.0);

		when(parser.parseBalances(ArgumentMatchers.any())).thenReturn(BalanceStubs.getNullBalances());
		when(handler.sendRequest(ArgumentMatchers.any())).thenReturn(BittrexResponseStub.getNullBalancesResponse());

		Map<String, Double> actual = bittrex.getBalances(credentials);

		assertEquals(expected, actual);
	}

	@Test
	void getBalances_whenHandlerThrowsIOException_shouldThrowExchangeException() throws Exception {
		when(handler.sendRequest(ArgumentMatchers.any())).thenThrow(IOException.class);

		assertThrows(ExchangeException.class, () -> bittrex.getBalances(credentials));
	}

	@Test
	void getOrderHistory_whenResponseIsSuccessful_shouldReturnListOfOrders() throws Exception {
		when(handler.sendRequest(ArgumentMatchers.any())).thenReturn(BittrexResponseStub.getSuccessfulResponse());
		List<Order> expected = Arrays.asList(new BittrexFilledOrder());
		when(parser.parseOrderHistory(ArgumentMatchers.notNull())).thenReturn(expected);

		List<Order> actual = bittrex.getOrderHistory(credentials);

		assertEquals(expected, actual);
	}

	@Test
	void getOrderHistory_whenResponseIsUnsuccessful_shouldThrowExchangeException()
			throws ExchangeException, IOException {
		when(handler.sendRequest(ArgumentMatchers.any())).thenReturn(BittrexResponseStub.getUnsuccessfulResponse());
		when(parser.parseOrderHistory(BittrexResponseStub.getUnsuccessfulResponse())).thenThrow(ExchangeException.class);

		assertThrows(ExchangeException.class, () -> bittrex.getOrderHistory(credentials));
	}

	@Test
	void getOrderHistory_whenHandlerThrowsIOException_shouldThrowExchangeException() throws Exception {
		when(handler.sendRequest(ArgumentMatchers.any())).thenThrow(IOException.class);

		assertThrows(ExchangeException.class, () -> bittrex.getOrderHistory(credentials));
	}

	@Test
	void getOpenOrders_whenResponseIsSuccessful_shouldReturnListOfOrders() throws Exception {
		when(handler.sendRequest(ArgumentMatchers.any())).thenReturn(BittrexResponseStub.getSuccessfulResponse());
		List<Order> expected = Arrays.asList(new BittrexFilledOrder());
		when(parser.parseOpenOrders(ArgumentMatchers.notNull())).thenReturn(expected);

		List<Order> actual = bittrex.getOpenOrders(credentials);

		assertEquals(expected, actual);
	}

	@Test
	void getOpenOrders_whenHandlerThrowsIOException_shouldThrowExchangeException() throws Exception {
		when(handler.sendRequest(ArgumentMatchers.any())).thenThrow(IOException.class);

		assertThrows(ExchangeException.class, () -> bittrex.getOpenOrders(credentials));
	}

	@Test
	void cancelOrder_whenResponseIsSuccessful_shouldReturnTrue() throws Exception {
		when(handler.sendRequest(ArgumentMatchers.any())).thenReturn(BittrexResponseStub.getSuccessfulResponse());
		when(parser.checkResponse(BittrexResponseStub.getSuccessfulResponse())).thenReturn(true);

		boolean actual = bittrex.cancelOrder(ORDERUUID, credentials);

		assertTrue(actual);
	}

	@Test
	void cancelOrder_whenHandlerThrowsIOException_shouldThrowExchangeException() throws Exception {
		when(handler.sendRequest(ArgumentMatchers.any())).thenThrow(IOException.class);

		assertFalse(bittrex.cancelOrder(ORDERUUID, credentials));
	}

	@Test
	void placeOrder_whenHandlerThrowsIOException_shouldReturnFalse() throws Exception {
		when(handler.sendRequest(ArgumentMatchers.any())).thenThrow(IOException.class);
		MarketOrder order = new MarketOrder();
		order.setAccount(ACCOUNTNAME);
		order.setExchange(EXCHANGENAME);
		order.setAltCoin(ETH);
		order.setMainCoin(BTC);
		
		assertFalse(bittrex.placeOrder(order, credentials));
	}

	@Test
	void placeOrder_whenUnsupportedOrder_shouldReturnFalse() throws Exception {
		when(api.placeOrder(ArgumentMatchers.notNull(), ArgumentMatchers.notNull()))
				.thenThrow(UnsupportedOrderTypeException.class);
		MarketOrder order = new MarketOrder();
		order.setAccount(ACCOUNTNAME);
		order.setExchange(EXCHANGENAME);
		order.setAltCoin(ETH);
		order.setMainCoin(BTC);
		order.setOrderType(OrderType.UNSUPPORTED);
		
		assertFalse(bittrex.placeOrder(order, credentials));
	}

	@Test
	void createMarket_whenProvidedWithTwoCoins_shouldCreateBittrexSpecificMarket() {
		String expected = BITTREX_BTC_ETH_MARKET;
		String actual = bittrex.createMarket(BTC, ETH);

		assertEquals(expected, actual);
	}

}
