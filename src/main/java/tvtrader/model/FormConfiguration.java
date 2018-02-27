package tvtrader.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.Email;

import lombok.Data;

@Data
public class FormConfiguration {

	@Email
	private String expectedSender;
	
	private String host;
	private String username;
	private String password;
	private String inbox;
	private boolean retryOrderFlag;
	
	@Min(0) @Max(65535)
	private int port;
	
	@Min(1)
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

}
