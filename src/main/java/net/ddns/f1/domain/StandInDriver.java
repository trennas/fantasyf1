package net.ddns.f1.domain;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class StandInDriver {
	@Id
	private String id;
	private int round;
	@ManyToOne(targetEntity = Driver.class, fetch = FetchType.EAGER)
	private Driver standInDriver;
	@ManyToOne(targetEntity = Driver.class, fetch = FetchType.EAGER)
	private Driver standingInFor;
	
	public StandInDriver() {		
	}
	
	public StandInDriver(int round, Driver standInDriver, Driver standingInFor) {
		this.id = UUID.randomUUID().toString();
		this.round = round;
		this.standInDriver = standInDriver;
		this.standingInFor = standingInFor;
	}
}
