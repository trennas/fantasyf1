package net.ddns.f1.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
import net.ddns.f1.service.impl.ValidationException;

@Profile({"!create", "!test"})
@Controller
public class MainController {

	private static final Logger LOG = Logger.getLogger(MainController.class);

	@Value("${season}")
	private int season;
	
	@Value("${auth.admin-role}")
	private String adminRole;

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

	@RequestMapping("/components")
	public String components() {
		return "components";
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

	@RequestMapping(value = { "/editresult/saveresult", "/addresult/saveresult" }, method = RequestMethod.POST)
	@ResponseBody
	public EventResult saveResult(@RequestBody final EventResult result) {
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
		final List<Driver> drivers = componentService.findAllDrivers();
		final List<String> remarks = new ArrayList<String>();
		remarks.add("This result was manually created");
		Collections.sort(drivers);
		for (final Driver driver : drivers) {
			order.put(driver.getName(),
					new Position(0, false, driver.getNumber()));
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

	@RequestMapping({"/refreshAllResults", "/{subpage}/refreshAllResults"})
	@ResponseBody
	public Boolean refreshAllResults() {
		eventService.refreshAllEvents();
		return true;
	}

	@RequestMapping({ "/teams", "/{subpage}/teams" })
	@ResponseBody
	public List<Team> getTeams() {
		return maskPreSeasonTeams(leagueService.calculateLeagueStandings());
	}

	@RequestMapping({ "/team", "/{subpage}/team" })
	@ResponseBody
	public Team getTeam(final Integer id) {
		return maskPreSeasonTeam(teamService.findById(id));
	}

	@RequestMapping({ "/besttheoreticalteam" })
	@ResponseBody
	public TheoreticalTeam getBestTheoreticalTeam() {
		return teamService.findTheoreticalTeamByName(bestTheoreticalTeamName);
	}

	@RequestMapping({ "/race/besttheoreticalteamforround" })
	@ResponseBody
	public TheoreticalTeam getBestTheoreticalTeamForRound(final Integer round) {
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
	public int deleteEvent(final int round) {
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
	public List<Driver> drivers() {
		return drivers(false);
	}

	@RequestMapping({ "/alldrivers", "/{subpage}/alldrivers" })
	@ResponseBody
	public List<Driver> allDrivers() {
		return drivers(true);
	}

	private List<Driver> drivers(final boolean includeStandin) {
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
	public Driver driver(final Integer number) {
		return componentService.findDriverByNumber(number);
	}
	
	@RequestMapping({ "/driverbyname", "/{subpage}/driverbyname" })
	@ResponseBody
	public Driver driver(final String name) {
		return componentService.findDriverByName(name);
	}

	@RequestMapping({ "/car", "/{subpage}/car" })
	@ResponseBody
	public Car car(final String name) {
		return componentService.findCarByName(name);
	}

	@RequestMapping({ "/engine", "/{subpage}/engine" })
	@ResponseBody
	public Engine engine(final String name) {
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
	public EventResult event(final int round) {
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
	public Team myTeam() {
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

	@RequestMapping(value = "/components/savedrivers", method = RequestMethod.POST)
	@ResponseBody
	public boolean saveDrivers(@RequestBody final List<Driver> drivers) {
		componentService.saveDrivers(drivers);
		return true;
	}

	@RequestMapping(value = "/components/savecars", method = RequestMethod.POST)
	@ResponseBody
	public boolean saveCars(@RequestBody final List<Car> cars) {
		componentService.saveCars(cars);
		return true;
	}

	@RequestMapping(value = "/components/saveengines", method = RequestMethod.POST)
	@ResponseBody
	public boolean saveEngines(@RequestBody final List<Engine> engines) {
		componentService.saveEngines(engines);
		return true;
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

	private List<Team> maskPreSeasonTeams(final List<Team> teams) {
		for (final Team team : teams) {
			maskPreSeasonTeam(team);
		}
		return teams;
	}

	private Team maskPreSeasonTeam(final Team team) {
		if (!isAdmin()) {
			if (!seasonStarted()) {
				team.setDrivers(new ArrayList<Driver>());
				team.setCar(null);
				team.setEngine(null);
			}
			team.setPassword(null);
		}
		return team;
	}
	
	private boolean isAdmin() {
		Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		for(GrantedAuthority authority : authorities) {
			if(authority.getAuthority().equals(adminRole)) {
				return true;
			}
		}
		return false;
	}
}
