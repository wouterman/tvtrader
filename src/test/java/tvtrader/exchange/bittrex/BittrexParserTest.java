package tvtrader.exchange.bittrex;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.gson.Gson;

import test.logger.Logger;
import tvtrader.exchange.ExchangeException;
import tvtrader.exchange.apidata.Order;
import tvtrader.exchange.apidata.JsonParser;
import tvtrader.exchange.apidata.Ticker;
import tvtrader.orders.OrderType;
import tvtrader.stubs.TickerStubs;
import tvtrader.stubs.bittrex.BittrexMarketSummaryStub;
import tvtrader.stubs.bittrex.BittrexResponseStub;
import tvtrader.stubs.bittrex.JsonOrderHistoryStubs;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
class BittrexParserTest {
	private static final String ETH = "ETH";
	private static final String BTC = "BTC";

	private static final String BTC1ST = "BTC-1ST";
	private static final String BTCARK = "BTC-ARK";
	private static final String BTCETH = "BTC-ETH";

	private JsonParser parser;
	
	@Mock
	Order expected;

	@BeforeAll
	synchronized static void startup() {
		Logger.turnOffLogging();
	}

	@BeforeEach
	void setup() {
		MockitoAnnotations.initMocks(this);
		
		when(expected.getCommission()).thenReturn(0.25);
		when(expected.getMainCoin()).thenReturn(BTC);
		when(expected.getAltCoin()).thenReturn(ETH);
		when(expected.getOrderType()).thenReturn(OrderType.LIMIT_SELL);
		when(expected.getPrice()).thenReturn(1.0);
		when(expected.getRate()).thenReturn(1.25);
		when(expected.getQuantityRemaining()).thenReturn(0.0);
		when(expected.getQuantity()).thenReturn(1.0);
		
		Gson gson = new Gson();
		parser = new BittrexParser(gson);
	}

	@Test
	void parseMarketSummaries_whenProvidedWithJsonMarketSummary_shouldBuildTickersFromThatSummary() throws Exception {
		String validMarketSummary = BittrexMarketSummaryStub.getValidJsonMarketSummary();
		Map<String, Ticker> tickers = parser.parseMarketSummaries(validMarketSummary);

		assertEquals(3, tickers.size());
		assertEquals(TickerStubs.getBtc1stTicker(), tickers.get(BTC1ST));
		assertEquals(TickerStubs.getBtcArkTicker(), tickers.get(BTCARK));
		assertEquals(TickerStubs.getBtcEthTicker(), tickers.get(BTCETH));
	}

	@Test
	void parseMarketSummaries_whenProvidedWithNullTicker_shouldSetFieldToZero() throws Exception {
		String nullTickerMarketSummary = BittrexMarketSummaryStub.getNullTickerMarketSummary();
		Map<String, Ticker> tickers = parser.parseMarketSummaries(nullTickerMarketSummary);

		assertEquals(1, tickers.size());
		assertEquals(TickerStubs.getNullTicker(), tickers.get(BTC1ST));
	}
	
	@Test
	void parseMarketSummaries_whenResponseUnsuccessful_shouldThrowExchangeException() throws Exception {
		String unsuccessfulResponse = BittrexResponseStub.getUnsuccessfulResponse();

		assertThrows(ExchangeException.class, () -> parser.parseMarketSummaries(unsuccessfulResponse));
	}

	@Test
	void parseBalances_whenProvidedWithValidJson_shouldReturnBalances() throws Exception {
		Map<String, Double> expected = new HashMap<>();
		expected.put(BTC, 0.5);
		expected.put(ETH, 1.0);

		String validBalances = BittrexResponseStub.getSuccessfulBalancesResponse();

		Map<String, Double> actual = parser.parseBalances(validBalances);

		assertEquals(expected, actual);
	}

	@Test
	void parseBalances_whenAvailableBalanceIsNull_shouldReturnZero() throws Exception {
		Map<String, Double> expected = new HashMap<>();
		expected.put(BTC, 0.0);
		expected.put(ETH, 0.0);

		String nullBalances = BittrexResponseStub.getNullBalancesResponse();

		Map<String, Double> actual = parser.parseBalances(nullBalances);

		assertEquals(expected, actual);
	}
	
