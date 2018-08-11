package tvtrader.dao;

import org.springframework.stereotype.Component;
import tvtrader.model.Account;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component
public class AccountRepository {
	private Map<String, Map<String, Account>> repository;

	public AccountRepository() {
		repository = new HashMap<>();
	}

	/**
	 * Adds the account to the repository.
	 * 
	 * @param exchange
	 *            The exchange where the account is registered.
	 * @param account
	 *            The account to add.
	 */
	public void addAccount(String exchange, Account account) {
		// Creates a map for the accounts if it isn't there.
		Map<String, Account> cache = repository.computeIfAbsent(exchange, value -> new HashMap<String, Account>());
		cache.put(account.getName(), account);
	}

	/**
	 * Removes the account from the repository.Ó
	 * 
	 * @param exchange
	 *            The exchange where the account is registered.
	 * @param account
	 *            The account to remove.
	 */
	public void removeAccount(String exchange, String account) {
		Map<String, Account> cache = repository.get(exchange);

		if (cache != null) {
			cache.remove(account);
		}
	}

	/**
	 * Fetches the account from the repository.
	 * 
	 * @param exchange
	 *            The exchange where the account is registered.
	 * @param account
	 *            The account to search for.
	 * @return The account or null if the account is unknown.
	 */
	public Account getAccount(String exchange, String account) {
		Map<String, Account> cache = repository.get(exchange);

		if (cache != null) {
			return cache.get(account);
		} else {
			return null;
		}

	}

	/**
	 * Checks if the account is registered with the repository.<br>
	 * 
	 * @param exchange
	 *            The exchange where the account is registered.
	 * @param account
	 *            The account to search for.
	 * @return True if the account is registered.
	 */
	public boolean hasAccount(String exchange, String account) {
		Account found = getAccount(exchange, account);

		return found != null;
	}

	/**
	 * Returns all the accounts associated with the exchange.<br>
	 * 
	 * @param exchange
	 *            The exchange for which to fetch the accounts.
	 * @return Iterator for the accounts.
	 */
	public Iterator<Account> getAccounts(String exchange) {
		return repository.get(exchange).values().iterator();
	}

}
