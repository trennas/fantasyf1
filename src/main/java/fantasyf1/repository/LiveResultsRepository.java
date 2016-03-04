package fantasyf1.repository;

import fantasyf1.domain.EventResult;

public interface LiveResultsRepository {
	public EventResult fetchEventResult(final int round);
}
