package tvtrader.stubs;

import java.util.Properties;

public class AccountPropertiesStubs {

	/**
	 * Account: defaultbtc<br>
	 * MainCurrency: btc<br>
	 * BuyLimit: 0.01<br>
	 * Apikey: key<br>
	 * Secret: secret<br>
	 * Stoploss: 10<br>
	 * Tssl: 5<br>
	 * MinimumGain: 1<br>
	 * 
	 */
	public static Properties getBittrexBTCAccount() {
		Properties accountProps = new Properties();
		accountProps.put("exchanges", "bittrex");
		accountProps.put("bittrex.account", "defaultbtc");
		accountProps.put("bittrex.defaultbtc.maincurrency", "btc");
		accountProps.put("bittrex.defaultbtc.buylimit", "0.01");
		accountProps.put("bittrex.defaultbtc.apikey", "key");
		accountProps.put("bittrex.defaultbtc.secret", "secret");
		accountProps.put("bittrex.defaultbtc.stoploss", "10");
		accountProps.put("bittrex.defaultbtc.trailingstoploss", "5");
		accountProps.put("bittrex.defaultbtc.minimumgain", "1");

		return accountProps;
	}
	
	/**
	 * Account: defaulteth<br>
	 * MainCurrency: eth<br>
	 * BuyLimit: 0.01<br>
	 * Apikey: key<br>
	 * Secret: secret<br>
	 * Stoploss: 10<br>
	 * Tssl: 5<br>
	 * MinimumGain: 1<br>
	 */
	public static Properties getBittrexETHAccount() {
		Properties accountProps = new Properties();
		accountProps.put("exchanges", "bittrex");

		accountProps.put("bittrex.account", "defaulteth");
		accountProps.put("bittrex.defaulteth.maincurrency", "eth");
		accountProps.put("bittrex.defaulteth.buylimit", "0.01");
		accountProps.put("bittrex.defaulteth.apikey", "key");
		accountProps.put("bittrex.defaulteth.secret", "secret");
		accountProps.put("bittrex.defaulteth.stoploss", "10");
		accountProps.put("bittrex.defaulteth.trailingstoploss", "5");
		accountProps.put("bittrex.defaulteth.minimumgain", "1");

		return accountProps;
	}

	/**
	 * Contains two, duplicate, btc accounts:<br>
	 * <br>
	 * Account: defaultbtc<br>
	 * MainCurrency: btc<br>
	 * BuyLimit: 0.01<br>
	 * Apikey: key<br>
	 * Secret: secret<br>
	 * Stoploss: 10<br>
	 * Tssl: 5<br>
	 * MinimumGain: 1<br>
	 * 
	 */
	public static Properties getDuplicateBTCAccounts() {
		Properties accountProps = new Properties();
		
		accountProps.putAll(getBittrexBTCAccount());
		accountProps.putAll(getBittrexBTCAccount());

		return accountProps;
	}

	/**
	 * Account: defaultbtc<br>
	 * MainCurrency: btc<br>
	 * BuyLimit: 0.01<br>
	 * Apikey: key<br>
	 * Secret: secret<br>
	 * Stoploss: 10<br>
	 * Tssl: 5<br>
	 * MinimumGain: 1<br>
	 * <br>
	 * Account: defaulteth<br>
	 * MainCurrency: eth<br>
	 * BuyLimit: 0.01<br>
	 * Apikey: ethkey<br>
	 * Secret: ethsecret<br>
	 * Stoploss: 10<br>
	 * Tssl: 5<br>
	 * MinimumGain: 1<br>
	 * 
	 */
	public static Properties getBTCAndETHAccounts() {
		Properties accountProps = new Properties();

		accountProps.putAll(getBittrexBTCAccount());
		accountProps.putAll(getBittrexETHAccount());
		
		accountProps.put("bittrex.account", "defaultbtc defaulteth");
		
		return accountProps;
	}
	
	public static Properties getNoAccounts() {
		Properties accountProps = new Properties();
		
		accountProps.putAll(getBittrexBTCAccount());
		accountProps.put("bittrex.account", "");
		
		return accountProps;
	}

	/**
	 * Account: btc<br>
	 * MainCurrency: btc<br>
	 * BuyLimit: UNPARSEABLE<br>
	 * Apikey: key<br>
	 * Secret: secret<br>
	 * Stoploss: 10<br>
	 * Tssl: 5<br>
	 * MinimumGain: 1<br>
	 * 
	 */
	public static Properties getUnparseableBuyLimit() {
		Properties accountProps = getBittrexBTCAccount();
		accountProps.put("bittrex.defaultbtc.buylimit", "UNPARSEABLE");

		return accountProps;
	}

	/**
	 * Account: btc<br>
	 * MainCurrency: btc<br>
	 * BuyLimit: 0.01<br>
	 * Apikey: key<br>
	 * Secret: secret<br>
	 * Stoploss: UNPARSEABLE<br>
	 * Tssl: 5<br>
	 * MinimumGain: 1<br>
	 * 
	 */
	public static Properties getUnparseableStoploss() {
		Properties accountProps = getBittrexBTCAccount();
		accountProps.put("bittrex.defaultbtc.stoploss", "UNPARSEABLE");

		return accountProps;
	}
	
