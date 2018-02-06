package tvtrader.accounts;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

/**
 * Holds all the relevant info for exchange accounts.<br>
 * Immutable.<br>
 * 
 * @author wouter
 *
 */
@Data
@ToString(exclude="credentials")
public class Account implements Serializable {
	private static final long serialVersionUID = 1L;

	private final String name;
	private final String mainCurrency;
	private final double buyLimit;
	private final double stoploss;
	private final double trailingStoploss;
	private final double minimumGain;
	private final ApiCredentials credentials;
}
