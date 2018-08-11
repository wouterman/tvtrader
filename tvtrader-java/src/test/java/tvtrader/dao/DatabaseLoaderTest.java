package tvtrader.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import tvtrader.model.Configuration;
import tvtrader.services.ConfigurationService;

import java.util.Optional;

class DatabaseLoaderTest {
	private static final int ASSET_REFRESH_RATE = 2;
	
	
	@Mock private ConfigurationService configurationService;
	@Mock private ConfigurationDao configurationDao;
	@Mock private MailConfigurationDao mailConfigDao;
	
	@InjectMocks private DatabaseFileLoader dbLoader;
	
	@BeforeAll
	synchronized static void startup() {

	}
	
	@BeforeEach
	void setup() {
		MockitoAnnotations.initMocks(this);
		
	}
	
	@Test
	void loadConfiguration_whenCalled_shouldLoadConfigurationFromDb() {
		Configuration storedConfiguration = new Configuration();
		storedConfiguration.setAssetRefreshRate(ASSET_REFRESH_RATE);
		
		Optional<Configuration> optional = Optional.of(storedConfiguration);
		Mockito.when(configurationDao.getConfiguration()).thenReturn(optional);
		
		dbLoader.loadConfiguration();
		
		Mockito.verify(configurationService, Mockito.times(1)).setConfiguration(storedConfiguration);
	}
}
