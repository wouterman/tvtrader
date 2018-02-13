package tvtrader.bittrex.response;

import java.util.List;

import tvtrader.bittrex.BittrexUnfilledOrder;

public class BittrexOpenOrderResponse extends BittrexBaseResponse<BittrexUnfilledOrder> {
	List<BittrexUnfilledOrder> result;

	@Override
	public List<BittrexUnfilledOrder> getResult() {
		return result;
	}
}
