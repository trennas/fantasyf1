package net.ddns.f1.repository;

import net.ddns.f1.domain.EventResult;
import net.ddns.f1.service.impl.Ff1Exception;

public interface LiveResultsRepository {
	public EventResult fetchEventResult(final int round) throws Ff1Exception;
}
