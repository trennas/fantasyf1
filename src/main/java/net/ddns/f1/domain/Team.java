package net.ddns.f1.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;

import lombok.Getter;
import net.ddns.f1.service.impl.ValidationException;

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

	public Team() {
	}

	public Team(final String name, final List<Driver> drivers, final Car car,
			final Engine engine) throws ValidationException {
		this.name = name;
		this.drivers = drivers;
		this.car = car;
		this.engine = engine;
	}

	@Override
	public String toString() {
		return getName();
	}
}
