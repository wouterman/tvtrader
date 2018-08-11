package tvtrader.bittrex.response;

import tvtrader.bittrex.BittrexFilledOrder;

import java.util.List;

public class BittrexOrderHistoryResponse extends BittrexBaseResponse<BittrexFilledOrder> {
	List<BittrexFilledOrder> result;

	@Override
	public List<BittrexFilledOrder> getResult() {
		return result;
	}
	
}
