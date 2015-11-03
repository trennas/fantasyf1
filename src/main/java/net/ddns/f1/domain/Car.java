package net.ddns.f1.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
public class Car {
	@Id
	private String name;
	private int price;
	private Engine engine;
	private List<Driver> drivers;
	
	@Override
	public String toString() {
		return getName();
	}
}
