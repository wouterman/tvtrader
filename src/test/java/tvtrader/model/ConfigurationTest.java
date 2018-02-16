package tvtrader.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import test.logger.Logger;
import test.logger.TestListener;
import tvtrader.services.AccountService;

public class ConfigurationTest {
	
	private TestListener listener;
	
	@Mock private AccountService service;
	
	@InjectMocks
	private Configuration configuration;
	
	@BeforeAll
	synchronized static void startup() {
		Logger.turnOffLogging();
	}

	@BeforeEach
	void init() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		listener = new TestListener();
		
		configuration.addChangeListener(listener);
	}
	
	@Test
	void setMailConfig_whenArgumentIsSameAsPrevious_shouldDoNothing() {
		MailConfiguration mailconfig = new MailConfiguration();
		configuration.setMailConfig(mailconfig);
		assertTrue(listener.isNotified());
		
		listener.reset();
		assertFalse(listener.isNotified());
		
		configuration.setMailConfig(mailconfig);
		
		assertFalse(listener.isNotified());
		assertEquals(mailconfig, configuration.getMailConfig());
	}
	
	@Test
	void setExpectedSender_whenArgumentIsSameAsPrevious_shouldDoNothing() {
		String sender = "SENDER";
		configuration.setExpectedSender(sender);
		assertTrue(listener.isNotified());
		
		listener.reset();
		assertFalse(listener.isNotified());
		
		configuration.setExpectedSender(sender);
		
		assertFalse(listener.isNotified());
		assertEquals(sender, configuration.getExpectedSender());
	}
	
	@Test
	void setMailPollingInterval_whenArgumentIsSameAsPrevious_shouldDoNothing() {
		int interval = 1;
		configuration.setMailPollingInterval(interval);
		assertTrue(listener.isNotified());
		
		listener.reset();
		assertFalse(listener.isNotified());
		
		configuration.setMailPollingInterval(interval);
		
		assertFalse(listener.isNotified());
		assertEquals(interval, configuration.getMailPollingInterval());
	}
	
	@Test
	void setMailPollingInterval_whenArgumentIsSmallerThanOne_shouldThrowIllegalArgumentException() throws Exception {
		int interval = 0;
		assertThrows(IllegalArgumentException.class, () -> configuration.setMailPollingInterval(interval));
	}
	
	@Test
	void setStoplossInterval_whenArgumentIsSameAsPrevious_shouldDoNothing() {
		int interval = 1;
		configuration.setStoplossInterval(interval);
		assertTrue(listener.isNotified());
		
		listener.reset();
		assertFalse(listener.isNotified());
		
		configuration.setStoplossInterval(interval);
		
		assertFalse(listener.isNotified());
		assertEquals(interval, configuration.getStoplossInterval());
	}
	
	@Test
	void setStoplossInterval_whenArgumentIsSmallerThanOne_shouldThrowIllegalArgumentException() throws Exception {
		int interval = 0;
		assertThrows(IllegalArgumentException.class, () -> configuration.setStoplossInterval(interval));
	}
	
	@Test
	void setOpenOrdersInterval_whenArgumentIsSameAsPrevious_shouldDoNothing() {
		int interval = 1;
		configuration.setOpenOrdersInterval(interval);
		assertTrue(listener.isNotified());
		
		listener.reset();
		assertFalse(listener.isNotified());
		
		configuration.setOpenOrdersInterval(interval);
		
		assertFalse(listener.isNotified());
		assertEquals(interval, configuration.getOpenOrdersInterval());
	}
	
	@Test
	void setOpenOrdersInterval_whenArgumentIsSmallerThanOne_shouldThrowIllegalArgumentException() throws Exception {
		int interval = 0;
		assertThrows(IllegalArgumentException.class, () -> configuration.setOpenOrdersInterval(interval));
	}
	
	@Test
	void setOpenOrdersExpirationTime_whenArgumentIsSameAsPrevious_shouldDoNothing() {
		int interval = 1;
		configuration.setOpenOrdersExpirationTime(interval);
		assertTrue(listener.isNotified());
		
		listener.reset();
		assertFalse(listener.isNotified());
		
		configuration.setOpenOrdersExpirationTime(interval);
		
		assertFalse(listener.isNotified());
		assertEquals(interval, configuration.getOpenOrdersExpirationTime());
	}
	
	@Test
	void setOpenOrdersExpirationTime_whenArgumentIsSmallerThanOne_shouldThrowIllegalArgumentException() throws Exception {
		int interval = 0;
		assertThrows(IllegalArgumentException.class, () -> configuration.setOpenOrdersExpirationTime(interval));
	}
	
	@Test
	void setUnfilledOrdersReplaceFlag_whenArgumentIsSameAsPrevious_shouldDoNothing() {
		configuration.setUnfilledOrdersReplaceFlag(true);
		assertTrue(listener.isNotified());
		
		listener.reset();
		assertFalse(listener.isNotified());
		
		configuration.setUnfilledOrdersReplaceFlag(true);
		
		assertFalse(listener.isNotified());
		assertTrue(configuration.isRetryOrderFlag());
	}
	
	@Test
	void setTickerRefreshRate_whenArgumentIsSameAsPrevious_shouldDoNothing() {
		int interval = 1;
		configuration.setTickerRefreshRate(interval);
		assertTrue(listener.isNotified());
		
		listener.reset();
		assertFalse(listener.isNotified());
		
		configuration.setTickerRefreshRate(interval);
		
		assertFalse(listener.isNotified());
		assertEquals(interval, configuration.getTickerRefreshRate());
	}
	
	@Test
	void setTickerRefreshRate_whenArgumentIsSmallerThanOne_shouldThrowIllegalArgumentException() throws Exception {
		int interval = 0;
		assertThrows(IllegalArgumentException.class, () -> configuration.setTickerRefreshRate(interval));
	}
	
	@Test
	void setAssetRefreshRate_whenArgumentIsSameAsPrevious_shouldDoNothing() {
		int interval = 1;
		configuration.setAssetRefreshRate(interval);
		assertTrue(listener.isNotified());
		
		listener.reset();
		assertFalse(listener.isNotified());
		
		configuration.setAssetRefreshRate(interval);
		
		assertFalse(listener.isNotified());
		assertEquals(interval, configuration.getAssetRefreshRate());
	}
	
	@Test
	void setAssetRefreshRate_whenArgumentIsSmallerThanOne_shouldThrowIllegalArgumentException() throws Exception {
		int interval = 0;
		assertThrows(IllegalArgumentException.class, () -> configuration.setAssetRefreshRate(interval));
	}
	
	@Test
	void addAccount_whenCalled_shouldDelegateToRepositoryAndNotNotifyListeners() {
		Account account = new Account(null, null, 0, 0, 0, 0, null);
		String exchange = "";
		configuration.addAccount(exchange, account);
		assertFalse(listener.isNotified());
		
		Mockito.verify(service, Mockito.times(1)).addAccount(exchange, account);
	}
	
	@Test
	void deleteAccount_whenCalled_shouldDelegateToRepositoryAndNotNotifyListeners() {
		String exchange = "";
		String account = "";
		configuration.deleteAccount(exchange, account);
		assertFalse(listener.isNotified());
		
		Mockito.verify(service, Mockito.times(1)).removeAccount(exchange, account);
	}
}
