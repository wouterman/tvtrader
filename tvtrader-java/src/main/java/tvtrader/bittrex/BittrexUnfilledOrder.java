package tvtrader.bittrex;

import com.google.gson.annotations.SerializedName;
import lombok.ToString;

@ToString
public class BittrexUnfilledOrder extends BittrexOrder {
	
	//Json response fields that differ from filled orders response.
	@SerializedName("Opened")
	private String opened;
	@SerializedName("CommissionPaid")
	private double commissionPaid;
	
	@Override
	public long getTimeStamp() {
		return getTimeStamp(opened);
	}
	
	@Override
	public double getCommission() {
		return commissionPaid;
	}

}
