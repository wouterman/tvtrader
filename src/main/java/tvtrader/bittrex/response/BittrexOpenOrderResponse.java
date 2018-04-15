package tvtrader.bittrex.response;

import tvtrader.bittrex.BittrexUnfilledOrder;

import java.util.List;

public class BittrexOpenOrderResponse extends BittrexBaseResponse<BittrexUnfilledOrder> {
	List<BittrexUnfilledOrder> result;

	@Override
	public List<BittrexUnfilledOrder> getResult() {
		return result;
	}
}
