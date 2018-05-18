package tvtrader.jobs;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tvtrader.orders.OrderPlacer;

@Log4j2
@Component
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
