package tvtrader.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Getter;
import tvtrader.accounts.Account;
import tvtrader.controllers.Listener;
import tvtrader.services.AccountService;

@Component
public class Configuration implements Serializable {
	private static final long serialVersionUID = 1L;
	private transient List<Listener> listeners = new ArrayList<>();

	@Getter private MailConfiguration mailConfig;
	@Getter private String expectedSender;
	@Getter private int mailPollingInterval;
	@Getter private int stoplossInterval;
	@Getter private int openOrdersInterval;
	@Getter private int openOrdersExpirationTime;
	@Getter private boolean retryOrderFlag;
	@Getter private int tickerRefreshRate;
	@Getter private int assetRefreshRate;
	@Getter private AccountService accountService;
	
	public Configuration(AccountService accountService) {
		this.accountService = accountService;
	}

	public void setMailConfig(MailConfiguration mailConfig) {
		if (this.mailConfig == null || !this.mailConfig.equals(mailConfig)) {
			this.mailConfig = mailConfig;
			notifyListeners(ConfigurationField.MAILCONFIG);
		}
	}

	public void setExpectedSender(String expectedSender) {
		if(this.expectedSender == null || !this.expectedSender.equals(expectedSender)) {
			this.expectedSender = expectedSender;
			notifyListeners(ConfigurationField.EXPECTEDSENDER);
		}
	}

	public void setMailPollingInterval(int mailPollingInterval) {
		if(this.mailPollingInterval != mailPollingInterval) {
			this.mailPollingInterval = mailPollingInterval;
			notifyListeners(ConfigurationField.MAILPOLLINGINTERVAL);
		}
	}

	public void setStoplossInterval(int stoplossInterval) {
		if(this.stoplossInterval != stoplossInterval) {
			this.stoplossInterval = stoplossInterval;
			notifyListeners(ConfigurationField.STOPLOSSINTERVAL);
		}
	}

	public void setOpenOrdersInterval(int openOrdersInterval) {
		if (this.openOrdersInterval != openOrdersInterval) {
			this.openOrdersInterval = openOrdersInterval;
			notifyListeners(ConfigurationField.OPENORDERSINTERVAL);
		}
	}

	public void setOpenOrdersExpirationTime(int openOrdersExpirationTime) {
		if (this.openOrdersExpirationTime != openOrdersExpirationTime) {
			this.openOrdersExpirationTime = openOrdersExpirationTime;
			notifyListeners(ConfigurationField.OPENORDERSEXPIRATIONTIME);
		}
	}

	public void setUnfilledOrdersReplaceFlag(boolean retryOrderFlag) {
		if(this.retryOrderFlag != retryOrderFlag) {
			this.retryOrderFlag = retryOrderFlag;
			notifyListeners(ConfigurationField.UNFILLEDORDERSREPLACEFLAG);
		}
	}

	public void setTickerRefreshRate(int tickerRefreshRate) {
		if (this.tickerRefreshRate != tickerRefreshRate) {
			this.tickerRefreshRate = tickerRefreshRate;
			notifyListeners(ConfigurationField.TICKERREFRESHRATE);
		}
	}

	public void setAssetRefreshRate(int assetRefreshRate) {
		if (this.assetRefreshRate != assetRefreshRate) {
			this.assetRefreshRate = assetRefreshRate;
			notifyListeners(ConfigurationField.ASSETREFRESHRATE);
		}
	}

	public void addAccount(String exchange, Account account) {
		accountService.addAccount(exchange, account);
	}
	
	public void deleteAccount(String exchange, String accountName) {
		accountService.removeAccount(exchange, accountName);
		
	}

	private void notifyListeners(ConfigurationField field) {
		for (Listener listener : listeners) {
			listener.update(field, this);
		}
	}

	public void addChangeListener(Listener newListener) {
		listeners.add(newListener);
	}

}