	/**
	 * Account: btc<br>
	 * MainCurrency: btc<br>
	 * BuyLimit: 0.01<br>
	 * Apikey: key<br>
	 * Secret: secret<br>
	 * Stoploss: 10<br>
	 * Tssl: UNPARSEABLE<br>
	 * MinimumGain: 1<br>
	 * 
	 */
	public static Properties getUnparseableTrailingstoploss() {
		Properties accountProps = getBittrexBTCAccount();
		accountProps.put("bittrex.defaultbtc.trailingstoploss", "UNPARSEABLE");

		return accountProps;
	}

	/**
	 * Account: btc<br>
	 * MainCurrency: btc<br>
	 * BuyLimit: 0.01<br>
	 * Apikey: key<br>
	 * Secret: secret<br>
	 * Stoploss: 10<br>
	 * Tssl: 5<br>
	 * MinimumGain: UNPARSEABLE<br>
	 * 
	 */
	public static Properties getUnparseableMinimumGain() {
		Properties accountProps = getBittrexBTCAccount();
		accountProps.put("bittrex.defaultbtc.minimumgain", "UNPARSEABLE");

		return accountProps;
	}

	/**
	 * Missing the MainCurrency field.
	 * 
	 * Account: btc<br>
	 * BuyLimit: 0.01<br>
	 * Apikey: key<br>
	 * Secret: secret<br>
	 * Stoploss: 10<br>
	 * Tssl: 5<br>
	 * MinimumGain: 1<br>
	 * 
	 */
	public static Properties getMissingMainCurrencyField() {
		Properties accountProps = getBittrexBTCAccount();
		accountProps.remove("bittrex.defaultbtc.maincurrency");

		return accountProps;
	}

	/**
	 * Missing the BuyLimit field.<br>
	 * <br>
	 * Account: btc<br>
	 * MainCurrency: btc<br>
	 * Apikey: key<br>
	 * Secret: secret<br>
	 * Stoploss: 10<br>
	 * Tssl: 5<br>
	 * MinimumGain: 1<br>
	 * 
	 */
	public static Properties getMissingBuyLimitField() {
		Properties accountProps = getBittrexBTCAccount();
		accountProps.remove("bittrex.defaultbtc.buylimit");

		return accountProps;
	}

	/**
	 * Missing the Apikey field.<br>
	 * <br>
	 * Account: btc<br>
	 * MainCurrency: btc<br>
	 * BuyLimit: 0.01<br>
	 * Secret: secret<br>
	 * Stoploss: 10<br>
	 * Tssl: 5<br>
	 * MinimumGain: 1<br>
	 * 
	 */
	public static Properties getMissingApikeyField() {
		Properties accountProps = getBittrexBTCAccount();
		accountProps.remove("bittrex.defaultbtc.apikey");

		return accountProps;
	}

	/**
	 * Missing the secret field.<br>
	 * <br>
	 * Account: btc<br>
	 * MainCurrency: btc<br>
	 * BuyLimit: 0.01<br>
	 * Apikey: key<br>
	 * Stoploss: 10<br>
	 * Tssl: 5<br>
	 * MinimumGain: 1<br>
	 * 
	 */
	public static Properties getMissingSecretField() {
		Properties accountProps = getBittrexBTCAccount();
		accountProps.remove("bittrex.defaultbtc.secret");

		return accountProps;
	}

	/**
	 * Missing the Stoploss field.<br>
	 * <br>
	 * Account: btc<br>
	 * MainCurrency: btc<br>
	 * BuyLimit: 0.01<br>
	 * Apikey: key<br>
	 * Secret: secret<br>
	 * Tssl: 5<br>
	 * MinimumGain: 1<br>
	 * 
	 */
	public static Properties getMissingStoplossField() {
		Properties accountProps = getBittrexBTCAccount();
		accountProps.remove("bittrex.defaultbtc.stoploss");

		return accountProps;
	}
	
	/**
	 * Missing the Trailingstoploss field.<br>
	 * <br>
	 * Account: btc<br>
	 * MainCurrency: btc<br>
	 * BuyLimit: 0.01<br>
	 * Apikey: key<br>
	 * Secret: secret<br>
	 * Stoploss: 10<br>
	 * MinimumGain: 1<br>
	 * 
	 */
	public static Properties getMissingTrailingstoplossField() {
		Properties accountProps = getBittrexBTCAccount();
		accountProps.remove("bittrex.defaultbtc.trailingstoploss");

		return accountProps;
	}

	/**
	 * Missing the MinimumGain field.<br>
	 * <br>
	 * Account: btc<br>
	 * MainCurrency: btc<br>
	 * BuyLimit: 0.01<br>
	 * Apikey: key<br>
	 * Secret: secret<br>
	 * Stoploss: 10<br>
	 * Tssl: 5<br>
	 * 
	 */
	public static Properties getMissingMinimumGainField() {
		Properties accountProps = getBittrexBTCAccount();
		accountProps.remove("bittrex.defaultbtc.minimumgain");

		return accountProps;
	}
}
