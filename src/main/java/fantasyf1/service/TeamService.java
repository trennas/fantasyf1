package fantasyf1.service;

import java.util.List;

import fantasyf1.domain.Team;
import fantasyf1.domain.TheoreticalTeam;
import fantasyf1.service.impl.ValidationException;

public interface TeamService {
	public void saveTeam(final Team team) throws ValidationException;

	public void saveTeamNoValidation(final Team team);
	
	public void saveTeamsNoValidation(final List<Team> teams);

	public void validateTeam(final Team team, final boolean newTeam)
			throws ValidationException;

	public void validateTeamComponents(final Team team)
			throws ValidationException;

	public void delete(final int id);

	public Team findByEmail(final String email);

	public Team findById(final int id);

	public List<Team> findAll();

	public void resetBestTheoreticalTeam();

	public void deleteTheoreticalTeam(TheoreticalTeam theoreticalTeam);

	public TheoreticalTeam findTheoreticalTeamByName(final String name);

	public void saveTheoreticalTeam(final TheoreticalTeam team);
	
	public TheoreticalTeam getBestTheoreticalTeam();
}
