package tvtrader.model;

import lombok.Data;

/**
 * Holds a pair of authentication credentials for an exchange.<br>
 * 
 * @author Wouter
 *
 */
@Data
public class ApiCredentials {
	private final String key;
	private final String secret;
}
