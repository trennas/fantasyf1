package fantasyf1.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import fantasyf1.domain.Car;
import fantasyf1.domain.Driver;
import fantasyf1.domain.Engine;
import fantasyf1.domain.EventResult;
import fantasyf1.domain.PointScorer;
import fantasyf1.domain.Position;
import fantasyf1.domain.Rules;
import fantasyf1.domain.Team;
import fantasyf1.domain.TheoreticalTeam;
import fantasyf1.service.ComponentService;
import fantasyf1.service.EventService;
import fantasyf1.service.LeagueService;
import fantasyf1.service.TeamService;

@Service
public class LeagueServiceImpl implements LeagueService {

	private static final Logger LOG = Logger.getLogger(LeagueServiceImpl.class);

	@Value("#{new java.text.SimpleDateFormat(\"${dateFormat}\").parse(\"${season-start-date-time}\")}")
	private Date seasonStartDateTime;

	@Value("${refresh-results-on-page-load}")
	private boolean refreshResultsOnPageLoad;

	@Value("${best-theoretical-team-name}")
	private String bestTheoreticalTeamName;

	@Autowired
	TeamService teamService;

	@Autowired
	Rules rules;

	@Autowired
	ComponentService componentService;

	@Autowired
	EventService eventService;

	@Override
	public List<Team> calculateLeagueStandings() {
		final List<Team> teams = teamService.findAll();

		if (refreshResultsOnPageLoad && eventService.checkForNewResults(true)) {
			calculateAllResults(teams);
		}

		Collections.sort(teams);
		return teams;
	}

	@Override
	public synchronized void recalculateAllResults() {
		calculateAllResults(teamService.findAll());
	}

	private synchronized void calculateAllResults(final List<Team> teams) {
		LOG.info("Recalculating scores...");
		final List<EventResult> results = eventService.getSeasonResults();
		resetAllScores(teams);
		for (final EventResult result : results) {
			calculateResult(result, teams);
		}
		LOG.info("Scores recalculated.");
	}

	private void calculateBestTheoreticalTeam(final EventResult result) {
		final List<Driver> allDrivers = componentService
				.findDriversByStandin(false);
		final List<Car> cars = componentService.findAllCars();
		final List<Engine> engines = componentService.findAllEngines();

		TheoreticalTeam bestTeamForRound;

		if(result.getBestTheoreticalTeam() != null) {
			bestTeamForRound = result.getBestTheoreticalTeam();
			bestTeamForRound.reset();
		} else {
			bestTeamForRound = new TheoreticalTeam();
		}

		bestTeamForRound.setName("Best Theoretical Team For Round "
				+ result.getRound());

		result.setBestTheoreticalTeam(bestTeamForRound);

		TheoreticalTeam bestOverallTeam;
		final TheoreticalTeam res = teamService
				.findTheoreticalTeamByName(bestTheoreticalTeamName);

		if (res == null) {
			bestOverallTeam = new TheoreticalTeam();
			bestOverallTeam.setName(bestTheoreticalTeamName);
		} else {
			bestOverallTeam = res;
		}

		long roundHighScore = 0;
		long totalHighScore = bestOverallTeam.getPoints() == null ? 0
				: bestOverallTeam.getPoints();

		for (final Driver driver1 : allDrivers) {
			final Team team = new Team();
			for (final Driver driver2 : allDrivers) {
				for (final Driver driver3 : allDrivers) {
					if (driver1.getNumber() != driver2.getNumber() && driver1.getNumber() != driver3.getNumber() && driver2.getNumber() != driver3.getNumber()) {
						team.setDrivers(new ArrayList<Driver>());
						team.getDrivers().add(driver1);
						team.getDrivers().add(driver2);
						team.getDrivers().add(driver3);
						for (final Car car : cars) {
							team.setCar(car);
							for (final Engine engine : engines) {
								team.setEngine(engine);
								try {
									teamService.validateTeamComponents(team);
									final long roundScore = calculateRoundScore(
											result.getRound(), team);
									final long totalScore = calculateTotalScore(team);

									if (roundScore > roundHighScore) {
										roundHighScore = roundScore;
										bestTeamForRound.setComponents(team);

										bestTeamForRound
												.getDrivers()
												.get(0)
												.setPoints(
														(long) team
																.getDrivers()
																.get(0)
																.getPointsPerEvent()
																.get(result.getRound()));
										bestTeamForRound
												.getDrivers()
												.get(1)
												.setPoints(
														(long) team
																.getDrivers()
																.get(1)
																.getPointsPerEvent()
																.get(result.getRound()));

										bestTeamForRound
										.getDrivers()
										.get(2)
										.setPoints(
												(long) team
														.getDrivers()
														.get(2)
														.getPointsPerEvent()
														.get(result.getRound()));
										bestTeamForRound.getCar().setPoints(
												(long) team.getCar()
														.getPointsPerEvent()
														.get(result.getRound()));
										bestTeamForRound.getEngine().setPoints(
												(long) team.getEngine()
														.getPointsPerEvent()
														.get(result.getRound()));

										bestTeamForRound.setPoints(roundScore);
									}
									if (totalScore > totalHighScore) {
										totalHighScore = totalScore;
										bestOverallTeam.setComponents(team);
										bestOverallTeam.setPoints(totalScore);
									}
								} catch (final ValidationException e) {
									continue;
								}
							}
						}
					}
				}
			}
		}
		teamService.saveTheoreticalTeam(bestOverallTeam);
		eventService.save(result);
	}

