package tvtrader.mail;

import tvtrader.exceptionlogger.GameBreakerException;

/**
 * This exception indicates that (some of) the settings for the mail server configuration are missing or corrupt.<br>
 * 
 * @author Wouter
 *
 */
public class InvalidMailConfigException extends GameBreakerException {
	private static final long serialVersionUID = 1L;
	
	public InvalidMailConfigException(String message) {
		super(message);
	}
}
