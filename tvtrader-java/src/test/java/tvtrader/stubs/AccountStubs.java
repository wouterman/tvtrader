package tvtrader.stubs;

import tvtrader.model.Account;
import tvtrader.model.ApiCredentials;

/**
 * Returns stub accounts for testing purposes.<br>
 * 
 * @author Wouter
 *
 */
public class AccountStubs {
	private final static Account BTC_ACCOUNT = new Account("BITTREX", "DEFAULTBTC", "BTC",0.01, 10, 5, 1, new ApiCredentials("key", "secret"));
	private final static Account ETH_ACCOUNT = new Account("BITTREX", "DEFAULTETH", "ETH",0.01, 10, 5, 1, new ApiCredentials("key", "secret"));
	private final static Account NO_GAIN = new Account("BITTREX", "NOGAINBTC", "BTC",0.01, 10, 5, 0, new ApiCredentials("key", "secret"));
	private final static Account NO_STOPLOSS = new Account("BITTREX", "NOSTOPLOSSBTC", "BTC",0.01, 0, 5, 1, new ApiCredentials("key", "secret"));
	
	/**
	 * Exchange: BITTREX<br>
	 * Name: defaultBtc<br>
	 * Main currency: btc<br>
	 * Buylimit: 0.01<br>
	 * Api key: key<br>
	 * Api secret: secret<br>
	 * Stoploss: 10<br>
	 * Tssl: 5<br>
	 * Minimum gain: 1<br>
	 */
	public static Account getBtcAccount() {
		return BTC_ACCOUNT;
	}
	
	/**
	 * Exchange: BITTREX<br>
	 * Name: defaultEth<br>
	 * Main currency: eth<br>
	 * Buylimit: 0.01<br>
	 * Api key: key<br>
	 * Api secret: secret<br>
	 * Stoploss: 10<br>
	 * Tssl: 5<br>
	 * Minimum gain: 1<br>
	 */
	public static Account getEthAccount() {
		return ETH_ACCOUNT;
	}
	
	/**
	 * Exchange: BITTREX<br>
	 * Name: noGainBtc<br>
	 * Main currency: btc<br>
	 * Buylimit: 0.01<br>
	 * Api key: key<br>
	 * Api secret: secret<br>
	 * Stoploss: 10<br>
	 * Tssl: 5<br>
	 * Minimum gain: 0<br>
	 */
	public static Account getNoGainAccount() {
		return NO_GAIN;
	}
	
	/**
	 * Exchange: BITTREX<br>
	 * Name: noStoplossBtc<br>
	 * Main currency: btc<br>
	 * Buylimit: 0.01<br>
	 * Api key: key<br>
	 * Api secret: secret<br>
	 * Stoploss: 0<br>
	 * Tssl: 5<br>
	 * Minimum gain: 1<br>
	 */
	public static Account getNoStoplossAccount() {
		return NO_STOPLOSS;
	}
}
