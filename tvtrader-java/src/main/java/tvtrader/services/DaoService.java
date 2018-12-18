package tvtrader.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tvtrader.dao.AccountDao;
import tvtrader.dao.ConfigurationDao;
import tvtrader.dao.MailConfigurationDao;
import tvtrader.model.Account;
import tvtrader.model.AccountId;
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

	public void save(Configuration configuration) {
		configurationDao.save(configuration);
	}
	
	public Optional<Configuration> getConfiguration(String name) {
		return configurationDao.findByName(name);
	}
	
	public void save(Account account) {
		accountDao.save(account);
	}
	
	public List<Account> getAccounts() {
		return accountDao.findAll();
	}
	
	public Optional<Account> getAccount(String exchange, String accountName) {
		return accountDao.findById(new AccountId(exchange, accountName));
	}

	public void save(MailConfiguration configuration) {
		mailConfigDao.save(configuration);
	}
	
	public Optional<MailConfiguration> getMailConfiguration(String name) {
		return mailConfigDao.findByName(name);
	}

}
