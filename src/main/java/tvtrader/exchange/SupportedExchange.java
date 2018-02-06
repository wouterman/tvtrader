package tvtrader.exchange;

/**
 * Enum containing all the supported exchanges.<br>
 * 
 * @author Wouter
 *
 */
public enum SupportedExchange {
	BITTREX("BITTREX");
	
	String name;
	
	SupportedExchange(String type) {
		this.name = type;
	}
	
	public String getName() {
		return name;
	}
}
