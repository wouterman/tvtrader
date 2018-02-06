package tvtrader.model;

import java.io.Serializable;
import java.util.Properties;

import lombok.Data;

/**
 * Data holder for the mail configuration.
 * 
 * @author Wouter
 *
 */
@Data
public class MailConfiguration implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String protocol;
	private String host;
	private String username;
	private String password;
	private String inbox;
	private int port;
	
	public Properties getProperties() {
		Properties mailProps = new Properties();
		mailProps.put("mail.store.protocol", protocol);
		mailProps.put("mail.host", host);
		mailProps.put("mail." + protocol + ".port", port);
		mailProps.put("mail." + protocol + ".inbox", inbox);
		
		return mailProps;
	}
}
