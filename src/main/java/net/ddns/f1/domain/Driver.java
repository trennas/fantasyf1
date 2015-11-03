package net.ddns.f1.domain;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class Driver {
	@EmbeddedId
	private FullName name;
//	@Id
//	private String name;

	private int price;
	@ManyToOne(targetEntity = Car.class, fetch = FetchType.EAGER)
	private Car car;

	public Driver() {
	}

	public Driver(final FullName name, final Car car, final int price) {
		this.name = name;
		this.car = car;
		this.price = price;
	}

	@Override
	public boolean equals(final Object otherDriver) {
		if (otherDriver instanceof Driver) {
			if (this.getName().equals(
					((Driver) otherDriver).getName())) {
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
