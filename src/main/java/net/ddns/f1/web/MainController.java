package net.ddns.f1.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
import net.ddns.f1.domain.Rules;
import net.ddns.f1.domain.Team;
import net.ddns.f1.domain.TheoreticalTeam;
import net.ddns.f1.service.ComponentService;
import net.ddns.f1.service.EventService;
import net.ddns.f1.service.LeagueService;
import net.ddns.f1.service.TeamService;
import net.ddns.f1.service.impl.Ff1Exception;
import net.ddns.f1.service.impl.ValidationException;

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

	private static final Logger LOG = Logger.getLogger(MainController.class);

	@Value("${season}")
	private int season;

	@Value("${best-theoretical-team-name}")
	private String bestTheoreticalTeamName;

	@Autowired
	private ComponentService componentService;
	@Autowired
	private LeagueService leagueService;
	@Autowired
	private TeamService teamService;
	@Autowired
	private EventService eventService;

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
	public int updateResults() throws Ff1Exception {
		final int res = eventService.updateResults();
		LOG.info("updateresults: " + res);
		return res;
	}

	@RequestMapping(value = { "/editresult/saveresult", "/addresult/saveresult" }, method = RequestMethod.POST)
	@ResponseBody
	public EventResult saveResult(@RequestBody final EventResult result)
			throws Ff1Exception {
		result.setFastestLapDriver(componentService.findDriverByName(result
				.getFastestLapDriver().getName()));
		eventService.save(result);
		leagueService.recalculateAllResults();
		final EventResult savedResult = eventService.findByRound(result
				.getRound());
		savedResult.setRemarks(result.getRemarks());
		eventService.save(savedResult);
		return event(savedResult.getRound());
	}

	@RequestMapping({ "/addresult/newevent" })
	@ResponseBody
	public EventResult createEvent() {
		final EventResult result = new EventResult();
		final Map<String, Position> order = new HashMap<String, Position>();
		final Position pos = new Position(0, false);
		final List<Driver> drivers = componentService.findAllDrivers();
		final List<String> remarks = new ArrayList<String>();
		remarks.add("This result was manually created");
		Collections.sort(drivers);
		for (final Driver driver : drivers) {
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
	public EventResult refreshResult(final int round) throws Ff1Exception {
		return eventService.refreshEvent(round);
	}

	@RequestMapping("/refreshAllResults")
	@ResponseBody
	public Boolean refreshAllResults() throws Ff1Exception {
		eventService.refreshAllEvents();
		return true;
	}

	@RequestMapping({ "/teams", "/{subpage}/teams" })
	@ResponseBody
	public List<Team> getTeams() throws Ff1Exception {
		return maskPreSeasonTeams(leagueService.calculateLeagueStandings());
	}

	@RequestMapping({ "/team", "/{subpage}/team" })
	@ResponseBody
	public Team getTeam(final Integer id) throws Ff1Exception {
		return maskPreSeasonTeam(teamService.findById(id));
	}

	@RequestMapping({ "/besttheoreticalteam" })
	@ResponseBody
	public TheoreticalTeam getBestTheoreticalTeam() throws Ff1Exception {
		return teamService.findTheoreticalTeamByName(bestTheoreticalTeamName);
	}

	@RequestMapping({ "/race/besttheoreticalteamforround" })
	@ResponseBody
	public TheoreticalTeam getBestTheoreticalTeamForRound(final Integer round)
			throws Ff1Exception {
		final EventResult result = eventService.findByRound(round);
		return result.getBestTheoreticalTeam();
	}

	@RequestMapping("/events")
	@ResponseBody
	public List<EventResult> events() {
		return eventService.getSeasonResults();
	}

	@RequestMapping("/editresult/deleteevent")
	@ResponseBody
	public int deleteEvent(final int round) throws Ff1Exception {
		return eventService.deleteEvent(round);
	}

	@RequestMapping("/deleteteam")
	@ResponseBody
	public int deleteTeam(final int id) {
		teamService.delete(id);
		return 1;
	}

	@RequestMapping({ "/drivers", "/{subpage}/drivers" })
	@ResponseBody
	public List<Driver> drivers() throws Ff1Exception {
		return drivers(false);
	}

	@RequestMapping({ "/alldrivers", "/{subpage}/alldrivers" })
	@ResponseBody
	public List<Driver> allDrivers() throws Ff1Exception {
		return drivers(true);
	}

	private List<Driver> drivers(final boolean includeStandin)
			throws Ff1Exception {
		List<Driver> drivers;
		if (includeStandin) {
			drivers = componentService.findAllDrivers();
		} else {
			drivers = componentService.findDriversByStandin(false);
		}
		Collections.sort(drivers);
		return drivers;
	}

	@RequestMapping({ "/driver", "/{subpage}/driver" })
	@ResponseBody
	public Driver driver(final String name) throws Ff1Exception {
		return componentService.findDriverByName(name);
	}

	@RequestMapping({ "/car", "/{subpage}/car" })
	@ResponseBody
	public Car car(final String name) throws Ff1Exception {
		return componentService.findCarByName(name);
	}

	@RequestMapping({ "/engine", "/{subpage}/engine" })
	@ResponseBody
	public Engine engine(final String name) throws Ff1Exception {
		return componentService.findEngineByName(name);
	}

	@RequestMapping({ "/cars", "/{subpage}/cars" })
	@ResponseBody
	public List<Car> cars() {
		final List<Car> cars = componentService.findAllCars();
		Collections.sort(cars);
		return cars;
	}

	@RequestMapping({ "/engines", "/{subpage}/engines" })
	@ResponseBody
	public List<Engine> engines() {
		final List<Engine> engines = componentService.findAllEngines();
		Collections.sort(engines);
		return engines;
	}

	@RequestMapping({ "/event", "/{subpage}/event" })
	@ResponseBody
	public EventResult event(final int round) throws Ff1Exception {
		final EventResult result = eventService.findByRound(round);
		result.setQualifyingOrder(sortPositions(result.getQualifyingOrder()));
		result.setRaceOrder(sortPositions(result.getRaceOrder()));
		return result;
	}

	private LinkedHashMap<String, Position> sortPositions(
			final Map<String, Position> map) {
		final Iterator<String> itr = map.keySet().iterator();
		final Map<Integer, String> positionSortedDrivers = new HashMap<Integer, String>();
		int last = 0;
		while (itr.hasNext()) {
			final String driverName = itr.next();
			final int position = map.get(driverName).getPosition();
			if (position > last) {
				last = position;
			}
			positionSortedDrivers.put(position, driverName);
		}

		final LinkedHashMap<String, Position> output = new LinkedHashMap<String, Position>();
		for (int i = 1; i <= last; i++) {
			final String driverName = positionSortedDrivers.get(i);
			output.put(driverName, map.get(driverName));
		}
		return output;
	}

	@RequestMapping({ "/seasonstarted", "/{subpage}/seasonstarted" })
	@ResponseBody
	public boolean seasonStarted() {
		return leagueService.seasonStarted();
	}

	@RequestMapping({ "/seasonstartdate", "/{subpage}/seasonstartdate" })
	@ResponseBody
	public Date seasonStartDate() {
		return leagueService.getSeasonStartDate();
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
	public Team myTeam() throws Ff1Exception {
		return teamService.findByEmail(SecurityContextHolder.getContext()
				.getAuthentication().getName());
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

	@RequestMapping("rules")
	public String rules() {
		return "rules";
	}

	@RequestMapping({ "/getRules", "/{subpage}/getRules" })
	@ResponseBody
	public Rules getRules() {
		return leagueService.getRules();
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
		for (final Team team : teams) {
			maskPreSeasonTeam(team);
		}
		return teams;
	}

	private Team maskPreSeasonTeam(final Team team) {
		if (!seasonStarted()) {
			team.setDrivers(new ArrayList<Driver>());
			team.setCar(null);
			team.setEngine(null);
		}
		team.setPassword(null);
		return team;
	}
}
