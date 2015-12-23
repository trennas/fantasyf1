package net.ddns.f1.service;

import java.util.List;

import net.ddns.f1.domain.EventResult;

public interface EventService {

	public EventResult refreshEvent(final int round);

	public void save(final EventResult result);

	public EventResult findByRound(final int round);

	public int deleteEvent(final int round);

	public void refreshAllEvents();

	public int updateResults();

	public boolean checkForNewResults(final boolean emailAlerts);

	public List<EventResult> getSeasonResults();
}
