package tvtrader.bittrex.response;

import java.util.List;

import tvtrader.bittrex.BittrexTicker;

public class BittrexTickerResponse extends BittrexBaseResponse<BittrexTicker> {
	List<BittrexTicker> result;
	
	@Override
	public List<BittrexTicker> getResult() {
		return result;
	}
}
