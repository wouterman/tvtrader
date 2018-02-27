package tvtrader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import lombok.extern.log4j.Log4j2;
import tvtrader.application.ConsoleRunner;
import tvtrader.application.GuiRunner;

@Log4j2
@SpringBootApplication
public class Main extends SpringBootServletInitializer {

	@Autowired private ConsoleRunner consoleRunner;
	@Autowired private GuiRunner guiRunner;

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(Main.class);
		context.registerShutdownHook();
		Main main = context.getBean(Main.class);
		try {
			main.escapeStatic(args);
		} catch (IllegalArgumentException e) {
			log.error("Couldn't start up. Received the following message: {}", e.getMessage());
		}
	}

	protected void escapeStatic(String[] args) {
		if (args.length > 0) {
			log.info("Starting console application.");
			consoleRunner.run(args);
		} else {
			log.info("Starting webgui.");
			guiRunner.run();
			
			
		}
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Main.class);
	}
}
