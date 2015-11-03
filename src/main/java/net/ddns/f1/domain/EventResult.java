package net.ddns.f1.domain;

import java.util.List;

import lombok.Data;

@Data
public class EventResult {
	private String venue;
	private int round;
	private int season;
	private Driver fastestLapDriver;
	List<DriverPosition> qualifyingOrder;
	List<DriverPosition> raceOrder;
}
