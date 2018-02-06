package tvtrader;

import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;
import tvtrader.services.CheckerService;
import tvtrader.utils.PropertiesFileLoader;

@Log4j2
@Component
public class ConsoleRunner {
	private static final String ARGUMENT_USAGE_MESSAGE = "Invalid console arguments! Usage console arguments: -[db|properties] [FILE_PATH]";
	private PropertiesFileLoader propertiesFileLoader;
	private CheckerService checkerService;

	public ConsoleRunner(PropertiesFileLoader propertiesFileLoader, CheckerService checkerService) {
		super();
		this.propertiesFileLoader = propertiesFileLoader;
		this.checkerService = checkerService;
	}

	public void run(String... args) {
		if (args.length != 2) {
			throw new IllegalArgumentException(ARGUMENT_USAGE_MESSAGE);
		}
		
		String configurationFlag = args[0];
		String filePath = args[1];
		
		boolean successful;
		if (configurationFlag.equalsIgnoreCase("-db") ) {
			throw new UnsupportedOperationException();
		} else if (configurationFlag.equalsIgnoreCase("-properties")) {
			successful = propertiesFileLoader.autoload(filePath);
		} else {
			throw new IllegalArgumentException(ARGUMENT_USAGE_MESSAGE);
		}

		if (successful) {
			checkerService.run();
		}
	}

}
