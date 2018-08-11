package tvtrader.properties;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tvtrader.exceptionlogger.GameBreakerException;
import tvtrader.exchange.ExchangeException;
import tvtrader.exchange.SupportedExchange;
import tvtrader.mail.InvalidMailConfigException;
import tvtrader.model.Account;
import tvtrader.model.MailConfiguration;
import tvtrader.utils.NumberParser;

import java.util.*;

/**
 * Responsible for parsing the properties configuration file.<br>
 * 
 * @author Wouter
 *
 */
@Log4j2
@Component
public class PropertiesFileParser {
	private static final String EXPECTED_SENDER = "expected_sender";
	private static final String MAIL_POLLING_INTERVAL = "mail_polling_interval";
	private static final String STOPLOSS_POLLING_INTERVAL = "stoploss_polling_interval";
	private static final String OPEN_ORDERS_POLLING_INTERVAL = "open_orders_polling_interval";
	private static final String OPEN_ORDERS_EXPIRATION_TIME = "open_orders_expiration_time";
	private static final String TICKER_REFRESH_RATE = "ticker_refresh_rate";
	private static final String ASSET_REFRESH_RATE = "asset_refresh_rate";
	private static final String RETRY_CANCELLED_ORDERS = "retry_cancelled_orders";
	private static final String IMAPS = "imaps";
	private boolean loaded = false;
	
	@Autowired
	private AccountCreator accountCreator;

	private Map<String, Integer> intervals = new HashMap<>();
	private Properties config;

	/**
	 * Loads the configuration file.<br>
	 * 
	 */
	public void load(Properties config) {
		log.debug("Loading properties file.");
		this.config = config;
		parseIntervals();
		loaded = true;
	}

	public MailConfiguration parseMailConfiguration() throws GameBreakerException {
		checkLoaded();

		MailConfiguration mailConfig = new MailConfiguration();

		String protocol = IMAPS;
		String host = config.getProperty("host");
		String inbox = config.getProperty("inbox");
		String password = config.getProperty("password");
		String port = config.getProperty("port");
		String username = config.getProperty("username");

		if (fieldsNotNull(host, inbox, password, port, username)) {
			int portNum = parsePortNumber(port);

			mailConfig.setHost(host);
			mailConfig.setInbox(inbox);
			mailConfig.setPassword(password);
			mailConfig.setPort(portNum);
			mailConfig.setProtocol(protocol);
			mailConfig.setUsername(username);

			return mailConfig;
		} else {
			throw new InvalidMailConfigException("Couldn't parse the mail configuration! Please check your config.");
		}
	}

	private boolean fieldsNotNull(String host, String inbox, String password, String port,
			String username) {
		return host != null && inbox != null && password != null && port != null && username != null;
	}

	private int parsePortNumber(String port) throws InvalidMailConfigException {
		try {
			return Integer.parseInt(port);
		} catch (NumberFormatException nfe) {
			throw new InvalidMailConfigException("Port needs to be a number: " + port);
		}
	}

	public String getExpectedSender() throws GameBreakerException {
		checkLoaded();
		return config.getProperty(EXPECTED_SENDER);
	}

	public int getPollingInterval() throws GameBreakerException {
		return getInterval(MAIL_POLLING_INTERVAL);
	}

	public int getStoplossInterval() throws GameBreakerException {
		return getInterval(STOPLOSS_POLLING_INTERVAL);
	}

	public int getOpenOrdersInterval() throws GameBreakerException {
		return getInterval(OPEN_ORDERS_POLLING_INTERVAL);
	}

	public int getOpenOrdersExpirationTime() throws GameBreakerException {
		return getInterval(OPEN_ORDERS_EXPIRATION_TIME);
	}

	public int getTickerRefreshRate() throws GameBreakerException {
		return getInterval(TICKER_REFRESH_RATE);
	}

	public int getAssetRefreshRate() throws GameBreakerException {
		return getInterval(ASSET_REFRESH_RATE);
	}

	private int getInterval(String interval) throws GameBreakerException {
		checkLoaded();
		return intervals.get(interval);
	}
	
	public boolean getCancelledOrdersFlag() throws GameBreakerException {
		checkLoaded();

		String retryFlag = config.getProperty(RETRY_CANCELLED_ORDERS);

		return new Boolean(retryFlag);
	}

	/**
	 * Checks if the configuration file has been loaded.<br>
	 * 
	 * @throws GameBreakerException
	 *             If the config file hasn't been loaded.<br>
	 */
	private void checkLoaded() throws GameBreakerException {
		if (!loaded) {
			throw new GameBreakerException("Config file hasn't been loaded yet!");
		}
	}

	/**
	 * Parses all the intervals from the configuration file in one go.<br>
	 * If an interval couldn't be extracted or isn't a number the interval will
	 * default to 0.<br>
	 */
	private void parseIntervals() {
		List<String> intervalProperties = new ArrayList<>();
		intervalProperties.add(MAIL_POLLING_INTERVAL);
		intervalProperties.add(STOPLOSS_POLLING_INTERVAL);
		intervalProperties.add(OPEN_ORDERS_POLLING_INTERVAL);
		intervalProperties.add(OPEN_ORDERS_EXPIRATION_TIME);
		intervalProperties.add(TICKER_REFRESH_RATE);
		intervalProperties.add(ASSET_REFRESH_RATE);

		for (String interval : intervalProperties) {
			String extracted = config.getProperty(interval);

			int parsed;
			try {
				parsed = parseInterval(extracted);
			} catch (NullPointerException | GameBreakerException e) {
				log.debug("Couldn't parse {}. Value was {}. Defaulting to 0.", interval, extracted);
				parsed = 0;
			}

			intervals.put(interval, parsed);
		}
	}

	/**
	 * Parses the interval.<br>
	 * 
	 * @throws GameBreakerException
	 *             If the interval couldn't be extracted.
	 */
	private int parseInterval(String interval) throws GameBreakerException {
		return NumberParser.parseInteger(interval);
	}

	/**
	 * Extracts all the accounts from the configuration file.<br>
	 * 
	 * @throws ExchangeException
	 *             If the details of an account are corrupt or couldn't be
	 *             extracted.
	 */
	public Map<String, List<Account>> parseAccounts() throws ExchangeException {
		checkLoaded();

		Map<String, List<Account>> extracted = new HashMap<>();
		SupportedExchange[] supported = SupportedExchange.values();

		for (SupportedExchange supportedExchange : supported) {
			String exchangeName = supportedExchange.getName();

			List<Account> accounts = accountCreator.extractAccounts(exchangeName, config);

			extracted.put(exchangeName, accounts);
		}

		return extracted;
	}

}
