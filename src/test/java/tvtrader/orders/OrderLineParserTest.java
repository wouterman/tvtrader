package tvtrader.orders;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import test.logger.Logger;
import tvtrader.exchange.ExchangeException;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
class OrderLineParserTest {
	private static final String EXCHANGE_NAME = "EXCHANGE";
	private static final String BTC_ACCOUNT_NAME = "DEFAULTBTC";
	private static final String EMPTY = "";
	private static final String VALID_BUY_ORDER_LINE = "TradingView Alert: BUY_EXCHANGE_DEFAULTBTC_ETH";
	private static final String VALID_SELL_ORDER_LINE = "TradingView Alert: SELL_EXCHANGE_DEFAULTBTC_ETH";
	private static final String INVALID_ORDERTYPE = "TradingView Alert: INVALID_EXCHANGE_DEFAULTBTC_ETH";
	private static final String INVALID_ORDER_LINE = "TradingView Alert: INVALID_PATTERN";
	private static final String ETH = "ETH";
	private static final double NOT_SET = 0.0;
	
	private OrderLineParser orderBuilder;
	
	@BeforeAll
	synchronized  static void startup() {
		Logger.turnOffLogging();
	}
	
	@BeforeEach
	void setup() throws ExchangeException {
		MockitoAnnotations.initMocks(this);
		
		orderBuilder = new OrderLineParser();
	}

	@Test
	void createOrders_whenBuyOrderMailIsRetrieved_shouldCreateMatchingBuyOrder() throws ExchangeException {
		Optional<MarketOrder> actual = orderBuilder.parseOrderLine(VALID_BUY_ORDER_LINE);

		assertAll("actual",
				() -> assertTrue(actual.isPresent()),
				() -> assertEquals(OrderType.LIMIT_BUY, actual.get().getOrderType()),
				() -> assertEquals(EXCHANGE_NAME, actual.get().getExchange()),
				() -> assertEquals(BTC_ACCOUNT_NAME, actual.get().getAccount()),
				() -> assertEquals(EMPTY, actual.get().getMainCoin()),
				() -> assertEquals(ETH, actual.get().getAltCoin()),
				() -> assertEquals(NOT_SET, actual.get().getQuantity()),
				() -> assertEquals(NOT_SET, actual.get().getRate()));
	}
	
	@Test
	void createOrders_whenSellOrderMailIsRetrieved_shouldCreateMatchingSellOrder() throws ExchangeException {
		Optional<MarketOrder> actual = orderBuilder.parseOrderLine(VALID_SELL_ORDER_LINE);

		assertAll("actual", 
				() -> assertTrue(actual.isPresent()),
				() -> assertEquals(OrderType.LIMIT_SELL, actual.get().getOrderType()),
				() -> assertEquals(EXCHANGE_NAME, actual.get().getExchange()),
				() -> assertEquals(BTC_ACCOUNT_NAME, actual.get().getAccount()),
				() -> assertEquals(EMPTY, actual.get().getMainCoin()),
				() -> assertEquals(ETH, actual.get().getAltCoin()),
				() -> assertEquals(NOT_SET, actual.get().getQuantity()),
				() -> assertEquals(NOT_SET, actual.get().getRate()));
	}
	
	@Test
	void createOrders_whenInvalidOrderTypeMailIsRetrieved_shouldSetUnsupportedOrderType() throws ExchangeException {
		Optional<MarketOrder> actual = orderBuilder.parseOrderLine(INVALID_ORDERTYPE);

		assertAll("actual", 
				() -> assertTrue(actual.isPresent()),
				() -> assertEquals(OrderType.UNSUPPORTED, actual.get().getOrderType()),
				() -> assertEquals(EXCHANGE_NAME, actual.get().getExchange()),
				() -> assertEquals(BTC_ACCOUNT_NAME, actual.get().getAccount()),
				() -> assertEquals(EMPTY, actual.get().getMainCoin()),
				() -> assertEquals(ETH, actual.get().getAltCoin()),
				() -> assertEquals(NOT_SET, actual.get().getQuantity()),
				() -> assertEquals(NOT_SET, actual.get().getRate()));
	}
	
	@Test
	void createOrder_whenAlertMailHasInvalidOrderPattern_shouldIgnoreMails() throws ExchangeException {
		Optional<MarketOrder> actual = orderBuilder.parseOrderLine(INVALID_ORDER_LINE);
		
		assertFalse(actual.isPresent());
	}
}
