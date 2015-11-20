package net.ddns.f1.repository;

import java.util.List;
import net.ddns.f1.domain.Config;

import org.springframework.data.repository.CrudRepository;

public interface ConfigRepository extends CrudRepository<Config, String> {
	List<Config> findByKey(final String key);
}
