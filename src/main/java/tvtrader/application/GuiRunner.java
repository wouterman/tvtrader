package tvtrader.application;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tvtrader.persistence.DatabaseFileLoader;

@Log4j2
@Component
public class GuiRunner {
	@Autowired private DatabaseFileLoader dbLoader;
	
	public void run() {
		log.info("Loading database file.");
		dbLoader.loadConfiguration();
	}

}
