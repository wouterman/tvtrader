package tvtrader.services;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import tvtrader.caches.TransactionCache;
import tvtrader.exchange.Exchange;
import tvtrader.exchange.ExchangeException;
import tvtrader.exchange.ExchangeFactory;
import tvtrader.exchange.apidata.Order;
import tvtrader.model.ApiCredentials;
import tvtrader.model.OrderType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
public class TransactionHistoryService {
	private ExchangeFactory factory;
	private AccountService accountService;
	private ConfigurationService configurationService;
	private Map<String, TransactionCache> accounts;

	public TransactionHistoryService(ExchangeFactory factory, AccountService accountService, ConfigurationService configurationService) {
		this.factory = factory;
		this.accountService = accountService;
		this.configurationService = configurationService;

		accounts = new HashMap<>();
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
		return currentTimeMilli >= (cache.getLastRefresh() + (configurationService.getAssetRefreshRate() * 1_000));
	}

}
