package tvtrader.stubs;

import tvtrader.model.MailConfiguration;

import java.util.Properties;

public class MailConfigurationStub {
	
	private static final int PORT_OFFSET = 3000;

	/**
	 * Protocol: imaps<br>
	 * Host: localhost<br>
	 * Port: 3993<br>
	 * Inbox: INBOX<br>
	 * Username: testuser<br>
	 * Password: testpw<br>
	 */
	public static MailConfiguration getGreenmailImapMailConfiguration() {
		MailConfiguration greenmailConfig = new MailConfiguration();
		greenmailConfig.setProtocol("imaps");
		greenmailConfig.setHost("localhost");
		greenmailConfig.setPort(PORT_OFFSET + 993);
		greenmailConfig.setInbox("INBOX");
		greenmailConfig.setUsername("testuser");
		greenmailConfig.setPassword("testpw");
		
		return greenmailConfig;
	}
	
	/**
	 * Protocol: imaps<br>
	 * Host: invalid<br>
	 * Port: 0<br>
	 * Inbox: invalid<br>
	 * Username: invalid<br>
	 * Password: invalid<br>
	 */
	public static MailConfiguration getGreenmailInvalidImapConfiguration() {
		MailConfiguration greenmailConfig = new MailConfiguration();
		greenmailConfig.setProtocol("imaps");
		greenmailConfig.setHost("invalid");
		greenmailConfig.setPort(0);
		greenmailConfig.setInbox("invalid");
		greenmailConfig.setUsername("invalid");
		greenmailConfig.setPassword("invalid");
		
		return greenmailConfig;
	}
	
	/**
	 * Protocol: INVALID<br>
	 * Host: null<br>
	 * Port: 0<br>
	 * Inbox: null<br>
	 * Username: null<br>
	 * Password: null<br>
	 */
	public static MailConfiguration getInvalidProtocolMailConfiguration() {
		MailConfiguration invalidProtocol = new MailConfiguration();
		invalidProtocol.setProtocol("INVALID");

		return invalidProtocol;
	}
	
	/**
	 * Protocol: testProtocol<br>
	 * Host: localhost<br>
	 * Port: 1<br>
	 * Inbox: INBOX<br>
	 * Username: testuser<br>
	 * Password: testpw<br>
	 */
	public static MailConfiguration getExpectedMailConfiguration() {
		MailConfiguration expected = new MailConfiguration();
		expected.setProtocol("imaps");
		expected.setHost("testhost");
		expected.setPort(1);
		expected.setInbox("testinbox");
		expected.setUsername("testuser");
		expected.setPassword("testpw");
		
		return expected;
	}
	
	/**
	 * Protocol: testprotocol<br>
	 * Host: testhost<br>
	 * Port: 1<br>
	 * Inbox: testinbox<br>
	 * Username: testuser<br>
	 * Password: testpw<br>
	 */
	public static Properties getValidPropertiesStub( ) {
		Properties stub = new Properties();
		stub.put("host", "testhost");
		stub.put("port", "1");
		stub.put("inbox", "testinbox");
		stub.put("username", "testuser");
		stub.put("password", "testpw");
		
		return stub;
	}
	
	/**
	 * Protocol: testprotocol<br>
	 * Host: testhost<br>
	 * Port: UNPARSEABLE<br>
	 * Inbox: testinbox<br>
	 * Username: testuser<br>
	 * Password: testpw<br>
	 */
	public static Properties getUnparseableportStub( ) {
		Properties stub = new Properties();
		stub.put("host", "testhost");
		stub.put("port", "UNPARSEABLE");
		stub.put("inbox", "testinbox");
		stub.put("username", "testuser");
		stub.put("password", "testpw");
		
		return stub;
	}
	
	/**
	 * Protocol: testprotocol<br>
	 * Host: null<br>
	 * Port: 1<br>
	 * Inbox: testinbox<br>
	 * Username: testuser<br>
	 * Password: testpw<br>
	 */
	public static Properties getMissingHostStub( ) {
		Properties stub = new Properties();
		stub.put("inbox", "testinbox");
		stub.put("port", "1");
		stub.put("username", "testuser");
		stub.put("password", "testpw");
		
		return stub;
	}
	
	/**
	 * Protocol: testprotocol<br>
	 * Host: testhost<br>
	 * Port: 1<br>
	 * Inbox: null<br>
	 * Username: testuser<br>
	 * Password: testpw<br>
	 */
	public static Properties getMissingInboxStub( ) {
		Properties stub = new Properties();
		stub.put("host", "testhost");
		stub.put("port", "1");
		stub.put("username", "testuser");
		stub.put("password", "testpw");
		
		return stub;
	}
	
	/**
	 * Protocol: testprotocol<br>
	 * Host: testhost<br>
	 * Port: null<br>
	 * Inbox: testinbox<br>
	 * Username: testuser<br>
	 * Password: testpw<br>
	 */
	public static Properties getMissingPortStub( ) {
		Properties stub = new Properties();
		stub.put("host", "testhost");
		stub.put("inbox", "testinbox");
		stub.put("password", "testpw");
		stub.put("username", "testuser");
		
		return stub;
	}
	
	/**
	 * Protocol: testprotocol<br>
	 * Host: testhost<br>
	 * Port: 1<br>
	 * Inbox: testinbox<br>
	 * Username: testuser<br>
	 * Password: null<br>
	 */
	public static Properties getMissingPasswordStub( ) {
		Properties stub = new Properties();
		stub.put("host", "testhost");
		stub.put("port", "1");
		stub.put("inbox", "testinbox");
		stub.put("username", "testuser");
		
		return stub;
	}
	
	/**
	 * Protocol: null<br>
	 * Host: testhost<br>
	 * Port: 1<br>
	 * Inbox: testinbox<br>
	 * Username: testuser<br>
	 * Password: testpw<br>
	 */
	public static Properties getMissingProtocolStub( ) {
		Properties stub = new Properties();
		stub.put("host", "testhost");
		stub.put("port", "1");
		stub.put("inbox", "testinbox");
		stub.put("username", "testuser");
		stub.put("password", "testpw");
		
		return stub;
	}
	
	/**
	 * Protocol: testprotocol<br>
	 * Host: testhost<br>
	 * Port: 1<br>
	 * Inbox: testinbox<br>
	 * Username: null<br>
	 * Password: testpw<br>
	 */
	public static Properties getMissingUsernameStub( ) {
		Properties stub = new Properties();
		stub.put("host", "testhost");
		stub.put("port", "1");
		stub.put("inbox", "testinbox");
		stub.put("password", "testpw");
		
		return stub;
	}
}
