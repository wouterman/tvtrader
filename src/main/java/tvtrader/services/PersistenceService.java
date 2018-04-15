package tvtrader.services;

import org.springframework.beans.factory.annotation.Autowired;
import tvtrader.model.*;
import tvtrader.persistence.AccountDao;
import tvtrader.persistence.ConfigurationDao;
import tvtrader.persistence.MailConfigurationDao;

import java.util.List;
import java.util.Optional;

public class PersistenceService implements Listener {
	
	@Autowired
	private ConfigurationDao configurationDao;
	
	@Autowired
	private MailConfigurationDao mailConfigDao;
	
	@Autowired
	private AccountDao accountDao;
	
	public void saveOrUpdate(Configuration configuration) {
		configurationDao.update(configuration);
	}
	
	public Optional<Configuration> getConfiguration() {
		return configurationDao.getConfiguration();
	}
	
	public void saveOrUpdate(Account account) {
		accountDao.update(account);
	}
	
	public List<Account> getAccounts() {
		return accountDao.getAccounts();
	}
	
	public Account getAccount(long id) {
		return accountDao.getAccount(id);
	}

	public void saveOrUpdate(MailConfiguration configuration) {
		mailConfigDao.update(configuration);		
	}
	
	public Optional<MailConfiguration> getMailConfiguration() {
		return mailConfigDao.getConfiguration();
	}
	
	@Override
	public void update(ListenerField changedField, Object subject) {
		if (subject instanceof Configuration) {
			saveOrUpdate((Configuration) subject);
		}
		
		if (subject instanceof Account) {
			saveOrUpdate((Account) subject);
		}
		
		if (subject instanceof MailConfiguration) {
			saveOrUpdate((MailConfiguration) subject);
		}
	}
}
