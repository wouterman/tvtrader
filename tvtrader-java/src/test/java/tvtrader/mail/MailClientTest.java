package tvtrader.mail;

import com.icegreen.greenmail.util.DummySSLSocketFactory;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import tvtrader.exceptionlogger.UnverifiedException;
import tvtrader.model.MailConfiguration;
import tvtrader.services.ConfigurationService;
import tvtrader.stubs.MailConfigurationStub;

import java.security.Security;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MailClientTest {
	private static final String FIRST_MAIL_SUBJECT = "firstMail";
	private static final String MAIL_BODY = "body";
	private static final String EXPECTED_SENDER = "testsender@localhost.com";
	private static final String UNEXPECTED_SENDER = "notExpected@localhost.com";
	private static final String USER_MAIL_ADDRESS = "testuser@test.com";
	private static final String USERNAME = "testuser";
	private static final String PASSWORD = "testpw";
	private static final int ZERO = 0;
	private static final int FIVE_MINUTES = 300;
	
	private GreenMail greenMail;
	
	@Mock
	private ConfigurationService service;
	
	@InjectMocks
	private MailClient client;
	
	@BeforeAll
	synchronized static void startup() {

	}
	
	@BeforeEach
	void setup() {
		MockitoAnnotations.initMocks(this);
		
	    greenMail = new GreenMail(ServerSetupTest.ALL);
	    Security.setProperty("ssl.SocketFactory.provider",
	    	      DummySSLSocketFactory.class.getName());
	    greenMail.setUser(USER_MAIL_ADDRESS, USERNAME, PASSWORD);
	    greenMail.start();
		
	    client.setTimeLimit(FIVE_MINUTES);
	}	
	
	@Test
	void verify_whenConfigurationIsValid_shouldReturn() throws Exception {
		MailConfiguration config = MailConfigurationStub.getGreenmailImapMailConfiguration();
		Mockito.when(service.getMailConfigurationAsProperties()).thenReturn(config.getProperties());
		Mockito.when(service.getUsername()).thenReturn(config.getUsername());
		Mockito.when(service.getPassword()).thenReturn(config.getPassword());

		client.verify();
	}
	
	
	@Test
	void verify_whenConfigurationIsInvalid_shouldThrowUnverifiedException() {
		MailConfiguration config = MailConfigurationStub.getGreenmailInvalidImapConfiguration();
		Mockito.when(service.getMailConfigurationAsProperties()).thenReturn(config.getProperties());
		Mockito.when(service.getUsername()).thenReturn(config.getUsername());
		Mockito.when(service.getPassword()).thenReturn(config.getPassword());

		assertThrows(UnverifiedException.class, () -> client.verify());
	}
	
	@Test
	void verify_whenServerIsOffline_shouldThrowUnverifiedException() {
		MailConfiguration config = MailConfigurationStub.getGreenmailImapMailConfiguration();
		Mockito.when(service.getMailConfigurationAsProperties()).thenReturn(config.getProperties());
		Mockito.when(service.getUsername()).thenReturn(config.getUsername());
		Mockito.when(service.getPassword()).thenReturn(config.getPassword());

		greenMail.stop();
		
		assertThrows(UnverifiedException.class, () -> client.verify());
	}
	
	@Test
	void fetchMails_whenServerHasMailFromExpectedSender_shouldRetrieveThatMail() throws Exception {
	    GreenMailUtil.sendTextEmailTest(USER_MAIL_ADDRESS, EXPECTED_SENDER,
	            FIRST_MAIL_SUBJECT, MAIL_BODY);
	    assertEquals(1, greenMail.getReceivedMessages().length);
	    
	    MailConfiguration config = MailConfigurationStub.getGreenmailImapMailConfiguration();
		Mockito.when(service.getMailConfigurationAsProperties()).thenReturn(config.getProperties());
		Mockito.when(service.getUsername()).thenReturn(config.getUsername());
		Mockito.when(service.getPassword()).thenReturn(config.getPassword());
		Mockito.when(service.getInbox()).thenReturn(config.getInbox());
		Mockito.when(service.getExpectedSender()).thenReturn(EXPECTED_SENDER);
	    
	    List<String> actual = client.fetchSubjectLines();
	    
	    assertEquals(1, actual.size());
	    assertTrue(actual.contains(FIRST_MAIL_SUBJECT), "Fetched mails should've contained the first mail subjectline!");
	}
	
	@Test
	void fetchMails_whenServerHasOldMails_shouldIgnoreMailsOlderThanTimeLimit() throws Exception {
	    GreenMailUtil.sendTextEmailTest(USER_MAIL_ADDRESS, EXPECTED_SENDER,
	            FIRST_MAIL_SUBJECT, MAIL_BODY);
	    assertEquals(1, greenMail.getReceivedMessages().length);
	    
	    MailConfiguration config = MailConfigurationStub.getGreenmailImapMailConfiguration();
		Mockito.when(service.getMailConfigurationAsProperties()).thenReturn(config.getProperties());
		Mockito.when(service.getUsername()).thenReturn(config.getUsername());
		Mockito.when(service.getPassword()).thenReturn(config.getPassword());
		Mockito.when(service.getInbox()).thenReturn(config.getInbox());
		Mockito.when(service.getExpectedSender()).thenReturn(EXPECTED_SENDER);
	    
	    client.setTimeLimit(ZERO);

	    List<String> actual = client.fetchSubjectLines();
	    
	    assertTrue(actual.isEmpty());
	}
	
	@Test
	void fetchMails_whenServerHasMailFromUnexpectedSender_shouldIgnoreThatMail() throws Exception {
	    GreenMailUtil.sendTextEmailTest(USER_MAIL_ADDRESS, UNEXPECTED_SENDER,
	            FIRST_MAIL_SUBJECT, MAIL_BODY);
	    assertEquals(1, greenMail.getReceivedMessages().length);
	    
	    MailConfiguration config = MailConfigurationStub.getGreenmailImapMailConfiguration();
		Mockito.when(service.getMailConfigurationAsProperties()).thenReturn(config.getProperties());
		Mockito.when(service.getUsername()).thenReturn(config.getUsername());
		Mockito.when(service.getPassword()).thenReturn(config.getPassword());
		Mockito.when(service.getInbox()).thenReturn(config.getInbox());
		Mockito.when(service.getExpectedSender()).thenReturn(EXPECTED_SENDER);

	    List<String> actual = client.fetchSubjectLines();
	    
	    assertTrue(actual.isEmpty());
	}
	
	@Test
	void fetchMails_whenServerHasNoMails_expectEmptyList() throws Exception {
		MailConfiguration config = MailConfigurationStub.getGreenmailImapMailConfiguration();
		Mockito.when(service.getMailConfigurationAsProperties()).thenReturn(config.getProperties());
		Mockito.when(service.getUsername()).thenReturn(config.getUsername());
		Mockito.when(service.getPassword()).thenReturn(config.getPassword());
		Mockito.when(service.getInbox()).thenReturn(config.getInbox());
		Mockito.when(service.getExpectedSender()).thenReturn(EXPECTED_SENDER);

	    List<String> actual = client.fetchSubjectLines();
	    
	    assertTrue(actual.isEmpty());
	}
	
	@Test
	void fetchMails_whenServerHasDuplicateMails_shouldIgnoreDuplicate() throws Exception {
	    GreenMailUtil.sendTextEmailTest(USER_MAIL_ADDRESS, EXPECTED_SENDER,
	            FIRST_MAIL_SUBJECT, MAIL_BODY);
	    GreenMailUtil.sendTextEmailTest(USER_MAIL_ADDRESS, EXPECTED_SENDER,
	            FIRST_MAIL_SUBJECT, MAIL_BODY);
	    assertEquals(2, greenMail.getReceivedMessages().length);
	    
	    MailConfiguration config = MailConfigurationStub.getGreenmailImapMailConfiguration();
		Mockito.when(service.getMailConfigurationAsProperties()).thenReturn(config.getProperties());
		Mockito.when(service.getUsername()).thenReturn(config.getUsername());
		Mockito.when(service.getPassword()).thenReturn(config.getPassword());
		Mockito.when(service.getInbox()).thenReturn(config.getInbox());
		Mockito.when(service.getExpectedSender()).thenReturn(EXPECTED_SENDER);
	    
	    List<String> actual = client.fetchSubjectLines();
	    
	    assertEquals(1, actual.size());
	    assertTrue(actual.contains(FIRST_MAIL_SUBJECT), "Fetched mails should've contained the first mail subjectline!");
	}

	@AfterEach
	void cleanUp() {
		client.closeConnection();
		greenMail.stop();
	}

}
