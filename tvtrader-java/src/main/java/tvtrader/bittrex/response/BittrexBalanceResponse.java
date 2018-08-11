package tvtrader.bittrex.response;

import tvtrader.bittrex.BittrexBalance;

import java.util.List;

public class BittrexBalanceResponse extends BittrexBaseResponse<BittrexBalance> {
	List<BittrexBalance> result;

	@Override
	public List<BittrexBalance> getResult() {
		return result;
	}

}
