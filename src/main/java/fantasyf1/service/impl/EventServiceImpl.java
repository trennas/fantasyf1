package fantasyf1.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.IteratorUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fantasyf1.domain.Correction;
import fantasyf1.domain.EventResult;
import fantasyf1.repository.CorrectionRepository;
import fantasyf1.repository.EventResultRepository;
import fantasyf1.repository.LiveResultsRepository;
import fantasyf1.service.EventService;
import fantasyf1.service.LeagueService;
import fantasyf1.service.MailService;

@Service
public class EventServiceImpl implements EventService {

	private static final Logger LOG = Logger.getLogger(EventServiceImpl.class);

	@Autowired
	EventResultRepository eventRepo;

	@Autowired
	LiveResultsRepository liveRepo;

	@Autowired
	CorrectionRepository correctionRepo;

	@Autowired
	LeagueService leagueService;

	@Autowired
	MailService mailService;

	@Autowired
	ServiceUtils utils;

	@Value("${results-refresh-interval}")
	private long resultRefreshInterval;
	private long timeOfLastResultCheck = 0;

	@Override
	public EventResult refreshEvent(final int round) {
		LOG.info("Manually invoked refresh result round " + round + "...");
		final EventResult result = liveRepo.fetchEventResult(round);
		if (result != null) {
			applyCorrections(result);
			eventRepo.deleteByRound(round);
			eventRepo.save(result);
			leagueService.recalculateAllResults();
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
			leagueService.recalculateAllResults();
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
		checkForNewResults(false);
		leagueService.recalculateAllResults();
	}

	@Override
	public synchronized int updateResults() {
		LOG.info("Updating results..");
		timeOfLastResultCheck = 0;
		if (checkForNewResults(true)) {
			leagueService.recalculateAllResults();
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public synchronized boolean checkForNewResults(final boolean emailAlerts) {
		boolean newResults = false;
		final Iterable<EventResult> itr = eventRepo.findAll();
		final List<EventResult> results = IteratorUtils.toList(itr.iterator());
		Collections.sort(results);
		if (System.currentTimeMillis() - timeOfLastResultCheck > resultRefreshInterval) {
			timeOfLastResultCheck = System.currentTimeMillis();
			LOG.info("Checking for new race results...");
			if (results.size() > 0) {
				final EventResult result = results.get(results.size() - 1);
				if (!result.isRaceComplete()) {
					final EventResult newResult = liveRepo.fetchEventResult(result.getRound());
					if (newResult.isRaceComplete()) {
						applyCorrections(newResult);
						eventRepo.delete(result);
						eventRepo.save(newResult);
						mailService.sendNewResultsMail(newResult);
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
				if (emailAlerts && num == 1) {
					// Don't bombarde with emails pulling in multiple results
					mailService
							.sendNewResultsMail(results.get(results.size() - 1));
				}
				newResults = true;
			} else {
				LOG.info("No new race results found");
			}
		}
		return newResults;
	}

	@Override
	public synchronized List<EventResult> getSeasonResults() {
		final Iterable<EventResult> itr = eventRepo.findAll();
		final List<EventResult> results = IteratorUtils.toList(itr.iterator());
		Collections.sort(results);
		return results;
	}

	private void applyCorrections(final EventResult result) {
		final List<Correction> corrections = correctionRepo.findByRound(result
				.getRound());
		if (corrections.size() > 0) {
			LOG.info("Applying corrections to event: " + result.getVenue());
			for (final Correction correction : corrections) {
				result.getQualifyingOrder().put(correction.getDriver(),
						correction.getPositions().get(0));
				result.getRaceOrder().put(correction.getDriver(),
						correction.getPositions().get(1));
				final List<String> remarks = result.getRemarks();
				for (final String remark : correction.getRemarks()) {
					if (!remarks.remove(remark)) {
						remarks.add(remark);
					}
				}
				eventRepo.save(result);
			}
		}
	}
}
