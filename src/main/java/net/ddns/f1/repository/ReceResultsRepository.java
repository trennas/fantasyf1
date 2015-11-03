package net.ddns.f1.service;

import net.ddns.f1.domain.EventResult;

public interface ResultsService {
	public EventResult getEventResult(int round);
}
