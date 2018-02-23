package tvtrader.properties;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;
import tvtrader.exceptionlogger.GameBreakerException;
import tvtrader.exchange.ExchangeException;
import tvtrader.model.Account;
import tvtrader.model.MailConfiguration;
import tvtrader.services.AccountService;
import tvtrader.services.ConfigurationService;

@Log4j2
@Component
public class PropertiesFileLoader {
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private ConfigurationService configurationService;
	
	@Autowired
	private PropertiesFileParser parser;
	
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
			
			//TODO CLEANER
			configurationService.setProtocol(mailConfig.getProtocol());
			configurationService.setHost(mailConfig.getHost());
			configurationService.setPort(mailConfig.getPort());
			configurationService.setInbox(mailConfig.getInbox());
			configurationService.setUsername(mailConfig.getUsername());
			configurationService.setPassword(mailConfig.getPassword());
			
			String expectedSender = parser.getExpectedSender();
			configurationService.setExpectedSender(expectedSender);

			int pollingInterval = parser.getPollingInterval();
			configurationService.setMailPollingInterval(pollingInterval);

			int stoplossInterval = parser.getStoplossInterval();
			configurationService.setStoplossInterval(stoplossInterval);

			int openOrdersInterval = parser.getOpenOrdersInterval();
			configurationService.setOpenOrdersInterval(openOrdersInterval);

			int openOrdersExpirationTime = parser.getOpenOrdersExpirationTime();
			configurationService.setOpenOrdersExpirationTime(openOrdersExpirationTime);

			boolean replaceCancelledOrdersFlag = parser.getCancelledOrdersFlag();
			configurationService.setUnfilledOrdersReplaceFlag(replaceCancelledOrdersFlag);

			int tickerRefreshRate = parser.getTickerRefreshRate();
			configurationService.setTickerRefreshRate(tickerRefreshRate);

			int assetRefreshRate = parser.getAssetRefreshRate();
			configurationService.setAssetRefreshRate(assetRefreshRate);

			log.info("Loading accounts.");
			Map<String, List<Account>> extractedAccounts = parser.parseAccounts();

			for (Map.Entry<String, List<Account>> entry : extractedAccounts.entrySet()) {
				String exchangeName = entry.getKey();
				List<Account> accounts = entry.getValue();

				for (Account account : accounts) {
					accountService.addAccount(exchangeName, account);
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
