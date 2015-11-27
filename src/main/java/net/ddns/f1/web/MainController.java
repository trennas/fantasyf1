package net.ddns.f1.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.ddns.f1.domain.Car;
import net.ddns.f1.domain.Driver;
import net.ddns.f1.domain.Engine;
import net.ddns.f1.domain.EventResult;
import net.ddns.f1.domain.Position;
import net.ddns.f1.domain.Team;
import net.ddns.f1.domain.TheoreticalTeam;
import net.ddns.f1.repository.CarRepository;
import net.ddns.f1.repository.DriverRepository;
import net.ddns.f1.repository.EngineRepository;
import net.ddns.f1.repository.EventResultRepository;
import net.ddns.f1.repository.TeamRepository;
import net.ddns.f1.repository.TheoreticalTeamRepository;
import net.ddns.f1.service.impl.EventServiceImpl;
import net.ddns.f1.service.impl.LeagueServiceImpl;
import net.ddns.f1.service.impl.TeamServiceImpl;
import net.ddns.f1.service.impl.ValidationException;

import org.apache.commons.collections4.IteratorUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Profile("!create")
@Controller
public class MainController implements ErrorController {

	private static final String ERROR_PATH = "/error";

	private static final Logger LOG = Logger
			.getLogger(MainController.class);
	
	@Value("${season}")
	private int season;
	
	@Value("${best-theoretical-team-name}")
	private String bestTheoreticalTeamName;

	@Autowired
	private LeagueServiceImpl leagueService;
	@Autowired
	private TeamRepository teamRepo;
	@Autowired
	private TeamServiceImpl teamService;
	@Autowired
	private EventResultRepository resultRepo;
	@Autowired
	private DriverRepository driverRepo;
	@Autowired
	private CarRepository carRepo;
	@Autowired
	private EngineRepository engineRepo;
	@Autowired
	private EventServiceImpl eventService;
	@Autowired
	private TheoreticalTeamRepository theoreticalRepo;

	@RequestMapping("/")
	public String mainPage() {
		return "league";
	}

	@RequestMapping("/race")
	public String mainPage(final Integer round) {
		return "race";
	}

	@RequestMapping("/editresult")
	public String editResult(final Integer round) {
		return "editresult";
	}

	@RequestMapping("/addresult")
	public String addResult(final Integer round) {
		return "addresult";
	}

	@RequestMapping("/updateresults")
	@ResponseBody
	public int updateResults() {
		final int res = eventService.updateResults();
		LOG.info("updateresults: " + res);
		return res;
	}

	@RequestMapping(value = {"/editresult/saveresult", "/addresult/saveresult"}, method = RequestMethod.POST)
	@ResponseBody
	public EventResult saveResult(@RequestBody final EventResult result) {
		result.setFastestLapDriver(driverRepo.findByName(result.getFastestLapDriver().getName()).get(0));
		resultRepo.save(result);
		leagueService.recalculateAllResults();
		final EventResult savedResult = resultRepo.findByRound(result.getRound()).get(0);
		savedResult.setRemarks(result.getRemarks());
		resultRepo.save(savedResult);
		return event(savedResult.getRound());
	}
	
	@RequestMapping({"/addresult/newevent"})
	@ResponseBody
	public EventResult event() {
		final EventResult result = new EventResult();		
		final Map<String, Position> order = new HashMap<String, Position>();
		final Position pos = new Position(0, false);
		final List<Driver> drivers = IteratorUtils.toList(driverRepo.findAll().iterator());
		final List<String> remarks = new ArrayList<String>();
		remarks.add("This result was manually created");
		Collections.sort(drivers);
		for(Driver driver : drivers) {
			order.put(driver.getName(), pos);
		}
		result.setRound(eventService.getSeasonResults().size() + 1);
		result.setQualifyingOrder(order);
		result.setRaceOrder(order);
		result.setVenue("Race Venue");
		result.setFastestLapDriver(drivers.get(0));
		result.setSeason(season);
		result.setRaceComplete(false);
		result.setRemarks(remarks);
		return result;
	}

	@RequestMapping("/editresult/refreshresult")
	@ResponseBody
	public EventResult refreshResult(final int round) {
		return eventService.refreshEvent(round);
	}

	@RequestMapping("/refreshAllResults")
	@ResponseBody
	public Boolean refreshAllResults() {
		eventService.refreshAllEvents();
		return true;
	}

	@RequestMapping({"/teams", "/{subpage}/teams"})
	@ResponseBody
	public List<Team> getTeams() {
		return maskPreSeasonTeams(leagueService.calculateLeagueStandings());
	}

	@RequestMapping({"/team", "/{subpage}/team"})
	@ResponseBody
	public Team getTeam(final Integer id) {
		return maskPreSeasonTeam(teamRepo.findById(id).get(0));
	}
	
	@RequestMapping({"/besttheoreticalteam"})
	@ResponseBody
	public TheoreticalTeam getBestTheoreticalTeam() {
		List<TheoreticalTeam> teams = theoreticalRepo.findByName(bestTheoreticalTeamName);
		if(teams.size() > 0) {
			return teams.get(0);
		} else {
			return null;
		}
	}
	
	@RequestMapping({"/race/besttheoreticalteamforround"})
	@ResponseBody
	public TheoreticalTeam getBestTheoreticalTeamForRound(final Integer round) {
		EventResult result = resultRepo.findByRound(round).get(0);
		return result.getBestTheoreticalTeam();
	}

	@RequestMapping("/events")
	@ResponseBody
	public List<EventResult> events() {
		return eventService.getSeasonResults();
	}
	
	@RequestMapping("/editresult/deleteevent")
	@ResponseBody
	public int deleteEvent(int round) {
		return eventService.deleteEvent(round);
	}
	
