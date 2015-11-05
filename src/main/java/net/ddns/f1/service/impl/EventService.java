package net.ddns.f1.service.impl;

import java.util.Collections;
import java.util.List;

import net.ddns.f1.domain.EventResult;
import net.ddns.f1.repository.EventResultRepository;

import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventService {
	@Autowired
	EventResultRepository eventRepo;

	public List<EventResult> getSeasonResults() {
		final Iterable<EventResult> itr = eventRepo.findAll();
		final List<EventResult> results = IteratorUtils.toList(itr.iterator());
		Collections.sort(results);
		return results;
	}
}
