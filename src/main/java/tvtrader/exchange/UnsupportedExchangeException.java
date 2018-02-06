package tvtrader.exchange;

import tvtrader.exceptionlogger.GameBreakerException;

/**
 * Should be thrown when an exchange is requested that isn't supported.
 * 
 * @author Wouter
 *
 */
public class UnsupportedExchangeException extends GameBreakerException {
	private static final long serialVersionUID = 1L;
	
	public UnsupportedExchangeException(String message) {
		super(message);
	}
}
