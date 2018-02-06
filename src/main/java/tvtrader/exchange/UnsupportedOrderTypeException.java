package tvtrader.exchange;

/**
 * Should be thrown when the ordertype is unsupported for that exchange.
 * 
 * @author Wouter
 *
 */
public class UnsupportedOrderTypeException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public UnsupportedOrderTypeException(String message) {
		super(message);
	}
}
