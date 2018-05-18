package tvtrader.jobs;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tvtrader.exchange.ExchangeException;
import tvtrader.mail.MailClient;
import tvtrader.orders.*;
import tvtrader.services.AccountService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@Component
public class MailFetchJob implements Runnable {

	@Autowired
	private MailClient client;
	@Autowired
	private AccountService accountService;
	@Autowired
	private OrderLineParser orderLineParser;
	@Autowired
	private GainChecker gainChecker;
	@Autowired
	private OrderPlacer orderPlacer;
	@Autowired
	private OrderBuilder orderBuilder;

	@Override
	public void run() {
		try {
			checkForOrders();
		} catch (Exception e) {
			log.debug("Exception: ", e);
			log.error("Something went wrong:\n{}", e.getMessage());
		}
	}

	/**
	 * Checks for orders.<br>
	 * Fetches mails from the server and parses the subject lines for orders.<br>
	 * Creates the orders and checks if they are valid and if the gain (if set) is
	 * met.<br>
	 * Finally adds the order to the order queue of the orderplacer.<br>
	 */
	private void checkForOrders() {
		List<String> subjectLines = client.fetchSubjectLines();

		if (!subjectLines.isEmpty()) {
			log.debug("Subjectlines: {}", subjectLines);
			List<MarketOrder> orders = createOrders(subjectLines);

			for (MarketOrder order : orders) {
				boolean gainMet = gainChecker.checkGain(order);

				if (gainMet) {
					orderPlacer.addOrder(order);
				}

			}

		}
	}

	/**
	 * Initializes orders from valid subject lines.<br>
	 * Retrieves the current rate from the exchange.<br>
	 * Calculates the quantity based on the account's buy limit.<br>
	 * <br>
	 * If any action fails the order will not be added.<br>
	 * 
	 * @param subject
	 *            The subjectline to parse.
	 * @return List<MarketOrder> List of orders ready to be placed.
	 * @throws ExchangeException
	 *             If something goes wrong while creating the order.
	 */
	private List<MarketOrder> createOrders(List<String> subjectLines) {
		List<MarketOrder> orders = new ArrayList<>();

		for (String subject : subjectLines) {
			Optional<MarketOrder> optionalOrder = createOrder(subject);

			if (optionalOrder.isPresent()) {
				MarketOrder order = optionalOrder.get();
				fetchRateAndQuantity(order);

				if (validOrder(order)) {
					orders.add(order);
				} else {
					logRemovalReason(order);
				}
			}
		}

		return orders;
	}

	/**
	 * Creates an Optional based on the subject line.<br>
	 * If the subject line is invalid the optional is empty.<br>
	 * 
	 * @param subject
	 *            Subject line to parse.
	 * @return Optional. Might be empty if the orderline is invalid.
	 */
	private Optional<MarketOrder> createOrder(String subject) {
		return orderLineParser.parseOrderLine(subject);
	}

	/**
	 * Determines the appropriate rate (bid price for sell orders, ask for buy.) and
	 * calculates the needed quantity based on the account's buy limit.<br>
	 * This method overwrites any previous rate and quantity values set on the
	 * order.<br>
	 * 
	 * @param order
	 *            The order for which the quantity and rate should be set.
	 */
	private void fetchRateAndQuantity(MarketOrder order) {
		try {
			String mainCoin = accountService.getMainCurrency(order.getExchange(), order.getAccount());
			order.setMainCoin(mainCoin);

			double buylimit = accountService.getBuyLimit(order.getExchange(), order.getAccount());
			orderBuilder.calculateQuantityAndRate(order, buylimit);
		} catch (ExchangeException e) {
			log.debug("Exception: ", e);
			log.error("Something went wrong while creating order. Received the following message: {}", e.getMessage());
		}
	}

	/**
	 * Determines if an order is 'valid' by checking if the rate and quantity are
	 * set.<br>
	 * 
	 * @param order
	 *            The order to check.
	 * @return True if rate and quantity are > 0.
	 */
	private boolean validOrder(MarketOrder order) {
		return order.getRate() > 0 && order.getQuantity() > 0;
	}

	/**
	 * Determines the appropriate log message for removing the order.<br>
	 * 
	 * @param order
	 *            The order that get's removed.
	 */
	private void logRemovalReason(MarketOrder order) {
		if (order.getOrderType() == OrderType.LIMIT_SELL) {
			log.info("Removing order: No altcoin balance.");
		} else if (order.getOrderType() == OrderType.LIMIT_BUY) {
			log.info("Removing order: Already bought.");
		}
	}
}
