package net.ddns.f1.repository;

import java.util.List;

import net.ddns.f1.domain.EventResult;

import org.springframework.data.repository.CrudRepository;

public interface EventResultRepository extends
		CrudRepository<EventResult, Integer> {
	List<EventResult> findByRound(final Integer round);
}
