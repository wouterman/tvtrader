package tvtrader.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Holds all the relevant info for exchange accounts.<br>
 * 
 * @author wouter
 *
 */
@Data
@ToString(exclude="credentials")
public class Account {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	private final String exchange;
	private final String name;
	private final String mainCurrency;
	private final double buyLimit;
	private final double stoploss;
	private final double trailingStoploss;
	private final double minimumGain;
	private final ApiCredentials credentials;
}