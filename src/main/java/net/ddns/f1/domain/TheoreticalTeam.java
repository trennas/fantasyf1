package net.ddns.f1.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
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

	@AttributeOverrides({
			@AttributeOverride(name = "name", column = @Column(name = "carName")),
			@AttributeOverride(name = "price", column = @Column(name = "carPrice")),
			@AttributeOverride(name = "points", column = @Column(name = "carPoints")),
			@AttributeOverride(name = "number", column = @Column(name = "carNumber"))})
	private MinimalTeamComponent car;

	@AttributeOverrides({
			@AttributeOverride(name = "name", column = @Column(name = "engineName")),
			@AttributeOverride(name = "price", column = @Column(name = "enginePrice")),
			@AttributeOverride(name = "points", column = @Column(name = "enginePoints")),
			@AttributeOverride(name = "number", column = @Column(name = "engineNumber"))})
	private MinimalTeamComponent engine;

	public TheoreticalTeam() {
	}

	public void setComponents(final Team team) {
		setDrivers(new ArrayList<MinimalTeamComponent>());
		for (final Driver driver : team.getDrivers()) {
			getDrivers().add(
					new MinimalTeamComponent(driver.getName(), driver
							.getPrice(), driver.getTotalPoints(), driver.getNumber()));
		}
		car = new MinimalTeamComponent(team.getCar().getName(), team.getCar()
				.getPrice(), team.getCar().getTotalPoints());
		engine = new MinimalTeamComponent(team.getEngine().getName(), team
				.getEngine().getPrice(), team.getEngine().getTotalPoints());
	}
}
