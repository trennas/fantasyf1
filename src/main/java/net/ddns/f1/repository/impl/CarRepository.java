package net.ddns.f1.repository.impl;

import java.util.List;

import net.ddns.f1.domain.Car;

import org.springframework.data.repository.CrudRepository;

public interface CarRepository extends CrudRepository<Car, String> {
	List<Car> findCarByName(final String name);
}
