package net.ddns.f1.repository.impl;

import java.util.List;

import net.ddns.f1.domain.Engine;

import org.springframework.data.repository.CrudRepository;

public interface EngineRepository extends CrudRepository<Engine, String> {
	List<Engine> findEngineByName(final String name);
}
