package tvtrader.jobs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;
import tvtrader.orders.OpenOrdersWatcher;

@Log4j2
@Component
public class OpenOrdersJob implements Runnable {

	@Autowired
	private OpenOrdersWatcher orderWatcher;
	
	@Override
	public void run() {
		try {
			checkForOpenorders();
		} catch (Exception e) {
			log.debug("Received the following exception: ", e);
			log.error("Something went wrong:\n{}", e.getMessage());
		}
	}
	
	private void checkForOpenorders() {
		orderWatcher.checkOrders();
	}

}
