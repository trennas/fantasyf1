package fantasyf1.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import fantasyf1.domain.Config;

public interface ConfigRepository extends CrudRepository<Config, String> {
	List<Config> findByKey(final String key);
}
