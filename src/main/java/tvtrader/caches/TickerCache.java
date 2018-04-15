package tvtrader.caches;

import lombok.Getter;
import tvtrader.exchange.apidata.Ticker;

import java.util.HashMap;
import java.util.Map;

public class TickerCache {
	@Getter private long lastRefresh;
	private Map<String, Ticker> tickers;
	
	public TickerCache() {
		tickers = new HashMap<>();
	}
	
	public Ticker getTicker(String market) {
		return tickers.get(market);
	}
	
	public void refreshCache(Map<String, Ticker> tickers) {
		lastRefresh = System.currentTimeMillis();
		
		tickers.forEach((k,v) -> this.tickers.put(k, v));
	}
	
}
