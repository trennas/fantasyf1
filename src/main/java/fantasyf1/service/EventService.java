package fantasyf1.service;

import java.util.List;

import fantasyf1.domain.EventResult;
import fantasyf1.domain.RaceInformation;

public interface EventService {

	public EventResult refreshEvent(final int round);

	public void save(final EventResult result);

	public EventResult findByRound(final int round);

	public int deleteEvent(final int round);

	public void refreshAllEvents();

	public int updateResults();
	
	public RaceInformation getNextRace();

	public List<EventResult> getSeasonResults();
}
