package tvtrader.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tvtrader.model.Configuration;

import javax.persistence.*;
import java.util.Optional;

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
