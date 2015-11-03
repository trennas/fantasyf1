package net.ddns.f1.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class Engine {
	@Id
	private String name;
	private int price;
	@ManyToOne(targetEntity=Car.class, fetch=FetchType.EAGER)
	private List<Car> cars;

	public Engine() {
	}

	public Engine(final String name, final int price) {
		this.name = name;
		this.price = price;
	}

	@Override
	public boolean equals(final Object otherDriver) {
		if(otherDriver instanceof Engine) {
			if(this.name.equalsIgnoreCase(((Engine) otherDriver).getName())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.name.hashCode();
	}

	@Override
	public String toString() {
		return getName();
	}
}
