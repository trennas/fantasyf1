package fantasyf1.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import fantasyf1.domain.EventResult;

public interface EventResultRepository extends
		CrudRepository<EventResult, Integer> {
	List<EventResult> findByRound(final Integer round);

	void deleteByRound(final Integer round);
}
