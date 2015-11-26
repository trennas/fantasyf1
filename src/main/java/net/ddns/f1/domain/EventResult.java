package net.ddns.f1.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.Data;

@Entity
@Data
public class EventResult implements Comparable<EventResult> {
	@Id
	private int round;
	private String venue;
	private int season;
	private boolean raceComplete;
	@OneToOne(targetEntity = Team.class, fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	private Team bestTheoreticalTeam;
	@ManyToOne(targetEntity = Driver.class, fetch = FetchType.EAGER)
	private Driver fastestLapDriver;
	@ElementCollection(fetch = FetchType.EAGER)
	private Map<String, Position> qualifyingOrder;
	@ElementCollection(fetch = FetchType.EAGER)
	private Map<String, Position> raceOrder;
	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> remarks;

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
	
	public void addRemark(String remark) {
		if(this.remarks == null) {
			this.remarks = new ArrayList<String>();
		}
		
		if(!this.remarks.contains(remark)) {
			this.remarks.add(remark);
		}
	}
}
