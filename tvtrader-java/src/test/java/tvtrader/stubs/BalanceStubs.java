package tvtrader.stubs;

import java.util.HashMap;
import java.util.Map;

public class BalanceStubs {
	
	public static Map<String, Double> getValidBalances() {
		Map<String, Double> balances = new HashMap<>();
		balances.put("BTC", 0.5);
		balances.put("ETH", 1.0);
		
		return balances;
	}
	
	public static Map<String, Double> getNullBalances() {
		Map<String, Double> balances = new HashMap<>();
		balances.put("BTC", 0.0);
		balances.put("ETH", 0.0);
		
		return balances;
	}
}
