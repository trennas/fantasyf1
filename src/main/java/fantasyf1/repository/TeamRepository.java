package fantasyf1.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import fantasyf1.domain.Team;

public interface TeamRepository extends CrudRepository<Team, Integer> {
	List<Team> findById(final Integer id);

	List<Team> findByName(final String name);

	List<Team> findByEmail(final String email);
}
