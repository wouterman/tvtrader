package tvtrader.services;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tvtrader.caches.BalanceCache;
import tvtrader.exchange.Exchange;
import tvtrader.exchange.ExchangeException;
import tvtrader.exchange.ExchangeFactory;
import tvtrader.model.ApiCredentials;
import tvtrader.model.Configuration;
import tvtrader.model.Listener;
import tvtrader.model.ListenerField;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

@Log4j2
@Component
public class BalanceService implements Listener {
	
	@Autowired @Setter private AccountService accountService;
	@Autowired @Setter private ExchangeFactory factory;
	
	private Map<String, BalanceCache> caches;
	@Getter	@Setter private int assetRefreshRate;

	public BalanceService(Configuration configuration) {
		caches = new HashMap<>();
		configuration.subscribe(this);
	}

	/**
	 * Fetches and return the latest balance for the provided currency.<br>
	 * 
	 * @param exchangeName
	 *            The exchange from which to fetch the balance.
	 * @param accountName
	 *            The account for which to fetch the balance.
	 * @param currency
	 *            The balance to fetch.
	 * @return
	 * @throws ExchangeException
	 *             * If anything goes wrong while contacting the exchange or if the
	 *             exchange or account is unknown.
	 */
	public double getBalance(String exchangeName, String accountName, String currency) throws ExchangeException {
		if (accountService.hasAccount(exchangeName, accountName)) {
			BalanceCache cache = getCache(exchangeName, accountName);

			log.debug("Getting balance for: {}", currency);
			try {
				return cache.getBalance(currency);
			} catch (NullPointerException e) {
				return 0;
			}
		} else {
			throw new ExchangeException("Can't fetch balance for account " + accountName + ". Account is unknown!");
		}

	}

	private BalanceCache getCache(String exchangeName, String accountName)
			throws ExchangeException {
		Exchange exchange = factory.getExchange(exchangeName);
		BalanceCache cache = caches.computeIfAbsent(accountName, value -> new BalanceCache());

		long currentTimeMilli = System.currentTimeMillis();
		if (refreshNeeded(cache, currentTimeMilli)) {
			ApiCredentials credentials = accountService.getCredentials(exchangeName, accountName);
			Map<String, Double> balances = exchange.getBalances(credentials);
			cache.refreshCache(balances);
		}
		return cache;
	}

	/**
	 * Fetches and returns all the balances for the account.<br>
	 * 
	 * @param exchangeName
	 *            The exchange from which to fetch the balances.
	 * @param accountName
	 *            The account for which to fetch the balances.
	 * @return Iterator for all the balances.
	 * @throws ExchangeException
	 *             * If anything goes wrong while contacting the exchange or if the
	 *             exchange or account is unknown.
	 */
	public Iterator<Entry<String, Double>> getBalances(String exchangeName, String accountName) throws ExchangeException {
		return getCache(exchangeName, accountName).getAll();
	}

	/**
	 * Checks if the cache needs to be refreshed.<br>
	 */
	private boolean refreshNeeded(BalanceCache cache, long currentTimeMilli) {
		return currentTimeMilli >= (cache.getLastRefresh() + (assetRefreshRate * 1_000));
	}

	@Override
	public void update(ListenerField changedField, Object subject) {
		if (changedField == ListenerField.ASSETREFRESHRATE) {
			setAssetRefreshRate(((Configuration)subject).getAssetRefreshRate());
		}
	}
}
