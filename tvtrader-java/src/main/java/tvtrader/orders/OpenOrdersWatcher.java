package tvtrader.orders;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tvtrader.exchange.ExchangeException;
import tvtrader.exchange.SupportedExchange;
import tvtrader.exchange.apidata.Order;
import tvtrader.model.Account;
import tvtrader.model.MarketOrder;
import tvtrader.services.AccountService;
import tvtrader.services.ConfigurationService;
import tvtrader.services.ExchangeService;

import java.util.Iterator;
import java.util.List;

/**
 * Responsible for checking (and canceling) open orders that have expired.<br>
 * An order is defined as 'expired' if it is older than the value of
 * expirationTime as set with setExpirationTime(int).<br>
 * 
 * @author Wouter
 *
 */
@Log4j2
@Component
public class OpenOrdersWatcher {

	private ExchangeService exchangeService;
	private AccountService accountService;
	private OrderBuilder orderBuilder;
	private OrderPlacer orderPlacer;
	private ConfigurationService configurationService;

	@Autowired
	public OpenOrdersWatcher(ExchangeService exchangeService, AccountService accountService, OrderBuilder orderBuilder, OrderPlacer orderPlacer, ConfigurationService configurationService) {
		this.exchangeService = exchangeService;
		this.accountService = accountService;
		this.orderBuilder = orderBuilder;
		this.orderPlacer = orderPlacer;
		this.configurationService = configurationService;
	}

	/**
	 * Checks each account at every exchange for expired open orders.<br>
	 * If an order is expired the method will call the cancelOrder method of the
	 * exchange for that account and order.<br>
	 * 
	 */
	public void checkOrders() {
		log.info("Checking open orders...");

		SupportedExchange[] exchanges = SupportedExchange.values();

		for (SupportedExchange exchange : exchanges) {
			checkAccounts(exchange.getName());
		}
	}

	private void checkAccounts(String exchange) {
		Iterator<Account> accounts = accountService.getAccounts(exchange);

		while (accounts.hasNext()) {
			String account = accounts.next().getName();
			log.debug("Checking account {}", account);

			try {
				checkOpenOrders(exchange, account);
			} catch (ExchangeException e) {
				log.error(
						"Received an exception while requesting the open orders for {} at {}. Received the following message: {}",
						exchange, account, e.getMessage());
			}
		}
	}

	private void checkOpenOrders(String exchange, String account) throws ExchangeException {
		List<Order> openOrders = exchangeService.getOpenOrders(exchange, account);
		int expirationTime = configurationService.getOpenOrdersExpirationTime();

		long expirationDate = System.currentTimeMillis() - (expirationTime * 1000);
		log.debug("Current expirationDate: {}", expirationDate);
		
		for (Order order : openOrders) {
			log.debug("Checking open order: {}", order);
			if (order.getTimeStamp() < expirationDate) {
				log.info("Canceling open order: {}", order);
				String orderId = order.getOrderId();

				boolean success = exchangeService.cancelOrder(exchange, account, orderId);
				boolean replace = configurationService.getRetryOrderFlag();

				if (success && replace) {
					MarketOrder newOrder = rebuildOrder(exchange, account, order);
					replaceOrder(newOrder, order.getQuantityRemaining());
				}
			}
		}
	}

	private MarketOrder rebuildOrder(String exchange, String account, Order order) {
		MarketOrder newOrder = new MarketOrder();
		newOrder.setExchange(exchange);
		newOrder.setAccount(account);
		newOrder.setMainCoin(order.getMainCoin());
		newOrder.setAltCoin(order.getAltCoin());
		newOrder.setOrderType(order.getOrderType());

		return newOrder;
	}

	/**
	 * Replaces the unfilled order. <br>
	 * Recalculates the needed quantity based on the remaining quantity of the
	 * order.<br>
	 * 
	 * @param order
	 *            The replacement order.
	 * @param remainingQuantity
	 *            The quantity remaining for the unfilled order.
	 * @throws ExchangeException
	 *             If anything goes wrong while contacting the exchange.
	 */
	void replaceOrder(MarketOrder order, double remainingQuantity) throws ExchangeException {
		double takerFee = exchangeService.getTakerFee(order.getExchange());
		double bidPrice = exchangeService.getBid(order.getExchange(), order.getMainCoin(), order.getAltCoin());

		double beforeFee = bidPrice * remainingQuantity;
		double buylimit = beforeFee - (beforeFee * takerFee);

		orderBuilder.calculateQuantityAndRate(order, buylimit);
		orderPlacer.addOrder(order);
	}

}
