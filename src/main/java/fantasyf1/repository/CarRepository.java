package fantasyf1.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import fantasyf1.domain.Car;
import fantasyf1.domain.Engine;

public interface CarRepository extends CrudRepository<Car, String> {
	List<Car> findById(final Integer id);	
	List<Car> findByName(final String name);
	List<Car> findByEngine(final Engine engine);
	List<Car> findAllByOrderByTotalPointsDesc(); 
}
