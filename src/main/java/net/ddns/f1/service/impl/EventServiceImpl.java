package net.ddns.f1.service.impl;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import net.ddns.f1.domain.EventResult;
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

	@Value("${results-refresh-interval}")
	private long resultRefreshInterval;
	private long timeOfLastResultCheck = 0;
		
	public class SeasonResults {
		@Getter @Setter List<EventResult> results;
		@Getter @Setter boolean newData = false;
	}

	public synchronized SeasonResults getSeasonResults() {
		SeasonResults seasonResults = new SeasonResults();
		final Iterable<EventResult> itr = eventRepo.findAll();
		seasonResults.setResults(IteratorUtils.toList(itr.iterator()));
		
		if(System.currentTimeMillis() - timeOfLastResultCheck > resultRefreshInterval) {
			LOG.info("Checking for new race results...");
			if(seasonResults.getResults().size() > 0) {
				EventResult result = seasonResults.getResults().get(seasonResults.getResults().size()-1);
				if(!result.isRaceComplete()) {
					result = liveRepo.fetchEventResult(result.getRound());
					eventRepo.save(result);
					seasonResults.setNewData(true);
				}
			}
			
			EventResult result = liveRepo.fetchEventResult(seasonResults.getResults().size() + 1);
			if (result != null) {
				LOG.info("Found new live race results... updating");
				while (result != null) {
					eventRepo.save(result);
					result = liveRepo.fetchEventResult(result.getRound() + 1);
				}
				seasonResults.setNewData(true);
			} else {
				LOG.info("No new race results found");
			}
		}

		return seasonResults;
	}
}
