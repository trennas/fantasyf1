package net.ddns.f1.service.impl;

import java.util.Collections;
import java.util.List;

import net.ddns.f1.domain.EventResult;
import net.ddns.f1.repository.EventResultRepository;
import net.ddns.f1.repository.LiveResultsRepository;

import org.apache.commons.collections4.IteratorUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventServiceImpl {
	@Autowired
	EventResultRepository eventRepo;

	@Autowired
	LiveResultsRepository liveRepo;

	private static final Logger LOG = Logger.getLogger(EventServiceImpl.class);

	@Transactional
	public synchronized List<EventResult> getSeasonResults() {
		final Iterable<EventResult> itr = eventRepo.findAll();
		final List<EventResult> results = IteratorUtils.toList(itr.iterator());
		Collections.sort(results);

		EventResult result = liveRepo.fetchEventResult(results.size() + 1);
		if (result != null) {
			LOG.info("Found new live race results... updating");
			while (result != null) {
				results.add(result);
				eventRepo.save(results);
				result = liveRepo.fetchEventResult(results.size() + 1);
			}
		} else {
			LOG.info("No new race results found");
		}

		return results;
	}
}
