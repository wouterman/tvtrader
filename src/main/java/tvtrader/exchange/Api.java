package tvtrader.exchange;

import tvtrader.model.ApiCredentials;
import tvtrader.orders.MarketOrder;
import tvtrader.web.Url;

/**
 * Interface that models an exchange api.<br>
 * Concrete classes should provide implementations for the specific endpoints and signatures.<br>
 * 
 * @author Wouter
 *
 */
public interface Api {
	
	/**
	 * Creates the url for all market summaries.
	 */
	public Url getMarketSummaries();

	/**
	 * Creates the url and signature to place this order.
	 * 
	 */
	public Url placeOrder(MarketOrder order, ApiCredentials credentials) throws UnsupportedOrderTypeException;

	/**
	 * Creates a url and signature to fetch all the balances.
	 * 
	 */
	public Url getBalances(ApiCredentials credentials);

	/**
	 * Creates a url and signature to fetch the order history for the given account.
	 * 
	 */
	public Url getOrderHistory(ApiCredentials credentials);

	/**
	 * Creates the url and signature to cancel the order connected to this id.
	 * 
	 */
	public Url cancelOrder(String orderId, ApiCredentials credentials);

	/**
	 * Creates the url and signature to fetch all the open orders for the account.
	 * 
	 */
	public Url getOpenOrders(ApiCredentials credentials);
}
