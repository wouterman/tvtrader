package tvtrader.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tvtrader.model.Configuration;
import tvtrader.services.AccountService;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ConfigurationTest {
	private static final int INTERVAL = 2;

	private static final int INVALID_INTERVAL = 0;
	
	@Mock private AccountService service;
	
	@InjectMocks
	private Configuration configuration;
	
	@BeforeAll
	synchronized static void startup() {

	}

	@BeforeEach
	void init() throws Exception {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	void setMailPollingInterval_whenArgumentIsSmallerThanOne_shouldThrowIllegalArgumentException() throws Exception {
		assertThrows(IllegalArgumentException.class, () -> configuration.setMailPollingInterval(INVALID_INTERVAL));
	}

	@Test
	void setStoplossInterval_whenArgumentIsSmallerThanOne_shouldThrowIllegalArgumentException() throws Exception {
		assertThrows(IllegalArgumentException.class, () -> configuration.setStoplossInterval(INVALID_INTERVAL));
	}
	
	@Test
	void setOpenOrdersInterval_whenArgumentIsSmallerThanOne_shouldThrowIllegalArgumentException() throws Exception {
		assertThrows(IllegalArgumentException.class, () -> configuration.setOpenOrdersInterval(INVALID_INTERVAL));
	}

	@Test
	void setOpenOrdersExpirationTime_whenArgumentIsSmallerThanOne_shouldThrowIllegalArgumentException() throws Exception {
		assertThrows(IllegalArgumentException.class, () -> configuration.setOpenOrdersExpirationTime(INVALID_INTERVAL));
	}
	
	@Test
	void setTickerRefreshRate_whenArgumentIsSmallerThanOne_shouldThrowIllegalArgumentException() throws Exception {
		assertThrows(IllegalArgumentException.class, () -> configuration.setTickerRefreshRate(INVALID_INTERVAL));
	}

	@Test
	void setAssetRefreshRate_whenArgumentIsSmallerThanOne_shouldThrowIllegalArgumentException() throws Exception {
		assertThrows(IllegalArgumentException.class, () -> configuration.setAssetRefreshRate(INVALID_INTERVAL));
	}
	
}
