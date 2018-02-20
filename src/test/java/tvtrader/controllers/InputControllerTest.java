package tvtrader.controllers;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import test.logger.Logger;
import tvtrader.mail.MailClient;
import tvtrader.model.Account;
import tvtrader.model.MailConfiguration;
import tvtrader.services.AccountService;
import tvtrader.services.ConfigurationService;

class InputControllerTest {
	@Mock private ConfigurationService configurationService;
	@Mock private AccountService accountService;
	@Mock private MailClient mailClient;
	
	@InjectMocks private InputController controller; 
	
	@BeforeAll
	synchronized static void startup() {
		Logger.turnOffLogging();
	}

	@BeforeEach
	void setup() throws Exception {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	void setExpectedSender_whenParamNull_shouldThrowNPE() throws Exception {
		String param = null;
		assertThrows(NullPointerException.class, () -> controller.setExpectedSender(param));
	}
	
	@Test
	void setExpectedSender_whenCalled_shouldSetSender() throws Exception {
		String param = "PARAM";
		
		controller.setExpectedSender(param);
		
		Mockito.verify(configurationService, Mockito.times(1)).setExpectedSender(param);
	}
	
	@Test
	void setMailConfiguration_whenParamNull_shouldThrowNPE() throws Exception {
		MailConfiguration param = null;
		assertThrows(NullPointerException.class, () -> controller.setMailConfiguration(param));
	}	
	
	@Test
	void setMailConfiguration_whenCalled_shouldSetConfigurationAndVerify() throws Exception {
		MailConfiguration param = new MailConfiguration();
		
		controller.setMailConfiguration(param);
		
		Mockito.verify(configurationService, Mockito.times(1)).setMailConfiguration(param);
		Mockito.verify(mailClient, Mockito.times(1)).verify();
	}
	
	@Test
	void setMailPollingInterval_whenCalled_shouldSetInterval() throws Exception {
		int interval = 0;
		
		controller.setMailPollingInterval(interval);
		
		Mockito.verify(configurationService, Mockito.times(1)).setMailPollingInterval(interval);
	}	
	
	@Test
	void setStoplossInterval_whenCalled_shouldSetInterval() throws Exception {
		int interval = 0;
		
		controller.setStoplossInterval(interval);
		
		Mockito.verify(configurationService, Mockito.times(1)).setStoplossInterval(interval);
	}	
	
	@Test
	void setOpenOrdersInterval_whenCalled_shouldSetInterval() throws Exception {
		int interval = 0;
		
		controller.setOpenOrdersInterval(interval);
		
		Mockito.verify(configurationService, Mockito.times(1)).setOpenOrdersInterval(interval);
	}
	
	@Test
	void setOpenOrdersExpirationTime_whenCalled_shouldSetInterval() throws Exception {
		int interval = 0;
		
		controller.setOpenOrdersExpirationTime(interval);
		
		Mockito.verify(configurationService, Mockito.times(1)).setOpenOrdersExpirationTime(interval);
	}
	
	@Test
	void setCancelledOrderReplaceFlag_whenCalled_shouldSetFlag() throws Exception {
		boolean flag = false;
		
		controller.setUnfilledOrdersReplaceFlag(flag);
		
		Mockito.verify(configurationService, Mockito.times(1)).setUnfilledOrdersReplaceFlag(flag);
	}
	
	@Test
	void setTickerRefreshRate_whenCalled_shouldSetInterval() throws Exception {
		int interval = 0;
		
		controller.setTickerRefreshRate(interval);
		
		Mockito.verify(configurationService, Mockito.times(1)).setTickerRefreshRate(interval);
	}
	
	@Test
	void setAssetRefreshRate_whenCalled_shouldSetInterval() throws Exception {
		int interval = 0;
		
		controller.setAssetRefreshRate(interval);
		
		Mockito.verify(configurationService, Mockito.times(1)).setAssetRefreshRate(interval);
	}
	
	@Test
	void addAccount_whenParamsNull_shouldThrowNPE() throws Exception {
		String exchange = null;
		Account account = new Account(null, null, null, 0, 0, 0, 0, null);
		assertThrows(NullPointerException.class, () -> controller.addAccount(exchange, account));
		
		String exchange2 = "EXCHANGE";
		Account account2 = null;
		assertThrows(NullPointerException.class, () -> controller.addAccount(exchange2, account2));
	}
	
	@Test
	void addAccount_whenCalled_shouldAddAccount() throws Exception {
		String exchangeName = "EXCHANGE";
		Account account = new Account(null, null, null, 0, 0, 0, 0, null);
		
		controller.addAccount(exchangeName, account);
		
		Mockito.verify(accountService, Mockito.times(1)).addAccount(exchangeName, account);
	}
	
	@Test
	void removeAccount_whenParamsNull_shouldThrowNPE() throws Exception {
		String exchange = null;
		String account = "ACCOUNT";
		assertThrows(NullPointerException.class, () -> controller.removeAccount(exchange, account));
		
		String exchange2 = "EXCHANGE";
		String account2 = null;
		assertThrows(NullPointerException.class, () -> controller.removeAccount(exchange2, account2));
	}
	
	@Test
	void removeAccount_whenCalled_shouldRemoveAccount() throws Exception {
		String exchangeName = "EXCHANGE";
		String account = "ACCOUNT";
		
		controller.removeAccount(exchangeName, account);
		
		Mockito.verify(accountService, Mockito.times(1)).removeAccount(exchangeName, account);
	}
}
