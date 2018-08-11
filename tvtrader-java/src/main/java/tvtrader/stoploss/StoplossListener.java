package tvtrader.stoploss;

import tvtrader.model.OrderType;

public interface StoplossListener {
	
	public void update(String exchange, String account, String altcoin, OrderType ordertype);
}
