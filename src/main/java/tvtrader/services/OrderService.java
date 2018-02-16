package tvtrader.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tvtrader.exchange.Exchange;
import tvtrader.exchange.ExchangeException;
import tvtrader.exchange.ExchangeFactory;
import tvtrader.exchange.apidata.Order;
import tvtrader.model.ApiCredentials;
import tvtrader.orders.MarketOrder;

@Component
public class OrderService {
	@Autowired 
	private ExchangeFactory factory;
	
	public boolean placeOrder(MarketOrder order, ApiCredentials credentials) throws ExchangeException {
		Exchange exchange = factory.getExchange(order.getExchange());

		return exchange.placeOrder(order, credentials);
	}

	public double getMinimumOrderAmount(String exchangeName) throws ExchangeException {
		Exchange exchange = factory.getExchange(exchangeName);

		return exchange.getMinimumOrderAmount();
	}

	public double getTakerFee(String exchangeName)  throws ExchangeException {
		Exchange exchange = factory.getExchange(exchangeName);

		return exchange.getTakerFee();
	}
	
	public List<Order> getOpenOrders(String exchangeName, ApiCredentials credentials) throws ExchangeException {
		Exchange exchange = factory.getExchange(exchangeName);
		
		return exchange.getOpenOrders(credentials);
	}

	public boolean cancelOrder(String exchangeName, ApiCredentials credentials, String orderId) throws ExchangeException {
		Exchange exchange = factory.getExchange(exchangeName);
		
		return exchange.cancelOrder(orderId, credentials);
	}

}
