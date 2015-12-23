package net.ddns.f1.repository;

import java.util.List;

import net.ddns.f1.domain.Car;
import net.ddns.f1.domain.Driver;

import org.springframework.data.repository.CrudRepository;

public interface DriverRepository extends CrudRepository<Driver, String> {
	List<Driver> findByNumber(final int number);

	List<Driver> findByName(final String name);

	List<Driver> findByCar(final Car car);

	List<Driver> findByStandin(final Boolean standin);

	List<Driver> findByCarAndStandin(final Car car, final Boolean standin);
}
