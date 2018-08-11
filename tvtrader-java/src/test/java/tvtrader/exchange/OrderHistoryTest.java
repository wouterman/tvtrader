package tvtrader.exchange;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import tvtrader.exchange.apidata.Order;
import tvtrader.model.OrderType;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
class OrderHistoryTest {
	private static final double BOUGHT_PRICE_FIRST_BUY_ORDER = 1.25;
	private static final double BOUGHT_PRICE_SECOND_BUY_ORDER = 1.25;
	private static final String BTC = "BTC";
	private static final String ETH = "ETH";
	private OrderHistory history;
	
	@Mock
	Order buyOrder;
	
	@Mock
	Order sellOrder;

	@BeforeEach
	void setup() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		when(buyOrder.getCommission()).thenReturn(0.25);
		when(buyOrder.getOrderId()).thenReturn("1");
		when(buyOrder.getMainCoin()).thenReturn(BTC);
		when(buyOrder.getAltCoin()).thenReturn(ETH);
		when(buyOrder.getPrice()).thenReturn(1.0);
		when(buyOrder.getRate()).thenReturn(1.25);
		when(buyOrder.getQuantityRemaining()).thenReturn(0.0);
		when(buyOrder.getQuantity()).thenReturn(1.0);
		when(buyOrder.getTimeStamp())
		.thenReturn(LocalDateTime.of(2000, 01, 01, 00, 00, 00).toEpochSecond(ZoneOffset.UTC));
		when(buyOrder.getOrderType()).thenReturn(OrderType.LIMIT_BUY);
		
		when(sellOrder.getOrderType()).thenReturn(OrderType.LIMIT_SELL);
		
		history = new OrderHistory();
	}

	@Test
	void getTotalBoughtPrice_whenOneBuyOrder_expectPriceSet() {
		List<Order> orders = Arrays.asList(buyOrder);

		double expected = BOUGHT_PRICE_FIRST_BUY_ORDER;

		assertEquals(expected, history.getTotalBoughtPrice(orders));
	}

	@Test
	void getTotalBoughtPrice_whenOneBuyOrderOneSellOrder_expectSellOrderIgnored() {
		List<Order> orders = Arrays.asList(buyOrder, sellOrder);
		
		double expected = BOUGHT_PRICE_FIRST_BUY_ORDER;

		double actual = history.getTotalBoughtPrice(orders);

		assertEquals(expected, actual);
	}

	@Test
	void getTotalBoughtPrice_whenOneBuyOrderOneSellOrderThenBuyOrderAgain_expectSellAndSecondBuyOrderIgnored() {
		List<Order> orders = Arrays.asList(buyOrder, sellOrder, buyOrder);
		
		double expected = BOUGHT_PRICE_FIRST_BUY_ORDER;

		double actual = history.getTotalBoughtPrice(orders);

		assertEquals(expected, actual);
	}

	@Test
	void getTotalBoughtPrice_whenTwoBuyOrders_expectPricesSummed() {
		List<Order> orders = Arrays.asList(buyOrder, buyOrder);

		double expected = BOUGHT_PRICE_FIRST_BUY_ORDER + BOUGHT_PRICE_SECOND_BUY_ORDER;

		double actual = history.getTotalBoughtPrice(orders);

		assertEquals(expected, actual);
	}
	
}
