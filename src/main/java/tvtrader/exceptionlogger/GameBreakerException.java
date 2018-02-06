package tvtrader.exceptionlogger;

import tvtrader.exchange.ExchangeException;

/**
 * Indicates a game breaking exception.<br>
 * This exception is thrown when something goes wrong while starting up or verifying any credentials.<br>
 * 
 * @author Wouter
 *
 */
public class GameBreakerException extends ExchangeException {
	private static final long serialVersionUID = 1L;

	public GameBreakerException(String message) {
		super(message);
	}

	public GameBreakerException(String message, Exception e) {
		super(message, e);
	}
}
