package tvtrader.properties;

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
import tvtrader.controllers.InputController;
import tvtrader.exceptionlogger.GameBreakerException;
import tvtrader.exchange.ExchangeException;
import tvtrader.model.MailConfiguration;

@Log4j2
@Component
public class PropertiesFileLoader {
	private InputController controller;
	private PropertiesFileParser parser;
	
	public PropertiesFileLoader(InputController controller, PropertiesFileParser parser) {
		this.controller = controller;
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
			controller.setMailConfiguration(mailConfig);
			String expectedSender = parser.getExpectedSender();
			controller.setExpectedSender(expectedSender);

			int pollingInterval = parser.getPollingInterval();
			controller.setMailPollingInterval(pollingInterval);

			int stoplossInterval = parser.getStoplossInterval();
			controller.setStoplossInterval(stoplossInterval);

			int openOrdersInterval = parser.getOpenOrdersInterval();
			controller.setOpenOrdersInterval(openOrdersInterval);

			int openOrdersExpirationTime = parser.getOpenOrdersExpirationTime();
			controller.setOpenOrdersExpirationTime(openOrdersExpirationTime);

			boolean replaceCancelledOrdersFlag = parser.getCancelledOrdersFlag();
			controller.setUnfilledOrdersReplaceFlag(replaceCancelledOrdersFlag);

			int tickerRefreshRate = parser.getTickerRefreshRate();
			controller.setTickerRefreshRate(tickerRefreshRate);

			int assetRefreshRate = parser.getAssetRefreshRate();
			controller.setAssetRefreshRate(assetRefreshRate);

			log.info("Loading accounts.");
			Map<String, List<Account>> extractedAccounts = parser.parseAccounts();

			for (Map.Entry<String, List<Account>> entry : extractedAccounts.entrySet()) {
				String exchangeName = entry.getKey();
				List<Account> accounts = entry.getValue();

				for (Account account : accounts) {
					controller.addAccount(exchangeName, account);
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
