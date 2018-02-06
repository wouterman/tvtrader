package tvtrader.checkers;

import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;
import tvtrader.services.StoplossService;

@Log4j2
@Component
public class StoplossChecker implements Runnable {
	private StoplossService protector;
	
	public StoplossChecker(StoplossService protector) {
		this.protector = protector;
	}
	
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
