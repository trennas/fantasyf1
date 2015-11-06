package net.ddns.f1.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Data;

@Data
@Entity
public class Car {
	@Id
	private String name;
	private int price;
	@OneToOne(targetEntity=Engine.class, fetch=FetchType.EAGER)
	private Engine engine;

	public Car() {
	}

	public Car(final String name, final int price, final Engine engine) {
		this.name = name;
		this.price = price;
		this.engine = engine;
	}

	@Override
	public String toString() {
		return getName();
	}
}
