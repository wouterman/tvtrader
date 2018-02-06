package tvtrader.orders;

/**
 * Enum for order types.
 * 
 * @author Wouter
 *
 */
public enum OrderType {
	LIMIT_BUY("limit_buy"),
	LIMIT_SELL("limit_sell"),
	UNSUPPORTED("Ordertype not supported!");
	
	String type;
	
	OrderType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
}
