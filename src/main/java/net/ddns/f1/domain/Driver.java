package net.ddns.f1.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class Driver {

	@Id
	private int number;
	private String name;
	private int price;

	@ManyToOne(targetEntity = Car.class, fetch = FetchType.EAGER)
	private Car car;

	public Driver() {
	}

	public Driver(final String name, final int number, final Car car, final int price) {
		this.number = number;
		this.name = name;
		this.car = car;
		this.price = price;
	}

	@Override
	public boolean equals(final Object otherDriver) {
		if (otherDriver instanceof Driver) {
			if (number == ((Driver) otherDriver).getNumber()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.name.hashCode();
	}
}
