package tvtrader.binance;

import org.springframework.beans.factory.annotation.Qualifier;

import lombok.extern.log4j.Log4j2;
import tvtrader.accounts.ApiCredentials;
import tvtrader.exchange.Api;
import tvtrader.exchange.UnsupportedOrderTypeException;
import tvtrader.orders.MarketOrder;
import tvtrader.utils.HashingUtility;
import tvtrader.utils.NonceCreator;
import tvtrader.web.Url;

@Log4j2
public class BinanceApi implements Api {
	private static final String BASE_URL = "https://api.binance.com/api/v1/";

	@Qualifier("BinanceHasher")
	private HashingUtility hasher;

	public BinanceApi(HashingUtility hasher) {
		super();
		this.hasher = hasher;
	}

	@Override
	public Url getMarketSummaries() {
		String parameterizedUrl = buildUrl(BinanceEndpoint.GET_MARKET_SUMMARIES.getEndpoint());
		Url url = new Url(parameterizedUrl);

		log.debug("Build getMarketSummaries url: {}", url.getUrl());
		return url;
	}

	@Override
	public Url placeOrder(MarketOrder order, ApiCredentials credentials) throws UnsupportedOrderTypeException {
		String market = order.getAltCoin()+order.getMainCoin();
		String parameterizedUrl = buildUrl(determineSide(order), setApiKeyParam(credentials.getKey()),
				setMarketParam(market), setQuantityParam(order.getQuantity()), setRateParam(order.getRate()),
				addNonce());

		Url url = new Url(parameterizedUrl);
		addSignatureHeader(url, credentials.getSecret());

		log.debug("Build placeOrder url: {}. Headers: {}", url.getUrl(), url.getHeaders());
		return url;
	}

	@Override
	public Url getBalances(ApiCredentials credentials) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Url getOrderHistory(ApiCredentials credentials) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Url cancelOrder(String orderId, ApiCredentials credentials) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Url getOpenOrders(ApiCredentials credentials) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Returns a nonce as a GET parameter.
	 * 
	 */
	private String addNonce() {
		return "timestamp=" + NonceCreator.getNonce();
	}

	/**
	 * Calculates the signature has from the provided url and adds this as a header.
	 * 
	 */
	private void addSignatureHeader(Url url, String secret) {
		String signatureHash = getSignatureHash(url.getUrl(), secret);
		url.addHeader("apisign", signatureHash);
	}

	private String getSignatureHash(String url, String secret) {
		return hasher.calculateSignature(url, secret);
	}

	/**
	 * Determine the order type.
	 * @throws UnsupportedOrderTypeException 
	 * 
	 * @throws IllegalArgumentException
	 *             If the ordertype is unsupported.
	 */
	private String determineSide(MarketOrder order) throws UnsupportedOrderTypeException  {
		String type;

		switch (order.getOrderType()) {
		case LIMIT_BUY:
			type = "side=BUY";
			break;
		case LIMIT_SELL:
			type = "side=SELL";
			break;
		default:
			throw new UnsupportedOrderTypeException(
					"Ordertype: " + order.getOrderType() + " is not supported for Binance!");
		}
		return type;
	}
	
	
	/**
	 * Returns the api key as a GET parameter.<br>
	 */
	private String setApiKeyParam(String key) {
		return "apikey=" + key;
	}

	/**
	 * Returns the market as a GET parameter.<br>
	 */
	private String setMarketParam(String market) {
		return "market=" + market;
	}

	/**
	 * Returns the rate as a GET parameter.<br>
	 */
	private String setRateParam(double rate) {
		return "rate=" + rate;
	}

	/**
	 * Returns the quantity as a GET parameter.<br>
	 */
	private String setQuantityParam(double quantity) {
		return "quantity=" + quantity;
	}

	/**
	 * Returns the order uuid as a GET parameter.<br>
	 */
	private String setUuidParam(String orderUuid) {
		return "uuid=" + orderUuid;
	}

	/**
	 * Builds the url with the provided parameters and returns it as a string.<br>
	 * Expects the endpoint type to be the first parameter.<br>
	 */
	private String buildUrl(String... parameters) {
		StringBuilder sb = new StringBuilder(BASE_URL);

		appendParameters(sb, parameters);

		return sb.toString();
	}

	private void appendParameters(StringBuilder sb, String... parameters) {
		for (int i = 0; i < parameters.length; i++) {
			if (i > 1) {
				// First parameter doesn't need an '&'
				sb.append("&");
			}

			sb.append(parameters[i]);
		}
	}

}
