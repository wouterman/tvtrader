package tvtrader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import tvtrader.application.ConsoleRunner;

class MainTest {
	@Mock private ConsoleRunner consoleRunner;

	@InjectMocks private Main main;

	@BeforeEach
	void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void mainWhenConsoleArgsPresentShouldStartConsoleRunner() {
		String[] args = { "Argument" };
		main.escapeStatic(args);

		Mockito.verify(consoleRunner, Mockito.times(1)).run(args);
	}

}
