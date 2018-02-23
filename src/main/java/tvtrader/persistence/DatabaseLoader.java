package tvtrader.persistence;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;
import tvtrader.model.Configuration;
import tvtrader.model.MailConfiguration;
import tvtrader.services.ConfigurationService;

@Log4j2
@Component
public class DatabaseLoader {
	@Autowired private ConfigurationService configurationService;
	@Autowired private ConfigurationDao configurationDao;
	@Autowired private MailConfigurationDao mailConfigDao;
	
	public void loadConfiguration() {
		Optional<Configuration> config = configurationDao.getConfiguration();
		Optional<MailConfiguration> mailConfig = mailConfigDao.getConfiguration();
		
		if(config.isPresent()) {
			log.debug("Retrieved configuration.");
			Configuration storedConfig = config.get();
			
			configurationService.cloneConfig(storedConfig);
		}
		
		if (mailConfig.isPresent()) {
			log.debug("Retrieved mailConfiguration");
			MailConfiguration storedMailConfig = mailConfig.get();
			
			configurationService.cloneMailConfig(storedMailConfig);
		}
	}
	
	
}
