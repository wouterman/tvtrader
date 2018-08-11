package tvtrader.model;

import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Properties;

/**
 * Data holder for the mail configuration.
 * 
 * @author Wouter
 *
 */
@Entity
@Data
@Component
public class MailConfiguration {
	private static final String DEFAULT_INBOX_NAME = "INBOX";
	private static final String DEFAULT_PROTOCOL = "imaps";

	public MailConfiguration() {
		this.name = "DEFAULT";
		this.protocol = DEFAULT_PROTOCOL;
		this.host = "host";
		this.username = "username";
		this.password = "password";
		this.inbox = DEFAULT_INBOX_NAME;
		this.port=0;
	}

	@Id @Setter(AccessLevel.NONE) @Getter(AccessLevel.NONE)
	private String name;

	@NonNull
	private String protocol;
	@NonNull
	private String host;
	@NonNull
	private String username;
	@NonNull
	private String password;
	@NonNull
	private String inbox;

	private int port;

	/**
	 * Sets the portnumber for the mailClient.
	 * 
	 */
	public void setPort(int port) {
		if (port < 0 || port > 65535) {
			throw new IllegalArgumentException(("Portnumber must be between 0 and 65535!"));
		}

		this.port = port;
	}

	public Properties getProperties() {
		Properties mailProps = new Properties();
		mailProps.put("mail.store.protocol", protocol);
		mailProps.put("mail.host", host);
		mailProps.put("mail." + protocol + ".port", port);
		mailProps.put("mail." + protocol + ".inbox", inbox);

		return mailProps;
	}

}
