package tvtrader.caches;

import lombok.Getter;
import tvtrader.exchange.apidata.Order;

import java.util.List;


public class TransactionCache {
	@Getter private long lastRefresh;
	private List<Order> orders;
	
	public List<Order> getFilledOrders() {
		return orders;
	}
	
	public void refreshCache(List<Order> transactions) {
		lastRefresh = System.currentTimeMillis();
		this.orders = transactions;
	}
}
