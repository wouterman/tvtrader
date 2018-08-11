package tvtrader.bittrex;

import com.google.gson.annotations.SerializedName;
import tvtrader.exchange.apidata.Order;
import tvtrader.model.OrderType;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * 
 * Data class for Bittrex transactions.<br>
 * 
 * @author Wouter
 *
 */
public abstract class BittrexOrder implements Order {
	private static final int BEGIN_INDEX = 0;
	private static final int INDEX_OFFSET = 1;
	private static final char MARKET_DELIMITER = '-';

	@SerializedName("OrderUuid")
	private String orderUuid;
	@SerializedName("Exchange")
	private String market;
	@SerializedName("OrderType")
	private String orderType;
	@SerializedName("Quantity")
	private double quantity;
	@SerializedName("QuantityRemaining")
	private double quantityRemaining;
	@SerializedName("Price")
	private double price;
	@SerializedName("PricePerUnit")
	private double pricePerUnit;

	@Override
	public String getOrderId() {
		return orderUuid;
	}

	@Override
	public String getMainCoin() {
		return market.substring(BEGIN_INDEX, market.indexOf(MARKET_DELIMITER));
	}

	@Override
	public String getAltCoin() {
		return market.substring(market.indexOf(MARKET_DELIMITER) + INDEX_OFFSET);
	}

	@Override
	public OrderType getOrderType() {
		return getOrderType(orderType);
	}

	private OrderType getOrderType(String orderType) {
		if (orderType.equalsIgnoreCase(OrderType.LIMIT_BUY.getType())) {
			return OrderType.LIMIT_BUY;
		} else if (orderType.equalsIgnoreCase(OrderType.LIMIT_SELL.getType())) {
			return OrderType.LIMIT_SELL;
		} else {
			return OrderType.UNSUPPORTED;
		}
	}

	@Override
	public double getQuantity() {
		return quantity;
	}

	@Override
	public double getQuantityRemaining() {
		return quantityRemaining;
	}

	@Override
	public double getPrice() {
		return price;
	}

	@Override
	public double getRate() {
		return pricePerUnit;
	}

	/**
	 * Determines the timestamp in unix time.<br>
	 * <br>
	 * Bittrex uses UTC timestamps.<br>
	 * 
	 */
	protected long getTimeStamp(String timeStamp) {
		LocalDateTime ldt = LocalDateTime.parse(timeStamp);

		return ldt.toEpochSecond(ZoneOffset.UTC);
	}

}
