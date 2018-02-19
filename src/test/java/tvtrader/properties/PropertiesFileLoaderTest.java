package tvtrader.properties;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import test.logger.Logger;
import tvtrader.controllers.InputController;
import tvtrader.properties.PropertiesFileLoader;
import tvtrader.properties.PropertiesFileParser;

class PropertiesFileLoaderTest {
	private static final String INVALID_PATH = "INVALID";

	@Mock
	private InputController controller;
	@Mock
	private PropertiesFileParser parser;

	@InjectMocks
	private PropertiesFileLoader loader;

	@BeforeAll
	synchronized static void startup() {
		Logger.turnOffLogging();
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

		boolean loaded = loader.autoload("src/test/resources/test.properties");

		assertTrue(loaded);
	}

}
