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
import org.mockito.MockitoAnnotations;

import test.logger.Logger;
import test.logger.TestListener;
import tvtrader.services.AccountService;

public class ConfigurationTest {
	private static final int INTERVAL = 2;

	private static final int INVALID_INTERVAL = 0;
	
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
		
		configuration.subscribe(listener);
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
		configuration.setMailPollingInterval(INTERVAL);
		assertTrue(listener.isNotified());
		
		listener.reset();
		assertFalse(listener.isNotified());
		
		configuration.setMailPollingInterval(INTERVAL);
		
		assertFalse(listener.isNotified());
		assertEquals(INTERVAL, configuration.getMailPollingInterval());
	}
	
	@Test
	void setMailPollingInterval_whenArgumentIsSmallerThanOne_shouldThrowIllegalArgumentException() throws Exception {
		assertThrows(IllegalArgumentException.class, () -> configuration.setMailPollingInterval(INVALID_INTERVAL));
	}
	
	@Test
	void setStoplossInterval_whenArgumentIsSameAsPrevious_shouldDoNothing() {
		configuration.setStoplossInterval(INTERVAL);
		assertTrue(listener.isNotified());
		
		listener.reset();
		assertFalse(listener.isNotified());
		
		configuration.setStoplossInterval(INTERVAL);
		
		assertFalse(listener.isNotified());
		assertEquals(INTERVAL, configuration.getStoplossInterval());
	}
	
	@Test
	void setStoplossInterval_whenArgumentIsSmallerThanOne_shouldThrowIllegalArgumentException() throws Exception {
		assertThrows(IllegalArgumentException.class, () -> configuration.setStoplossInterval(INVALID_INTERVAL));
	}
	
	@Test
	void setOpenOrdersInterval_whenArgumentIsSameAsPrevious_shouldDoNothing() {
		configuration.setOpenOrdersInterval(INTERVAL);
		assertTrue(listener.isNotified());
		
		listener.reset();
		assertFalse(listener.isNotified());
		
		configuration.setOpenOrdersInterval(INTERVAL);
		
		assertFalse(listener.isNotified());
		assertEquals(INTERVAL, configuration.getOpenOrdersInterval());
	}
	
	@Test
	void setOpenOrdersInterval_whenArgumentIsSmallerThanOne_shouldThrowIllegalArgumentException() throws Exception {
		assertThrows(IllegalArgumentException.class, () -> configuration.setOpenOrdersInterval(INVALID_INTERVAL));
	}
	
	@Test
	void setOpenOrdersExpirationTime_whenArgumentIsSameAsPrevious_shouldDoNothing() {
		configuration.setOpenOrdersExpirationTime(INTERVAL);
		assertTrue(listener.isNotified());
		
		listener.reset();
		assertFalse(listener.isNotified());
		
		configuration.setOpenOrdersExpirationTime(INTERVAL);
		
		assertFalse(listener.isNotified());
		assertEquals(INTERVAL, configuration.getOpenOrdersExpirationTime());
	}
	
	@Test
	void setOpenOrdersExpirationTime_whenArgumentIsSmallerThanOne_shouldThrowIllegalArgumentException() throws Exception {
		assertThrows(IllegalArgumentException.class, () -> configuration.setOpenOrdersExpirationTime(INVALID_INTERVAL));
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
		configuration.setTickerRefreshRate(INTERVAL);
		assertTrue(listener.isNotified());
		
		listener.reset();
		assertFalse(listener.isNotified());
		
		configuration.setTickerRefreshRate(INTERVAL);
		
		assertFalse(listener.isNotified());
		assertEquals(INTERVAL, configuration.getTickerRefreshRate());
	}
	
	@Test
	void setTickerRefreshRate_whenArgumentIsSmallerThanOne_shouldThrowIllegalArgumentException() throws Exception {
		assertThrows(IllegalArgumentException.class, () -> configuration.setTickerRefreshRate(INVALID_INTERVAL));
	}
	
	@Test
	void setAssetRefreshRate_whenArgumentIsSameAsPrevious_shouldDoNothing() {
		configuration.setAssetRefreshRate(INTERVAL);
		assertTrue(listener.isNotified());
		
		listener.reset();
		assertFalse(listener.isNotified());
		
		configuration.setAssetRefreshRate(INTERVAL);
		
		assertFalse(listener.isNotified());
		assertEquals(INTERVAL, configuration.getAssetRefreshRate());
	}
	
	@Test
	void setAssetRefreshRate_whenArgumentIsSmallerThanOne_shouldThrowIllegalArgumentException() throws Exception {
		assertThrows(IllegalArgumentException.class, () -> configuration.setAssetRefreshRate(INVALID_INTERVAL));
	}
	
}
