package tvtrader.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tvtrader.model.Account;
import tvtrader.model.AccountId;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Transactional
@Repository
public class AccountDao {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public void update(Account account) {
		entityManager.merge(account);
	}

	public List<Account> getAccounts() {
		TypedQuery<Account> query = entityManager.createQuery("from Account", Account.class);
		
		return query.getResultList();
	}
	
	public Account getAccount(String exchange, String accountName) {
		return entityManager.find(Account.class, new AccountId(exchange, accountName));
	}
}
