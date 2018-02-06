package tvtrader.accounts;

import tvtrader.exchange.ExchangeException;

/**
 * An InvalidAccountException is thrown if the AccountCreator can't extract all the information associated with an account from the configuration file.<br>
 * 
 * @author Wouter
 *
 */
class InvalidAccountException extends ExchangeException {
	private static final long serialVersionUID = 1L;
	
	public InvalidAccountException(String message) {
		super(message);
	}
}
