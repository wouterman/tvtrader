package tvtrader.stubs.bittrex;

public class BittrexResponseStub {
	
	/**
	 * Success: true<br>
	 * Message: ""<br>
	 * Result:<br>
	 * Ask: 2.0<br>
	 * Bid: 1.0<br>
	 * Last: 3.0<br>
	 */
	public static String getSuccessfulTickerResponse() {
		return "{\"success\" : true,	"
				+ "\"message\" : \"\","
				+ "\"result\" : "
				+ "{\"Bid\" : 1.0,\"Ask\" : 2.0,\"Last\" : 3.0}}";
	}
	
	/**
	 * Success: false<br>
	 * Message: "INVALID_MARKET"<br>
	 * Result: null<br>
	 */
	public static String getUnsuccessfulResponse () {
		return"{\"success\" : false,	"
				+ "\"message\" : \"INVALID_MARKET\",\""
				+ "result\" : null}";

	}
	
	/**
	 * Success: 123<br>
	 */
	public static String getMalformedResponse() {
		return "{\"success\" : 123 }";
	}
	
	/**
	 * Success: true<br>
	 * Message: ""<br>
	 * Result: null<br>
	 * @return
	 */
	public static String getSuccessfulResponse() {
		return "{\"success\" : true,	\"message\" : \"\",\"result\" : \"null\"}";

	}
	
	/**
	 * Success: true<br>
	 * Message: ""<br>
	 * Result: <br>
	 * Currency: BTC<br>
	 * Balance: 1.0<br>
	 * Available: 0.5<br>
	 * Pending: 0.0<br>
	 * CryptoAddress: 123456<br>
	 * Requested: false<br>
	 * Uuid: null<br>
	 */
	public static String getSuccessfulBalanceResponse() {
		return "{\"success\" : true,	"
				+ "\"message\" : \"\","
				+ "\"result\" : {"
				+ "\"Currency\" : \"BTC\","
				+ "\"Balance\" : 1.0," + "		"
				+ "\"Available\" : 0.5," 
				+ "\"Pending\" : 0.00000000,"
				+ "\"CryptoAddress\" : \"123456\"," 
				+ "\"Requested\" : false," 
				+ "\"Uuid\" : null }}";
	}
	
	/**
	 * Success: true<br>
	 * Message: ""<br>
	 * Result: <br>
	 * Currency: BTC<br>
	 * Balance: null<br>
	 * Available: null<br>
	 * Pending: 0.0<br>
	 * CryptoAddress: 123456<br>
	 * Requested: false<br>
	 * Uuid: null<br>
	 */
	public static String getNullBalanceResponse() {
		return "{\"success\" : true,	"
				+ "\"message\" : \"\","
				+ "\"result\" : {"
				+ "\"Currency\" : \"BTC\","
				+ "\"Balance\" : null," + "		"
				+ "\"Available\" : null," 
				+ "\"Pending\" : 0.00000000,"
				+ "\"CryptoAddress\" : \"123456\"," 
				+ "\"Requested\" : false," 
				+ "\"Uuid\" : null }}";
	}
	
	/**
	 * Success: true<br>
	 * Message: ""<br>
	 * Result: <br>
	 * Currency: BTC<br>
	 * Balance: 1.0<br>
	 * Available: 0.5<br>
	 * Pending: 0.0<br>
	 * CryptoAddress: 123456<br>
	 * Requested: false<br>
	 * Uuid: null<br>
	 * <br>
	 * Currency: ETH<br>
	 * Balance: 2.0<br>
	 * Available: 1.0<br>
	 * Pending: 0.0<br>
	 * CryptoAddress: 123456<br>
	 * Requested: false<br>
	 * Uuid: null<br>
	 */
	public static String getSuccessfulBalancesResponse() {
		return "{\"success\" : true,	"
				+ "\"message\" : \"\","
				+ "\"result\" :"
				+ "["
				+ "{"
				+ "\"Currency\" : \"BTC\","
				+ "\"Balance\" : 1.0,"
				+ "\"Available\" : 0.5," 
				+ "\"Pending\" : 0.00000000,"
				+ "\"CryptoAddress\" : \"123456\"," 
				+ "\"Requested\" : false," 
				+ "\"Uuid\" : null "
				+ "}"
				+","
				+"{"
				+ "\"Currency\" : \"ETH\","
				+ "\"Balance\" : 2.0,"
				+ "\"Available\" : 1.0," 
				+ "\"Pending\" : 0.00000000,"
				+ "\"CryptoAddress\" : \"123456\"," 
				+ "\"Requested\" : false," 
				+ "\"Uuid\" : null "
				+ "}"
				+ "]"
				+ "}";
	}
	
	/**
	 * Success: true<br>
	 * Message: ""<br>
	 * Result: <br>
	 * Currency: BTC<br>
	 * Balance: null<br>
	 * Available: null<br>
	 * Pending: 0.0<br>
	 * CryptoAddress: 123456<br>
	 * Requested: false<br>
	 * Uuid: null<br>
	 * <br>
	 * Currency: ETH<br>
	 * Balance: null<br>
	 * Available: null<br>
	 * Pending: 0.0<br>
	 * CryptoAddress: 123456<br>
	 * Requested: false<br>
	 * Uuid: null<br>
	 */
	public static String getNullBalancesResponse() {
		return "{\"success\" : true,	"
				+ "\"message\" : \"\","
				+ "\"result\" :"
				+ "["
				+ "{"
				+ "\"Currency\" : \"BTC\","
				+ "\"Balance\" : null,"
				+ "\"Available\" : null," 
				+ "\"Pending\" : 0.00000000,"
				+ "\"CryptoAddress\" : \"123456\"," 
				+ "\"Requested\" : false," 
				+ "\"Uuid\" : null "
				+ "}"
				+","
				+"{"
				+ "\"Currency\" : \"ETH\","
				+ "\"Balance\" : null,"
				+ "\"Available\" : null," 
				+ "\"Pending\" : 0.00000000,"
				+ "\"CryptoAddress\" : \"123456\"," 
				+ "\"Requested\" : false," 
				+ "\"Uuid\" : null "
				+ "}"
				+ "]"
				+ "}";
	}

}
