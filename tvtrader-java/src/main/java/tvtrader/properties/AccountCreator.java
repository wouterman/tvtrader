package tvtrader.properties;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import tvtrader.exchange.ExchangeException;
import tvtrader.model.Account;
import tvtrader.model.ApiCredentials;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

/**
 * Responsible for extracting from the configuration file all the accounts
 * linked to an exchange.
 * 
 * @author Wouter
 *
 */
@Log4j2
@Component
public class AccountCreator {

	/**
	 * Extracts the accounts linked to the provided exchange. If any parameter
	 * pertaining to the account is missing (main currency, key, secret, buy limit,
	 * stoploss or minimumgain) an InvalidAccountException is thrown.
	 * 
	 * @throws InvalidAccountException
	 *             If the account information from the properties file could not be
	 *             parsed. E.g. a field is missing or a field that should be a
	 *             number could not be parsed.
	 */
	public List<Account> extractAccounts(String exchangeName, Properties config) throws ExchangeException {
		String exchange = exchangeName.toLowerCase();
		List<Account> accounts = new ArrayList<>();
		String names = config.getProperty(exchange + ".account", "");

		if (names.length() > 0) {
			log.info("Got the following account names: {}", names);
		}

		try (Scanner scanner = new Scanner(names);) {

			while (scanner.hasNext()) {
				String name = scanner.next();

				Account account = createAccount(exchange, name, config);
				accounts.add(account);

			}
		}

		log.info("Created the following accounts for {}: {}", exchange, accounts);

		return accounts;
	}

	private Account createAccount(String exchangeName, String accountName, Properties config)
			throws InvalidAccountException {
		log.debug("Processing account: {}", accountName);

		String mainCurrency = config.getProperty(exchangeName + "." + accountName + "." + "maincurrency");
		String key = config.getProperty(exchangeName + "." + accountName + "." + "apikey");
		String secret = config.getProperty(exchangeName + "." + accountName + "." + "secret");
		String limit = config.getProperty(exchangeName + "." + accountName + "." + "buylimit");
		String stoplossNum = config.getProperty(exchangeName + "." + accountName + "." + "stoploss");
		String tsslNum = config.getProperty(exchangeName + "." + accountName + "." + "trailingstoploss");
		String mingain = config.getProperty(exchangeName + "." + accountName + "." + "minimumgain");

		log.debug("Maincurrency {}. Key {}. Buylimit {}. Stoploss {}. Minimumgain {}.", mainCurrency, key, secret,
				limit, stoplossNum, mingain);
		if (notPresent(mainCurrency, key, secret, limit, stoplossNum, tsslNum, mingain)) {
			throw new InvalidAccountException("Couldn't parse account: " + accountName + " on exchange: " + exchangeName
					+ ". Please check your config.");
		}

		double buyLimit = parseField(limit);
		double stoploss = parseField(stoplossNum);
		double trailingStoploss = parseField(tsslNum);
		double minimumGain = parseField(mingain);
		ApiCredentials credentials = new ApiCredentials(key.trim(), secret.trim());

		// Build account and return
		return new Account(exchangeName.trim().toUpperCase(), accountName.trim().toUpperCase(), mainCurrency.trim().toUpperCase(), buyLimit, stoploss, trailingStoploss, minimumGain, credentials);
	}

	/**
	 * Checks if any of the extracted values wasn't present.<br>
	 * 
	 * @return
	 */
	private boolean notPresent(String mainCurrency, String key, String secret, String limit, String stoplossNum,
			String tsslNum, String mingain) {
		return mainCurrency == null || key == null || secret == null || limit == null || stoplossNum == null
				|| tsslNum == null || mingain == null;
	}

	private double parseField(String field) throws InvalidAccountException {
		try {
			return Double.parseDouble(field);
		} catch (NumberFormatException nfe) {
			throw new InvalidAccountException(
					"Couldn't parse one of the following fields: buyLimit, stoploss or minimumGain. Please check your config.");
		}
	}
}
