package tvtrader.bittrex;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import tvtrader.exchange.Api;
import tvtrader.exchange.Exchange;
import tvtrader.exchange.ExchangeException;
import tvtrader.exchange.UnsupportedOrderTypeException;
import tvtrader.exchange.apidata.JsonParser;
import tvtrader.exchange.apidata.Order;
import tvtrader.exchange.apidata.Ticker;
import tvtrader.model.ApiCredentials;
import tvtrader.model.MarketOrder;
import tvtrader.request.Url;
import tvtrader.services.WebService;

import java.util.List;
import java.util.Map;

/**
 * Representation of Bittrex.<br>
 * <br>
 * 
 * @author Wouter
 *
 */
@Log4j2
@Component(value = "Bittrex")
public class Bittrex implements Exchange {
	private static final String NAME = "BITTREX";
	private static final String MARKET_DELIMITER = "-";
	private static final double MINIMUM_ORDER_AMOUNT = 0.0005;
	private static final String RECEIVED_RESPONSE = "Received response: {}";

	/**
	 * Bittrex uses one fee for all orders.
	 */
	private static final double TAKER_FEE = 0.0025;
	private static final double MAKER_FEE = 0.0025;

	@Autowired
	@Qualifier("BittrexApi")
	private Api api;

	@Autowired
	@Qualifier("BittrexParser")
	private JsonParser parser;

	@Autowired
	private WebService webService;

	@Override
	public String createMarket(String mainCoin, String altCoin) {
		return mainCoin + MARKET_DELIMITER + altCoin;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public double getTakerFee() {
		return TAKER_FEE;
	}

	@Override
	public double getMakerFee() {
		return MAKER_FEE;
	}

	@Override
	public double getMinimumOrderAmount() {
		return MINIMUM_ORDER_AMOUNT;
	}

	@Override
	public Map<String, Ticker> getTickers() throws ExchangeException {
		Url url = api.getMarketSummaries();

		try {
			String response = webService.sendRequest(url);
			log.debug(RECEIVED_RESPONSE, response);
			return parser.parseMarketSummaries(response);
		} catch (ExchangeException e) {
			throw new ExchangeException("Couldn't get tickers from " + getName(), e);
		}
	}

	@Override
	public Map<String, Double> getBalances(ApiCredentials credentials) throws ExchangeException {
		try {
			Url url = api.getBalances(credentials);
			String response = webService.sendRequest(url);
			log.debug(RECEIVED_RESPONSE, response);

			return parser.parseBalances(response);
		} catch (ExchangeException e) {
			throw new ExchangeException("Couldn't get balances for " + credentials.getKey() + " at " + getName(), e);
		}
	}

	@Override
	public boolean placeOrder(MarketOrder order, ApiCredentials credentials) {
		try {
			Url url = api.placeOrder(order, credentials);

			String response = webService.sendRequest(url);
			log.debug(RECEIVED_RESPONSE, response);

			return parser.checkResponse(response);
		} catch (UnsupportedOrderTypeException | ExchangeException e) {
			log.info("Couldn't place order for {}. Received the following message: {}", order.getAccount(),
					e.getMessage());
			log.debug("Received exception: ", e);
			return false;
		}
	}

	@Override
	public boolean cancelOrder(String orderId, ApiCredentials credentials) {
		try {
			Url url = api.cancelOrder(orderId, credentials);

			String response = webService.sendRequest(url);
			log.debug(RECEIVED_RESPONSE, response);

			return parser.checkResponse(response);
		} catch (ExchangeException e) {
			log.info("Couldn't cancel order {}. Received the following message: {}", orderId, e.getMessage());
			log.debug("Received exception: ", e);
			return false;
		}
	}

	@Override
	public List<Order> getOpenOrders(ApiCredentials credentials) throws ExchangeException {
		try {
			Url url = api.getOpenOrders(credentials);
			String response = webService.sendRequest(url);
			log.debug(RECEIVED_RESPONSE, response);

			return parser.parseOpenOrders(response);
		} catch (ExchangeException e) {
			throw new ExchangeException(
					"Couldn't fetch open orders for account: " + credentials.getKey() + " on exchange: " + getName(),
					e);
		}
	}

	@Override
	public List<Order> getOrderHistory(ApiCredentials credentials) throws ExchangeException {
		try {
			Url url = api.getOrderHistory(credentials);
			String response = webService.sendRequest(url);
			log.debug(RECEIVED_RESPONSE, response);

			return parser.parseOrderHistory(response);
		} catch (ExchangeException e) {
			throw new ExchangeException("Couldn't fetch the order history for account: " + credentials.getKey()
					+ " on exchange: " + getName() + ".", e);
		}
	}

	@Override
	public String toString() {
		return getName();
	}

}
