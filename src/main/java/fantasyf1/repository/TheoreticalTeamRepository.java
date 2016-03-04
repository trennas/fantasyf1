package fantasyf1.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import fantasyf1.domain.TheoreticalTeam;

public interface TheoreticalTeamRepository extends
		CrudRepository<TheoreticalTeam, String> {
	List<TheoreticalTeam> findByName(final String name);
}
