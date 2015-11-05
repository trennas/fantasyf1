package net.ddns.f1.repository;

import java.util.List;

import net.ddns.f1.domain.Car;
import net.ddns.f1.domain.Engine;

import org.springframework.data.repository.CrudRepository;

public interface CarRepository extends CrudRepository<Car, String> {
	List<Car> findByName(final String name);
	List<Car> findByEngine(final Engine engine);
}
