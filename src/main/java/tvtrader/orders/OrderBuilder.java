package tvtrader.orders;

import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;
import tvtrader.exchange.ExchangeException;
import tvtrader.services.ExchangeService;

/**
 * Responsible for building orders from the list of subjectlines.<br>
 * Initializes orders from valid subject lines.<br>
 * Retrieves the current rate from the exchange.<br>
 * Calculates the quantity based on the account's buy limit.<br>
 * <br>
 * If any action fails the order will not be processed.<br>
 * 
 * @author Wouter
 *
 */
@Log4j2
@Component
public class OrderBuilder {
	private static final double EIGHT_DECIMALS = 1_0000_0000d;

	private ExchangeService exchangeService;

	public OrderBuilder(ExchangeService exchangeService) {
		this.exchangeService = exchangeService;
	}

	/**
	 * Retrieves the current rate for the market.<br>
	 * Calculates the quantity based on the provided buy limit.<br>
	 * If any action fails these field will not be set. (e.g. 0.0)<br>
	 * 
	 * @param order
	 * @param buylimit
	 */
	public void calculateQuantityAndRate(MarketOrder order, double buylimit) throws ExchangeException {
		log.debug("Fetching price and quantity for: {}/{}", order.getMainCoin(), order.getAltCoin());
		setRate(order);
		
		if (rateSet(order)) {
			calculateQuantity(order, buylimit);
		}
	}

	private void setRate(MarketOrder order) throws ExchangeException {
		double rate = 0;
		OrderType orderType = order.getOrderType();
		if (orderType == OrderType.LIMIT_BUY) {
			rate = exchangeService.getAsk(order.getExchange(), order.getMainCoin(), order.getAltCoin());
		} else if (orderType == OrderType.LIMIT_SELL) {
			rate = exchangeService.getBid(order.getExchange(), order.getMainCoin(), order.getAltCoin());
		}

		order.setRate(rate);
	}

	private boolean rateSet(MarketOrder order) {
		return order.getRate() > 0;
	}
	
	/**
	 * Calculates the quantity based on the fetched rate, the account's buy limit
	 * and the exchange fee.<br>
	 * @param buylimit 
	 * 
	 * @throws ExchangeException
	 *             If anything goes wrong i.e. the account is unknown.
	 */
	private void calculateQuantity(MarketOrder order, double buylimit) throws ExchangeException {
		OrderType orderType = order.getOrderType();

		double quantity = 0;
		double altCoinBalance = exchangeService.getBalance(order.getExchange(), order.getAccount(), order.getAltCoin());
		double valueInMainCurrency = order.getRate() * altCoinBalance;
		
		if (orderType == OrderType.LIMIT_BUY && valueInMainCurrency < exchangeService.getMinimumOrderAmount(order.getExchange())) {
			quantity = setQuantityForBuyOrder(order, buylimit);
		} else if (orderType == OrderType.LIMIT_SELL && altCoinBalance > 0) {
			quantity = altCoinBalance;
		}

		log.debug("Setting quantity: {}", quantity);
		order.setQuantity(quantity);
	}
	
	/**
	 * Calculates the quantity based on the buy limit and the exchange's
	 * fee. The total price including fees will be equal (or almost equal) to the
	 * buy limit.
	 * 
	 */
	private double setQuantityForBuyOrder(MarketOrder order, double buylimit) throws ExchangeException {
		double rate = order.getRate();
		double exchangeFee = exchangeService.getTakerFee(order.getExchange());
		double quantity = buylimit / (1 + exchangeFee) / rate;

		log.debug("Buy limit is {}. Rate is {}. Exchangefee is {}.", buylimit, rate, exchangeFee);
		return (double) Math.round(quantity * EIGHT_DECIMALS) / EIGHT_DECIMALS;
	}

}
