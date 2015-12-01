package net.ddns.f1.service;

import java.util.List;

import net.ddns.f1.domain.Team;
import net.ddns.f1.service.impl.Ff1Exception;

public interface LeagueService {
	public List<Team> calculateLeagueStandings() throws Ff1Exception;

	public void recalculateAllResults() throws Ff1Exception;
}
