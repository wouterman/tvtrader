package tvtrader.exchange;

import java.util.List;
import java.util.Map;

import tvtrader.accounts.ApiCredentials;
import tvtrader.exchange.apidata.Order;
import tvtrader.exchange.apidata.Ticker;
import tvtrader.orders.MarketOrder;

/**
 * Models an exchange and all the requests it should handle.<br>
 * 
 * @author Wouter
 *
 */
public interface Exchange {
	/**
	 * Fetches the latest tickers.<br>
	 * 
	 * @throws ExchangeException
	 *             If anything goes wrong while contacting the exchange.
	 */
	public Map<String, Ticker> getTickers() throws ExchangeException;

	/**
	 * Fetches all the balances for the current account.
	 * 
	 * @param credentials
	 *            The api credentials for the account.
	 *            
	 * @return A Map with all the balances for the account.
	 * @throws ExchangeException
	 *             If anything goes wrong while contacting the exchange.
	 */
	public Map<String, Double> getBalances(ApiCredentials credentials) throws ExchangeException;

	/**
	 * Places the order at the exchange.
	 * 
	 * @param order
	 *            The order to place at the exchange
	 * @param credentials
	 *            The api credentials for the account.	 * 
	 * @return
	 * 	True if success. False if anything goes wrong.
	 */
	public boolean placeOrder(MarketOrder order, ApiCredentials credentials) ;

	/**
	 * Cancels the order connected to the provided orderId and credentials.
	 * 
	 * @param orderId
	 *            The id of the order to cancel.
	 * @param credentials
	 *            The api credentials for the account.
	 * 
	 * @return True if successful. False if anything goes wrong.
	 */
	public boolean cancelOrder(String orderId, ApiCredentials credentials);

	/**
	 * Fetches all the open orders for the specified account.
	 * 
	 * @param credentials
	 *            The api credentials for the account.
	 * 
	 * @throws ExchangeException
	 *             If anything goes wrong while contacting the exchange.
	 * 
	 * @Return A list of TradeOrders representing all the currently unfilled orders
	 *         for this market and account.
	 */
	public List<Order> getOpenOrders(ApiCredentials credentials) throws ExchangeException;

	/**
	 * Fetches the orderhistory for the specified market/account.
	 * 
	 * @param credentials
	 *            The api credentials for the account.
	 * @return A list of TradeOrders representing all the trades made for this account.
	 * 
	 * @throws ExchangeException
	 *             If anything goes wrong while contacting the exchange.
	 */
	public List<Order> getOrderHistory(ApiCredentials credentials)
			throws ExchangeException;

	/**
	 * Creates the proper market syntax for the specific exchange.
	 * 
	 * @param mainCoin
	 *            The main currency of the market.
	 * @param altCoin
	 *            The alt currency of the market
	 * @return BTC-ETH for Bittrex, BTC_ETH for Poloniex, etc.
	 */
	public String createMarket(String mainCoin, String altCoin);

	/**
	 * @return The name of the excchange.
	 */
	public String getName();

	/**
	 * Returns the taker fee per order for the exchange.
	 */
	public double getTakerFee();

	/**
	 * Returns the maker fee per order for the exchange.
	 */
	public double getMakerFee();

	/**
	 * Return the minimum order amount for the exchange.
	 * 
	 */
	public double getMinimumOrderAmount();
}
