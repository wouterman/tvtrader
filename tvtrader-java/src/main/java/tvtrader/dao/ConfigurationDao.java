package tvtrader.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tvtrader.model.Configuration;

import java.util.Optional;

@Transactional
@Repository
public interface ConfigurationDao extends JpaRepository<Configuration, String> {

	public Optional<Configuration> findByName(String name);

	public Configuration save(Configuration configuration);

}
