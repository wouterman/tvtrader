package tvtrader.jobs;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.log4j.Log4j2;
import tvtrader.orders.OrderPlacer;

@Log4j2
public class OrderPlacerJob implements Runnable {
	
	@Autowired
	private OrderPlacer orderPlacer;

	@Override
	public void run() {
		try {
			orderPlacer.placeOrders();
		} catch (Exception e) {
			log.debug("Received the following exception: ", e);
			log.error("Something went wrong:\n{}", e.getMessage());
		}
	}

}
