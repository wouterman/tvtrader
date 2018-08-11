package tvtrader.exceptionlogger;

/**
 * Should be thrown when 'something' could not be verified. (I.e. Account/Mail credentials or a stoplosswatcher)<br>
 * 
 * @author Wouter
 *
 */
public class UnverifiedException extends GameBreakerException {
	private static final long serialVersionUID = 1L;
	
	public UnverifiedException(String message) {
		super(message);
	}
	
	public UnverifiedException(String message, Exception e) {
		super(message, e);
	}
}
