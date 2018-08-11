package tvtrader;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import tvtrader.application.ConsoleRunner;
import tvtrader.properties.PropertiesFileLoader;
import tvtrader.services.JobService;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ConsoleRunnerTest {
	private static final String PROPERTIES_FLAG = "-properties";
	private static final String PROPERTIES_FILE_PATH = "";
	private static final String DB_FLAG = "-db";
	private static final String DB_FILE_PATH = "";
	private static final String INVALID_FLAG = "INVALID";
	
	
	@Mock private PropertiesFileLoader propertiesLoader;
	@Mock private JobService service;
	
	@InjectMocks ConsoleRunner runner;
	
	@BeforeAll
	synchronized static void startup() {

	}

	@BeforeEach
	void init() throws Exception {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	void run_whenArgumentsProvidedIsNotTwo_shouldThrowIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class, () -> runner.run(DB_FLAG));
		assertThrows(IllegalArgumentException.class, () -> runner.run(DB_FLAG, DB_FLAG, DB_FLAG));
	}
	
	@Test
	void run_whenUnKnowFlagIsUsed_shouldThrowIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class, () -> runner.run(INVALID_FLAG, PROPERTIES_FILE_PATH));
	}
	
	@Test
	void run_whenDBFlagIsUsed_shouldThrowUnsupportedOperation() {
		assertThrows(UnsupportedOperationException.class, () -> runner.run(DB_FLAG, DB_FILE_PATH));
	}
	
	@Test
	void run_whenPropertiesFlagIsUsedAndFileIsPresent_shouldCallCheckerService() {
		when(propertiesLoader.autoload(PROPERTIES_FILE_PATH)).thenReturn(true);
		
		runner.run(PROPERTIES_FLAG, PROPERTIES_FILE_PATH);
		
		verify(service, Mockito.times(1)).startJobs();
	}
	
	@Test
	void run_whenPropertiesFlagIsUsedAndFileIsNotPresent_shouldNotCallCheckerService() {
		when(propertiesLoader.autoload(PROPERTIES_FILE_PATH)).thenReturn(false);
		
		runner.run(PROPERTIES_FLAG, PROPERTIES_FILE_PATH);
		
		verify(service, Mockito.never()).startJobs();
	}
	
}
