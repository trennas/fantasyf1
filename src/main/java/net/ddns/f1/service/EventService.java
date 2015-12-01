package net.ddns.f1.service;

import java.util.List;

import net.ddns.f1.domain.EventResult;
import net.ddns.f1.service.impl.Ff1Exception;

public interface EventService {

	public EventResult refreshEvent(final int round) throws Ff1Exception;

	public void save(final EventResult result);

	public EventResult findByRound(final int round) throws Ff1Exception;

	public int deleteEvent(final int round) throws Ff1Exception;

	public void refreshAllEvents() throws Ff1Exception;

	public int updateResults() throws Ff1Exception;

	public boolean checkForNewResults(final boolean emailAlerts)
			throws Ff1Exception;

	public List<EventResult> getSeasonResults();
}
