package net.ddns.f1.service.impl;

import java.util.ArrayList;
import java.util.List;

import net.ddns.f1.domain.Driver;
import net.ddns.f1.domain.DriverPosition;
import net.ddns.f1.domain.EventResult;
import net.ddns.f1.repository.DriverRepository;
import net.ddns.f1.service.ResultsService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ergast.mrd._1.MRDataType;
import com.ergast.mrd._1.QualifyingResultType;
import com.ergast.mrd._1.ResultType;

@Service
public class ResultsServiceErgastImpl implements ResultsService {

	private static final Logger LOG = Logger
			.getLogger(ResultsServiceErgastImpl.class);

	@Autowired
	DriverRepository driverRepo;

	@Override
	public EventResult getEventResult(final int round) {
		final RestTemplate restTemplate = new RestTemplate();
		final MRDataType qual = restTemplate
				.getForObject("http://ergast.com/api/f1/current/" + round
						+ "/qualifying.xml", MRDataType.class);
		final MRDataType race = restTemplate.getForObject(
				"http://ergast.com/api/f1/current/" + round + "/results.xml",
				MRDataType.class);
		final MRDataType fastestLap = restTemplate.getForObject(
				"http://ergast.com/api/f1/2015/" + round
						+ "/fastest/1/drivers.xml", MRDataType.class);

		final EventResult result = new EventResult();

		final Driver fastestLapDriver = driverRepo.findByNumber(fastestLap.getDriverTable().getDriver().get(0).getPermanentNumber().intValue()).get(0);
		result.setFastestLapDriver(fastestLapDriver);

		if (qual.getRaceTable().getRace().size() > 0) {
			result.setVenue(qual.getRaceTable().getRace().get(0).getRaceName());
			result.setRound(qual.getRaceTable().getRace().get(0).getRound().intValue());
			result.setSeason(qual.getRaceTable().getRace().get(0).getSeason().intValue());

			final List<DriverPosition> positions = new ArrayList<DriverPosition>();

			for (final QualifyingResultType res : qual.getRaceTable().getRace()
					.get(0).getQualifyingList().getQualifyingResult()) {
				final Driver driver = findDriver(res);
				positions.add(new DriverPosition(driver, res.getPosition()
						.intValue(), res.getQ1() != null));
			}

			for (final ResultType res : race.getRaceTable().getRace().get(0)
					.getResultsList().getResult()) {
				final Driver driver = findDriver(res);
				positions.add(new DriverPosition(driver, res.getPosition()
						.intValue(), res.getPositionText()
						.matches("[0-9]{1,2}")));
			}
		}

		return result;
	}

	private Driver findDriver(final QualifyingResultType res) {
		int number;
		if(res.getDriver().getPermanentNumber() != null) {
			number = res.getDriver().getPermanentNumber().intValue();
		} else {
			number = res.getNumber().intValue();
		}
		final List<Driver> driversFound = driverRepo.findByNumber(number);
		if(driversFound.size() > 0 ) {
			return driversFound.get(0);
		} else {
			LOG.info("Couldn't find driver: " + res.getDriver().getGivenName() + " "
					+ res.getDriver().getFamilyName());
			return null;
		}
	}

	private Driver findDriver(final ResultType res) {
		int number;
		if(res.getDriver().get(0).getPermanentNumber() != null) {
			number = res.getDriver().get(0).getPermanentNumber().intValue();
		} else {
			number = res.getNumber().intValue();
		}
		final List<Driver> driversFound = driverRepo.findByNumber(number);
		if(driversFound.size() > 0 ) {
			return driversFound.get(0);
		} else {
			LOG.info("Couldn't find driver: " + res.getDriver().get(0).getPermanentNumber().intValue() + " - " + res.getDriver().get(0).getGivenName() + " "
					+ res.getDriver().get(0).getFamilyName());
			return null;
		}
	}
}
