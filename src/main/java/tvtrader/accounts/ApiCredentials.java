package tvtrader.accounts;

import java.io.Serializable;

import lombok.Data;

@Data
public class ApiCredentials implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final String key;
	private final String secret;
}
