package net.ddns.f1.repository.impl;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ddns.f1.domain.Driver;
import net.ddns.f1.domain.Position;
import net.ddns.f1.domain.EventResult;
import net.ddns.f1.repository.CarRepository;
import net.ddns.f1.repository.DriverRepository;
import net.ddns.f1.repository.LiveResultsRepository;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ergast.mrd._1.MRDataType;
import com.ergast.mrd._1.ResultType;

@Service
public class LiveResultsRepositoryErgastImpl implements LiveResultsRepository {

	private static final Logger LOG = Logger
			.getLogger(LiveResultsRepositoryErgastImpl.class);
	
	private static final String ERGAST_BASE_URL = "http://ergast.com/api/f1/current/";

	@Autowired
	DriverRepository driverRepo;
	
	@Autowired
	CarRepository carRepo;

	@Override
	public EventResult fetchEventResult(final int round) {
		final RestTemplate restTemplate = new RestTemplate();
		final MRDataType qual = restTemplate
				.getForObject(ERGAST_BASE_URL + round
						+ "/qualifying.xml", MRDataType.class);
		final MRDataType race = restTemplate.getForObject(
				ERGAST_BASE_URL + round + "/results.xml",
				MRDataType.class);
		final MRDataType fastestLap = restTemplate.getForObject(
				ERGAST_BASE_URL + round
						+ "/fastest/1/drivers.xml", MRDataType.class);

		if (qual.getRaceTable().getRace().size() > 0) {
			final EventResult result = new EventResult();
			result.setRaceComplete(false);

			result.setVenue(qual.getRaceTable().getRace().get(0).getRaceName());
			LOG.info("Retrieving qualifying results for round " + round + " "
					+ result.getVenue());

			result.setRound(qual.getRaceTable().getRace().get(0).getRound().intValue());
			result.setSeason(qual.getRaceTable().getRace().get(0).getSeason().intValue());			
			result.setQualifyingOrder(new HashMap<Driver, Position>());
			
			long fastestQ1Time = 0;
			
			Map<ResultType, Driver> qualResultDriverMap = new HashMap<ResultType, Driver>();
			
			for (final ResultType res : qual.getRaceTable().getRace()
					.get(0).getQualifyingList().getQualifyingResult()) {
				if(res.getQ1() != null) {
					long millis = durationToMillis(res.getQ1().getValue());
					if (fastestQ1Time == 0 || millis < fastestQ1Time) {
						fastestQ1Time = millis;
					}
				}
				
				final Driver driver = findDriver(res);
				qualResultDriverMap.put(res,  driver);
				result.getQualifyingOrder().put(driver, new Position(res
						.getPosition()
						.intValue(), true));
			}
			
			final long classifiedTime = fastestQ1Time * 107;
			for (final ResultType res : qual.getRaceTable().getRace()
					.get(0).getQualifyingList().getQualifyingResult()) {				
				Position pos = result.getQualifyingOrder().get(qualResultDriverMap.get(res));				
				if(res.getQ1() == null) {
					pos.setClassified(false);
				} else {
					long millis = durationToMillis(res.getQ1().getValue());
					if(millis*100 > classifiedTime) {
						pos.setClassified(false); // Q1 outside 107%
					}
				}
					
			}

			if (race.getRaceTable().getRace().size() > 0) {
				LOG.info("Retrieving race results for round " + round + " "
						+ result.getVenue());
				result.setRaceOrder(new HashMap<Driver, Position>());
				for (final ResultType res : race.getRaceTable().getRace()
						.get(0).getResultsList().getResult()) {
					boolean classified = res.getPositionText().matches("[0-9]{1,2}");
					final Driver driver = findDriver(res);
					result.getRaceOrder().put(driver, new Position(res.getPosition()
							.intValue(), classified));
				}

				final Driver fastestLapDriver = driverRepo.findByNumber(
						fastestLap.getDriverTable().getDriver().get(0)
								.getPermanentNumber().intValue()).get(0);
				result.setFastestLapDriver(fastestLapDriver);
				result.setRaceComplete(true);
			}
			return result;
		}

		return null;
	}

	private Driver findDriver(final ResultType res) {
		int number;
		if(res.getDriver().getPermanentNumber() != null) {
			number = res.getDriver().getPermanentNumber().intValue();
		} else {
			number = res.getNumber().intValue();
		}
		List<Driver> driversFound = driverRepo.findByNumber(number);
		if(driversFound.size() > 0 ) {
			return driversFound.get(0);
		} else {
			driversFound = driverRepo.findByName(res.getDriver().getGivenName() + " " + res.getDriver().getFamilyName());
			if(driversFound.size() > 0 ) {
				return driversFound.get(0);
			} else {
				LOG.error("Couldn't find driver: " + res.getDriver().getGivenName() + " "
						+ res.getDriver().getFamilyName());
				return null;
			}
		}
	}
	
	private long durationToMillis(String duration) {
		//sample input: 2:03.194
		duration = duration.replaceAll("\\.", ":");
		String[] tokens = duration.split(":");
		int minutes = Integer.parseInt(tokens[0]);
		int seconds = Integer.parseInt(tokens[1]);
		int milliseconds = Integer.parseInt(tokens[2]);
		long millis = (((minutes*60) + seconds) * 1000) + milliseconds;
		return millis;
		
	}
}
