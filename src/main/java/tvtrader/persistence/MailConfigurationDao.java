package tvtrader.persistence;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import tvtrader.model.MailConfiguration;

@Transactional
@Repository
public class MailConfigurationDao {
	
	@PersistenceContext
	private EntityManager entityManager;

	public void update(MailConfiguration configuration) {
		entityManager.merge(configuration);
	}

	public Optional<MailConfiguration> getConfiguration() {
		Query query = entityManager.createQuery("from MailConfiguration");

		Optional<MailConfiguration> optional;
		try {
			MailConfiguration configuration = (MailConfiguration) query.getSingleResult();
			optional = Optional.of(configuration);
		} catch (NoResultException | NonUniqueResultException e) {
			optional = Optional.empty();
		}

		return optional;
	}
}
