package tvtrader.bittrex;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;
import tvtrader.bittrex.response.*;
import tvtrader.exchange.ExchangeException;
import tvtrader.exchange.apidata.JsonParser;
import tvtrader.exchange.apidata.Order;
import tvtrader.exchange.apidata.Ticker;

import java.util.*;

@Component(value="BittrexParser")
public class BittrexParser implements JsonParser {
	private static final String RESPONSE_NOT_SUCCESSFUL_MESSAGE = "Response not successful. Received the following message: ";
	
	private Gson gson;

	public BittrexParser(Gson gson) {
		super();
		this.gson = gson;
	}
	
	@Override
	public Map<String, Ticker> parseMarketSummaries(String json) throws ExchangeException {
		BittrexTickerResponse response = gson.fromJson(json, BittrexTickerResponse.class);

		if (response.isSuccess()) {
			List<BittrexTicker> tickerList = response.getResult();

			Map<String, Ticker> tickers = new HashMap<>();

			tickerList.forEach(ticker -> tickers.put(ticker.getMarket(), ticker));

			return tickers;
		} else {
			throw new ExchangeException(
					RESPONSE_NOT_SUCCESSFUL_MESSAGE + response.getMessage());
		}

	}

	@Override
	public Map<String, Double> parseBalances(String json) throws ExchangeException {
		BittrexBalanceResponse response = gson.fromJson(json, BittrexBalanceResponse.class);

		if (response.isSuccess()) {
			List<BittrexBalance> balanceList = response.getResult();

			Map<String, Double> balances = new HashMap<>();

			balanceList.forEach(balance -> balances.put(balance.getCurrency(), balance.getAvailableBalance()));

			return balances;
		} else {
			throw new ExchangeException(
					RESPONSE_NOT_SUCCESSFUL_MESSAGE + response.getMessage());
		}
	}

	@Override
	public List<Order> parseOrderHistory(String json) throws ExchangeException {
		BittrexOrderHistoryResponse response = gson.fromJson(json, BittrexOrderHistoryResponse.class);

		if (response.isSuccess()) {
			List<BittrexFilledOrder> orders = response.getResult();

			List<Order> converted = new ArrayList<>();

			orders.forEach(converted::add);

			Collections.sort(converted);

			return converted;
		} else {
			throw new ExchangeException(
					RESPONSE_NOT_SUCCESSFUL_MESSAGE + response.getMessage());
		}

	}

	@Override
	public List<Order> parseOpenOrders(String json) throws ExchangeException {
		BittrexOpenOrderResponse response = gson.fromJson(json, BittrexOpenOrderResponse.class);

		if (response.isSuccess()) {
			List<BittrexUnfilledOrder> orders = response.getResult();
			List<Order> converted = new ArrayList<>();
			orders.forEach(converted::add);
			Collections.sort(converted);

			return converted;
		} else {
			throw new ExchangeException(
					RESPONSE_NOT_SUCCESSFUL_MESSAGE + response.getMessage());
		}
	}

	@Override
	public boolean checkResponse(String json) {
		BittrexBaseResponse<Object> response = gson.fromJson(json, BittrexOrderResponse.class);
		
		return response.isSuccess();
	}
}
