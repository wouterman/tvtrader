package tvtrader.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import tvtrader.accounts.ApiCredentials;
import tvtrader.caches.TransactionCache;
import tvtrader.controllers.Listener;
import tvtrader.exchange.Exchange;
import tvtrader.exchange.ExchangeException;
import tvtrader.exchange.ExchangeFactory;
import tvtrader.exchange.apidata.Order;
import tvtrader.model.Configuration;
import tvtrader.model.ConfigurationField;
import tvtrader.orders.OrderType;

@Log4j2
@Component
public class TransactionHistoryService implements Listener {
	private ExchangeFactory factory;
	private AccountService accountService;
	private Map<String, TransactionCache> accounts;

	@Getter
	@Setter
	private int boughtPriceRefreshRate;

	public TransactionHistoryService(ExchangeFactory factory, AccountService accountService, Configuration configuration) {
		this.factory = factory;
		this.accountService = accountService;
		accounts = new HashMap<>();
		
		configuration.addChangeListener(this);
	}

	public double getBoughtPrice(String exchangeName, String accountName, String altCoin, double balance)
			throws ExchangeException {

		if (accountService.hasAccount(exchangeName, accountName)) {
			return calculateBoughtPrice(exchangeName, accountName, altCoin, balance);
		} else {
			throw new ExchangeException(
					"Can't calculate bought price for account " + accountName + ". Account is unknown!");
		}
	}

	private double calculateBoughtPrice(String exchangeName, String accountName, String altCoin, double balance)
			throws ExchangeException {
		Exchange exchange = factory.getExchange(exchangeName);

		TransactionCache cache = accounts.computeIfAbsent(accountName, value -> new TransactionCache());
		checkCacheForRefresh(exchangeName, accountName, exchange, cache);

		log.debug("Getting history for: {}", altCoin);
		List<Order> orders = cache.getFilledOrders();

		double remaining = balance;
		double sum = 0;
		for (Order order : orders) {
			if (expectedOrder(exchangeName, accountName, altCoin, order)) {

				if (remaining > 0) {
					double quantity = determineQuantityRemaining(remaining, order);

					double priceExFee = quantity * order.getRate();
					double fee = priceExFee * exchange.getTakerFee();
					double totalPrice = priceExFee + fee;

					sum += totalPrice;
					remaining -= quantity;
				} else {
					break;
				}
			}
		}

		// Transaction history incomplete. Resetting bought price.
		if (remaining > 0) {
			sum = 0;
		}

		return sum;
	}

	private void checkCacheForRefresh(String exchangeName, String accountName, Exchange exchange,
			TransactionCache cache) throws ExchangeException {
		long currentTimeMilli = System.currentTimeMillis();
		if (refreshNeeded(cache, currentTimeMilli)) {
			ApiCredentials credentials = accountService.getCredentials(exchangeName, accountName);
			cache.refreshCache(exchange.getOrderHistory(credentials));
		}
	}

	private boolean expectedOrder(String exchangeName, String accountName, String altCoin, Order order)
			throws ExchangeException {
		boolean expectedOrderType = order.getOrderType() == OrderType.LIMIT_BUY;
		boolean expectedMainCurrency = checkMainCurrency(exchangeName, accountName, order);
		boolean expectedAltCoin = order.getAltCoin().equalsIgnoreCase(altCoin);

		return expectedOrderType && expectedMainCurrency && expectedAltCoin;
	}

	private boolean checkMainCurrency(String exchangeName, String accountName, Order order) throws ExchangeException {
		String mainCurrency = order.getMainCoin();
		String expectedMainCurrency = accountService.getMainCurrency(exchangeName, accountName);
		
		return mainCurrency.equalsIgnoreCase(expectedMainCurrency);
	}

	private double determineQuantityRemaining(double remaining, Order order) {
		double quantity;
		if (order.getQuantity() > remaining) {
			quantity = remaining;
		} else {
			quantity = order.getQuantity();
		}
		return quantity;
	}

	/**
	 * Checks if the cache needs to be refreshed.<br>
	 */
	private boolean refreshNeeded(TransactionCache cache, long currentTimeMilli) {
		return currentTimeMilli >= (cache.getLastRefresh() + (boughtPriceRefreshRate * 1_000));
	}

	@Override
	public void update(ConfigurationField changedField, Configuration configuration) {
		if (changedField == ConfigurationField.ASSETREFRESHRATE) {
			setBoughtPriceRefreshRate(configuration.getAssetRefreshRate());
		}
	}

}
