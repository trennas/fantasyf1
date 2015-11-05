package net.ddns.f1.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Transient;

import lombok.Data;
import net.ddns.f1.service.impl.ValidationException;

@Entity
@Data
public class Team {
	@Id
	private String name;
	@ManyToMany(targetEntity = Driver.class, fetch = FetchType.EAGER)
	@OrderColumn
	private List<Driver> drivers;
	@OneToOne(targetEntity = Car.class, fetch = FetchType.EAGER)
	private Car car;
	@OneToOne(targetEntity = Engine.class, fetch = FetchType.EAGER)
	private Engine engine;

	@Transient
	private int points;

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
