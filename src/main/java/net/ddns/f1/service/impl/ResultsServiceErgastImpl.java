package net.ddns.f1.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ergast.mrd._1.MRDataType;
import com.ergast.mrd._1.QualifyingResultType;

import net.ddns.f1.domain.Driver;
import net.ddns.f1.domain.DriverPosition;
import net.ddns.f1.domain.EventResult;
import net.ddns.f1.domain.FullName;
import net.ddns.f1.repository.DriverRepository;
import net.ddns.f1.service.ResultsService;

@Service
public class ResultsServiceErgastImpl implements ResultsService {
	
	@Autowired
	DriverRepository driverRepo;

	@Override
	public EventResult getEventResult(int round) {
		RestTemplate restTemplate = new RestTemplate();
		MRDataType qual = restTemplate.getForObject("http://ergast.com/api/f1/current/" + round + "/qualifying.xml", MRDataType.class);
		MRDataType race = restTemplate.getForObject("http://ergast.com/api/f1/current/" + round + "/results.xml", MRDataType.class);
		MRDataType fastestLap = restTemplate.getForObject("http://ergast.com/api/f1/2015/" + round + "/fastest/1/drivers.xml", MRDataType.class);
		
		EventResult result = new EventResult();
		Driver fastestLapDriver = new Driver();
		fastestLapDriver.setName(new FullName(fastestLap.getDriverTable().getDriver().get(0).getGivenName(), fastestLap.getDriverTable().getDriver().get(0).getFamilyName()));
		result.setFastestLapDriver(fastestLapDriver);
		
		if(qual.getRaceTable().getRace().size() > 0) {
			result.setVenue(qual.getRaceTable().getRace().get(0).getRaceName());
			
			List<DriverPosition> positions = new ArrayList<DriverPosition>();
			
			for(QualifyingResultType res : qual.getRaceTable().getRace().get(0).getQualifyingList().getQualifyingResult()) {
				Driver driver = driverRepo.findDriverByName(res.getDriver().getGivenName() + " " + res.getDriver().getFamilyName()).get(0);
				positions.add(new DriverPosition(driver, res.getPosition().intValue(), true));
			}
		}
		
		return result;
	}

}
