package net.ddns.f1.repository;

import java.util.List;

import net.ddns.f1.domain.Team;

import org.springframework.data.repository.CrudRepository;

public interface TeamRepository extends CrudRepository<Team, String> {
	List<Team> findTeamByName(final String name);
}
