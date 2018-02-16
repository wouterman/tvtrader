package tvtrader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tvtrader.properties.PropertiesFileLoader;
import tvtrader.services.JobService;

@Component
public class ConsoleRunner {
	private static final String ARGUMENT_USAGE_MESSAGE = "Invalid console arguments! Usage console arguments: -[db|properties] [FILE_PATH]";
	
	@Autowired private PropertiesFileLoader propertiesFileLoader;
	@Autowired private JobService jobService;

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
			jobService.startJobs();
		}
	}

}
