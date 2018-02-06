package tvtrader.caches;

import java.util.Map;

import lombok.Getter;
import tvtrader.exchange.apidata.Ticker;

public class TickerCache {
	@Getter private long lastRefresh;
	private Map<String, Ticker> tickers;
	
	public Ticker getTicker(String market) {
		return tickers.get(market);
	}
	
	public void refreshCache(Map<String, Ticker> tickers) {
		lastRefresh = System.currentTimeMillis();
		this.tickers = tickers;
	}
	
}
