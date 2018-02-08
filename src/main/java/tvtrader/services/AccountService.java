package tvtrader.services;

import java.util.Iterator;

import org.springframework.stereotype.Component;

import tvtrader.accounts.Account;
import tvtrader.accounts.ApiCredentials;
import tvtrader.exchange.ExchangeException;
import tvtrader.model.AccountRepository;

@Component
public class AccountService {
	private AccountRepository repository;

	public AccountService(AccountRepository repository) {
		this.repository = repository;
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
		repository.addAccount(exchange, account);
	}
	
	/**
	 * Removes the account to the repository.
	 * 
	 * @param exchange
	 *            The exchange where the account is registered.
	 * @param account
	 *            The account to remove.
	 */
	public void removeAccount(String exchange, String accountName) {
		repository.removeAccount(exchange, accountName);
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
		return repository.hasAccount(exchange, account);
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
		return repository.getAccount(exchange, account);
	}

	/**
	 * Fetches the buylimit for the account.
	 * 
	 * @param exchange
	 *            The exchange where the account is registered.
	 * @param account
	 *            The account to search for.
	 * @return The buylimit for the account.
	 * @throws ExchangeException
	 *             If the account is unknown.
	 */
	public double getBuyLimit(String exchange, String account) throws ExchangeException {
		Account found = repository.getAccount(exchange, account);

		if (found != null) {
			return found.getBuyLimit();
		} else {
			throw new ExchangeException(createErrorMessage(exchange, account));
		}
	}

	/**
	 * Fetches the stoploss percentage for the account.
	 * 
	 * @param exchange
	 *            The exchange where the account is registered.
	 * @param account
	 *            The account to search for.
	 * @return The stoploss percentage for the account.
	 * @throws ExchangeException
	 *             If the account is unknown.
	 */
	public double getStoploss(String exchange, String account) throws ExchangeException {
		Account found = repository.getAccount(exchange, account);

		if (found != null) {
			return found.getStoploss();
		} else {
			throw new ExchangeException(createErrorMessage(exchange, account));
		}
	}

	/**
	 * Fetches the trailing stoploss percentage for the account.
	 * 
	 * @param exchange
	 *            The exchange where the account is registered.
	 * @param account
	 *            The account to search for.
	 * @return The tssl percentage for the account.
	 * @throws ExchangeException
	 *             If the account is unknown.
	 */
	public double getTrailingStoploss(String exchange, String account) throws ExchangeException {
		Account found = repository.getAccount(exchange, account);

		if (found != null) {
			return found.getTrailingStoploss();
		} else {
			throw new ExchangeException(createErrorMessage(exchange, account));
		}
	}

	/**
	 * Fetches the minimum gain percentage for the account.
	 * 
	 * @param exchange
	 *            The exchange where the account is registered.
	 * @param account
	 *            The account to search for.
	 * @return The minimum gain percentage for the account.
	 * @throws ExchangeException
	 *             If the account is unknown.
	 */
	public double getMinimumGain(String exchange, String account) throws ExchangeException {
		Account found = repository.getAccount(exchange, account);

		if (found != null) {
			return found.getMinimumGain();
		} else {
			throw new ExchangeException(createErrorMessage(exchange, account));
		}
	}

	/**
	 * Fetches the main currency for the account.
	 * 
	 * @param exchange
	 *            The exchange where the account is registered.
	 * @param account
	 *            The account to search for.
	 * @return The main currency for the account.
	 * @throws ExchangeException
	 *             If the account is unknown.
	 */
	public String getMainCurrency(String exchange, String account) throws ExchangeException {
		Account found = repository.getAccount(exchange, account);

		if (found != null) {
			return found.getMainCurrency();
		} else {
			throw new ExchangeException(createErrorMessage(exchange, account));
		}
	}

	/**
	 * Fetches the api credentials for the account.
	 * 
	 * @param exchange
	 *            The exchange where the account is registered.
	 * @param account
	 *            The account to search for.
	 * @return The api credentials for the account.
	 * @throws ExchangeException
	 *             If the account is unknown.
	 */
	public ApiCredentials getCredentials(String exchange, String account) throws ExchangeException {
		Account found = repository.getAccount(exchange, account);

		if (found != null) {
			return found.getCredentials();
		} else {
			throw new ExchangeException(createErrorMessage(exchange, account));
		}
	}

	/**
	 * Returns all the accounts associated with the exchange.<br>
	 * @param exchange
	 * The exchange for which to fetch the accounts.
	 * @return Iterator for the accounts.
	 */
	public Iterator<Account> getAccounts(String exchange) {
		return repository.getAccounts(exchange);
	}

	/**
	 * Creates a default account unknown error message.
	 * 
	 */
	private String createErrorMessage(String exchange, String account) {
		return "Account " + account + " at " + exchange + " is unknown!";
	}

}