	private long calculateRoundScore(final int round, final Team team) {
		long score = 0;
		for(final Driver driver : team.getDrivers()) {
			score += driver.getPointsPerEvent().get(round);
		}
		score += team.getCar().getPointsPerEvent().get(round)
			  + team.getEngine().getPointsPerEvent().get(round);
		return score;
	}

	private long calculateTotalScore(final Team team) {
		long score = 0;
		for(final Driver driver : team.getDrivers()) {
			score += driver.getTotalPoints();
		}
		score +=  team.getCar().getTotalPoints()
			  + team.getEngine().getTotalPoints();
		return score;
	}

	private void resetAllScores(final List<Team> teams) {
		for (final Team team : teams) {
			resetPointsScorer(team);
			teamService.saveTeamNoValidation(team);
		}
		final List<Driver> drivers = componentService
				.findAllDrivers();
		for (final Driver driver : drivers) {
			driver.setFastestLaps(0);
			resetPointsScorer(driver);
			componentService.saveDriver(driver);
		}

		final List<Car> cars = componentService.findAllCars();
		for (final Car car : cars) {
			car.setBothCarsFinishBonuses(0);
			resetPointsScorer(car);
			componentService.saveCar(car);
		}

		final List<Engine> engines = componentService.findAllEngines();
		for (final Engine engine : engines) {
			resetPointsScorer(engine);
			componentService.saveEngine(engine);
		}
		teamService.resetBestTheoreticalTeam();
	}

	private void resetPointsScorer(final PointScorer scorer) {
		scorer.setTotalPoints(0);
		scorer.setPointsPerEvent(new LinkedHashMap<Integer, Integer>());
	}

