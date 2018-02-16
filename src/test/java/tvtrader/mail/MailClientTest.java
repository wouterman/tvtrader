package tvtrader.mail;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.Security;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.icegreen.greenmail.util.DummySSLSocketFactory;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;

import test.logger.Logger;
import tvtrader.exceptionlogger.UnverifiedException;
import tvtrader.model.Configuration;
import tvtrader.model.ConfigurationField;
import tvtrader.model.MailConfiguration;
import tvtrader.stubs.MailConfigurationStub;

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
	private Configuration configuration;
	
	@InjectMocks
	private MailClient client;
	
	@BeforeAll
	synchronized static void startup() {
		Logger.turnOffLogging();
	}
	
	@BeforeEach
	void setup() {
		MockitoAnnotations.initMocks(this);
		
	    greenMail = new GreenMail(ServerSetupTest.ALL);
	    Security.setProperty("ssl.SocketFactory.provider",
	    	      DummySSLSocketFactory.class.getName());
	    greenMail.setUser(USER_MAIL_ADDRESS, USERNAME, PASSWORD);
	    greenMail.start();
		
		client.setExpectedSender(EXPECTED_SENDER);
	    client.setTimeLimit(FIVE_MINUTES);
	}	
	
	@Test
	void verify_whenConfigurationIsValid_shouldReturn() throws Exception {
		MailConfiguration config = MailConfigurationStub.getGreenmailImapMailConfiguration();
		client.setMailConfiguration(config);
		
		client.verify();
	}
	
	
	@Test
	void verify_whenConfigurationIsInvalid_shouldThrowUnverifiedException() {
		MailConfiguration config = MailConfigurationStub.getGreenmailInvalidImapConfiguration();
		client.setMailConfiguration(config);
		
		assertThrows(UnverifiedException.class, () -> client.verify());
	}
	
	@Test
	void verify_whenServerIsOffline_shouldThrowUnverifiedException() {
		MailConfiguration config = MailConfigurationStub.getGreenmailImapMailConfiguration();
		client.setMailConfiguration(config);
		greenMail.stop();
		
		assertThrows(UnverifiedException.class, () -> client.verify());
	}
	
	@Test
	void fetchMails_whenServerHasMailFromExpectedSender_shouldRetrieveThatMail() throws Exception {
	    GreenMailUtil.sendTextEmailTest(USER_MAIL_ADDRESS, EXPECTED_SENDER,
	            FIRST_MAIL_SUBJECT, MAIL_BODY);
	    assertEquals(1, greenMail.getReceivedMessages().length);
	    
	    MailConfiguration config = MailConfigurationStub.getGreenmailImapMailConfiguration();
	    
	    client.setMailConfiguration(config);
	    
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
	    
	    client.setTimeLimit(ZERO);
	    client.setMailConfiguration(config);
	    
	    List<String> actual = client.fetchSubjectLines();
	    
	    assertTrue(actual.isEmpty());
	}
	
	@Test
	void fetchMails_whenServerHasMailFromUnexpectedSender_shouldIgnoreThatMail() throws Exception {
	    GreenMailUtil.sendTextEmailTest(USER_MAIL_ADDRESS, UNEXPECTED_SENDER,
	            FIRST_MAIL_SUBJECT, MAIL_BODY);
	    assertEquals(1, greenMail.getReceivedMessages().length);
	    
	    MailConfiguration config = MailConfigurationStub.getGreenmailImapMailConfiguration();
	    
	    client.setMailConfiguration(config);
	    
	    List<String> actual = client.fetchSubjectLines();
	    
	    assertTrue(actual.isEmpty());
	}
	
	@Test
	void fetchMails_whenServerHasNoMails_expectEmptyList() throws Exception {
		MailConfiguration config = MailConfigurationStub.getGreenmailImapMailConfiguration();
	    client.setMailConfiguration(config);
	    
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
	    
	    client.setMailConfiguration(config);
	    
	    List<String> actual = client.fetchSubjectLines();
	    
	    assertEquals(1, actual.size());
	    assertTrue(actual.contains(FIRST_MAIL_SUBJECT), "Fetched mails should've contained the first mail subjectline!");
	}
	
	@Test
	void update_shouldUpdateExpectedSender_whenReceivingUpdate() throws Exception {
		Configuration configuration = new Configuration();
		configuration.setExpectedSender(EXPECTED_SENDER);
		
		client.update(ConfigurationField.EXPECTEDSENDER, configuration);
		
		assertEquals(EXPECTED_SENDER, client.getExpectedSender());
	}
	
	@Test
	void update_shouldNotUpdateExpectedSender_whenReceivingIrrelevantUpdate() throws Exception {
		Configuration configuration = new Configuration();
		configuration.setExpectedSender(UNEXPECTED_SENDER);
		
		client.update(ConfigurationField.TICKERREFRESHRATE, configuration);
		
		assertEquals(EXPECTED_SENDER, client.getExpectedSender());
	}
	
	@Test
	void update_shouldUpdateMailConfig_whenReceivingUpdate() throws Exception {
		MailConfiguration config = MailConfigurationStub.getExpectedMailConfiguration();
		Configuration configuration = new Configuration();
		configuration.setMailConfig(config);
		
		client.update(ConfigurationField.MAILCONFIG, configuration);
		
		assertEquals(config, client.getConfig());
	}
	
	@Test
	void update_shouldNotUpdateMailConfig_whenReceivingIrrelevantUpdate() throws Exception {
		MailConfiguration config = MailConfigurationStub.getExpectedMailConfiguration();
		
		Configuration configuration = new Configuration();
		configuration.setMailConfig(config);
		
		client.update(ConfigurationField.TICKERREFRESHRATE, configuration);
		
		assertNull(null);
	}
	
	@AfterEach
	void cleanUp() {
		client.closeConnection();
		greenMail.stop();
	}

}
