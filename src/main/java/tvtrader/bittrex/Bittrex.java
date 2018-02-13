package tvtrader.bittrex;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import tvtrader.exchange.Api;
import tvtrader.exchange.Exchange;
import tvtrader.exchange.apidata.JsonParser;
import tvtrader.services.WebService;

/**
 * Representation of Bittrex.<br>
 * <br>
 * 
 * @author Wouter
 *
 */
@Component(value = "Bittrex")
public class Bittrex extends Exchange {
	private static final String NAME = "BITTREX";
	private static final String MARKET_DELIMITER = "-";
	private static final double MINIMUM_ORDER_AMOUNT = 0.0005;

	/**
	 * Bittrex uses one fee for all orders.
	 */
	private static final double TAKER_FEE = 0.0025;
	private static final double MAKER_FEE = 0.0025;

	public Bittrex(@Qualifier("BittrexApi") Api api, WebService requestService, @Qualifier("BittrexParser") JsonParser parser) {
		super(api, requestService, parser);
	}

	@Override
	public String createMarket(String mainCoin, String altCoin) {
		return mainCoin + MARKET_DELIMITER + altCoin;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public double getTakerFee() {
		return TAKER_FEE;
	}

	@Override
	public double getMakerFee() {
		return MAKER_FEE;
	}

	@Override
	public double getMinimumOrderAmount() {
		return MINIMUM_ORDER_AMOUNT;
	}

}
