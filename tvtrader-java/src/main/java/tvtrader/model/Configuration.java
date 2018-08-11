package tvtrader.model;

import lombok.Data;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

/**
 * Backend model representing the bot configuration.<br>
 *
 * @author Wouter
 */

@Data
@Entity
@Component
public class Configuration {
	private static final String INVALID_INTERVAL_MESSAGE = "Interval has to be > 0!";
	
	@Id
	@NotEmpty
	private String name;

	private String expectedSender;
	private int mailPollingInterval;
	private int stoplossInterval;
	private int openOrdersInterval;
	private int openOrdersExpirationTime;
	private boolean retryOrderFlag;
	private int tickerRefreshRate;
	private int assetRefreshRate;

	/**
	 * Constructor defaults to a 'valid' configuration.<br>
	 * Intervals are set to '1'.
	 * Name and expectedsender are not null.
	 *
	 */
	public Configuration() {
		name = "DEFAULT";
		expectedSender = "SENDER@DEFAULT.com";
		mailPollingInterval = 1;
		stoplossInterval = 1;
		openOrdersInterval = 1;
		openOrdersExpirationTime = 1;
		tickerRefreshRate = 1;
		assetRefreshRate = 1;
	}

	/**
	 * Sets the expected sender for the mailclient.
	 */
	public void setExpectedSender(@NonNull String expectedSender) {
		if (!this.expectedSender.equals(expectedSender)) {
			this.expectedSender = expectedSender;
		}
	}

	/**
	 * Sets the interval for polling the mail server.<br>
	 * 
	 * @throws IllegalArgumentException
	 *             If the provided interval is <= 0.
	 */
	public void setMailPollingInterval(int mailPollingInterval) {
		if (mailPollingInterval <= 0) {
			throw new IllegalArgumentException(INVALID_INTERVAL_MESSAGE);
		}

		if (this.mailPollingInterval != mailPollingInterval) {
			this.mailPollingInterval = mailPollingInterval;
		}
	}

	/**
	 * Sets the interval for checking stoploss conditions.<br>
	 * 
	 * @throws IllegalArgumentException
	 *             If the provided interval is <= 0.
	 */
	public void setStoplossInterval(int stoplossInterval) {
		if (stoplossInterval <= 0) {
			throw new IllegalArgumentException(INVALID_INTERVAL_MESSAGE);
		}

		if (this.stoplossInterval != stoplossInterval) {
			this.stoplossInterval = stoplossInterval;
		}
	}

	/**
	 * Sets the interval for checking open orders..<br>
	 * 
	 * @throws IllegalArgumentException
	 *             If the provided interval is <= 0.
	 */
	public void setOpenOrdersInterval(int openOrdersInterval) {
		if (openOrdersInterval <= 0) {
			throw new IllegalArgumentException(INVALID_INTERVAL_MESSAGE);
		}
		if (this.openOrdersInterval != openOrdersInterval) {
			this.openOrdersInterval = openOrdersInterval;
		}
	}

	/**
	 * Sets the interval for checking open orders.<br>
	 *
	 * @throws IllegalArgumentException
	 *             If the provided interval is <= 0.
	 */
	public void setOpenOrdersExpirationTime(int openOrdersExpirationTime) {
		if (openOrdersExpirationTime <= 0) {
			throw new IllegalArgumentException("Expiration time has to be > 0!");
		}

		if (this.openOrdersExpirationTime != openOrdersExpirationTime) {
			this.openOrdersExpirationTime = openOrdersExpirationTime;
		}
	}

	/**
	 * Boolean flag. Indicating if we should replace unfilled cancelled orders.<br>
	 * 
	 */
	public void setRetryOrderFlag(boolean retryOrderFlag) {
		if (this.retryOrderFlag != retryOrderFlag) {
			this.retryOrderFlag = retryOrderFlag;
		}
	}

	/**
	 * Sets the interval for refreshing the ticker cache.<br>
	 *
	 * @throws IllegalArgumentException
	 *             If the provided interval is <= 0.
	 */
	public void setTickerRefreshRate(int tickerRefreshRate) {
		if (tickerRefreshRate <= 0) {
			throw new IllegalArgumentException(INVALID_INTERVAL_MESSAGE);
		}

		if (this.tickerRefreshRate != tickerRefreshRate) {
			this.tickerRefreshRate = tickerRefreshRate;
		}
	}

	/**
	 * Sets the interval for refreshing the asset cache.<br>
	 *
	 * @throws IllegalArgumentException
	 *             If the provided interval is <= 0.
	 */
	public void setAssetRefreshRate(int assetRefreshRate) {
		if (assetRefreshRate <= 0) {
			throw new IllegalArgumentException(INVALID_INTERVAL_MESSAGE);
		}

		if (this.assetRefreshRate != assetRefreshRate) {
			this.assetRefreshRate = assetRefreshRate;
		}
	}

}
