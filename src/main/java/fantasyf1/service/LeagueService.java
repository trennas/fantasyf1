package fantasyf1.service;

import java.util.Date;
import java.util.List;

import fantasyf1.domain.Rules;
import fantasyf1.domain.Team;

public interface LeagueService {
	public List<Team> calculateLeagueStandings();

	public void recalculateAllResults();

	public Rules getRules();

	public boolean seasonStarted();

	public Date getSeasonStartDate();
}
