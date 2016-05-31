package fantasyf1.repository.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ergast.mrd._1.MRDataType;
import com.ergast.mrd._1.ResultType;

import fantasyf1.domain.Car;
import fantasyf1.domain.Driver;
import fantasyf1.domain.EventResult;
import fantasyf1.domain.Position;
import fantasyf1.repository.LiveResultsRepository;
import fantasyf1.service.ComponentService;

@Service
public class LiveResultsRepositoryErgastImpl implements LiveResultsRepository {

	private static final Logger LOG = Logger
			.getLogger(LiveResultsRepositoryErgastImpl.class);

	@Value("${season}")
	private String season;

	@Value("${ergast-base-url}")
	private String ergastBaseUrl;

	@Autowired
	ComponentService componentService;

	@Autowired
	RestTemplate restTemplate;

	@Override
	public EventResult fetchEventResult(final int round) {
		final String seasonUrl = ergastBaseUrl + season + "/";
		final MRDataType qual;
		final MRDataType race;
		final MRDataType fastestLap;

		try {
			qual = restTemplate.getForObject(seasonUrl + round
					+ "/qualifying.xml", MRDataType.class);
			race = restTemplate.getForObject(
					seasonUrl + round + "/results.xml", MRDataType.class);
			fastestLap = restTemplate.getForObject(seasonUrl + round
					+ "/fastest/1/drivers.xml", MRDataType.class);
		} catch (final Exception e) {
			LOG.error("Unable to contact results service at " + seasonUrl);
			return null;
		}

		if (qual.getRaceTable().getRace().size() > 0) {
			final EventResult result = new EventResult();
			result.setRaceComplete(false);

			boolean q3Complete = false;

			result.setVenue(qual.getRaceTable().getRace().get(0).getRaceName());
			LOG.info("Retrieving qualifying results for round " + round + " "
					+ result.getVenue());

			result.setRound(qual.getRaceTable().getRace().get(0).getRound()
					.intValue());
			result.setSeason(qual.getRaceTable().getRace().get(0).getSeason()
					.intValue());
			result.setQualifyingOrder(new LinkedHashMap<>());

			long fastestQ1Time = 0;

			final Map<ResultType, Driver> qualResultDriverMap = new HashMap<>();

			for (final ResultType res : qual.getRaceTable().getRace().get(0)
					.getQualifyingList().getQualifyingResult()) {
				if (res.getQ1() != null) {
					final long millis = durationToMillis(res.getQ1().getValue());
					if (fastestQ1Time == 0 || millis < fastestQ1Time) {
						fastestQ1Time = millis;
					}
				}
				if (res.getQ3() != null) {
					q3Complete = true;
				}

				final Driver driver = findDriver(res, result);
				qualResultDriverMap.put(res, driver);
				final Car car = findCar(res, result);
				result.getQualifyingOrder().put(driver.getName(),
						new Position(res.getPosition().intValue(), true, driver.getNumber(), car.getName()));
			}

			final List<Driver> drivers = componentService
					.findDriversByStandin(false);
			for (final Driver driver : drivers) {
				if (!qualResultDriverMap.containsValue(driver)) {
					result.addRemark(driver.getName()
							+ " did not participate in qualifying");
				}
			}

			final long classifiedTime = fastestQ1Time * 107;
			for (final ResultType res : qual.getRaceTable().getRace().get(0)
					.getQualifyingList().getQualifyingResult()) {
				final Driver driver = qualResultDriverMap.get(res);
				final Position pos = result.getQualifyingOrder().get(
						driver.getName());
				if (res.getQ1() == null) {
					pos.setClassified(false);
					result.addRemark(driver.getName()
							+ " did not set a qualifying time");
				} else {
					final long millis = durationToMillis(res.getQ1().getValue());
					if (millis * 100 > classifiedTime) {
						pos.setClassified(false); // Q1 outside 107%
						result.addRemark(driver.getName()
								+ " not classified in qualifying (Q1 time was outside 107%)");
					}
				}
			}

			if (!q3Complete) {
				result.addRemark("Qualifying was not completed in full");
			}

			if (race.getRaceTable().getRace().size() > 0) {
				LOG.info("Retrieving race results for round " + round + " "
						+ result.getVenue());
				result.setRaceOrder(new LinkedHashMap<>());
				final Map<ResultType, Driver> raceResultDriverMap = new HashMap<>();
				for (final ResultType res : race.getRaceTable().getRace()
						.get(0).getResultsList().getResult()) {
					final boolean classified = res.getPositionText().matches(
							"[0-9]{1,2}");
					final Driver driver = findDriver(res, result);
					raceResultDriverMap.put(res, driver);
					final Car car = findCar(res, result);
					result.getRaceOrder().put(
							driver.getName(),
							new Position(res.getPosition().intValue(),
									classified, driver.getNumber(), car.getName()));
				}

				final Driver fastestLapDriver = componentService
						.findDriverByNumber(fastestLap.getDriverTable()
								.getDriver().get(0).getPermanentNumber()
								.intValue());
				result.setFastestLapDriver(fastestLapDriver);
				result.setRaceComplete(true);

				for (final Driver driver : drivers) {
					if (!raceResultDriverMap.containsValue(driver)) {
						result.addRemark(driver.getName()
								+ " did not participate in the race");
					}
				}

			} else {
				result.addRemark("Awaiting Race Results");
			}
			return result;
		}

		return null;
	}

	private Driver findDriver(final ResultType res,
			final EventResult eventResult) {
		int number;
		if (res.getDriver().getPermanentNumber() != null) {
			number = res.getDriver().getPermanentNumber().intValue();
		} else {
			number = res.getNumber().intValue();
		}
		Driver driver = componentService.findDriverByNumber(number);
		if (driver != null) {
			return driver;
		} else {
			driver = componentService.findDriverByName(res.getDriver()
					.getGivenName() + " " + res.getDriver().getFamilyName());
			if (driver != null) {
				return driver;
			} else {
				final String message = "Driver: "
						+ res.getDriver().getGivenName() + " "
						+ res.getDriver().getFamilyName()
						+ " in results list could not be found.";
				LOG.error(message);
				eventResult.addRemark(message);
				return null;
			}
		}
	}

	private Car findCar(final ResultType res,
			final EventResult eventResult) {
		final String name = res.getConstructor().getName();

		final Car car = componentService.findCarByName(name);
		if (car != null) {
			return car;
		} else {
			final String message = "Car: "
					+ name + " in results list could not be found.";
			LOG.error(message);
			eventResult.addRemark(message);
			return null;
		}
	}

	private long durationToMillis(String duration) {
		// sample input: 2:03.194
		duration = duration.replaceAll("\\.", ":");
		final String[] tokens = duration.split(":");
		final int minutes = Integer.parseInt(tokens[0]);
		final int seconds = Integer.parseInt(tokens[1]);
		final int milliseconds = Integer.parseInt(tokens[2]);
		final long millis = (((minutes * 60) + seconds) * 1000) + milliseconds;
		return millis;

	}
}
