package tvtrader.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import tvtrader.model.Configuration;
import tvtrader.persistence.ConfigurationDao;

@Transactional
@Component
public class PersistenceService {
	
	@Autowired
	private Configuration configuration;

	@Autowired
	private ConfigurationDao configurationDao;

	public void save() {
		configurationDao.create(configuration);
	}

}
