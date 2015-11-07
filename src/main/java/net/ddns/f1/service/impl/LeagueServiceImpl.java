package net.ddns.f1.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.ddns.f1.domain.Car;
import net.ddns.f1.domain.Driver;
import net.ddns.f1.domain.Engine;
import net.ddns.f1.domain.Position;
import net.ddns.f1.domain.EventResult;
import net.ddns.f1.domain.StandInDriver;
import net.ddns.f1.domain.Team;
import net.ddns.f1.repository.CarRepository;
import net.ddns.f1.repository.DriverRepository;
import net.ddns.f1.repository.EngineRepository;
import net.ddns.f1.repository.StandInDriverRepository;
import net.ddns.f1.repository.TeamRepository;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LeagueServiceImpl {
	
	private static final Logger LOG = Logger
			.getLogger(LeagueServiceImpl.class);
	
	@Autowired
	TeamService teamService;

	@Autowired
	TeamRepository teamRepo;
	
	@Autowired
	CarRepository carRepo;
	
	@Autowired
	EngineRepository engineRepo;
	
	@Autowired
	DriverRepository driverRepo;
	
	@Autowired
	StandInDriverRepository standInDriverRepo;

	@Autowired
	EventServiceImpl eventService;

	public List<Team> calculateLeagueStandings() {
		List<Team> teams = teamService.getAllTeams();		

		if(eventService.checkForNewResults()) {			
			calculateAllResults(teams);
		}
		
		Collections.sort(teams);		
		return teams;
	}
	
	private synchronized void calculateAllResults(List<Team> teams) {
		LOG.info("Recalculating scores...");
		List<EventResult> results = eventService.getSeasonResults();
		for(Team team : teams) {
			team.setPointsPerEvent(new HashMap<Integer, Integer>());
			team.setTotalPoints(0);
		}
		for(EventResult result : results) {
			calculateResult(result, teams);
		}
		LOG.info("Scores recalculated.");
	}
	
	private synchronized void calculateResult(EventResult result, List<Team> teams) {
		Iterator<Driver> driversItr = driverRepo.findAll().iterator();
		
		while(driversItr.hasNext()) {
			int points = 0;
			Driver driver = driversItr.next();
			Position pos = result.getQualifyingOrder().get(driver.getName());
			if(pos != null) {
				if(pos.isClassified()) {
					points += DRIVER_QUAL_POINTS.get(pos.getPosition());
				}
			} else {
				// Has the driver been replaced?
				List<StandInDriver> standInDrivers = standInDriverRepo.findByRoundAndStandingInFor(result.getRound(), driver);
				if(standInDrivers.size() == 1) {						
					pos = result.getQualifyingOrder().get(standInDrivers.get(0).getStandInDriver().getName());
					if(pos != null) {
						if(pos.isClassified()) {
							points += DRIVER_QUAL_POINTS.get(pos.getPosition());
						}
					}
				}
			}
			
			pos = result.getRaceOrder().get(driver.getName());
			if(pos != null) {
				if(pos.isClassified()) {
					points += DRIVER_RACE_POINTS.get(pos.getPosition());
				}
				if(driver.equals(result.getFastestLapDriver())) {
					points += FASTEST_LAP_BONUS;
				}
			} else {
				// Has the driver been replaced?
				List<StandInDriver> standInDrivers = standInDriverRepo.findByRoundAndStandingInFor(result.getRound(), driver);
				if(standInDrivers.size() == 1) {						
					pos = result.getRaceOrder().get(standInDrivers.get(0).getStandInDriver().getName());
					if(pos != null) {
						if(pos.isClassified()) {
							points += DRIVER_RACE_POINTS.get(pos.getPosition());
						}
						if(standInDrivers.get(0).getStandInDriver().equals(result.getFastestLapDriver())) {
							points += FASTEST_LAP_BONUS;
						}
					}
				}
			}
			driver.getPointsPerEvent().put(result.getRound(), points);
			driver.setTotalPoints(driver.getTotalPoints() + points);
		}
		
		Iterator<Car> carItr = carRepo.findAll().iterator();
		while(carItr.hasNext()) {			
			Car car = carItr.next();
			int points = 0;
			List<Driver> carDrivers = driverRepo.findByCar(car);
			boolean bothCarsFinished = true;
			
			for(Driver driver : carDrivers) {
				Position pos = result.getQualifyingOrder().get(driver.getName());
				if(pos != null) {
					if(pos.isClassified()) {
						points += CAR_QUAL_POINTS.get(pos.getPosition());
					}
				}
				pos = result.getRaceOrder().get(driver.getName());
				if(pos != null) {
					if(pos.isClassified()) {
						points += CAR_RACE_POINTS.get(pos.getPosition());
					} else {
						bothCarsFinished = false;
					}
				}
			}
			if(bothCarsFinished) {
				points += BOTH_CARS_FINISHED_BONUS;
			}
			car.getPointsPerEvent().put(result.getRound(), points);
			car.setTotalPoints(car.getTotalPoints() + points);
		}
		
		Iterator<Engine> engineItr = engineRepo.findAll().iterator();
		while(engineItr.hasNext()) {			
			Engine engine = engineItr.next();
			
			List<Car> carsUsingEngine = carRepo.findByEngine(engine);
			List<Driver> engineDrivers = new ArrayList<Driver>();
			for(Car car : carsUsingEngine) {
				List<Driver> drivers = driverRepo.findByCar(car);
				for(Driver driver : drivers) {
					engineDrivers.add(driver);
				}
			}
			
			int points = 0;
			
			for(Driver driver : engineDrivers) {
				Position pos = result.getQualifyingOrder().get(driver.getName());
				if(pos != null) {
					if(pos.isClassified()) {
						points += ENGINE_QUAL_POINTS.get(pos.getPosition());
					}
				}
				
				pos = result.getRaceOrder().get(driver.getName());
				if(pos != null) {
					if(pos.isClassified()) {
						points += ENGINE_RACE_POINTS.get(pos.getPosition());
					}
				}
			}
			engine.getPointsPerEvent().put(result.getRound(), points);
			engine.setTotalPoints(engine.getTotalPoints() + points);			
		}
		
		for(Team team : teams) {
			int points = 0;
			points += team.getDrivers().get(0).getPointsPerEvent().get(result.getRound());
			points += team.getDrivers().get(1).getPointsPerEvent().get(result.getRound());
			points += team.getCar().getPointsPerEvent().get(result.getRound());
			points += team.getEngine().getPointsPerEvent().get(result.getRound());
			team.getPointsPerEvent().put(result.getRound(), points);
			team.setTotalPoints(team.getTotalPoints() + points);
			teamRepo.save(team);
		}
	}
	
	private static final Integer FASTEST_LAP_BONUS = 50;
	private static final Integer BOTH_CARS_FINISHED_BONUS = 50;
	
	private static final Map<Integer, Integer> DRIVER_QUAL_POINTS = new HashMap<Integer, Integer>();
	static {
		DRIVER_QUAL_POINTS.put(1, 200);
		DRIVER_QUAL_POINTS.put(2, 160);
		DRIVER_QUAL_POINTS.put(3, 128);
		DRIVER_QUAL_POINTS.put(4, 104);
		DRIVER_QUAL_POINTS.put(5, 88);
		DRIVER_QUAL_POINTS.put(6, 80);
		DRIVER_QUAL_POINTS.put(7, 72);
		DRIVER_QUAL_POINTS.put(8, 68);
		DRIVER_QUAL_POINTS.put(9, 64);
		DRIVER_QUAL_POINTS.put(10, 60);
		DRIVER_QUAL_POINTS.put(11, 56);
		DRIVER_QUAL_POINTS.put(12, 52);
		DRIVER_QUAL_POINTS.put(13, 48);
		DRIVER_QUAL_POINTS.put(14, 44);
		DRIVER_QUAL_POINTS.put(15, 40);
		DRIVER_QUAL_POINTS.put(16, 36);
		DRIVER_QUAL_POINTS.put(17, 32);
		DRIVER_QUAL_POINTS.put(18, 28);
		DRIVER_QUAL_POINTS.put(19, 24);
		DRIVER_QUAL_POINTS.put(20, 20);
	}
	
	private static final Map<Integer, Integer> DRIVER_RACE_POINTS = new HashMap<Integer, Integer>();
	static {
		DRIVER_RACE_POINTS.put(1, 500);
		DRIVER_RACE_POINTS.put(2, 400);
		DRIVER_RACE_POINTS.put(3, 320);
		DRIVER_RACE_POINTS.put(4, 260);
		DRIVER_RACE_POINTS.put(5, 220);
		DRIVER_RACE_POINTS.put(6, 200);
		DRIVER_RACE_POINTS.put(7, 180);
		DRIVER_RACE_POINTS.put(8, 170);
		DRIVER_RACE_POINTS.put(9, 160);
		DRIVER_RACE_POINTS.put(10, 150);
		DRIVER_RACE_POINTS.put(11, 140);
		DRIVER_RACE_POINTS.put(12, 130);
		DRIVER_RACE_POINTS.put(13, 120);
		DRIVER_RACE_POINTS.put(14, 110);
		DRIVER_RACE_POINTS.put(15, 100);
		DRIVER_RACE_POINTS.put(16, 90);
		DRIVER_RACE_POINTS.put(17, 80);
		DRIVER_RACE_POINTS.put(18, 70);
		DRIVER_RACE_POINTS.put(19, 60);
		DRIVER_RACE_POINTS.put(20, 50);
	}
	
	private static final Map<Integer, Integer> CAR_QUAL_POINTS = new HashMap<Integer, Integer>();
	static {
		CAR_QUAL_POINTS.put(1, 100);
		CAR_QUAL_POINTS.put(2, 80);
		CAR_QUAL_POINTS.put(3, 64);
		CAR_QUAL_POINTS.put(4, 52);
		CAR_QUAL_POINTS.put(5, 44);
		CAR_QUAL_POINTS.put(6, 40);
		CAR_QUAL_POINTS.put(7, 36);
		CAR_QUAL_POINTS.put(8, 34);
		CAR_QUAL_POINTS.put(9, 32);
		CAR_QUAL_POINTS.put(10, 30);
		CAR_QUAL_POINTS.put(11, 28);
		CAR_QUAL_POINTS.put(12, 26);
		CAR_QUAL_POINTS.put(13, 24);
		CAR_QUAL_POINTS.put(14, 22);
		CAR_QUAL_POINTS.put(15, 20);
		CAR_QUAL_POINTS.put(16, 18);
		CAR_QUAL_POINTS.put(17, 16);
		CAR_QUAL_POINTS.put(18, 14);
		CAR_QUAL_POINTS.put(19, 12);
		CAR_QUAL_POINTS.put(20, 10);
	}
	
	private static final Map<Integer, Integer> CAR_RACE_POINTS = new HashMap<Integer, Integer>();
	static {
		CAR_RACE_POINTS.put(1, 200);
		CAR_RACE_POINTS.put(2, 160);
		CAR_RACE_POINTS.put(3, 128);
		CAR_RACE_POINTS.put(4, 104);
		CAR_RACE_POINTS.put(5, 88);
		CAR_RACE_POINTS.put(6, 80);
		CAR_RACE_POINTS.put(7, 72);
		CAR_RACE_POINTS.put(8, 68);
		CAR_RACE_POINTS.put(9, 64);
		CAR_RACE_POINTS.put(10, 60);
		CAR_RACE_POINTS.put(11, 56);
		CAR_RACE_POINTS.put(12, 52);
		CAR_RACE_POINTS.put(13, 48);
		CAR_RACE_POINTS.put(14, 44);
		CAR_RACE_POINTS.put(15, 40);
		CAR_RACE_POINTS.put(16, 36);
		CAR_RACE_POINTS.put(17, 32);
		CAR_RACE_POINTS.put(18, 28);
		CAR_RACE_POINTS.put(19, 24);
		CAR_RACE_POINTS.put(20, 20);
	}
	
	private static final Map<Integer, Integer> ENGINE_QUAL_POINTS = new HashMap<Integer, Integer>();
	static {
		ENGINE_QUAL_POINTS.put(1, 50);
		ENGINE_QUAL_POINTS.put(2, 40);
		ENGINE_QUAL_POINTS.put(3, 32);
		ENGINE_QUAL_POINTS.put(4, 26);
		ENGINE_QUAL_POINTS.put(5, 22);
		ENGINE_QUAL_POINTS.put(6, 20);
		ENGINE_QUAL_POINTS.put(7, 18);
		ENGINE_QUAL_POINTS.put(8, 17);
		ENGINE_QUAL_POINTS.put(9, 16);
		ENGINE_QUAL_POINTS.put(10, 15);
		ENGINE_QUAL_POINTS.put(11, 14);
		ENGINE_QUAL_POINTS.put(12, 13);
		ENGINE_QUAL_POINTS.put(13, 12);
		ENGINE_QUAL_POINTS.put(14, 11);
		ENGINE_QUAL_POINTS.put(15, 10);
		ENGINE_QUAL_POINTS.put(16, 9);
		ENGINE_QUAL_POINTS.put(17, 8);
		ENGINE_QUAL_POINTS.put(18, 7);
		ENGINE_QUAL_POINTS.put(19, 6);
		ENGINE_QUAL_POINTS.put(20, 5);
	}
	
	private static final Map<Integer, Integer> ENGINE_RACE_POINTS = new HashMap<Integer, Integer>();
	static {
		ENGINE_RACE_POINTS.put(1, 100);
		ENGINE_RACE_POINTS.put(2, 80);
		ENGINE_RACE_POINTS.put(3, 64);
		ENGINE_RACE_POINTS.put(4, 52);
		ENGINE_RACE_POINTS.put(5, 44);
		ENGINE_RACE_POINTS.put(6, 40);
		ENGINE_RACE_POINTS.put(7, 36);
		ENGINE_RACE_POINTS.put(8, 34);
		ENGINE_RACE_POINTS.put(9, 32);
		ENGINE_RACE_POINTS.put(10, 30);
		ENGINE_RACE_POINTS.put(11, 28);
		ENGINE_RACE_POINTS.put(12, 26);
		ENGINE_RACE_POINTS.put(13, 24);
		ENGINE_RACE_POINTS.put(14, 22);
		ENGINE_RACE_POINTS.put(15, 20);
		ENGINE_RACE_POINTS.put(16, 18);
		ENGINE_RACE_POINTS.put(17, 16);
		ENGINE_RACE_POINTS.put(18, 14);
		ENGINE_RACE_POINTS.put(19, 12);
		ENGINE_RACE_POINTS.put(20, 10);
	}
}
