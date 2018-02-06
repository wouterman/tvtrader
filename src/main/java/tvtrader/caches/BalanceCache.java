package tvtrader.caches;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import lombok.Getter;

public class BalanceCache {
	@Getter private long lastRefresh;
	private Map<String, Double> balances;
	
	public double getBalance(String currency) {
		return balances.get(currency);
	}
	
	public Iterator<Entry<String, Double>> getAll() {
		return balances.entrySet().iterator();
	}
	
	public void refreshCache(Map<String, Double> balances) {
		lastRefresh = System.currentTimeMillis();
		this.balances = balances;
	}
}
