package net.ddns.f1.repository;

import java.util.List;

import net.ddns.f1.domain.Car;
import net.ddns.f1.domain.Driver;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

public interface DriverRepository extends CrudRepository<Driver, Integer> {
	List<Driver> findByNumber(final Integer number);	
	List<Driver> findByCar(final Car car);
}
