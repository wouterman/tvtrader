package tvtrader;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class Main {
	private GuiRunner guiRunner;
	private ConsoleRunner consoleRunner;
	
	public Main(GuiRunner guiRunner, ConsoleRunner consoleRunner) {
		this.guiRunner = guiRunner;
		this.consoleRunner = consoleRunner;
	}
	
	public static void main(String[] args) {
		@SuppressWarnings("resource")
		AbstractApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		context.registerShutdownHook();
		Main main = context.getBean(Main.class);
		try {
			main.escapeStatic(args);
		} catch (IllegalArgumentException e) {
			log.error("Couldn't start up. Received the following message: {}", e.getMessage());
		}
	}

	private void escapeStatic(String[] args) {
		if (args.length > 0) {
			log.info("Starting console application.");
			consoleRunner.run(args);
		} else {
			log.info("Starting webgui.");
			guiRunner.run();
		}
	}
	
}
