package tvtrader.bittrex.response;

import java.util.List;

import tvtrader.bittrex.BittrexBalance;

public class BittrexBalanceResponse extends BittrexBaseResponse<BittrexBalance> {
	List<BittrexBalance> result;

	@Override
	public List<BittrexBalance> getResult() {
		return result;
	}

}
