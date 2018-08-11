package tvtrader.exchange;


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
