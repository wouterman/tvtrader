package tvtrader.services;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tvtrader.exchange.ExchangeException;
import tvtrader.exchange.apidata.Order;
import tvtrader.model.Account;
import tvtrader.model.ApiCredentials;
import tvtrader.model.MarketOrder;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

@Log4j2
@Component
public class ExchangeService {
	@Autowired private TickerService tickerService;
	@Autowired private OrderService orderService;
	@Autowired private BalanceService balanceService;
	@Autowired private AccountService accountService;
	@Autowired private TransactionHistoryService transactionHistoryService;

	/**
	 * Fetches and returns the latest askprice for the provided market.<br>
	 * 
	 * @param exchangeName
	 *            The exchange from which to fetch the price.
	 * @param mainCoin
	 *            Main currency of the market.
	 * @param altCoin
	 *            Alt currency of the market.
	 * @return The latest ask price.
	 * @throws ExchangeException
	 *             If anything goes wrong while contacting the exchange or if the
	 *             exchange is unknown.
	 */
	public double getAsk(String exchangeName, String mainCoin, String altCoin) throws ExchangeException {
		return tickerService.getAsk(exchangeName, mainCoin, altCoin);
	}

	/**
	 * Fetches and returns the latest bidprice for the provided market.<br>
	 * 
	 * @param exchangeName
	 *            The exchange from which to fetch the price.
	 * @param mainCoin
	 *            Main currency of the market.
	 * @param altCoin
	 *            Alt currency of the market.
	 * @return The latest bid price.
	 * @throws ExchangeException
	 *             If anything goes wrong while contacting the exchange or if the
	 *             exchange is unknown.
	 */
	public double getBid(String exchangeName, String mainCoin, String altCoin) throws ExchangeException {
		return tickerService.getBid(exchangeName, mainCoin, altCoin);
	}

	/**
	 * Fetches and return the latest balance for the provided currency.<br>
	 * 
	 * @param exchangeName
	 *            The exchange from which to fetch the balance.
	 * @param accountName
	 *            The account for which to fetch the balance.
	 * @param currency
	 *            The balance to fetch.
	 * @return
	 * @throws ExchangeException
	 *             * If anything goes wrong while contacting the exchange or if the
	 *             exchange or account is unknown.
	 */
	public double getBalance(String exchangeName, String accountName, String currency) throws ExchangeException {
		return balanceService.getBalance(exchangeName, accountName, currency);
	}

	/**
	 * Fetches and returns all the balances for the account.<br>
	 * 
	 * @param exchangeName
	 *            The exchange from which to fetch the balances.
	 * @param accountName
	 *            The account for which to fetch the balances.
	 * @return Iterator for the balances.
	 * @throws ExchangeException
	 *             * If anything goes wrong while contacting the exchange or if the
	 *             exchange or account is unknown.
	 */
	public Iterator<Entry<String, Double>> getBalances(String exchangeName, String accountName) throws ExchangeException {
		return balanceService.getBalances(exchangeName, accountName);
	}

	/**
	 * Fetches the minimum order amount for the exchange.<br>
	 * 
	 * @param exchange
	 *            The exchange to check.
	 * @return The minimum order amount for an order.
	 * @throws ExchangeException
	 *             If the exchange is unknown.
	 */
	public double getMinimumOrderAmount(String exchange) throws ExchangeException {
		return orderService.getMinimumOrderAmount(exchange);
	}

	/**
	 * Fetches the taker fee for the exchange.<br>
	 * 
	 * @param exchange
	 *            The exchange to check.
	 * @return The exchange's taker fee.
	 * @throws ExchangeException
	 *             If the exchange is unknown.
	 */
	public double getTakerFee(String exchange) throws ExchangeException {
		return orderService.getTakerFee(exchange);
	}

	/**
	 * Places the provided order at the exchange.<br>
	 * 
	 * @param order
	 *            The order to place.
	 * @return True if the order succeeded.
	 * 
	 */
	public boolean placeOrder(MarketOrder order) {
		Account account = accountService.getAccount(order.getExchange(), order.getAccount());
		ApiCredentials credentials = account.getCredentials();

		try {
			return orderService.placeOrder(order, credentials);
		} catch (ExchangeException e) {
			log.debug("Exception: ", e);
			log.error("Couldn't place order at exchange. Received the following message: {}", e.getMessage());
			return false;
		}
	}

	/**
	 * Calculates the bought price for the given balance and altcoin.<br>
	 * If the bought price can't be determined returns 0.<br>
	 * 
	 * @param exchange
	 *            The exchange to fetch the order history from.
	 * @param account
	 *            The account to fetch the order history for.
	 * @param altCoin
	 *            The altcoin connected to the balance.
	 * @param balance
	 *            The amount to calculate the bought price for.
	 * 
	 * @return double The bought price of the balance. 0 if the bought price can't
	 *         be reliably calculated.
	 * @throws ExchangeException
	 *             If something goes wrong. (I.e. Account/exchange is unknown.
	 *             Something goes wrong while fetching orderhistory,)<br>
	 */
	public double getBoughtPrice(String exchange, String account, String altCoin, double balance)
			throws ExchangeException {
		return transactionHistoryService.getBoughtPrice(exchange, account, altCoin, balance);
	}

	/**
	 * Fetches and returns all the open orders for the account.<br>
	 * 
	 * @param exchangeName
	 *            The exchange from which to fetch the orders.
	 * @param accountName
	 *            The account for which to fetch the orders.
	 * @return List of all the open orders
	 * @throws ExchangeException
	 *             * If anything goes wrong while contacting the exchange or if the
	 *             exchange or account is unknown.
	 */
	public List<Order> getOpenOrders(String exchangeName, String accountName) throws ExchangeException {
		Account account = accountService.getAccount(exchangeName, accountName);
		ApiCredentials credentials = account.getCredentials();

		return orderService.getOpenOrders(exchangeName, credentials);
	}

	/**
	 * Cancels the order.<br>
	 * 
	 * @param exchangeName
	 *            The exchange where the order is open.
	 * @param accountName
	 *            The account where the order is open.
	 * @param orderId
	 *            The order to cancel.
	 */
	public boolean cancelOrder(String exchangeName, String accountName, String orderId) throws ExchangeException {
		Account account = accountService.getAccount(exchangeName, accountName);
		ApiCredentials credentials = account.getCredentials();

		return orderService.cancelOrder(exchangeName, credentials, orderId);
	}
}
