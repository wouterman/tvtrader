package tvtrader.exchange.apidata;

/**
 * Represents a ticker as returned from the exchange with a market, a bid, ask and last price.<br>
 * 
 * @author Wouter
 *
 */
public interface Ticker {
	public abstract String getMarket();
	public abstract double getAsk();
	public abstract double getBid();
	public abstract double getLast();
}
