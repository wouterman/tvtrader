package tvtrader.bittrex;

import com.google.gson.annotations.SerializedName;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import tvtrader.exchange.apidata.Ticker;

/**
 * Immutable.<br>
 * Represents a ticker as returned from the exchange with a market, a bid, ask and last price.<br>
 * 
 * @author Wouter
 *
 */
@ToString @EqualsAndHashCode
public class BittrexTicker implements Ticker {
	
	@SerializedName("MarketName")
	private String market;
	@SerializedName("Ask")
	private double ask;
	@SerializedName("Bid")
	private double bid;
	@SerializedName("Last")
	private double last;
	
	public BittrexTicker(String market, double ask, double bid, double last) {
		this.market = market;
		this.ask = ask;
		this.bid = bid;
		this.last = last;
	}
	
	@Override
	public String getMarket() {
		return market;
	}
	
	@Override
	public double getAsk() {
		return ask;
	}
	
	@Override
	public double getBid() {
		return bid;
	}
	
	@Override
	public double getLast() {
		return last;
	}
	
}
