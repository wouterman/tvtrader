package tvtrader.exchange;

import org.springframework.stereotype.Component;
import tvtrader.exchange.apidata.Order;
import tvtrader.model.OrderType;

import java.util.List;

/**
 * Interface modeling the order history from the exchange.
 * 
 * @author Wouter
 *
 */
@Component
public class OrderHistory  {

	/**
	 * Returns the total bought price for the last (unsold) buy orders.
	 * @param exchangeName 
	 */
	public double getTotalBoughtPrice(List<Order> tradeOrders) {
		double sum = 0;
		for (Order to : tradeOrders) {
			if (to.getOrderType() == OrderType.LIMIT_BUY) {
				sum += (to.getPrice() + to.getCommission());
			} else if (to.getOrderType() == OrderType.LIMIT_SELL) {
				break;
			}
		}
		return sum;
	}
}
