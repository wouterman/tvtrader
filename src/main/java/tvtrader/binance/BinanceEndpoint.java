package tvtrader.binance;

public enum BinanceEndpoint {
	GET_MARKET_SUMMARIES("ticker/24hr"), 
	TRADE("/api/v3/order/test"), 
	CANCEL_ORDER(""), 
	GET_OPEN_ORDERS(""), 
	GET_BALANCES(""), 
	GET_ORDERHISTORY("");

	String endpoint;

	BinanceEndpoint(String type) {
		this.endpoint = type;
	}

	public String getEndpoint() {
		return endpoint;
	}
}
