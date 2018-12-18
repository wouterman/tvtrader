package tvtrader.model;

import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Holds all the relevant info for exchange accounts.<br>
 *
 * @author wouter
 */
@Data
@Entity
@ToString(exclude = "credentials")
public class Account {

	@Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE)
	@EmbeddedId
	private AccountId accountId;

	private String mainCurrency;
	private double buyLimit;
	private double stoploss;
	private double trailingStoploss;
	private double minimumGain;

	@ManyToOne(cascade= CascadeType.ALL)
	private final ApiCredentials credentials;

	/**
	 * Creates a default account.
	 */
	public Account() {
		this("EXCHANGE", "DEFAULT ACCOUNT", "MAIN CURRENCY",
		     0.1, 10.0, 10.0, 10.0,
		     new ApiCredentials("KEY", "SECRET"));
	}

	public Account(@NonNull String exchange, @NonNull String accountName, @NonNull String mainCurrency,
	               double buyLimit, double stoploss, double trailingStoploss, double minimumGain,
	                @NonNull ApiCredentials credentials) {
		this.mainCurrency = mainCurrency;
		this.buyLimit = buyLimit;
		this.stoploss = stoploss;
		this.trailingStoploss = trailingStoploss;
		this.minimumGain = minimumGain;
		this.credentials = credentials;

		this.accountId = new AccountId(exchange, accountName);
	}

	public void setApiKey(String key) { credentials.setKey(key); }
	public void setApiSecret(String secret) { credentials.setSecret(secret); }

	public void setName(String name) { accountId.setName(name); }
	public String getName() {
		return accountId.getName();
	}

	public void setExchange(String exchange) { accountId.setExchange(exchange); }
	public String getExchange() {
		return accountId.getExchange();
	}
}