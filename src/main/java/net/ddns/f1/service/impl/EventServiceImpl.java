package net.ddns.f1.service.impl;

import java.util.Collections;
import java.util.List;

import net.ddns.f1.domain.Correction;
import net.ddns.f1.domain.EventResult;
import net.ddns.f1.repository.CorrectionRepository;
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
	
	@Autowired
	LeagueServiceImpl leagueService;
	
	@Autowired
	MailServiceImpl mailService;
	
	@Autowired
	ServiceUtils utils;

	@Value("${results-refresh-interval}")
	private long resultRefreshInterval;
	private long timeOfLastResultCheck = 0;

	public EventResult refreshEvent(int round) {
		LOG.info("Manually invoked refresh result round " + round + "...");		
		EventResult result = liveRepo.fetchEventResult(round);
		if(result != null) {			
			applyCorrections(result);
			eventRepo.deleteByRound(round);
			eventRepo.save(result);
			leagueService.recalculateAllResults();
		}
		return result;
	}
	
	public void save(EventResult result) {
		eventRepo.save(result);
	}

	public EventResult findByRound(int round) throws Ff1Exception {
		return utils.get(eventRepo.findByRound(round), Integer.toString(round));
	}

	@Transactional
	public int deleteEvent(int round) {
		LOG.info("Manually invoked delete result round " + round + "...");
		EventResult res = eventRepo.findByRound(round).get(0);
		if(res != null) {
			eventRepo.deleteByRound(round);
			LOG.info("Deleted result round " + round);
			leagueService.recalculateAllResults();
			return 1;
		} else {
			LOG.info("Result round " + round + " does not exist");
			return 0;
		}
	}
	
	public synchronized void refreshAllEvents() {
		LOG.info("Manually invoked refresh of all results..");
		eventRepo.deleteAll();
		timeOfLastResultCheck = 0;
		checkForNewResults(false);
		leagueService.recalculateAllResults();
	}
	
	public synchronized int updateResults() {
		LOG.info("Updating results..");
		timeOfLastResultCheck = 0;
		if(checkForNewResults(true)) {
			leagueService.recalculateAllResults();
			return 1;
		} else {
			return 0;
		}
	}

	public synchronized boolean checkForNewResults(boolean emailAlerts) {
		boolean newResults = false;
		final Iterable<EventResult> itr = eventRepo.findAll();
		List<EventResult> results = IteratorUtils.toList(itr.iterator());
		Collections.sort(results);
		if(System.currentTimeMillis() - timeOfLastResultCheck > resultRefreshInterval) {
			timeOfLastResultCheck = System.currentTimeMillis();
			LOG.info("Checking for new race results...");
			if(results.size() > 0) {
				EventResult result = results.get(results.size()-1);
				if(!result.isRaceComplete()) {
					result = liveRepo.fetchEventResult(result.getRound());
					if(result.isRaceComplete()) {
						applyCorrections(result);
						eventRepo.save(result);
						mailService.sendNewResultsMail(result);
						newResults = true;
					}
				}
			}
			
			EventResult result = liveRepo.fetchEventResult(results.size() + 1);
			if (result != null) {
				int num = 0;
				LOG.info("Found new live race results... updating");
				while (result != null) {
					num++;
					applyCorrections(result);
					results.add(result);
					eventRepo.save(result);					
					result = liveRepo.fetchEventResult(result.getRound() + 1);
				}
				if(emailAlerts && num == 1) {
					// Don't bombarde with emails pulling in multiple results
					mailService.sendNewResultsMail(results.get(results.size() - 1));
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
	
	private void applyCorrections(EventResult result) {
		List<Correction> corrections = correctionRepo.findByRound(result.getRound());
		if(corrections.size() > 0) {
			LOG.info("Applying corrections to event: " + result.getVenue());
			for(Correction correction : corrections) {
				result.getQualifyingOrder().put(correction.getDriver(), correction.getPositions().get(0));
				result.getRaceOrder().put(correction.getDriver(), correction.getPositions().get(1));
				List<String> remarks = result.getRemarks();
				for(String remark : correction.getRemarks()) {					
					if(!remarks.remove(remark)) {						
						remarks.add(remark);
					}
				}
				eventRepo.save(result);
			}
		}
	}
}
