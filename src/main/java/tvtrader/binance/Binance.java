package tvtrader.binance;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import tvtrader.exchange.Api;
import tvtrader.exchange.Exchange;
import tvtrader.exchange.apidata.JsonParser;
import tvtrader.services.WebService;

@Component(value="Binance")
public class Binance extends Exchange {
	private static final String NAME = "BINANCE";
	private static final double MINIMUM_ORDER_AMOUNT = 0.001;
	
	private static final double TAKER_FEE = 0.001;
	private static final double MAKER_FEE = 0.001;

	public Binance(@Qualifier("BinanceApi") Api api, WebService requestService, @Qualifier("BinanceParser") JsonParser parser) {
		super(api, requestService, parser);
	}

	@Override
	public String createMarket(String mainCoin, String altCoin) {
		return altCoin+mainCoin;
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
