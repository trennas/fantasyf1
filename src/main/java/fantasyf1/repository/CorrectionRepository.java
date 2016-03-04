package fantasyf1.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import fantasyf1.domain.Correction;

public interface CorrectionRepository extends
		CrudRepository<Correction, Integer> {
	List<Correction> findByRound(final int round);
}
