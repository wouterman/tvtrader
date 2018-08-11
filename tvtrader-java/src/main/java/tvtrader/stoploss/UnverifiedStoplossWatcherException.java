package tvtrader.stoploss;

import tvtrader.exchange.ExchangeException;

/**
 * Is thrown to indicate that a stoploss watcher can't verify the bought price at the exchange.<br>
 * For example if the exchange is repeatedly unavailable.<br>
 * 
 * @author Wouter
 *
 */
public class UnverifiedStoplossWatcherException extends ExchangeException {

	public UnverifiedStoplossWatcherException(String message) {
		super(message);
	}

	private static final long serialVersionUID = 1L;

}
