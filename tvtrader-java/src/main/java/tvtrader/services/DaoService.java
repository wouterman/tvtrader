package tvtrader.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tvtrader.dao.AccountDao;
import tvtrader.dao.ConfigurationDao;
import tvtrader.dao.MailConfigurationDao;
import tvtrader.model.Account;
import tvtrader.model.Configuration;
import tvtrader.model.MailConfiguration;

import java.util.List;
import java.util.Optional;

@Service
public class DaoService {
	
	private ConfigurationDao configurationDao;
	private MailConfigurationDao mailConfigDao;
	private AccountDao accountDao;

	@Autowired
	public DaoService(ConfigurationDao configurationDao, MailConfigurationDao mailConfigDao, AccountDao accountDao) {
		this.configurationDao = configurationDao;
		this.mailConfigDao = mailConfigDao;
		this.accountDao = accountDao;
	}

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
	
	public Account getAccount(String exchange, String accountName) {
		return accountDao.getAccount(exchange, accountName);
	}

	public void saveOrUpdate(MailConfiguration configuration) {
		mailConfigDao.update(configuration);		
	}
	
	public Optional<MailConfiguration> getMailConfiguration() {
		return mailConfigDao.getConfiguration();
	}

}
