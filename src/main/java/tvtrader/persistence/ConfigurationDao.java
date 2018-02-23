package tvtrader.persistence;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import tvtrader.model.Configuration;

@Transactional
@Repository
public class ConfigurationDao {

	@PersistenceContext
	private EntityManager entityManager;

	public void update(Configuration configuration) {
		entityManager.merge(configuration);
	}

	public Optional<Configuration> getConfiguration() {
		Query query = entityManager.createQuery("from Configuration");

		Optional<Configuration> optional;
		try {
			Configuration configuration = (Configuration) query.getSingleResult();
			optional = Optional.of(configuration);
		} catch (NoResultException | NonUniqueResultException e) {
			optional = Optional.empty();
		}

		return optional;
	}

}
