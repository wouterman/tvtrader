package tvtrader.mail;

public class MailClientException extends Exception {
	private static final long serialVersionUID = 1L;

	public MailClientException(String message, Exception e) {
		super(message, e);
	}

}
