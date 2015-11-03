package net.ddns.f1.repository;

import java.util.List;

import net.ddns.f1.domain.Driver;

import org.springframework.data.repository.CrudRepository;

public interface DriverRepository extends CrudRepository<Driver, String> {
	List<Driver> findDriverByName(String name);
}
