package tvtrader.mail;

import lombok.extern.log4j.Log4j2;

import javax.mail.event.ConnectionEvent;
import javax.mail.event.ConnectionListener;

/**
 * ConnectionListener for the mail store.
 * 
 * @author Wouter
 *
 */
@Log4j2
class ConnectionListenerImpl implements ConnectionListener {
	private boolean connected;
	private boolean disconnected;
	private boolean closed;

	@Override
	public void opened(ConnectionEvent e) {
		log.debug("Connected to the mail store.");
		connected = true;
		closed = false;
	}

	@Override
	public void disconnected(ConnectionEvent e) {
		log.debug("Disconnected from the mail store.");
		connected = false;
		disconnected = true;

	}

	@Override
	public void closed(ConnectionEvent e) {
		log.debug("Closed the connection to the mail store.");
		connected = false;
		closed = true;
	}

	public boolean isConnected() {
		return connected;
	}

	public boolean isDisconnected() {
		return disconnected;
	}

	public boolean isClosed() {
		return closed;
	}
}
