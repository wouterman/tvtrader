package tvtrader.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;
import tvtrader.accounts.Account;
import tvtrader.exchange.ExchangeException;
import tvtrader.exchange.SupportedExchange;
import tvtrader.orders.OrderPlacer;
import tvtrader.orders.OrderType;
import tvtrader.orders.StoplossOrder;
import tvtrader.stoploss.StoplossListener;
import tvtrader.stoploss.StoplossWatcher;
import tvtrader.stoploss.UnverifiedStoplossWatcherException;

/**
 * Responsible for the stoploss protection for altcoins.<br>
 * Has methods for marking specific exchanges/accounts/altcoins for protection
 * or for starting up the protection for all exchanges/accounts/altcoins
 * provided by an iterator.<br>
 * Stoploss conditions can be checked by calling checkStoploss().
 * 
 * @author Wouter
 *
 */
@Log4j2
@Component
public class StoplossService implements StoplossListener {
	private AccountService accountService;
	private ExchangeService exchangeService;
	private StoplossWatcher watcher;
	private List<StoplossOrder> orders;

	public StoplossService(ExchangeService exchangeService, AccountService accountService, StoplossWatcher watcher,
			OrderPlacer orderPlacer) {
		this.exchangeService = exchangeService;
		this.accountService = accountService;
		this.watcher = watcher;
		orders = new ArrayList<>();

		orderPlacer.addChangeListener(this);
	}

	/**
	 * Starts up stoploss protection for the provided coin.<br>
	 * If the account has no (0) stoploss set no protection will be provided.<br>
	 * <br>
	 * Order watchers check the stoploss condition dynamically based on the order
	 * history, so multiple buy orders for the same market will not create
	 * additional watchers.
	 * 
	 */
	public boolean addStoplossProtection(StoplossOrder order) {
		try {
			double stoplossPercentage = accountService.getStoploss(order.getExchange(), order.getAccount());
			if (stoplossPercentage > 0) {
				if (!orders.contains(order)) {
					log.info("Adding stoploss protection for {}.", order);
					orders.add(order);
					return true;
				} else {
					log.info("Already have stoploss protection for {}.", order);
				}
			}
		} catch (NullPointerException | ExchangeException e) {
			// Can't really happen, as we just requested this information from that
			// exchange.
			log.debug("Received exception: ", e);
			log.info("Couldn't start stoplossprotection for {}. Received the following message; {}", order,
					e.getMessage());
		}
		return false;
	}
	
	/**
	 * Stops stoplossprotection for the provided coin.<br>
	 */
	public boolean stopStoplossProtection(StoplossOrder order) {
		boolean removed = orders.remove(order);
		if (removed) {
			log.info("Stopping stoploss protection for {}.", order);
		}

		return removed;
	}
	
	/**
	 * Starts up stoplossprotection for every altcoin for every account registered
	 * with all the exchanges.<br>
	 * 
	 */
	public void startStoplossProtection() {
		SupportedExchange[] exchanges = SupportedExchange.values();

		for (SupportedExchange exchange : exchanges) {
			Iterator<Account> accIter = accountService.getAccounts(exchange.getName());

			while (accIter.hasNext()) {
				Account currentAccount = accIter.next();

				checkAltCoinBalances(exchange.getName(), currentAccount);
			}
		}
	}

	private void checkAltCoinBalances(String exchange, Account account) {
		try {
			Iterator<Entry<String, Double>>  balanceIterator = exchangeService.getBalances(exchange, account.getName());

			while (balanceIterator.hasNext()) {
				Map.Entry<String, Double> currency = balanceIterator.next();

				processBalance(exchange, account, currency);
			}

		} catch (ExchangeException e) {
			log.debug("Received the following exception: ", e);
			log.info(
					"Something went wrong while starting stoplossprotection for {}. Received the following message: {}",
					account.getName(), e.getMessage());
		}

	}

	private void processBalance(String exchange, Account account, Map.Entry<String, Double> balance) {
		if (notMainCoin(account, balance.getKey())) {
			processAltcoin(exchange, account, balance);
		}
	}

	private boolean notMainCoin(Account currentAccount, String altCoin) {
		return !altCoin.equalsIgnoreCase(currentAccount.getMainCurrency());
	}

	private void processAltcoin(String exchange, Account account, Map.Entry<String, Double> balance) {
		String altCoin = balance.getKey();
		double altCoinBalance = balance.getValue();

		if (altCoinBalance > 0) {

			try {
				double price = exchangeService.getBid(exchange, account.getMainCurrency(), altCoin);
				double priceInBtc = price * altCoinBalance;
				double stoplossPrice = priceInBtc * ((100 - account.getStoploss()) / 100);

				double minimumOrderAmount = exchangeService.getMinimumOrderAmount(exchange);

				if (stoplossPrice > minimumOrderAmount) {
					signalStoploss(exchange, account.getName(), altCoin, OrderType.LIMIT_BUY);
				}

			} catch (ExchangeException e) {
				log.debug("Received the following exception: ", e);
				log.info(
						"Something went wrong while starting stoplossprotection for {}. Received the following message: {}",
						altCoin, e.getMessage());
			}
		}
	}

	/**
	 * Checks all the watchers for stoploss conditions.<br>
	 * A watcher is ready if the stoploss interval period has passed since the last
	 * check.<br>
	 * If the stoploss condition has been triggered the watcher will be removed from
	 * protection.<br>
	 * <br>
	 * If a watcher couldn't verify the bought price of the altcoin (indicated by
	 * the watcher throwing an UnverifiedStoplossWatcherException) the watcher will
	 * also be removed.<br>
	 */
	public void checkStoploss() {
		log.info("Checking stoploss conditions...");

		Iterator<StoplossOrder> iterator = orders.iterator();

		while (iterator.hasNext()) {
			StoplossOrder order = iterator.next();

			if (stoplossTriggered(order)) {
				iterator.remove();
			}

		}
	}

	private boolean stoplossTriggered(StoplossOrder order) {
		try {
			return watcher.checkStoploss(order);
		} catch (UnverifiedStoplossWatcherException e) {
			return true;
		}

	}

	public void signalStoploss(String exchange, String account, String altcoin, OrderType ordertype) {
		StoplossOrder order = new StoplossOrder(exchange, account, altcoin);

		if (ordertype == OrderType.LIMIT_BUY) {
			addStoplossProtection(order);
		} else if (ordertype == OrderType.LIMIT_SELL) {
			stopStoplossProtection(order);
		}
	}

	@Override
	public void update(String exchange, String account, String altcoin, OrderType ordertype) {
		signalStoploss(exchange, account, altcoin, ordertype);
	}
	
	/**
	 * Used for testing purposes.<br>
	 * <br>
	 * Verifies if the order has protection.<br>
	 * 
	 * @param order
	 * @return
	 */
	protected boolean hasProtection(StoplossOrder order) {
		return orders.contains(order);
	}

	/**
	 * Used for testing purposes.<br>
	 * <br>
	 * Replaces the current list of watchers with the provided list.<br>
	 * 
	 * @param watchers
	 */
	protected void addWatchers(List<StoplossOrder> orders) {
		this.orders = orders;
	}

}
