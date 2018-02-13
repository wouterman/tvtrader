package tvtrader.orders;

import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

/**
 * Helper class for the OrderBuilder. Determines if the subject line matches the
 * pattern for a valid order and builds an order from it.<br>
 * 
 * @author Wouter
 *
 */
@Log4j2
@Component
public class OrderLineParser {
	// Expected pattern:
	// ORDERTYPE_EXCHANGE_ACCOUNT_ALTCOIN'
	private static final String ORDERLINE_PATTERN = "\\w*_\\w*_\\w*_\\w*$";
	private Pattern pattern;

	private static final String LIMIT_BUY = "BUY";
	private static final String LIMIT_SELL = "SELL";
	private static final String SUBJECT_DELIMITER = "_";

	public OrderLineParser() {
		pattern = Pattern.compile(ORDERLINE_PATTERN);
	}

	/**
	 * Checks if the subject line matches the expected pattern for a valid
	 * order.<br>
	 * If it does, it will build an order based on that pattern.<br>
	 * Doesn't check if the ordertype is valid for the exchange.<br>
	 * If the type can't be determined this field will be set to UNSUPPORTED.
	 * 
	 * @param subject
	 *            The subjectline to parse.
	 * @return Optional<MarketOrder> based on the order line.
	 */
	public Optional<MarketOrder> parseOrderLine(String subject) {
		Matcher matcher = pattern.matcher(subject);

		if (matcher.find()) {
			String orderLine = subject.substring(matcher.start(), matcher.end());
			log.info("Received order: {}", orderLine);

			try (Scanner scanner = new Scanner(orderLine)) {
				return createOrder(scanner);
			}
		}
		
		return Optional.empty();
	}

	private Optional<MarketOrder> createOrder(Scanner scanner) {
		scanner.useDelimiter(SUBJECT_DELIMITER);

		OrderType orderType = determineOrderType(scanner.next().trim());
		String exchange = scanner.next();
		String account = scanner.next();
		String altCoin = scanner.next();

		MarketOrder order = new MarketOrder();
		order.setExchange(exchange);
		order.setAccount(account);
		order.setAltCoin(altCoin);
		order.setOrderType(orderType);

		log.debug("Created order.");
		return Optional.of(order);
	}

	private OrderType determineOrderType(String type) {
		OrderType ordertype = null;
		if (type.equalsIgnoreCase(LIMIT_BUY)) {
			ordertype = OrderType.LIMIT_BUY;
		} else if (type.equalsIgnoreCase(LIMIT_SELL)) {
			ordertype = OrderType.LIMIT_SELL;
		} else {
			ordertype = OrderType.UNSUPPORTED;
		}

		return ordertype;
	}
}
