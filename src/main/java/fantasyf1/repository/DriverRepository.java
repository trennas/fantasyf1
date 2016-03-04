package fantasyf1.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import fantasyf1.domain.Car;
import fantasyf1.domain.Driver;

public interface DriverRepository extends CrudRepository<Driver, String> {
	List<Driver> findByNumber(final int number);

	List<Driver> findByName(final String name);

	List<Driver> findByCar(final Car car);

	List<Driver> findByStandin(final Boolean standin);

	List<Driver> findByCarAndStandin(final Car car, final Boolean standin);
}
