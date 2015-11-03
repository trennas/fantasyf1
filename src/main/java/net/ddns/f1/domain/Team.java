package net.ddns.f1.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;

import lombok.Getter;

@Entity
public class Team {
	@Id
	@Getter
	private String name;
	@Getter
	@ManyToMany(targetEntity = Driver.class, fetch = FetchType.EAGER)
	@OrderColumn
	private List<Driver> drivers;
	@Getter
	@OneToOne(targetEntity = Car.class, fetch = FetchType.EAGER)
	private Car car;
	@Getter
	@OneToOne(targetEntity = Engine.class, fetch = FetchType.EAGER)
	private Engine engine;

	private static final int BUDGET = 60;
	private static final String TEAM_NAME_REGEX = "^[a-z\\d\\-\\!_\\s]+$";
	private static final int TEAM_NAME_MAX_LENGTH = 30;

	public Team() {
	}

	public Team(final String name, final List<Driver> drivers, final Car car,
			final Engine engine) throws ValidationException {
		validate(name, drivers, car, engine);
		this.name = name;
		this.drivers = drivers;
		this.car = car;
		this.engine = engine;
	}

	private void validate(final String name, final List<Driver> drivers,
			final Car car, final Engine engine) throws ValidationException {
		/*
		 * Name cannot be.. stupid Drivers cannot be equal Car cannot use chosen
		 * engine Driver cannot use chosen engine Team must be within budget
		 */
		int cost = 0;

		if (name.matches(TEAM_NAME_REGEX)) {
			throw new ValidationException(
					"Name must match the following regex: " + TEAM_NAME_REGEX);
		} else if (name.length() > TEAM_NAME_MAX_LENGTH) {
			throw new ValidationException(
					"Name must not exceed " + TEAM_NAME_MAX_LENGTH + " characters.");
		}

		if (drivers.get(0).equals(drivers.get(1))) {
			throw new ValidationException("Both drivers are the same");
		}

		if (car.getEngine().equals(engine)) {
			throw new ValidationException("Car cannot use chosen engine");
		}

		for (final Driver driver : drivers) {
			if (driver.getCar().getEngine().equals(engine)) {
				throw new ValidationException("Driver (" + driver.getName()
						+ ") cannot use chosen engine");
			}
			cost += driver.getPrice();
		}
		cost += car.getPrice();
		cost += engine.getPrice();

		if (cost > BUDGET) {
			throw new ValidationException("Team is over budget by £"
					+ (cost - BUDGET) + "m");
		}
	}

	@Override
	public String toString() {
		return getName();
	}
}
