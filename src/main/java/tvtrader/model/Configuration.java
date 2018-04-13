package tvtrader.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Email;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

@ToString
@Entity
@Component
public class Configuration {
	private static final String INVALID_INTERVAL_MESSAGE = "Interval has to be > 0!";
	
	@Id
	@Getter
	@Setter
	private long id = 1;

	@Getter @Email
	private String expectedSender;
	@Getter @Min(1)
	private int mailPollingInterval;
	@Getter @Min(1)
	private int stoplossInterval;
	@Getter @Min(1)
	private int openOrdersInterval;
	@Getter  @Min(1)
	private int openOrdersExpirationTime;
	@Getter
	private boolean retryOrderFlag;
	@Getter @Min(1)
	private int tickerRefreshRate;
	@Getter  @Min(1)
	private int assetRefreshRate;

	@Transient
	private List<Listener> listeners = new ArrayList<>();
	
	public Configuration() {
		this.mailPollingInterval = 1;
		this.stoplossInterval = 1;
		this.openOrdersInterval = 1;
		this.openOrdersExpirationTime = 1;
		this.tickerRefreshRate = 1;
		this.assetRefreshRate = 1;
		
		this.expectedSender = "DEFAULT@EXAMPLE.COM";
		this.retryOrderFlag = false;
	}

	/**
	 * Sets the expected sender for the mailclient.
	 */
	public void setExpectedSender(String expectedSender) {
		if (this.expectedSender == null || !this.expectedSender.equals(expectedSender)) {
			this.expectedSender = expectedSender;
			notifyListeners(ListenerField.EXPECTEDSENDER);
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
			notifyListeners(ListenerField.MAILPOLLINGINTERVAL);
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
			notifyListeners(ListenerField.STOPLOSSINTERVAL);
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
			notifyListeners(ListenerField.OPENORDERSINTERVAL);
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
			notifyListeners(ListenerField.OPENORDERSEXPIRATIONTIME);
		}
	}

	/**
	 * Boolean flag. Indicating if we should replace unfilled cancelled orders.<br>
	 * 
	 */
	public void setUnfilledOrdersReplaceFlag(boolean retryOrderFlag) {
		if (this.retryOrderFlag != retryOrderFlag) {
			this.retryOrderFlag = retryOrderFlag;
			notifyListeners(ListenerField.UNFILLEDORDERSREPLACEFLAG);
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
			notifyListeners(ListenerField.TICKERREFRESHRATE);
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
			notifyListeners(ListenerField.ASSETREFRESHRATE);
		}
	}

	private void notifyListeners(ListenerField field) {
		for (Listener listener : listeners) {
			listener.update(field, this);
		}
	}

	public void subscribe(Listener newListener) {
		listeners.add(newListener);
	}

}
