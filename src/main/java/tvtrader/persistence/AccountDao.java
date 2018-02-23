package tvtrader.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import tvtrader.model.Account;

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
	
	public Account getAccount(long id) {
		return entityManager.find(Account.class, id);
	}
}
