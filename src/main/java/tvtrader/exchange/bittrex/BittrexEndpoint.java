package tvtrader.exchange.bittrex;

/**
 * Enum with all the supported Bittrex endpoints.
 * 
 * @author Wouter
 *
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