	private synchronized void calculateResult(final EventResult result,
			final List<Team> teams) {
		final List<Driver> drivers = componentService.findDriversByStandin(false);
		final List<Driver> standinDrivers = componentService.findDriversByStandin(true);
		final List<Car> cars = componentService.findAllCars();
		final List<Engine> engines = componentService.findAllEngines();
		
		Map<Integer, Driver> driverMap =
				drivers.stream().collect(Collectors.toMap(Driver::getNumber, Function.identity()));
		Map<Integer, Driver> standinDriverMap =
				standinDrivers.stream().collect(Collectors.toMap(Driver::getNumber, Function.identity()));
		Map<String, Car> carMap =
				cars.stream().collect(Collectors.toMap(Car::getName, Function.identity()));
		Map<String, Engine> engineMap =
				engines.stream().collect(Collectors.toMap(Engine::getName, Function.identity()));
		
		final Map <String, Integer> numCarsParticipated = new HashMap<>();
		final Map <String, Integer> numCarsFinished = new HashMap<>();

	    for(final Position pos : result.getQualifyingOrder().values()) {
	    	Car car = carMap.get(pos.getCarName());
			Engine engine = engineMap.get(car.getEngine().getName());
			Driver driver = getDriver(pos, driverMap, standinDriverMap, result, Session.QUALIFYING);
			
			if (pos.isClassified()) {
				add(driver.getPointsPerEvent(), result.getRound(), rules.getDriverQualPoints().get(pos.getPosition()));
				driver.setTotalPoints(driver.getTotalPoints() + rules.getDriverQualPoints().get(pos.getPosition()));
				add(car.getPointsPerEvent(), result.getRound(), rules.getCarQualPoints().get(pos.getPosition()));				
				car.setTotalPoints(car.getTotalPoints() + rules.getCarQualPoints().get(pos.getPosition()));				
				add(engine.getPointsPerEvent(), result.getRound(), rules.getEngineQualPoints().get(pos.getPosition()));
				engine.setTotalPoints(engine.getTotalPoints() + rules.getEngineQualPoints().get(pos.getPosition()));
			}
	    }

	    if (result.isRaceComplete()) {
		    for(final Position pos : result.getRaceOrder().values()) {
		    	Car car = carMap.get(pos.getCarName());
				Engine engine = engineMap.get(car.getEngine().getName());
				Driver driver = getDriver(pos, driverMap, standinDriverMap, result, Session.RACE);
				
				if (pos.isClassified()) {
					add(driver.getPointsPerEvent(), result.getRound(), rules.getDriverRacePoints().get(pos.getPosition()));				
					driver.setTotalPoints(driver.getTotalPoints() + rules.getDriverRacePoints().get(pos.getPosition()));
					
					if(result.getFastestLapDriver().getNumber() == pos.getDriverNumber()) {
						driver.setFastestLaps(driver.getFastestLaps() + 1);
						add(driver.getPointsPerEvent(), result.getRound(), rules.getFastestLapBonus());				
						driver.setTotalPoints(driver.getTotalPoints() + rules.getFastestLapBonus());
					}
					
					add(car.getPointsPerEvent(), result.getRound(), rules.getCarRacePoints().get(pos.getPosition()));				
					car.setTotalPoints(car.getTotalPoints() + rules.getCarRacePoints().get(pos.getPosition()));				
					add(engine.getPointsPerEvent(), result.getRound(), rules.getEngineRacePoints().get(pos.getPosition()));
					engine.setTotalPoints(engine.getTotalPoints() + rules.getEngineRacePoints().get(pos.getPosition()));
					
					add(numCarsFinished, car.getName(), 1);
					if(numCarsFinished.get(car.getName()) == 2) {
						add(car.getPointsPerEvent(), result.getRound(), rules.getBothCarsFinishedBonus());						
						car.setTotalPoints(car.getTotalPoints() + rules.getBothCarsFinishedBonus());
						car.setBothCarsFinishBonuses(car.getBothCarsFinishBonuses() + 1);
					}
				}
				add(numCarsParticipated, pos.getCarName(), 1);
		    }
	    }
	    componentService.saveDrivers(drivers, true);
	    componentService.saveCars(cars, true);
	    componentService.saveEngines(engines, true);

		final Iterator<Team> teamItr = teamService.findAll().iterator();
		while (teamItr.hasNext()) {
			final Team team = teamItr.next();
			if (!team.getName().equals(bestTheoreticalTeamName)) {
				int points = 0;
				for (final Driver driver : team.getDrivers()) {
					points += driver.getPointsPerEvent().get(result.getRound());
				}
				points += team.getCar().getPointsPerEvent()
						.get(result.getRound());
				points += team.getEngine().getPointsPerEvent()
						.get(result.getRound());
				team.getPointsPerEvent().put(result.getRound(), points);
				team.setTotalPoints(team.getTotalPoints() + points);
				teamService.saveTeamNoValidation(team);
			}
		}
		calculateBestTheoreticalTeam(result);
	}
	
	private Driver getDriver(Position pos, Map<Integer, Driver> driverMap, Map<Integer, Driver> standinDriverMap, EventResult result, final Session session) {
		Driver driver = driverMap.get(pos.getDriverNumber());					
		if (driver == null && (standinDriverMap.containsKey(pos.getDriverNumber()) &&
				standinDriverMap.get(pos.getDriverNumber()).getStandinRoundsDrivers().containsKey(result.getRound()))) {
			driver = driverMap.get(standinDriverMap.get(pos.getDriverNumber()).getStandinRoundsDrivers().get(result.getRound()));
			result.getRemarks().add(driverMap.get(standinDriverMap.get(pos.getDriverNumber()).getStandinRoundsDrivers().get(result.getRound())).getName() + " scores " + session + " points from stand-in driver " + standinDriverMap.get(pos.getDriverNumber()).getName());
		}
		return driver;
	}
	
	private enum Session {
		QUALIFYING("qualifying"),
		RACE("race");
		
		private final String name;       

	    private Session(String s) {
	        name = s;
	    }

	    @Override
	    public String toString() {
	       return this.name;
	    }
	}

	private <T> void add(final Map <T, Integer> map, final T key, final Integer value) {
		Integer newValue;
		if(map.containsKey(key)) {
			newValue = map.get(key) + value;
		} else {
			newValue = value;
		}
		map.put(key, newValue);
	}

	@Override
	public Rules getRules() {
		return rules;
	}

	@Override
	public boolean seasonStarted() {
		return new Date().after(seasonStartDateTime);
	}

	@Override
	public Date getSeasonStartDate() {
		return seasonStartDateTime;
	}
}
