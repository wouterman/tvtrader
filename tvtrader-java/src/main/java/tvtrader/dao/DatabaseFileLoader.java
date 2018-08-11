package tvtrader.dao;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tvtrader.model.Configuration;
import tvtrader.model.MailConfiguration;
import tvtrader.services.ConfigurationService;

import java.util.Optional;


@Log4j2
@Component
public class DatabaseFileLoader {
	private ConfigurationService configurationService;
	private ConfigurationDao configurationDao;
	private MailConfigurationDao mailConfigDao;

	@Autowired
	public DatabaseFileLoader(ConfigurationService configurationService, ConfigurationDao configurationDao, MailConfigurationDao mailConfigDao) {
		this.configurationService = configurationService;
		this.configurationDao = configurationDao;
		this.mailConfigDao = mailConfigDao;
	}

	public void loadConfiguration() {
		Optional<Configuration> config = configurationDao.getConfiguration();
		Optional<MailConfiguration> mailConfig = mailConfigDao.getConfiguration();
		
		if(config.isPresent()) {
			log.debug("Retrieved configuration.");
			Configuration storedConfig = config.get();

			configurationService.setConfiguration(storedConfig);
		}
		
		if (mailConfig.isPresent()) {
			log.debug("Retrieved mailConfiguration");
			MailConfiguration storedMailConfig = mailConfig.get();
			
			configurationService.setMailConfiguration(storedMailConfig);
		}
	}
	
	
}
