package net.ddns.f1.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.ddns.f1.domain.Car;
import net.ddns.f1.domain.Driver;
import net.ddns.f1.domain.Engine;
import net.ddns.f1.domain.EventResult;
import net.ddns.f1.domain.Position;
import net.ddns.f1.domain.Team;
import net.ddns.f1.repository.CarRepository;
import net.ddns.f1.repository.DriverRepository;
import net.ddns.f1.repository.EngineRepository;
import net.ddns.f1.repository.EventResultRepository;
import net.ddns.f1.repository.TeamRepository;
import net.ddns.f1.service.impl.EventServiceImpl;
import net.ddns.f1.service.impl.LeagueServiceImpl;
import net.ddns.f1.service.impl.TeamService;

import org.apache.commons.collections4.IteratorUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
	@Autowired
	private DriverRepository driverRepo;
	@Autowired
	private CarRepository carRepo;
	@Autowired
	private EngineRepository engineRepo;
	@Autowired
	private EventServiceImpl eventService;
	
	@RequestMapping("/")
	public String mainPage() {
		return "league";
	}
	
	@RequestMapping("/race")
	public String mainPage(Integer round) {
		return "race";
	}
	
	@RequestMapping("/editresult")
	public String editResult(Integer round) {
		return "editresult";
	}
	
	@RequestMapping(value = {"/saveResult", "/{subpage}/saveResult"}, method = RequestMethod.POST)
	@ResponseBody
	public Boolean saveResult(@RequestBody EventResult result) {
		result.setFastestLapDriver(driverRepo.findByName(result.getFastestLapDriver().getName()).get(0));
		resultRepo.save(result);
		leagueService.recalculateAllResults();
		return true;
	}
	
	@RequestMapping({"/teams", "/{subpage}/teams"})
	@ResponseBody
	public List<Team> getTeams() {
		return leagueService.calculateLeagueStandings();
	}
	
	@RequestMapping("/team")
	@ResponseBody
	public Team getTeam(Integer id) {
		return teamRepo.findById(id).get(0);
	}
	
	@RequestMapping("/events")
	@ResponseBody
	public List<EventResult> events() {
		return eventService.getSeasonResults();
	}
	
	@RequestMapping({"/drivers", "/{subpage}/drivers"})
	@ResponseBody
	public List<Driver> drivers() {
		List<Driver> drivers = driverRepo.findByStandin(false);
		Collections.sort(drivers);
		return drivers;
	}
	
	@RequestMapping({"/driver", "/{subpage}/driver"})
	@ResponseBody
	public Driver driver(String name) {
		return driverRepo.findByName(name).get(0);
	}
	
	@RequestMapping({"/car", "/{subpage}/car"})
	@ResponseBody
	public Car car(String name) {
		return carRepo.findByName(name).get(0);
	}
	
	@RequestMapping({"/engine", "/{subpage}/engine"})
	@ResponseBody
	public Engine engine(String name) {
		return engineRepo.findByName(name).get(0);
	}

	@RequestMapping({"/cars", "/{subpage}/cars"})
	@ResponseBody
	public List<Car> cars() {
		List<Car> cars = IteratorUtils.toList(carRepo.findAll().iterator());
		Collections.sort(cars);
		return cars;
	}
	
	@RequestMapping({"/engines", "/{subpage}/engines"})
	@ResponseBody
	public List<Engine> engines() {
		List<Engine> engines = IteratorUtils.toList(engineRepo.findAll().iterator());
		Collections.sort(engines);
		return engines;
	}
	
	@RequestMapping({"/event", "/{subpage}/event"})
	@ResponseBody
	public EventResult event(int round) {		
		EventResult result = resultRepo.findByRound(round).get(0);
		result.setQualifyingOrder(sortPositions(result.getQualifyingOrder()));
		result.setRaceOrder(sortPositions(result.getRaceOrder()));
		return result;		
	}
	
	private LinkedHashMap<String, Position> sortPositions(Map<String, Position> map) {		
		Iterator<String> itr = map.keySet().iterator();
		Map<Integer, String> positionSortedDrivers = new HashMap<Integer, String>();
		int last = 0;
		while(itr.hasNext()) {
			String driverName = itr.next();
			int position = map.get(driverName).getPosition();
			if(position > last) {
				last = position;
			}
			positionSortedDrivers.put(position, driverName);
		}
				
		LinkedHashMap<String, Position> output = new LinkedHashMap<String, Position>();
		for(int i = 1; i <= last; i++) {
			String driverName = positionSortedDrivers.get(i);
			output.put(driverName, map.get(driverName));
		}
		return output;
	}
	
	@RequestMapping("/login")
	public String login() {
		return "login";
	}
}
