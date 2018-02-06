package tvtrader.exchange;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import lombok.extern.log4j.Log4j2;
import tvtrader.accounts.ApiCredentials;
import tvtrader.exchange.apidata.JsonParser;
import tvtrader.exchange.apidata.Order;
import tvtrader.exchange.apidata.Ticker;
import tvtrader.orders.MarketOrder;
import tvtrader.web.RequestHandler;
import tvtrader.web.Url;

/**
 * Provides default behavior that is most likely exchange independent.<br>
 * <br>
 * Concrete classes should provide implementations for any exchange specific
 * behavior.<br>
 * 
 * @author Wouter
 *
 */
@Log4j2
public abstract class Exchange implements IExchange {
	private Api api;
	private RequestHandler handler;
	private JsonParser parser;

	protected Exchange(Api api, RequestHandler handler, JsonParser parser) {
		log.debug("Creating new {} exchange.", getName());
		this.api = api;
		this.handler = handler;
		this.parser = parser;
	}

	@Override
	public Map<String, Ticker> getTickers() throws ExchangeException {
		Url url = api.getMarketSummaries();

		try {
			String response = handler.sendRequest(url);

			return parser.parseMarketSummaries(response);
		} catch (IOException | ExchangeException e) {
			throw new ExchangeException("Couldn't get tickers from " + getName(), e);
		}
	}

	@Override
	public Map<String, Double> getBalances(ApiCredentials credentials) throws ExchangeException {
		log.debug("Exchange: {}. Fetching {} balances for {}", getName(), credentials.getKey());

		try {
			Url url = api.getBalances(credentials);
			String response = handler.sendRequest(url);

			return parser.parseBalances(response);
		} catch (IOException e) {
			throw new ExchangeException("Couldn't get balances for " + credentials.getKey() + " at " + getName(), e);
		}
	}

	@Override
	public boolean placeOrder(MarketOrder order, ApiCredentials credentials) {
		log.debug("Exchange: {}. Placing order for {}", getName(), credentials.getKey());

		try {
			Url url = api.placeOrder(order, credentials);

			String response = handler.sendRequest(url);

			return parser.checkResponse(response);
		} catch (UnsupportedOrderTypeException | IOException e) {
			log.info("Couldn't place order for {}. Received the following message: {}", order.getAccount(),
					e.getMessage());
			log.debug("Received exception: ", e);
			return false;
		}
	}

	@Override
	public boolean cancelOrder(String orderId, ApiCredentials credentials) {
		log.debug("Exchange: {}. Canceling order for {}", getName(), credentials.getKey());

		try {
			Url url = api.cancelOrder(orderId, credentials);

			String response = handler.sendRequest(url);

			return parser.checkResponse(response);
		} catch (IOException e) {
			log.info("Couldn't cancel order {}. Received the following message: {}", orderId, e.getMessage());
			log.debug("Received exception: ", e);
			return false;
		}
	}

	@Override
	public List<Order> getOpenOrders(ApiCredentials credentials) throws ExchangeException {
		log.debug("Fetching open orders for {}.", credentials.getKey());

		String response;
		try {
			Url url = api.getOpenOrders(credentials);
			response = handler.sendRequest(url);

			return parser.parseOpenOrders(response);
		} catch (IOException e) {
			throw new ExchangeException(
					"Couldn't fetch open orders for account: " + credentials.getKey() + " on exchange: " + getName(),
					e);
		}
	}

	@Override
	public List<Order> getOrderHistory(ApiCredentials credentials) throws ExchangeException {
		log.debug("Fetching {} orderhistory for {}.", credentials.getKey());

		String response;
		try {
			Url url = api.getOrderHistory(credentials);
			response = handler.sendRequest(url);

			return parser.parseOrderHistory(response);
		} catch (IOException e) {
			throw new ExchangeException("Couldn't fetch the order history for account: " + credentials.getKey()
					+ " on exchange: " + getName() + ".", e);
		}
	}

	@Override
	public String toString() {
		return getName();
	}

}
