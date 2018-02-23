package tvtrader.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.NonNull;
import tvtrader.controllers.Listener;

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
		this.protocol = DEFAULT_PROTOCOL;
		this.host = "host";
		this.username = "username";
		this.password = "password";
		this.inbox = DEFAULT_INBOX_NAME;
	}
	
	@Id
	private long id = 1;

	@Transient
	private List<Listener> listeners = new ArrayList<>();

	private String protocol;
	private String host;
	private String username;
	private String password;
	private String inbox;
	private int port;

	/**
	 * Sets the host for the mailClient.
	 * 
	 */
	public void setHost(@NonNull String host) {
		if (!this.host.equalsIgnoreCase(host)) {
			this.host = host;
			notifyListeners();
		}
	}

	/**
	 * Sets the protocol for the mailClient.
	 * 
	 */
	public void setProtocol(@NonNull String protocol) {
		if (!this.protocol.equalsIgnoreCase(protocol)) {
			this.protocol = protocol;
			notifyListeners();
		}
	}

	/**
	 * Sets the inbox for the mailClient.
	 * 
	 */
	public void setInbox(@NonNull String inbox) {
		if (!this.inbox.equalsIgnoreCase(inbox)) {
			this.inbox = inbox;
			notifyListeners();
		}
	}

	/**
	 * Sets the username for the mailClient.
	 * 
	 */
	public void setUsername(@NonNull String username) {
		if (!this.username.equalsIgnoreCase(username)) {
			this.username = username;
			notifyListeners();
		}
	}

	/**
	 * Sets the password for the mailClient.
	 * 
	 */
	public void setPassword(@NonNull String password) {
		if (!this.password.equalsIgnoreCase(password)) {
			this.password = password;
			notifyListeners();
		}
	}

	/**
	 * Sets the portnumber for the mailClient.
	 * 
	 */
	public void setPort(int port) {
		if (this.port != port && port >= 0 && port <= 65535) {
			this.port = port;
		}
	}

	public Properties getProperties() {
		Properties mailProps = new Properties();
		mailProps.put("mail.store.protocol", protocol);
		mailProps.put("mail.host", host);
		mailProps.put("mail." + protocol + ".port", port);
		mailProps.put("mail." + protocol + ".inbox", inbox);

		return mailProps;
	}

	private void notifyListeners() {
		for (Listener listener : listeners) {
			listener.update(ListenerField.MAILCONFIG, this);
		}
	}

	public void addChangeListener(Listener newListener) {
		listeners.add(newListener);
	}
}
