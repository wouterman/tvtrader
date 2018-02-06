package tvtrader.exchange.bittrex.response;

import java.util.List;

import tvtrader.exchange.bittrex.BittrexTicker;

public class BittrexTickerResponse extends BittrexBaseResponse<BittrexTicker> {
	List<BittrexTicker> result;
	
	@Override
	public List<BittrexTicker> getResult() {
		return result;
	}
}
