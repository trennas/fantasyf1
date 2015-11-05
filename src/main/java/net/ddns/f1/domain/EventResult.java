package net.ddns.f1.domain;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity
@Data
public class EventResult implements Comparable<EventResult> {
	@Id
	private int round;
	private String venue;
	private int season;
	@ManyToOne(targetEntity = Driver.class, fetch = FetchType.EAGER)
	private Driver fastestLapDriver;
	@ElementCollection
	private List<DriverPosition> qualifyingOrder;
	@ElementCollection
	private List<DriverPosition> raceOrder;

	@Override
	public int compareTo(final EventResult otherEvent) {
		if (this.round > otherEvent.getRound()) {
			return 1;
		} else if (this.round < otherEvent.getRound()) {
			return -1;
		} else {
			return 0;
		}
	}
}
