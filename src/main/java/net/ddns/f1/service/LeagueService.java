package net.ddns.f1.service;

import java.util.Date;
import java.util.List;

import net.ddns.f1.domain.Rules;
import net.ddns.f1.domain.Team;

public interface LeagueService {
	public List<Team> calculateLeagueStandings();

	public void recalculateAllResults();

	public Rules getRules();

	public boolean seasonStarted();

	public Date getSeasonStartDate();
}
