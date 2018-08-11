package tvtrader.exchange.apidata;

import tvtrader.model.OrderType;

/**
 * Wrapper class for exchange transactions.<br>
 * 
 * @author Wouter
 *
 */
public interface Order extends Comparable<Order> {
	public String getOrderId();
	public String getMainCoin();
	public String getAltCoin();
	public long getTimeStamp();
	public OrderType getOrderType();
	public double getQuantity();
	public double getQuantityRemaining();
	public double getCommission();
	public double getPrice();
	public double getRate();

	/**
	 * Needs to be sorted last to first.
	 */
	@Override
	public default int compareTo(Order other) {
		if (getTimeStamp() > other.getTimeStamp()) {
			return -1;
		} else if (getTimeStamp() == other.getTimeStamp()) {
			return 0;
		} else {
			return 1;
		}
	}
}
