package net.ddns.f1.repository;

import net.ddns.f1.domain.EventResult;

public interface LiveResultsRepository {	
	public EventResult fetchEventResult(final int round);
}
