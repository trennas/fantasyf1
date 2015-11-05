package net.ddns.f1.repository;

import java.util.List;

import net.ddns.f1.domain.Engine;

import org.springframework.data.repository.CrudRepository;

public interface EngineRepository extends CrudRepository<Engine, String> {
	List<Engine> findByName(final String name);
}
