package tvtrader.model;

import lombok.Data;
import tvtrader.utils.PriceFormatter;

import java.text.DecimalFormat;

/**
 * Contains all the information needed to place a valid order at the exchange.
 * 
 * @author Wouter
 *
 */
@Data
public class MarketOrder {
	private String exchange = "";
	private String account = "";
	private String mainCoin = "";
	private String altCoin = "";
	private OrderType orderType = OrderType.UNSUPPORTED;
	private double quantity;
	private double rate;

	@Override
	public String toString() {
		DecimalFormat formatter = PriceFormatter.getFormatter();
		return "Exchange: " + exchange + " account: " + account + " type: " + orderType + " market: " + mainCoin + "/" + altCoin + " quantity: " + formatter.format(quantity) + " rate: " + formatter.format(rate);
	}
	
}
