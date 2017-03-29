package fantasyf1.repository;

import fantasyf1.domain.EventResult;
import fantasyf1.domain.SeasonInformation;

public interface LiveResultsRepository {
	EventResult fetchEventResult(final int round);
	SeasonInformation getSeasonInformation();
}
