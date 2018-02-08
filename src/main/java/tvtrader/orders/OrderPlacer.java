package tvtrader.orders;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;
import tvtrader.services.ExchangeService;
import tvtrader.stoploss.StoplossListener;

/**
 * Responsible for placing the orders at the exchange and, if successful,
 * signaling the stoploss protection.<br>
 * 
 * @author Wouter
 *
 */
@Log4j2
@Component
public class OrderPlacer {
	private static List<MarketOrder> orders = new ArrayList<>();

	private ExchangeService exchangeService;

	private List<StoplossListener> listeners;

	public OrderPlacer(ExchangeService exchangeService) {
		this.exchangeService = exchangeService;

		listeners = new ArrayList<>();
	}

	/**
	 * Places the orders at the exchange and signals the stoploss protection for
	 * each buy order.<br>
	 * 
	 */
	public void placeOrders() {
		Iterator<MarketOrder> iterator = orders.iterator();

		while (iterator.hasNext()) {
			MarketOrder order = iterator.next();

			log.info("Placing order: {}", order);
			boolean placed;
			placed = exchangeService.placeOrder(order);

			if (placed) {
				iterator.remove();

				notifyListeners(order);
			}
		}
	}

	public void addOrder(MarketOrder order) {
		log.debug("Adding order: {}", order);
		orders.add(order);
	}

	private void notifyListeners(MarketOrder order) {
		for (StoplossListener listener : listeners) {
			listener.update(order.getExchange(), order.getAccount(), order.getAltCoin(), order.getOrderType());
		}
	}

	public void addChangeListener(StoplossListener newListener) {
		listeners.add(newListener);
	}

}
