package tvtrader.web.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

/**
 * DTO class with basic validation for the frontend form.
 *
 * @author Wouter
 */
@Data
public class ConfigurationForm {

	@Email
	@NotEmpty
	private String expectedSender;
	@Min(value = 1)
	private int mailPollingInterval;
    @Min(1)
	private int stoplossInterval;
	@Min(1)
	private int openOrdersInterval;
	@Min(1)
	private int openOrdersExpirationTime;
	@Min(1)
	private int tickerRefreshRate;
	@Min(1)
	private int assetRefreshRate;

	private boolean retryOrderFlag;
}
