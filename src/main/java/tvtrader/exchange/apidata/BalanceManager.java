package tvtrader.exchange.apidata;

import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;

/**
 * Data holder for all the assets (altcoin balances) for an account. <br>
 * 
 * @author Wouter
 *
 */
@Log4j2
public class BalanceManager {
	private String accountName;
	private Map<String, Double> assets;
	private long lastRefresh;
	
	public BalanceManager(String accountName) {
		this.accountName = accountName;
		assets = new HashMap<>();
	}
	
	public void replaceBalances(Map<String, Double> balances) {
		this.assets = balances;
		lastRefresh = System.currentTimeMillis();
	}
	
	public double getBalance(String currency) {
		log.debug("Fetching currency {}", currency);
		try {
			return assets.get(currency);
		} catch (NullPointerException e) {
			return 0;
		}
	}
	
	public String getName() {
		return accountName;
	}
	
	public long getLastRefresh() {
		return lastRefresh;
	}

}
