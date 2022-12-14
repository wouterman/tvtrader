package tvtrader.properties;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import tvtrader.model.MailConfiguration;
import tvtrader.services.AccountService;
import tvtrader.services.ConfigurationService;
import tvtrader.stubs.MailConfigurationStub;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PropertiesFileLoaderTest {
	private static final String INVALID_PATH = "INVALID";

	@Mock
	private ConfigurationService configurationService;
	@Mock
	private AccountService accountService;
	@Mock
	private PropertiesFileParser parser;

	@InjectMocks
	private PropertiesFileLoader loader;

	@BeforeAll
	synchronized static void startup() {

	}

	@BeforeEach
	void setup() {
		MockitoAnnotations.initMocks(this);

	}

	@Test
	void load_whenPathIsInvalid_shouldReturnFalse() {
		boolean loaded = loader.autoload(INVALID_PATH);
		
		assertFalse(loaded);
	}

	@Test
	void load_whenPathIsValid_shoudReturnTrue() throws Exception {
		MailConfiguration mailConfig = MailConfigurationStub.getExpectedMailConfiguration();
		Mockito.when(parser.parseMailConfiguration()).thenReturn(mailConfig);

		boolean loaded = loader.autoload("src/test/resources/test.properties");

		assertTrue(loaded);
	}

}
