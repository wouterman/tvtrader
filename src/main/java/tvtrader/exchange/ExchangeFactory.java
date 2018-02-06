package tvtrader.exchange;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

/**
 * Responsible for creating the exchanges.<br>
 * 
 * @author Wouter
 *
 */
@Log4j2
@Component
public class ExchangeFactory {
	private static final String BITTREX = "BITTREX";

	@Autowired
	@Qualifier("Bittrex")
	private Exchange bittrexExchange;

	/**
	 * Returns a list with all the supported exchanges.<br>
	 * 
	 */
	public List<Exchange> getExchanges() {
		List<Exchange> exchanges = new ArrayList<>();

		SupportedExchange[] supported = SupportedExchange.values();

		for (SupportedExchange supportedExchange : supported) {
			String exchangeName = supportedExchange.getName();

			try {
				exchanges.add(getExchange(exchangeName));
			} catch (UnsupportedExchangeException e) {
				continue;
			}
		}

		log.debug("getExchanges(): {}", exchanges);
		return exchanges;
	}

	/**
	 * Returns an initialized exchange.<br>
	 * .
	 * 
	 * @throws UnsupportedExchangeException
	 *             If the exchange is not supported.
	 */
	public Exchange getExchange(String exchangeName) throws UnsupportedExchangeException {
		if (exchangeName.equalsIgnoreCase(BITTREX)) {
			return bittrexExchange;
		} else {
			throw new UnsupportedExchangeException("Unsupported exchange: " + exchangeName);
		}
	}
}
