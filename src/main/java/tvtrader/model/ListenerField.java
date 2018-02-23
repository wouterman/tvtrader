package tvtrader.model;

/**
 * Enum representing all the fields in the Configuration model class.<br>
 * Used to indicate to listeners what exactly has changed in the configuration.<br>
 * 
 * @author Wouter
 *
 */
public enum ConfigurationField {
	MAILCONFIG,
	EXPECTEDSENDER,
	MAILPOLLINGINTERVAL,
	STOPLOSSINTERVAL,
	OPENORDERSINTERVAL,
	OPENORDERSEXPIRATIONTIME,
	UNFILLEDORDERSREPLACEFLAG,
	TICKERREFRESHRATE,
	ASSETREFRESHRATE
}
