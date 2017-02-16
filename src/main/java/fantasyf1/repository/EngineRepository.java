package fantasyf1.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import fantasyf1.domain.Engine;

public interface EngineRepository extends CrudRepository<Engine, String> {
	List<Engine> findByName(final String name);
	List<Engine> findAllByOrderByTotalPointsDesc(); 
}
