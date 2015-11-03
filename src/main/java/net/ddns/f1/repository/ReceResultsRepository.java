package net.ddns.f1.repository;

import java.util.List;

import net.ddns.f1.domain.EventResult;

public interface ReceResultsRepository {
	public EventResult getEventResult(final int round);
	public List<EventResult> getSeasonResults();
}
