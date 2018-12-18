package tvtrader.stoploss;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tvtrader.exchange.ExchangeException;
import tvtrader.model.MarketOrder;
import tvtrader.model.OrderType;
import tvtrader.model.StoplossOrder;
import tvtrader.orders.OrderPlacer;
import tvtrader.services.AccountService;
import tvtrader.services.ExchangeService;
import tvtrader.utils.PriceFormatter;

import java.text.DecimalFormat;

/**
 * Responsible for checking if the stoploss condition for a certain account and
 * market has been reached.<br>
 * When we start up this class will use the stoploss percentage to protect
 * against major losses.<br>
 * However, as soon as we are in profit (defined by a current value that is
 * higher than the bought price + exchange fees + trailing stoploss percentage)
 * we switch over to a trailing stoploss (tssl) mode.<br>
 * <br>
 * The tssl percentage is usually tighter than the initial stoploss percentage
 * to better protect profits.<br>
 * The bought price is updated with every check to make sure that additional
 * buys are not ignored.<br>
 * 
 * @author Wouter
 *
 */
@Log4j2
@Component
public class StoplossWatcher {
	private final DecimalFormat formatter = PriceFormatter.getFormatter();
	@Autowired
	private ExchangeService exchangeService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private OrderPlacer orderPlacer;

	/**
	 * Checks if the stoploss has been triggered.
	 * 
	 * @return True if stoploss is triggered.
	 * @throws UnverifiedStoplossWatcherException
	 *             Thrown to indicate that the bought price repeatedly couldn't be
	 *             verified at the exchange. (And that we therefore don't have a
	 *             solid point of reference.)
	 */
	public boolean checkStoploss(StoplossOrder order) throws UnverifiedStoplossWatcherException {
		boolean triggered = false;

		if (order.isVerified()) {
			triggered = checkOrder(order);
		} else {
			boolean verified = verify(order);
			if (verified) {
				order.setVerified(verified);
			}
		}

		return triggered;
	}

	/**
	 * Checks if the stoploss condition for the order is met. If this condition is
	 * met the method will place an order at the exchange for the current bid
	 * price.<br>
	 * 
	 * @return true if the stoploss condition is triggered or if there is no altcoin
	 *         balance.
	 */
	private boolean checkOrder(StoplossOrder order) {
		boolean isSold = false;

		try {
			String exchange = order.getExchange();
			String account = order.getAccount();
			String altcoin = order.getAltcoin();
			double balance = exchangeService.getBalance(exchange, account, altcoin);
			double boughtPrice = exchangeService.getBoughtPrice(exchange, account, altcoin, balance);
			double referencePrice = order.getReferencePrice();

			log.debug("{}-{}: Balance: {}. Boughtprice: {}. ReferencePrice: {}.", account, altcoin, balance,
					boughtPrice, referencePrice);

			if (balance > 0) {
				String maincoin = accountService.getMainCurrency(exchange, account);
				double bidPrice = exchangeService.getBid(exchange, maincoin, altcoin);
				log.debug("{}-{}: Bidprice: {}", account, altcoin, bidPrice);

				if (bidPrice > 0) {
					double takerFee = exchangeService.getTakerFee(exchange);
					double currentValue = calculateCurrentValue(balance, bidPrice, takerFee);

					updateReferencePrice(order, currentValue);

					double stoplossPrice;
					if (notTrailing(order, currentValue)) {
						double stoplossPercentage = accountService.getStoploss(exchange, account);
						stoplossPrice = boughtPrice * ((100 - stoplossPercentage) / 100);
					} else {
						double trailingStoplossPercentage = accountService.getTrailingStoploss(exchange, account);
						double tsslPercentage = (100 - trailingStoplossPercentage) / 100;
						stoplossPrice = referencePrice * tsslPercentage;
					}

					log.debug("{}: Current value: {}-{}. StoplossPrice: {}.", account, altcoin, currentValue,
							stoplossPrice);
					if (stoplossPrice > currentValue) {
						log.info("STOPLOSS TRIGGERED: {}-{}. Selling for {}.", account, altcoin, currentValue);
						placeStoplossOrder(order, balance, bidPrice);
						isSold = true;
					}
				}

			} else {
				log.info("Order seems to be sold. Stopping stoploss protection for: {}-{}", account, altcoin);
				isSold = true;
			}

		} catch (ExchangeException | NullPointerException e) {
			// Don't really want to do anything here, just continue
			log.debug("Received an exception for: {}", toString());
			log.debug("Exception: ", e);
		}

		return isSold;
	}

	/**
	 * We shouldn't switch to the trailing stoploss unless our current value is
	 * higher than the bought price + stoploss.<br>
	 * (To prevent we sell for loss because the trailing stoploss is triggered
	 * before we are in profit)
	 * 
	 */
	private boolean notTrailing(StoplossOrder order, double currentValue) {
		return currentValue < order.getTsslPrice();
	}

