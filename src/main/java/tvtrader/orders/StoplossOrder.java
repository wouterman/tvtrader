package tvtrader.orders;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

@Data
@EqualsAndHashCode(of = { "exchange", "account", "altcoin" })
@ToString(of = { "exchange", "account", "altcoin" })
public class StoplossOrder {
	private final String exchange;
	private final String account;
	private final String altcoin;
	private double referencePrice;
	private double tsslPrice;
	private boolean verified;
	@Setter(lombok.AccessLevel.NONE)
	private int verifyCounter = 0;

	public void incrementVerifyCounter() {
		verifyCounter++;
	}
}
