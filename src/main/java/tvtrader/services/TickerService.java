package tvtrader.services;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import tvtrader.caches.TickerCache;
import tvtrader.controllers.Listener;
import tvtrader.exchange.Exchange;
import tvtrader.exchange.ExchangeException;
import tvtrader.exchange.ExchangeFactory;
import tvtrader.exchange.apidata.Ticker;
import tvtrader.model.Configuration;
import tvtrader.model.ConfigurationField;

@Log4j2
@Component
public class TickerService implements Listener {
	private static final String BID = "BID";
	private static final String ASK = "ASK";
	private Map<String, TickerCache> caches;
	@Getter	@Setter private int tickerRefreshRate;
	@Autowired @Setter private ExchangeFactory factory;

	public TickerService(Configuration configuration) {
		caches = new HashMap<>();
		configuration.addChangeListener(this);
	}

	/**
	 * Fetches and returns the latest askprice for the provided market.<br>
	 * 
	 * @param exchangeName
	 *            The exchange from which to fetch the price.
	 * @param mainCoin
	 *            Main currency of the market.
	 * @param altCoin
	 *            Alt currency of the market.
	 * @return The latest ask price.
	 * @throws ExchangeException
	 *             If anything goes wrong while contacting the exchange or if the
	 *             exchange is unknown.
	 */
	public double getAsk(String exchangeName, String mainCoin, String altCoin) throws ExchangeException {
		return getPrice(exchangeName, mainCoin, altCoin, ASK);
	}

	/**
	 * Fetches and returns the latest bidprice for the provided market.<br>
	 * 
	 * @param exchangeName
	 *            The exchange from which to fetch the price.
	 * @param mainCoin
	 *            Main currency of the market.
	 * @param altCoin
	 *            Alt currency of the market.
	 * @return The latest bid price.
	 * @throws ExchangeException
	 *             If anything goes wrong while contacting the exchange or if the
	 *             exchange is unknown.
	 */
	public double getBid(String exchangeName, String mainCoin, String altCoin) throws ExchangeException {
		return getPrice(exchangeName, mainCoin, altCoin, BID);
	}

	private double getPrice(String exchangeName, String mainCoin, String altCoin, String type)
			throws ExchangeException {

		Exchange exchange = factory.getExchange(exchangeName);

		// If we have never fetched a price for this exchange we first need to create a
		// new
		// cache.
		TickerCache cache = caches.computeIfAbsent(exchangeName, value -> new TickerCache());

		if (refreshNeeded(cache)) {
			log.debug("Refreshing ticker cache for {}.", exchangeName);
			cache.refreshCache(exchange.getTickers());

		}

		String market = exchange.createMarket(mainCoin, altCoin);
		log.debug("Getting ticker for: {}", market);

		Ticker current = cache.getTicker(market);

		if (current == null) {
			throw new ExchangeException(market + " isn't a valid market on " + exchangeName);
		} else {
			return determinePriceType(type, current);
		}
	}

	/**
	 * Checks if the cache needs to be refreshed.<br>
	 */
	private boolean refreshNeeded(TickerCache cache) {
		long currentTimeMilli = System.currentTimeMillis();
		long refreshtime = cache.getLastRefresh() + (tickerRefreshRate * 1_000);
		log.debug("{} >= {}", LocalDateTime.ofEpochSecond(currentTimeMilli / 1000, 0, ZoneOffset.UTC),
				LocalDateTime.ofEpochSecond(refreshtime / 1000, 0, ZoneOffset.UTC));
		return currentTimeMilli >= refreshtime;
	}

	private double determinePriceType(String type, Ticker current) {
		if (type.equals(BID)) {
			return current.getBid();
		} else if (type.equals(ASK)) {
			return current.getAsk();
		} else {
			return 0;
		}
	}

	@Override
	public void update(ConfigurationField changedField, Configuration configuration) {
		if (changedField == ConfigurationField.TICKERREFRESHRATE) {
			setTickerRefreshRate(configuration.getTickerRefreshRate());
		}
	}

}
