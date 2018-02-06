package tvtrader.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;
import tvtrader.accounts.Account;
import tvtrader.exceptionlogger.GameBreakerException;
import tvtrader.exchange.ExchangeException;
import tvtrader.model.Configuration;
import tvtrader.model.MailConfiguration;

@Log4j2
@Component
public class PropertiesFileLoader {
	private Configuration configuration;
	private PropertiesFileParser parser;
	
	public PropertiesFileLoader(Configuration configuration, PropertiesFileParser parser) {
		this.configuration = configuration;
		this.parser = parser;
	}
	
	/**
	 * Autoloads the provided configuration file.<br>
	 * Parses the configuration file for accounts, intervals and email credentials
	 * and sets the appropriate fields in the configuration file.<br>
	 * 
	 * @return True if success.
	 */
	public boolean autoload(String propertiesPath) {
		boolean successful;

		try {
			Properties config = load(propertiesPath);
			log.info("Loading configuration file.");
			parser.load(config);

			log.info("Loading mailclient.");
			MailConfiguration mailConfig = parser.parseMailConfiguration();
			configuration.setMailConfig(mailConfig);
			String expectedSender = parser.getExpectedSender();
			configuration.setExpectedSender(expectedSender);

			int pollingInterval = parser.getPollingInterval();
			configuration.setMailPollingInterval(pollingInterval);

			int stoplossInterval = parser.getStoplossInterval();
			configuration.setStoplossInterval(stoplossInterval);

			int openOrdersInterval = parser.getOpenOrdersInterval();
			configuration.setOpenOrdersInterval(openOrdersInterval);

			int openOrdersExpirationTime = parser.getOpenOrdersExpirationTime();
			configuration.setOpenOrdersExpirationTime(openOrdersExpirationTime);

			boolean replaceCancelledOrdersFlag = parser.getCancelledOrdersFlag();
			configuration.setUnfilledOrdersReplaceFlag(replaceCancelledOrdersFlag);

			int tickerRefreshRate = parser.getTickerRefreshRate();
			configuration.setTickerRefreshRate(tickerRefreshRate);

			int assetRefreshRate = parser.getAssetRefreshRate();
			configuration.setAssetRefreshRate(assetRefreshRate);

			log.info("Loading accounts.");
			Map<String, List<Account>> extractedAccounts = parser.parseAccounts();

			for (Map.Entry<String, List<Account>> entry : extractedAccounts.entrySet()) {
				String exchangeName = entry.getKey();
				List<Account> accounts = entry.getValue();

				for (Account account : accounts) {
					configuration.addAccount(exchangeName, account);
				}
			}

			successful = true;
		} catch (ExchangeException e) {
			log.error("Couldn't start up. Received the following message: {}", e.getMessage());
			successful = false;
		}

		return successful;
	}
	
	/**
	 * Reads the provided properties file.
	 * 
	 * @throws GameBreakerException
	 *             On failure. (File not found, etc.)
	 * 
	 */
	private Properties load(final String propertiesPath) throws GameBreakerException {
		log.debug("Loading properties file: {}", propertiesPath);
		Properties config = new Properties();

		try (FileInputStream fis = new FileInputStream(propertiesPath);
				BufferedReader input = new BufferedReader(new InputStreamReader(fis));) {

			config.load(input);
			log.debug("Properties loaded");
		} catch (IOException e) {
			throw new GameBreakerException("Couldn't load the properties file!", e);
		}
		
		return config;
	}
}
