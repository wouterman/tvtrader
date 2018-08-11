package tvtrader.stubs.bittrex;

public class JsonOrderHistoryStubs {
	
	/**
	 * Sell order:<br>
	 * Commission: 0.25000000<br>
	 * Limit: 1.00000000<br>
	 * Market: BTC-ETH<br>
	 * OrderType: LIMIT_SELL<br>
	 * OrderUuid: 1<br>
	 * Price: 1.00000000<br>
	 * PricePerUnit: 1.25000000<br>
	 * Quantity: 1.00000000<br>
	 * QuantityRemaining: 0.00000000<br>
	 * TimeStamp: 01-01-2000T00:00:00<br>
	 */
	public static String getOneSellOrder() {
		return "{\"success\" : true,\"message\" : \"\",\"result\" : [" + 
				first_sellOrder +
				"	]" + 
				"}";
	}
	
	/**
	 * Sell order:<br>
	 * Commission: 0.25000000<br>
	 * Limit: 1.00000000<br>
	 * Market: BTC-ETH<br>
	 * OrderType: LIMIT_SELL<br>
	 * OrderUuid: 1<br>
	 * Price: 1.00000000<br>
	 * PricePerUnit: 1.25000000<br>
	 * Quantity: 1.00000000<br>
	 * QuantityRemaining: 0.00000000<br>
	 * TimeStamp: 01-01-2000T00:00:00<br>
	 * <br>
	 * Buy order:<br>
	 * Commission: 0.25000000<br>
	 * Limit: 1.00000000<br>
	 * Market: BTC-ETH<br>
	 * OrderType: LIMIT_BUY<br>
	 * OrderUuid: 2<br>
	 * Price: 1.00000000<br>
	 * PricePerUnit: 1.25000000<br>
	 * Quantity: 1.00000000<br>
	 * QuantityRemaining: 0.00000000<br>
	 * TimeStamp: 31-12-1999T00:00:00<br>
	 */
	public static String getOneSellOneBuyOrder() {
		return "{\"success\" : true,\"message\" : \"\",\"result\" : [" +
				first_sellOrder +
				", " +
				second_buyOrder +
				"	]" + 
				"}";
	}
	
	/**
	 * Sell order:<br>
	 * Commission: 0.25000000<br>
	 * Limit: 1.00000000<br>
	 * Market: BTC-ETH<br>
	 * OrderType: LIMIT_SELL<br>
	 * OrderUuid: 1<br>
	 * Price: 1.00000000<br>
	 * PricePerUnit: 1.25000000<br>
	 * Quantity: 1.00000000<br>
	 * QuantityRemaining: 0.00000000<br>
	 * TimeStamp: 01-01-2000T00:00:00<br>
	 * <br>
	 * Sell order:<br>
	 * Commission: 0.25000000<br>
	 * Limit: 1.00000000<br>
	 * Market: BTC-ETH<br>
	 * OrderType: LIMIT_SELL<br>
	 * OrderUuid: 1<br>
	 * Price: 1.00000000<br>
	 * PricePerUnit: 1.25000000<br>
	 * Quantity: 1.00000000<br>
	 * QuantityRemaining: 0.00000000<br>
	 * TimeStamp: 30-12-1999T00:00:00<br>
	 */
	public static String getTwoSellOrders() {
		return "{\"success\" : true,\"message\" : \"\",\"result\" : [" +
				first_sellOrder +
				", " +
				third_sellOrder +
				"	]" + 
				"}";
	}
	
	/**
	 * Sell order:<br>
	 * Commission: 0.25000000<br>
	 * Limit: 1.00000000<br>
	 * Market: BTC-ETH<br>
	 * OrderType: LIMIT_SELL<br>
	 * OrderUuid: 1<br>
	 * Price: 1.00000000<br>
	 * PricePerUnit: 1.25000000<br>
	 * Quantity: 1.00000000<br>
	 * QuantityRemaining: 0.00000000<br>
	 * TimeStamp: 01-01-2000T00:00:00<br>
	 * <br>
	 * Buy order:<br>
	 * Commission: 0.25000000<br>
	 * Limit: 1.00000000<br>
	 * Market: BTC-ETH<br>
	 * OrderType: LIMIT_BUY<br>
	 * OrderUuid: 2<br>
	 * Price: 1.00000000<br>
	 * PricePerUnit: 1.25000000<br>
	 * Quantity: 1.00000000<br>
	 * QuantityRemaining: 0.00000000<br>
	 * TimeStamp: 31-12-1999T00:00:00<br>
	 * <br>
	 * Sell order:<br>
	 * Commission: 0.25000000<br>
	 * Limit: 1.00000000<br>
	 * Market: BTC-ETH<br>
	 * OrderType: LIMIT_SELL<br>
	 * OrderUuid: 1<br>
	 * Price: 1.00000000<br>
	 * PricePerUnit: 1.25000000<br>
	 * Quantity: 1.00000000<br>
	 * QuantityRemaining: 0.00000000<br>
	 * TimeStamp: 30-12-1999T00:00:00<br>
	 */
	public static String getOneSellOneBuyOneSellAgain() {
		return "{\"success\" : true,\"message\" : \"\",\"result\" : [{" + 
				first_sellOrder +
				", " +
				second_buyOrder +
				", " +
				third_sellOrder +
				"	]" + 
				"}";
	}
	