	/**
	 * If the current price is higher than the reference price then that should be
	 * our new point of reference.
	 */
	private void updateReferencePrice(StoplossOrder order, double price) {

		if (price > order.getReferencePrice()) {
			log.debug("{}-{}: New referencePrice: {}", order.getAccount(), order.getAltcoin(), price);
			order.setReferencePrice(price);
		}
	}

	/**
	 * Calculates the current value of the speculation.<br>
	 * Takes into account the current bid price and the exchange fee.<br>
	 * 
	 */
	private double calculateCurrentValue(double balance, double bidPrice, double takerFee) {
		double beforeFee = bidPrice * balance;
		return beforeFee - (beforeFee * takerFee);
	}

	/**
	 * Creates and places the sell order at the orderplacer.
	 * 
	 * @Throws ExchangeException If anything goes wrong while creating the order.
	 */
	private void placeStoplossOrder(StoplossOrder order, double balance, double bidPrice) throws ExchangeException {
		MarketOrder sellOrder = createSellOrder(order, balance, bidPrice);
		orderPlacer.addOrder(sellOrder);
	}

	/**
	 * Creates the sell order based on the current market price.
	 * 
	 * @throws ExchangeException
	 */
	private MarketOrder createSellOrder(StoplossOrder order, double balance, double bidPrice) throws ExchangeException {
		MarketOrder sellOrder = new MarketOrder();
		sellOrder.setExchange(order.getExchange());
		sellOrder.setAccount(order.getAccount());
		String mainCoin = accountService.getMainCurrency(order.getExchange(), order.getAccount());
		sellOrder.setMainCoin(mainCoin);
		sellOrder.setAltCoin(order.getAltcoin());
		sellOrder.setOrderType(OrderType.LIMIT_SELL);
		sellOrder.setQuantity(balance);
		sellOrder.setRate(bidPrice);
		return sellOrder;
	}

	/**
	 * Tries to verify the bought price from the exchange.<br>
	 * Some exchanges don't save the order history immediately after completing an
	 * order. So if this fails, we will retry 10 more times.<br>
	 * 
	 * @return
	 * @throws UnverifiedStoplossWatcherException
	 *             If after ten times we couldn't get a bought price or if something
	 *             goes wrong while contacting the exchange.
	 */
	private boolean verify(StoplossOrder order) throws UnverifiedStoplossWatcherException {
		if (order.getVerifyCounter() >= 10) {
			log.info("{} -> {}-{}: Tried fetching bought price ten times, stopping stoploss watcher.",
					getClass().getSimpleName(), order.getAccount(), order.getAltcoin());
			throw new UnverifiedStoplossWatcherException("Couldn't verify order: " + order);
		}

		return verifyBoughtPrice(order);
	}

	/**
	 * Fetches the bought price from the exchange.<br>
	 * 
	 * @return True if the bought price could be fetched and isn't 0.
	 * @throws UnverifiedStoplossWatcherException
	 */
	private boolean verifyBoughtPrice(StoplossOrder order) throws UnverifiedStoplossWatcherException {
		try {
			log.info("{} -> {}-{}: Checking order history for bought price.", getClass().getSimpleName(),
					order.getAccount(), order.getAltcoin());
			double boughtPrice = getTotalBoughtPrice(order);
			double trailingStoplossPercentage = accountService.getTrailingStoploss(order.getExchange(),
					order.getAccount());
			double referencePrice = 0;

			if (boughtPrice > 0) {
				log.info("{} -> {}: Got bought price: {}.", getClass().getSimpleName(), order,
						formatter.format(boughtPrice));

				double takerFee = exchangeService.getTakerFee(order.getExchange());
				double tsslPrice = referencePrice = boughtPrice * (1 + takerFee) / (100 - trailingStoplossPercentage)
						* 100;
				order.setTsslPrice(tsslPrice);
				log.debug("Setting reference price to {}", referencePrice);
				return true;
			} else {
				log.info("{} -> {}-{}: Couldn't retrieve bought price. Trying again.", getClass().getSimpleName(),
						order.getAccount(), order.getAltcoin());
				order.incrementVerifyCounter();

				return false;
			}
		} catch (ExchangeException e) {
			return false;
		}
	}

	/**
	 * Wrapper method.<br>
	 * Fetches the total bought price for the market and account.<br>
	 * 
	 * @param boughtPrice
	 * @return
	 */
	private double getTotalBoughtPrice(StoplossOrder order) {
		try {
			double balance = exchangeService.getBalance(order.getExchange(), order.getAccount(), order.getAltcoin());
			return exchangeService.getBoughtPrice(order.getExchange(), order.getAccount(), order.getAltcoin(), balance);
		} catch (ExchangeException e) {
			log.debug("Received the following exception: ", e);
			log.info("Received an error while fetching bought price: {}", e.getMessage());

			return 0;
		}
	}

}
