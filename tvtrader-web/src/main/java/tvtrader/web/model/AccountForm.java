package tvtrader.web.model;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

/**
 * DTO class with basic validation for the frontend form.
 *
 * @author Wouter
 */
@Data
public class AccountForm {

	@NotEmpty
	private String accountName;
	@NotEmpty
	private String exchangeName;
	@NotEmpty
	private String apiKey;
	@NotEmpty
	private String apiSecret;
	@NotEmpty
	private String mainCurrency;
	@Min(1)
	private double buyLimit;
	@Min(1)
	private double stoploss;
	@Min(1)
	private double trailingStoploss;
	@Min(1)
	private double minimumGain;
}
