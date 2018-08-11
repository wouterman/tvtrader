package tvtrader.exchange;

/**
 * General exception indicating something went wrong while contacting the exchange.
 * 
 * @author Wouter
 *
 */
public class ExchangeException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public ExchangeException(String message) {
		super(message);
	}
	
	public ExchangeException(String message, Exception e) {
		super(message, e);
	}
}
