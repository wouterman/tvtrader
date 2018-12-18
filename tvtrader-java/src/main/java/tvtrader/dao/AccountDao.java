package tvtrader.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tvtrader.model.Account;
import tvtrader.model.AccountId;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface AccountDao extends JpaRepository<Account, AccountId> {

	public Account save(Account account);

	public List<Account> findAll();
	
	public Optional<Account> findById(AccountId id);
}
