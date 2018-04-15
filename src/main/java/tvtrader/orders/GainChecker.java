package tvtrader.orders;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tvtrader.exchange.ExchangeException;
import tvtrader.services.AccountService;
import tvtrader.services.ExchangeService;

@Log4j2
@Component
public class GainChecker {
	@Autowired
	private ExchangeService exchangeService;
	
	@Autowired
	private AccountService accountService;

	/**
	 * Checks if the minimum gain for the order is met. If no minimum gain is set
	 * for the account (i.e. 0) then the gain will always be met. <br>
	 * 
	 * @param order
	 *            The order to check
	 * @return True if the gain is met. If something goes wrong while contacting the
	 *         exchange this method will return false.
	 */
	public boolean checkGain(MarketOrder order) {
		try {
			if ( sellOrder(order) && !gainMet(order)) {
				log.info("Gain not met for {}/{}! Removing order.", order.getMainCoin(), order.getAltCoin());

				return false;
			}
		} catch (ExchangeException e) {
			log.info("Something went wrong while checking if the gain was met: {}.\nRemoving order.", e.getMessage());
			log.debug("Received the following error: ", e);

			return false;
		}

		return true;
	}

	private boolean sellOrder(MarketOrder order) {
		return order.getOrderType() == OrderType.LIMIT_SELL;
	}

	private boolean gainMet(MarketOrder order) throws ExchangeException {
		String exchange = order.getExchange();
		String account = order.getAccount();

		double minimumGain = accountService.getMinimumGain(exchange, account);

		if (minimumGain > 0) {
			double priceExFee = order.getQuantity() * order.getRate();
			double fee = priceExFee * exchangeService.getTakerFee(exchange);
			double sellPrice = priceExFee - fee;

			double orderPrice = exchangeService.getBoughtPrice(exchange, account, order.getAltCoin(),
					order.getQuantity());
			double minimumPrice = orderPrice * (1 + (minimumGain / 100));

			log.debug(
					"PriceExFee: {}\n" + "Fee: {}\n" + "Sellprice: {}\n" + "orderPrice: {}\n" + "minimumprice: {}\n"
							+ "Gainmet: {}",
					priceExFee, fee, sellPrice, orderPrice, minimumPrice, (sellPrice > minimumPrice));
			return sellPrice > minimumPrice;
		} else {
			return true;
		}
	}
}
