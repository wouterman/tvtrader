package tvtrader;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import test.logger.Logger;
import tvtrader.application.ConsoleRunner;

public class MainTest {
	@Mock private ConsoleRunner consoleRunner;
	@InjectMocks private Main main;

	@BeforeAll
	synchronized static void startup() {
		Logger.turnOffLogging();
	}

	@BeforeEach
	void init() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void main_whenConsoleArgsPresent_shouldStartConsoleRunner() throws Exception {
		String[] args = { "Argument" };
		main.escapeStatic(args);

		Mockito.verify(consoleRunner, Mockito.times(1)).run(args);
	}

}
