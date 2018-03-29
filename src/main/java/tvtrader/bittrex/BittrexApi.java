package tvtrader.bittrex;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import tvtrader.exchange.Api;
import tvtrader.exchange.UnsupportedOrderTypeException;
import tvtrader.model.ApiCredentials;
import tvtrader.orders.MarketOrder;
import tvtrader.utils.HashingUtility;
import tvtrader.utils.NonceCreator;
import tvtrader.web.Url;

/**
 * Wrapper for the Bittrex api. Returns the correct endpoint url for the
 * requested action.
 *
 * @author Wouter
 */
@Log4j2
@Component(value = "BittrexApi")
public class BittrexApi implements Api {
    private static final String MARKET_DELIMITER = "-";
    private static final String BASE_URL = "https://bittrex.com/api/v1.1/";
    private static final String METHOD = "GET";
    private static final String SCHEME = "https";
    private static final String HOST = "bittrex.com";
    private static final String API_PATH = "api/v1.1";

    @Autowired
    @Qualifier("BittrexHasher")
    private HashingUtility hasher;

    @Override
    public Url getMarketSummaries() {
        String parameterizedUrl = buildUrl(BittrexEndpoint.GET_MARKET_SUMMARIES.getEndpoint());

        Url url = new Url(parameterizedUrl);

        log.debug("Build getMarketSummaries url: {}", url.getUrl());
        return url;
    }

    @Override
    public Url placeOrder(MarketOrder order, ApiCredentials credentials) throws UnsupportedOrderTypeException {
        String market = order.getMainCoin() + MARKET_DELIMITER + order.getAltCoin();
        String parameterizedUrl = buildUrl(determineOrderType(order), setApiKeyParam(credentials.getKey()),
                setMarketParam(market), setQuantityParam(order.getQuantity()), setRateParam(order.getRate()),
                addNonce());

        Url url = new Url(parameterizedUrl);

        addSignatureHeader(url, credentials.getSecret());

        log.debug("Build placeOrder url: {}. Headers: {}", url.getUrl(), url.getHeaders());
        return url;
    }

    @Override
    public Url getBalances(ApiCredentials credentials) {
        String parameterizedUrl = buildUrl(BittrexEndpoint.GET_BALANCES.getEndpoint(), setApiKeyParam(credentials.getKey()),
                addNonce());
        Url url = new Url(parameterizedUrl);
        addSignatureHeader(url, credentials.getSecret());

        return url;
    }

    @Override
    public Url getOrderHistory(ApiCredentials credentials) {
        String parameterizedUrl = buildUrl(BittrexEndpoint.GET_ORDERHISTORY.getEndpoint(),
                setApiKeyParam(credentials.getKey()), addNonce());

        Url url = new Url(parameterizedUrl);
        addSignatureHeader(url, credentials.getSecret());

        log.debug("Build getOrderHistory url: {}. Headers: {}", url.getUrl(), url.getHeaders());
        return url;
    }

    @Override
    public Url cancelOrder(String orderUuid, ApiCredentials credentials) {
        String parameterizedUrl = buildUrl(BittrexEndpoint.CANCEL_ORDER.getEndpoint(), setApiKeyParam(credentials.getKey()),
                setUuidParam(orderUuid), addNonce());

        Url url = new Url(parameterizedUrl);
        addSignatureHeader(url, credentials.getSecret());

        log.debug("Build cancelOrder url: {}. Headers: {}", url.getUrl(), url.getHeaders());
        return url;
    }

    @Override
    public Url getOpenOrders(ApiCredentials credentials) {
        String parameterizedUrl = buildUrl(BittrexEndpoint.GET_OPEN_ORDERS.getEndpoint(),
                setApiKeyParam(credentials.getKey()), addNonce());

        Url url = new Url(parameterizedUrl);
        addSignatureHeader(url, credentials.getSecret());

        log.debug("Build getOpenOrders url: {}. Headers: {}", url.getUrl(), url.getHeaders());
        return url;
    }

    /**
     * Returns a nonce as a GET parameter.
     */
    private String addNonce() {
        return "nonce=" + NonceCreator.getNonce();
    }

    /**
     * Calculates the signature has from the provided url and adds this as a header.
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
     *
     * @throws UnsupportedOrderTypeException
     * @throws IllegalArgumentException      If the ordertype is unsupported.
     */
    private String determineOrderType(MarketOrder order) throws UnsupportedOrderTypeException {
        String type;

        switch (order.getOrderType()) {
            case LIMIT_BUY:
                type = BittrexEndpoint.LIMIT_BUY.getEndpoint();
                break;
            case LIMIT_SELL:
                type = BittrexEndpoint.LIMIT_SELL.getEndpoint();
                break;
            default:
                throw new UnsupportedOrderTypeException(
                        "Ordertype: " + order.getOrderType() + " is not supported for Bittrex!");
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

    /**
     * Enum with all the supported Bittrex endpoints.
     */
    enum BittrexEndpoint {
        GET_MARKET_SUMMARIES("public/getmarketsummaries"),
        LIMIT_BUY("market/buylimit?"),
        LIMIT_SELL("market/selllimit?"),
        CANCEL_ORDER("market/cancel?"),
        GET_OPEN_ORDERS("market/getopenorders?"),
        GET_BALANCES("account/getbalances?"),
        GET_ORDERHISTORY("account/getorderhistory?");

        String endpoint;

        BittrexEndpoint(String type) {
            this.endpoint = type;
        }

        public String getEndpoint() {
            return endpoint;
        }
    }
}
