package net.ddns.f1.repository;

import java.util.List;

import net.ddns.f1.domain.Driver;
import net.ddns.f1.domain.FullName;

import org.springframework.data.repository.CrudRepository;

public interface DriverRepository extends CrudRepository<Driver, FullName> {
	List<Driver> findDriverByName(final FullName name);
}
