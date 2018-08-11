package tvtrader.stubs.bittrex;

public class BittrexMarketSummaryStub {
	
	public static String getValidJsonMarketSummary() {
		return tickerHistory;
	}
	
	public static String getNullTickerMarketSummary() {
		return startMsg
				+ "[" 
				+ nullTicker
				+ "] "
				+ "}";
	}
	
	private static String startMsg = "{ \"success\": true, \"message\": \"\", \"result\": ";
	
	private static String nullTicker = "{"
			+ "\"MarketName\": \"BTC-1ST\","
			+ "\"High\": null,"
			+ "\"Low\": null,"
			+ "\"Volume\": null," 
			+ "\"Last\": null," 
			+ "\"BaseVolume\": null,"
			+ "\"TimeStamp\": \"2018-01-06T18:43:06.96\","
			+ "\"Bid\": null,"
			+ "\"Ask\": null,"
			+ "\"OpenBuyOrders\": null," 
			+ "\"OpenSellOrders\": null," 
			+ "\"PrevDay\": null,"
			+ "\"Created\": \"2017-06-06T01:22:35.727\"" 
			+ "}";
	
	private static String btc1st = "{"
			+ "\"MarketName\": \"BTC-1ST\","
			+ "\"High\": 0.00011099,"
			+ "\"Low\": 0.00009101,"
			+ "\"Volume\": 3656048.08729169," 
			+ "\"Last\": 3.0," 
			+ "\"BaseVolume\": 366.33505052,"
			+ "\"TimeStamp\": \"2018-01-06T18:43:06.96\","
			+ "\"Bid\": 2.0,"
			+ "\"Ask\": 1.0,"
			+ "\"OpenBuyOrders\": 777," 
			+ "\"OpenSellOrders\": 4128," 
			+ "\"PrevDay\": 0.00010328,"
			+ "\"Created\": \"2017-06-06T01:22:35.727\"" 
			+ "}";
	
	private static String btcArk = 	"{" + 
			"\"MarketName\": \"BTC-ARK\","
			+ "\"High\": 0.00000242,"
			+ "\"Low\": 0.00000146," 
			+ "\"Volume\": 39431891.57216151,"
			+ "\"Last\": 3.0," 
			+ "\"BaseVolume\": 74.06446937,"
			+ "\"TimeStamp\": \"2018-01-06T18:43:07.663\"," 
			+ "\"Bid\": 2.0,"
			+ "\"Ask\": 1.0," 
			+ "\"OpenBuyOrders\": 562," 
			+ "\"OpenSellOrders\": 852,"
			+ "\"PrevDay\": 0.00000149," 
			+ "\"Created\": \"2016-05-16T06:44:15.287\"" 
			+ "}";
	
	private static String btcEth = "{"
			+ "\"MarketName\": \"BTC-ETH\"," 
			+ "\"High\": 0.0000022," 
			+ "\"Low\": 0.00000142,"
			+ "\"Volume\": 59116811.64618425," 
			+ "\"Last\": 3.0,"
			+ "\"BaseVolume\": 111.95328865,"
			+ "\"TimeStamp\": \"2018-01-06T18:43:06.9\"," 
			+ "\"Bid\": 2.0,"
			+ "\"Ask\": 1.0,"
			+ "\"OpenBuyOrders\": 556,"
			+ "\"OpenSellOrders\": 2436," 
			+ "\"PrevDay\": 0.00000142,"
			+ "\"Created\": \"2014-10-31T01:43:25.743\""
			+ "} ";
	
	private static String tickerHistory = startMsg
			+ "[" 
			+ btc1st
			+ "," 
			+ btcArk
			+ ","
			+ btcEth
			+ "] "
			+ "}";
	
}
