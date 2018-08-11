package tvtrader.web.model;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

/**
 * DTO class with basic validation for the frontend form.
 *
 * @author Wouter
 */
@Data
public class MailConfigurationForm {

	@NotEmpty
	private String protocol;
	@NotEmpty
	private String host;
	@NotEmpty
	private String username;
	@NotEmpty
	private String password;
	@NotEmpty
	private String inbox;
	@Min(0) @Max(65535)
	private int port;
}