	@Test
	void parseBalances_whenResponseUnsuccessful_shouldThrowExchangeException() throws Exception {
		String unsuccessfulResponse = BittrexResponseStub.getUnsuccessfulResponse();

		assertThrows(ExchangeException.class, () -> parser.parseBalances(unsuccessfulResponse));
	}

	@Test
	void parseOrderHistory_oneSellOrder_expectOneSellOrder() throws Exception {
		String orders = JsonOrderHistoryStubs.getOneSellOrder();

		when(expected.getOrderId()).thenReturn("1");
		when(expected.getTimeStamp())
				.thenReturn(LocalDateTime.of(2000, 01, 01, 00, 00, 00).toEpochSecond(ZoneOffset.UTC));

		List<Order> actuals = parser.parseOrderHistory(orders);

		assertEquals(1, actuals.size());
		Order actual = actuals.get(0);

		assertAll("actual", () -> assertEquals(expected.getCommission(), actual.getCommission()),
				() -> assertEquals(expected.getCommission(), actual.getCommission()),
				() -> assertEquals(expected.getMainCoin(), actual.getMainCoin()),
				() -> assertEquals(expected.getAltCoin(), actual.getAltCoin()),
				() -> assertEquals(expected.getOrderType(), actual.getOrderType()),
				() -> assertEquals(expected.getOrderId(), actual.getOrderId()),
				() -> assertEquals(expected.getPrice(), actual.getPrice()),
				() -> assertEquals(expected.getRate(), actual.getRate()),
				() -> assertEquals(expected.getQuantityRemaining(), actual.getQuantityRemaining()),
				() -> assertEquals(expected.getQuantity(), actual.getQuantity()),
				() -> assertEquals(expected.getTimeStamp(), actual.getTimeStamp()));
	}

	@Test
	void parseOrderHistory_oneStoplossOrder_expectOneOrderWithOrdertypeNull() throws Exception {
		String orders = JsonOrderHistoryStubs.getOneStoplossOrder();

		List<Order> actual = parser.parseOrderHistory(orders);

		assertAll("actual", () -> assertEquals(1, actual.size()),
				() -> assertEquals(OrderType.UNSUPPORTED, actual.get(0).getOrderType()));
	}
	
	@Test
	void parseOrderHistory_whenResponseUnsuccessful_shouldThrowExchangeException() throws Exception {
		String unsuccessfulResponse = BittrexResponseStub.getUnsuccessfulResponse();

		assertThrows(ExchangeException.class, () -> parser.parseOrderHistory(unsuccessfulResponse));
	}

	@Test
	void parseOpenOrders_whenProvidedWithOpenOrdersHistory_expectOpenOrder() throws Exception {
		String orders = JsonOrderHistoryStubs.getOpenOrder();

		when(expected.getOrderId()).thenReturn("10");
		when(expected.getTimeStamp())
				.thenReturn(LocalDateTime.of(1999, 12, 01, 00, 00, 00).toEpochSecond(ZoneOffset.UTC));

		List<Order> actuals = parser.parseOpenOrders(orders);

		assertEquals(1, actuals.size());
		Order actual = actuals.get(0);

		assertAll("actual", () -> assertEquals(expected.getCommission(), actual.getCommission()),
				() -> assertEquals(expected.getMainCoin(), actual.getMainCoin()),
				() -> assertEquals(expected.getAltCoin(), actual.getAltCoin()),
				() -> assertEquals(expected.getOrderType(), actual.getOrderType()),
				() -> assertEquals(expected.getOrderId(), actual.getOrderId()),
				() -> assertEquals(expected.getPrice(), actual.getPrice()),
				() -> assertEquals(expected.getRate(), actual.getRate()),
				() -> assertEquals(expected.getQuantityRemaining(), actual.getQuantityRemaining()),
				() -> assertEquals(expected.getQuantity(), actual.getQuantity()),
				() -> assertEquals(expected.getTimeStamp(), actual.getTimeStamp()));
	}

	@Test
	void parseOpenOrder_whenResponseUnsuccessful_shouldThrowExchangeException() throws Exception {
		String unsuccessfulResponse = BittrexResponseStub.getUnsuccessfulResponse();

		assertThrows(ExchangeException.class, () -> parser.parseOpenOrders(unsuccessfulResponse));
	}
}
