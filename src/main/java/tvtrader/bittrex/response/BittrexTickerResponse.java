package tvtrader.bittrex.response;

import tvtrader.bittrex.BittrexTicker;

import java.util.List;

public class BittrexTickerResponse extends BittrexBaseResponse<BittrexTicker> {
	List<BittrexTicker> result;
	
	@Override
	public List<BittrexTicker> getResult() {
		return result;
	}
}
