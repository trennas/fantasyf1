package net.ddns.f1.repository;

import java.util.List;

import net.ddns.f1.domain.TheoreticalTeam;

import org.springframework.data.repository.CrudRepository;

public interface TheoreticalTeamRepository extends CrudRepository<TheoreticalTeam, String> {
	List<TheoreticalTeam> findByName(final String name);
}
