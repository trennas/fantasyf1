package net.ddns.f1.service;

import java.util.List;

import net.ddns.f1.domain.Team;
import net.ddns.f1.domain.TheoreticalTeam;
import net.ddns.f1.service.impl.Ff1Exception;
import net.ddns.f1.service.impl.ValidationException;

public interface TeamService {
	public List<Team> getAllRealTeams();

	public boolean seasonStarted();

	public void saveTeam(final Team team) throws ValidationException;

	public void saveTeamNoValidation(final Team team);

	public void validateTeam(final Team team, final boolean newTeam)
			throws ValidationException;

	public void validateTeamComponents(final Team team)
			throws ValidationException;

	public void delete(final int id);

	public Team findByEmail(final String email) throws Ff1Exception;

	public Team findById(final int id) throws Ff1Exception;

	public List<Team> findAll();

	public void deleteAllTheoreticalTeams();

	public TheoreticalTeam findTheoreticalTeamByName(final String name)
			throws Ff1Exception;

	public void saveTheoreticalTeam(final TheoreticalTeam team);
}
