package tvtrader.exchange.bittrex;

import com.google.gson.annotations.SerializedName;

public class BittrexBalance {
	@SerializedName("Currency")
	private String currency;
	
	@SerializedName("Available")
	private double available;
	
	public String getCurrency() {
		return currency;
	}
	public double getAvailableBalance() {
		return available;
	}
}
