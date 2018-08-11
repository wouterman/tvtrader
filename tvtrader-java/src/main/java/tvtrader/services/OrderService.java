package tvtrader.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tvtrader.exchange.Exchange;
import tvtrader.exchange.ExchangeException;
import tvtrader.exchange.ExchangeFactory;
import tvtrader.exchange.apidata.Order;
import tvtrader.model.ApiCredentials;
import tvtrader.model.MarketOrder;

import java.util.List;

@Component
public class OrderService {

	private ExchangeFactory factory;

	@Autowired
	public OrderService(ExchangeFactory factory) {
		this.factory = factory;
	}

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
