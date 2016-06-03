package fantasyf1.service;

import java.util.Date;
import java.util.List;

import fantasyf1.domain.EventResult;
import fantasyf1.domain.Rules;
import fantasyf1.domain.Team;

public interface LeagueService {
	public List<Team> calculateLeagueStandings();

	public void recalculateAllResults();
	
	public void calculateResult(EventResult result);

	public Rules getRules();

	public boolean seasonStarted();

	public Date getSeasonStartDate();
	
	public void deletePointsForRound(int round);
	
	public void resetAllScores();
}
