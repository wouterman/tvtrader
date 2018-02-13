package tvtrader.bittrex.response;

import java.util.List;

import tvtrader.bittrex.BittrexFilledOrder;

public class BittrexOrderHistoryResponse extends BittrexBaseResponse<BittrexFilledOrder> {
	List<BittrexFilledOrder> result;

	@Override
	public List<BittrexFilledOrder> getResult() {
		return result;
	}
	
}
