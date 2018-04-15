package tvtrader.bittrex;

import com.google.gson.annotations.SerializedName;
import lombok.ToString;

@ToString
public class BittrexFilledOrder extends BittrexOrder {
	//Json response fields that differ from open orders response.
	
	@SerializedName("TimeStamp")
	private String timestamp;
	
	@SerializedName("Commission")
	private double commission;
	
	@Override
	public long getTimeStamp() {
		return getTimeStamp(timestamp);
	}
	
	@Override
	public double getCommission() {
		return commission;
	}
	
}
