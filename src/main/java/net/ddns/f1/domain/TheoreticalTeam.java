package net.ddns.f1.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class TheoreticalTeam {
	@Id
	private String name;
	private Long points;
	
	@ElementCollection(fetch = FetchType.EAGER)
	private List<MinimalTeamComponent> drivers;
	
	@AttributeOverrides( {
		@AttributeOverride(name="name", column = @Column(name = "carName")),
		@AttributeOverride(name="price", column = @Column(name = "carPrice")),
		@AttributeOverride(name="points", column = @Column(name = "carPoints"))
	})
	private MinimalTeamComponent car;
	
	@AttributeOverrides( {
		@AttributeOverride(name="name", column = @Column(name = "engineName")),
		@AttributeOverride(name="price", column = @Column(name = "enginePrice")),
		@AttributeOverride(name="points", column = @Column(name = "enginePoints"))
	})	
	private MinimalTeamComponent engine;

	public TheoreticalTeam() {		
	}

	public void setComponents(Team team) {
		this.setDrivers(new ArrayList<MinimalTeamComponent>());
		for(Driver driver : team.getDrivers()) {
			this.getDrivers().add(new MinimalTeamComponent(driver.getName(), driver.getPrice(), driver.getTotalPoints()));
		}
		this.car = new MinimalTeamComponent(team.getCar().getName(), team.getCar().getPrice(), team.getCar().getTotalPoints());
		this.engine = new MinimalTeamComponent(team.getEngine().getName(), team.getEngine().getPrice(), team.getEngine().getTotalPoints());
	}
}


