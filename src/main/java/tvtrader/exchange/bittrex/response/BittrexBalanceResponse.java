package tvtrader.exchange.bittrex.response;

import java.util.List;

import tvtrader.exchange.bittrex.BittrexBalance;

public class BittrexBalanceResponse extends BittrexBaseResponse<BittrexBalance> {
	List<BittrexBalance> result;

	@Override
	public List<BittrexBalance> getResult() {
		return result;
	}

}