	@RequestMapping("/deleteteam")
	@ResponseBody
	public int deleteTeam(int id) {
		teamRepo.delete(id);
		return 1;
	}

	@RequestMapping({"/drivers", "/{subpage}/drivers"})
	@ResponseBody
	public List<Driver> drivers() {
		return drivers(false);
	}
	
	@RequestMapping({"/alldrivers", "/{subpage}/alldrivers"})
	@ResponseBody
	public List<Driver> allDrivers() {
		return drivers(true);
	}
	
	private List<Driver> drivers(boolean includeStandin) {
		List<Driver> drivers;
		if(includeStandin) {
			drivers = IteratorUtils.toList(driverRepo.findAll().iterator());
		} else {
			drivers = driverRepo.findByStandin(false);
		}
		Collections.sort(drivers);
		return drivers;
	}

	@RequestMapping({"/driver", "/{subpage}/driver"})
	@ResponseBody
	public Driver driver(final String name) {
		return driverRepo.findByName(name).get(0);
	}

	@RequestMapping({"/car", "/{subpage}/car"})
	@ResponseBody
	public Car car(final String name) {
		return carRepo.findByName(name).get(0);
	}

	@RequestMapping({"/engine", "/{subpage}/engine"})
	@ResponseBody
	public Engine engine(final String name) {
		return engineRepo.findByName(name).get(0);
	}

	@RequestMapping({"/cars", "/{subpage}/cars"})
	@ResponseBody
	public List<Car> cars() {
		final List<Car> cars = IteratorUtils.toList(carRepo.findAll().iterator());
		Collections.sort(cars);
		return cars;
	}

	@RequestMapping({"/engines", "/{subpage}/engines"})
	@ResponseBody
	public List<Engine> engines() {
		final List<Engine> engines = IteratorUtils.toList(engineRepo.findAll().iterator());
		Collections.sort(engines);
		return engines;
	}

	@RequestMapping({"/event", "/{subpage}/event"})
	@ResponseBody
	public EventResult event(final int round) {
		final EventResult result = resultRepo.findByRound(round).get(0);
		result.setQualifyingOrder(sortPositions(result.getQualifyingOrder()));
		result.setRaceOrder(sortPositions(result.getRaceOrder()));
		return result;
	}

	private LinkedHashMap<String, Position> sortPositions(final Map<String, Position> map) {
		final Iterator<String> itr = map.keySet().iterator();
		final Map<Integer, String> positionSortedDrivers = new HashMap<Integer, String>();
		int last = 0;
		while(itr.hasNext()) {
			final String driverName = itr.next();
			final int position = map.get(driverName).getPosition();
			if(position > last) {
				last = position;
			}
			positionSortedDrivers.put(position, driverName);
		}

		final LinkedHashMap<String, Position> output = new LinkedHashMap<String, Position>();
		for(int i = 1; i <= last; i++) {
			final String driverName = positionSortedDrivers.get(i);
			output.put(driverName, map.get(driverName));
		}
		return output;
	}

	@RequestMapping({"/seasonstarted", "/{subpage}/seasonstarted"})
	@ResponseBody
	public boolean seasonStarted() {
		return teamService.seasonStarted();
	}

	@RequestMapping("/myaccount")
	public String myAccount() {
		return "myaccount";
	}

	@RequestMapping("/register")
	public String register() {
		return "register";
	}

	@RequestMapping("/myaccount/myteam")
	@ResponseBody
	public Team myTeam() {
		return teamRepo.findByEmail(SecurityContextHolder
			.getContext().getAuthentication().getName()).get(0);
	}

	@RequestMapping(value = "/myaccount/savemyteam", method = RequestMethod.POST)
	@ResponseBody
	public String saveTeam(@RequestBody final Team team) {
		try {
			teamService.saveTeam(team);
			return jsonMessage("Account updated successfully.");
		} catch (final ValidationException e) {
			return jsonMessage(e.getMessage());
		}
	}

	@RequestMapping(value = "/register/savemyteam", method = RequestMethod.POST)
	@ResponseBody
	public String registerTeam(@RequestBody final Team team) {
		try {
			teamService.saveTeam(team);
			return jsonMessage("Team registered successfully. You can modify your team by logging in and using 'My Account', until the start of the season.");
		} catch (final ValidationException e) {
			return jsonMessage(e.getMessage());
		}
	}

	private String jsonMessage(final String message) {
		return "{\"message\":\"" + message + "\"}";
	}

	@RequestMapping("/login")
	public String login() {
		return "login";
	}

	@RequestMapping(ERROR_PATH)
	public String error(final HttpServletRequest request, final Model model) {
		model.addAttribute("message",
				request.getAttribute("javax.servlet.error.message"));
		model.addAttribute("code",
				request.getAttribute("javax.servlet.error.status_code"));
		return "error";
	}

	@Override
	public String getErrorPath() {
		return ERROR_PATH;
	}

	@RequestMapping("loginError")
	public String loginError(final Model model) {
		model.addAttribute("message", "Invalid authentication credentials.");
		model.addAttribute("returnLocation", "/login");
		return "error";
	}

	@RequestMapping("accessError")
	public String accessError(final Model model) {
		model.addAttribute("message",
				"You don't have access to view this page.");
		model.addAttribute("returnLocation", "/login");
		SecurityContextHolder.getContext().setAuthentication(null);
		return "error";
	}

	private List<Team> maskPreSeasonTeams(final List<Team> teams) {
		for(final Team team : teams) {
			maskPreSeasonTeam(team);
		}
		return teams;
	}

	private Team maskPreSeasonTeam(final Team team) {
		if(!seasonStarted()) {
			team.setDrivers(new ArrayList<Driver>());
			team.setCar(null);
			team.setEngine(null);
		}
		return team;
	}
}
