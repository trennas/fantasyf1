package net.ddns.f1.service.impl;

import java.util.Collections;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import net.ddns.f1.domain.Correction;
import net.ddns.f1.domain.EventResult;
import net.ddns.f1.repository.CorrectionRepository;
import net.ddns.f1.repository.DriverRepository;
import net.ddns.f1.repository.EventResultRepository;
import net.ddns.f1.repository.LiveResultsRepository;

import org.apache.commons.collections4.IteratorUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventServiceImpl {
	
	private static final Logger LOG = Logger
			.getLogger(EventServiceImpl.class);
	
	@Autowired
	EventResultRepository eventRepo;

	@Autowired
	LiveResultsRepository liveRepo;
	
	@Autowired
	CorrectionRepository correctionRepo;

	@Value("${results-refresh-interval}")
	private long resultRefreshInterval;
	private long timeOfLastResultCheck = 0;
	
	private void actionCorrections(EventResult result) {
		List<Correction> corrections = correctionRepo.findByRound(result.getRound());
		if(corrections.size() > 0) {
			LOG.info("Applying corrections to event: " + result.getVenue());
			for(Correction correction : corrections) {
				result.getQualifyingOrder().put(correction.getDriver(), correction.getPositions().get(0));
				result.getRaceOrder().put(correction.getDriver(), correction.getPositions().get(1));
				eventRepo.save(result);
			}
		}
	}

	public synchronized boolean checkForNewResults() {
		boolean newResults = false;
		final Iterable<EventResult> itr = eventRepo.findAll();
		List<EventResult> results = IteratorUtils.toList(itr.iterator());
		
		if(System.currentTimeMillis() - timeOfLastResultCheck > resultRefreshInterval) {
			timeOfLastResultCheck = System.currentTimeMillis();
			LOG.info("Checking for new race results...");
			if(results.size() > 0) {
				EventResult result = results.get(results.size()-1);
				if(!result.isRaceComplete()) {
					result = liveRepo.fetchEventResult(result.getRound());
					if(result.isRaceComplete()) {
						actionCorrections(result);
						eventRepo.save(result);
						newResults = true;
					}
				}
			}
			
			EventResult result = liveRepo.fetchEventResult(results.size() + 1);
			if (result != null) {
				LOG.info("Found new live race results... updating");
				while (result != null) {
					actionCorrections(result);
					eventRepo.save(result);					
					result = liveRepo.fetchEventResult(result.getRound() + 1);
				}
				newResults = true;
			} else {
				LOG.info("No new race results found");
			}
		}
		return newResults;
	}

	public synchronized List<EventResult> getSeasonResults() {
		final Iterable<EventResult> itr = eventRepo.findAll();
		List<EventResult> results = IteratorUtils.toList(itr.iterator());
		Collections.sort(results);
		return results;
	}
}
