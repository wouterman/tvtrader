package tvtrader.stubs;

import java.util.HashMap;
import java.util.Map;

import tvtrader.bittrex.BittrexTicker;
import tvtrader.exchange.apidata.Ticker;

public class TickerStubs {
	private static String btc1st = "BTC-1ST";
	private static String btcArk = "BTC-ARK";
	private static String btcEth = "BTC-ETH";
	
	private static double ask = 1.0;
	private static double bid = 2.0;
	private static double last = 3.0;
	
	private static final Ticker BTC1ST = new BittrexTicker(btc1st, ask, bid, last);	
	private static final Ticker BTCARK = new BittrexTicker(btcArk, ask, bid, last);	
	private static final Ticker BTCETH = new BittrexTicker(btcEth, ask, bid, last);	
	private static final Ticker NULL_TICKER = new BittrexTicker(btc1st, 0, 0, 0);	
	
	/**
	 * Market: BTC-1ST<br>
	 * Ask: 1.0<br>
	 * Bid: 2.0<br>
	 * Last: 3.0<br>
	 */
	public static Ticker getBtc1stTicker() {
		return BTC1ST;
	}
	
	/**
	 * Market: BTC-ARK<br>
	 * Ask: 1.0<br>
	 * Bid: 2.0<br>
	 * Last: 3.0<br>
	 */
	public static Ticker getBtcArkTicker() {
		return BTCARK;
	}
	
	/**
	 * Market: BTC-ETH<br>
	 * Ask: 1.0<br>
	 * Bid: 2.0<br>
	 * Last: 3.0<br>
	 */
	public static Ticker getBtcEthTicker() {
		return BTCETH;
	}
	
	/**
	 * Market: BTC-1ST<br>
	 * Ask: 0.0<br>
	 * Bid: 0.0<br>
	 * Last: 0.0<br>
	 */
	public static Ticker getNullTicker() {
		return NULL_TICKER;
	}

	public static Map<String, Ticker> getAllTickers() {
		Map<String, Ticker> tickers = new HashMap<>();
		
		tickers.put(btc1st, BTC1ST);
		tickers.put(btcArk, BTCARK);
		tickers.put(btcEth, BTCETH);
		
		return tickers;
	}
	
}
