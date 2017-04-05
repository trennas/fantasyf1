package fantasyf1.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.IteratorUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fantasyf1.domain.EventResult;
import fantasyf1.domain.RaceInformation;
import fantasyf1.domain.SeasonInformation;
import fantasyf1.repository.EventResultRepository;
import fantasyf1.repository.LiveResultsRepository;
import fantasyf1.service.ComponentService;
import fantasyf1.service.EventService;
import fantasyf1.service.LeagueService;
import fantasyf1.service.MailService;

@Service
public class EventServiceImpl implements EventService {

	private static final Logger LOG = Logger.getLogger(EventServiceImpl.class);

	@Autowired
	private EventResultRepository eventRepo;

	@Autowired
	private LiveResultsRepository liveRepo;

	@Autowired
	private LeagueService leagueService;

	@Autowired
	private MailService mailService;

	@Autowired
	private ServiceUtils utils;
	
	@Autowired
	private ComponentService componentService;

	@Value("${results-refresh-interval}")
	private long resultRefreshInterval;
	private long timeOfLastResultCheck = 0;
	
	@Override
	public RaceInformation getNextRace() {
		SeasonInformation seasonInformation = componentService.getSeasonInformation();
		if(seasonInformation == null ) {
			seasonInformation = liveRepo.getSeasonInformation();
			if(seasonInformation != null && !seasonInformation.getRaces().isEmpty()) {
				componentService.setSeasonInformation(seasonInformation);
			} else {
				LOG.error("Unable to get season information from Live Repo");
				return null;
			}
		}
		
		int nextRound = IteratorUtils.toList(eventRepo.findAll().iterator()).size() + 1;
		if (seasonInformation.getRaces().containsKey(nextRound)) {
			return seasonInformation.getRaces().get(nextRound);
		} else {
			if(!seasonInformation.getComplete()) {
				seasonInformation.setComplete(true);
				mailService.sendEndOfSeasonMail();
			}			
			return null;
		}
	}

	@Override
	public EventResult refreshEvent(final int round) {
		LOG.info("Manually invoked refresh result round " + round + "...");
		final EventResult result = liveRepo.fetchEventResult(round);
		if (result != null) {
			eventRepo.deleteByRound(round);			
			leagueService.deletePointsForRound(result, true);
			leagueService.calculateResult(result);
			eventRepo.save(result);
		}
		return result;
	}

	@Override
	public void save(final EventResult result) {
		eventRepo.save(result);
	}

	@Override
	public EventResult findByRound(final int round) {
		return utils.get(eventRepo.findByRound(round), Integer.toString(round));
	}

	@Override
	@Transactional
	public int deleteEvent(final int round) {
		LOG.info("Manually invoked delete result round " + round + "...");
		final EventResult res = eventRepo.findByRound(round).get(0);
		if (res != null) {
			eventRepo.deleteByRound(round);
			LOG.info("Deleted result round " + round);
			leagueService.deletePointsForRound(res, true);
			return 1;
		} else {
			LOG.info("Result round " + round + " does not exist");
			return 0;
		}
	}

	@Override
	public synchronized void refreshAllEvents() {
		LOG.info("Manually invoked refresh of all results..");
		eventRepo.deleteAll();
		timeOfLastResultCheck = 0;
		leagueService.resetAllScores();
		
		final SeasonInformation seasonInformation = liveRepo.getSeasonInformation();
		if(seasonInformation != null) {
			componentService.setSeasonInformation(seasonInformation);
		}
		checkForNewResults(false);		
	}

	@Override
	public synchronized int updateResults() {
		LOG.info("Updating results..");
		timeOfLastResultCheck = 0;
		return checkForNewResults(true);		
	}

	@Override
	public synchronized int checkForNewResults(final boolean emailAlerts) {
		int numFound = 0;
		final List<EventResult> results = IteratorUtils.toList(eventRepo.findAll().iterator());
		Collections.sort(results);
		if (System.currentTimeMillis() - timeOfLastResultCheck > resultRefreshInterval) {
			timeOfLastResultCheck = System.currentTimeMillis();			
			LOG.info("Checking for new race results...");
			
			// Start by checking for race results for existing result where only qual is complete.
			if (!results.isEmpty()) {
				final EventResult prevResult = results.get(results.size() - 1);
				if(!prevResult.isRaceComplete()) {
					final EventResult newResult = liveRepo.fetchEventResult(prevResult.getRound());
					if (newResult.isRaceComplete()) {
						eventRepo.delete(prevResult);						
						leagueService.deletePointsForRound(prevResult, false);
						leagueService.calculateResult(newResult);
						eventRepo.save(newResult);
						mailService.sendNewResultsMail(newResult);
						results.remove(results.size() - 1);
						results.add(newResult);
						numFound++;
					}
				}
			}
			
			// Now check for brand new results			
			EventResult result = liveRepo.fetchEventResult(results.size() + 1);
			if (result != null) {
				int num = 0;
				LOG.info("Found new live race results... updating");
				while (result != null) {
					num++;
					results.add(result);					
					leagueService.calculateResult(result);
					eventRepo.save(result);
					numFound++;
					result = liveRepo.fetchEventResult(result.getRound() + 1);					
				}
				if (emailAlerts && num == 1) {
					// Don't bombarde with emails pulling in multiple results
					mailService.sendNewResultsMail(results.get(results.size() - 1));
				}
				getNextRace();
			} else {
				LOG.info("No new race results found");
			}
		}
		return numFound;
	}

	@Override
	public synchronized List<EventResult> getSeasonResults() {
		final Iterable<EventResult> itr = eventRepo.findAll();
		final List<EventResult> results = IteratorUtils.toList(itr.iterator());
		Collections.sort(results);
		return results;
	}
}