	/**
	 * Stoploss order:<br>
	 * Commission: 0.25000000<br>
	 * Limit: 1.00000000<br>
	 * Market: BTC-ETH<br>
	 * OrderType: STOP_LOSS<br>
	 * OrderUuid: 1<br>
	 * Price: 1.00000000<br>
	 * PricePerUnit: 1.25000000<br>
	 * Quantity: 1.00000000<br>
	 * QuantityRemaining: 0.00000000<br>
	 * TimeStamp: 28-12-1999T00:00:00<br>
	 */
	public static String getOneStoplossOrder() {
		return "{\"success\" : true,\"message\" : \"\",\"result\" : [" +
				fourth_stoplossOrder +
				"	]" + 
				"}";
	}
	
	/**
	 * Open Orders:<br>
	 * Uuid: null<br>
	 * OrderUuid: 10<br>
	 * OrderType: LIMIT_SELL<br>
	 * Quantity: 1.00000000<br>
	 * QuantityRemaining: 0.00000000<br>
	 * Commission: 0.25000000<br>
	 * Limit: 1.00000000<br>
	 * Market: BTC-ETH<br>
	 * Price: 1.00000000<br>
	 * PricePerUnit: 1.25000000<br>
	 * Opened: 01-12-1999T00:00:00<br>
	 */
	public static String getOpenOrder() {
		return "{\"success\" : true,\"message\" : \"\",\"result\" : [" +
				openOrder +
				"	]" + 
				"}";
	}
	
	private static String first_sellOrder = 
			"{" + 
			"			\"OrderUuid\" : \"1\"," + 
			"			\"Exchange\" : \"BTC-ETH\"," + 
			"			\"TimeStamp\" : \"2000-01-01T00:00:00.000\"," + 
			"			\"OrderType\" : \"LIMIT_SELL\"," + 
			"			\"Limit\" : 1.00000000," + 
			"			\"Quantity\" : 1.00000000," + 
			"			\"QuantityRemaining\" : 0.00000000," + 
			"			\"Commission\" : 0.25000000," + 
			"			\"Price\" : 1.00000000," + 
			"			\"PricePerUnit\" : 1.25000000" + 
			"		}";
	
	private static String second_buyOrder = 
			"{" +
			"			\"OrderUuid\" : \"2\"," + 
			"			\"Exchange\" : \"BTC-ETH\"," + 
			"			\"TimeStamp\" : \"1999-12-31T00:00:00.000\"," + 
			"			\"OrderType\" : \"LIMIT_BUY\"," + 
			"			\"Limit\" : 1.00000000," + 
			"			\"Quantity\" : 1.00000000," + 
			"			\"QuantityRemaining\" : 0.00000000," + 
			"			\"Commission\" : 0.25000000," + 
			"			\"Price\" : 1.00000000," + 
			"			\"PricePerUnit\" : 1.25000000" + 
			"		}";
	
	private static String third_sellOrder = 
			"{" + 
			"			\"OrderUuid\" : \"3\"," + 
			"			\"Exchange\" : \"BTC-ETH\"," + 
			"			\"TimeStamp\" : \"1999-12-30T00:00:00.000\"," + 
			"			\"OrderType\" : \"LIMIT_SELL\"," + 
			"			\"Limit\" : 1.00000000," + 
			"			\"Quantity\" : 1.00000000," + 
			"			\"QuantityRemaining\" : 0.00000000," + 
			"			\"Commission\" : 0.25000000," + 
			"			\"Price\" : 1.00000000," + 
			"			\"PricePerUnit\" : 1.25000000" + 
			"		}";
	
	private static String fourth_stoplossOrder =
			"{" + 
			"			\"OrderUuid\" : \"4\"," + 
			"			\"Exchange\" : \"BTC-ETH\"," + 
			"			\"TimeStamp\" : \"1999-12-28T00:00:00.000\"," + 
			"			\"OrderType\" : \"STOPLOSS\"," + 
			"			\"Limit\" : 1.00000000," + 
			"			\"Quantity\" : 1.00000000," + 
			"			\"QuantityRemaining\" : 0.00000000," + 
			"			\"Commission\" : 0.25000000," + 
			"			\"Price\" : 1.00000000," + 
			"			\"PricePerUnit\" : 1.25000000" + 
			"		}";
	
	private static String openOrder = 
			"{" + 
			"			\"uuid\" : \"null\"," + 
			"			\"OrderUuid\" : \"10\"," + 
			"			\"Exchange\" : \"BTC-ETH\"," + 
			"			\"OrderType\" : \"LIMIT_SELL\"," + 
			"			\"Quantity\" : 1.00000000," + 
			"			\"QuantityRemaining\" : 0.00000000," + 
			"			\"Limit\" : \"1.00000000\"," + 
			"			\"CommissionPaid\" : 0.25000000," + 
			"			\"Price\" : 1.00000000," + 
			"			\"PricePerUnit\" : 1.25000000," + 
			"			\"Opened\" : \"1999-12-01T00:00:00.000\"" + 
			"		}";
			
}
