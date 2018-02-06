package tvtrader.utils;

import tvtrader.exceptionlogger.GameBreakerException;

/**
 * Is thrown to indicate that a string couldn't be parsed into a number.<br>
 * 
 * @author Wouter
 *
 */
public class ParserException extends GameBreakerException {
	private static final long serialVersionUID = 1L;

	public ParserException(String message) {
		super(message);
	}

}
