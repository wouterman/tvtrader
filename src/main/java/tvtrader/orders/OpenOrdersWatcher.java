package tvtrader.orders;

import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import tvtrader.accounts.Account;
import tvtrader.controllers.Listener;
import tvtrader.exchange.ExchangeException;
import tvtrader.exchange.SupportedExchange;
import tvtrader.exchange.apidata.Order;
import tvtrader.model.Configuration;
import tvtrader.model.ConfigurationField;
import tvtrader.services.AccountService;
import tvtrader.services.ExchangeService;

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
public class OpenOrdersWatcher implements Listener {
	private long expirationDate;

	@Getter
	private int expirationTime;
	@Getter
	private boolean replace;

	private ExchangeService exchangeService;
	private AccountService accountService;
	private OrderBuilder orderBuilder;
	private OrderPlacer orderPlacer;

	public OpenOrdersWatcher(ExchangeService exchangeService, AccountService accountService, OrderBuilder orderBuilder,
			OrderPlacer orderPlacer, Configuration configuration) {
		this.exchangeService = exchangeService;
		this.accountService = accountService;
		this.orderBuilder = orderBuilder;
		this.orderPlacer = orderPlacer;

		configuration.addChangeListener(this);
	}

	/**
	 * Checks each account at every exchange for expired open orders.<br>
	 * If an order is expired the method will call the cancelOrder method of the
	 * exchange for that account and order.<br>
	 * 
	 */
	public void checkOrders() {
		log.info("Checking open orders...");

		expirationDate = System.currentTimeMillis() - (expirationTime * 1000);
		log.debug("Current expirationDate: {}", expirationDate);

		SupportedExchange[] exchanges = SupportedExchange.values();

		for (SupportedExchange exchange : exchanges) {
			checkAccounts(exchange.getName());
		}
	}

	private void checkAccounts(String exchange) {
		log.debug("Checking for {}", exchange);

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

		for (Order order : openOrders) {
			log.debug("Checking open order: {}", order);
			if (order.getTimeStamp() < expirationDate) {
				log.info("Canceling open order: {}", order);
				String orderId = order.getOrderId();

				boolean success = exchangeService.cancelOrder(exchange, account, orderId);

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

	public void setExpirationTime(int expirationTime) {
		log.info("Setting the expiration time for open orders to {} seconds.", expirationTime);
		this.expirationTime = expirationTime;
	}

	public void setReplaceFlag(boolean replace) {
		log.info("Setting the cancelled order replace flag to {}.", replace);
		this.replace = replace;
	}

	@Override
	public void update(ConfigurationField changedField, Configuration configuration) {
		if (changedField == ConfigurationField.OPENORDERSEXPIRATIONTIME) {
			setExpirationTime(configuration.getOpenOrdersExpirationTime());
		} else if (changedField == ConfigurationField.UNFILLEDORDERSREPLACEFLAG) {
			setReplaceFlag(configuration.isRetryOrderFlag());
		}
	}

}
