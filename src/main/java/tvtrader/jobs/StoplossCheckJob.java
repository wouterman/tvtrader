package tvtrader.jobs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;
import tvtrader.services.StoplossService;

@Log4j2
@Component
public class StoplossCheckJob implements Runnable {
	
	@Autowired
	private StoplossService protector;
	
	@Override
	public void run() {
		try {
			checkForStoploss();
		} catch (Exception e) {
			log.debug("Received the following exception: ", e);
			log.error("Something went wrong:\n{}", e.getMessage());
		}
	}
	
	private void checkForStoploss() {
		protector.checkStoploss();
	}
	
	public void startStoplossProtection() {
		protector.startStoplossProtection();
	}

}
