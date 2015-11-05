package net.ddns.f1.service.impl;

import java.util.List;

import net.ddns.f1.domain.Team;
import net.ddns.f1.repository.TeamRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LeagueServiceImpl {
	@Autowired
	TeamService teamService;

	@Autowired
	TeamRepository teamRepo;

	@Autowired
	EventServiceImpl eventService;

	public List<Team> calculateLeagueStandings() {
		return teamService.getAllTeams();
	}
}
