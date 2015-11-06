package net.ddns.f1.web;

import java.util.Collections;
import java.util.List;

import net.ddns.f1.domain.EventResult;
import net.ddns.f1.domain.Team;
import net.ddns.f1.repository.EventResultRepository;
import net.ddns.f1.repository.TeamRepository;
import net.ddns.f1.service.impl.LeagueServiceImpl;
import net.ddns.f1.service.impl.TeamService;

import org.apache.commons.collections4.IteratorUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Profile("!create")
public class MainController {

	private static final Logger LOG = Logger
			.getLogger(MainController.class);
	
	@Autowired
	private LeagueServiceImpl leagueService;
	@Autowired
	private TeamRepository teamRepo;
	@Autowired
	private TeamService teamService;
	@Autowired
	private EventResultRepository resultRepo;
	
	@RequestMapping("/")
	public String mainPage() {
		return "league";
	}
	
	@RequestMapping("/teams")
	@ResponseBody
	public List<Team> getTeams() {
		return leagueService.calculateLeagueStandings();
	}
	
	@RequestMapping("/team")
	@ResponseBody
	public Team getTeam(String name) {
		return teamRepo.findTeamByName(name).get(0);
	}
	
	@RequestMapping("/events")
	@ResponseBody
	public List<EventResult> events() {
		List<EventResult> results = IteratorUtils.toList(resultRepo.findAll().iterator());
		Collections.sort(results);
		return results;
	}
	
	@RequestMapping("/event")
	@ResponseBody
	public EventResult event(int round) {
		return resultRepo.findByRound(round).get(0);
	}
	
	@RequestMapping("/login")
	public String login() {
		return "login";
	}
}
