package tvtrader.exchange.apidata;

import tvtrader.exchange.ExchangeException;

import java.util.List;
import java.util.Map;

public interface JsonParser {

	/**
	 * Parses the response for all the tickers.<br>
	 * 
	 * @throws ExchangeException
	 *             If the response from the exchange was not successful.
	 */
	public Map<String, tvtrader.exchange.apidata.Ticker> parseMarketSummaries(String json) throws ExchangeException;

	/**
	 * Parses the response for all the available balances.<br>
	 * 
	 * @throws ExchangeException
	 *             If the response from the exchange was not successful.
	 * 
	 */
	public Map<String, Double> parseBalances(String json) throws ExchangeException;

	/**
	 * Creates all the past orders from the provided JSON order history.
	 *
	 */
	public List<Order> parseOrderHistory(String json) throws ExchangeException;

	/**
	 * Creates a list of all open orders from the provided JSON.
	 * 
	 */
	public List<Order> parseOpenOrders(String json) throws ExchangeException;

	/**
	 * Checks if the response is succesful.
	 * 
	 * @param response
	 *            The json to parse.
	 * @return
	 * 	True if it is.
	 */
	public boolean checkResponse(String response);

}
