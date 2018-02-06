package test.logger;

import tvtrader.orders.OrderType;
import tvtrader.stoploss.StoplossListener;

public class TestStoplossListener implements StoplossListener {
	boolean notified = false;

	@Override
	public void update(String exchange, String account, String altcoin, OrderType ordertype) {
		notified=true;
	}
	
	public boolean isNotified() {
		return notified;
	}
	
	public void reset() {
		notified = false;
	}
	
}
