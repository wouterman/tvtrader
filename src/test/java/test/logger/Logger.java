package test.logger;

import java.net.URISyntaxException;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.xml.XmlConfigurationFactory;

public class Logger {
	public static void turnOffLogging() {
		Logger logger = new Logger();
		try {
			logger.turnOff();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	private void turnOff() throws URISyntaxException {
		System.clearProperty(XmlConfigurationFactory.CONFIGURATION_FILE_PROPERTY);
		LoggerContext context = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
		context.setConfigLocation(Paths.get("src/test/resources/testLog.xml").toUri());
	}
}
