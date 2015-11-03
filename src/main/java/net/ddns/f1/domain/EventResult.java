package net.ddns.f1.domain;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class EventResult {
	private String venue;
	private Driver fastestLapDriver;
	List<DriverPosition> qualifyingOrder;
	List<DriverPosition> raceOrder;	
}
