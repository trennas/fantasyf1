package net.ddns.f1.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Value;

import lombok.Data;
import lombok.Getter;

@Entity
public class Team {
	@Id @Getter
	private String name;
	@Getter
	private Driver[] drivers;
	@Getter
	private Car car;
	@Getter
	private Engine engine;
	
	@Value("${budget}")
	private int budget;
	
	@Value("${teamNameRegex}")
	private String teamNameRegex;
	
	public Team(String name, Driver[] drivers, Car car, Engine engine) throws ValidationException {
		validate(name, drivers, car, engine);
	}
	
	private void validate(String name, Driver[] drivers, Car car, Engine engine) throws ValidationException {
		/*
		 * Name cannot be.. stupid
		 * Drivers cannot be equal
		 * Car cannot use chosen engine
		 * Driver cannot use chosen engine
		 * Team must be within budget
		 */
		int cost = 0;
		
		if(name.matches(teamNameRegex)) {
			throw new ValidationException("Name must match the following regex: " + teamNameRegex);
		}
		
		if (drivers[0].equals(drivers[1])) {
			throw new ValidationException("Both drivers are the same");
		}
		
		if(car.getEngine().equals(engine)) {
			throw new ValidationException("Car cannot use chosen engine");
		}
		
		for(Driver driver : drivers) {
			if(driver.getCar().getEngine().equals(engine)) {
				throw new ValidationException("Driver (" + driver.getName() + ") cannot use chosen engine");
			}
			cost += driver.getPrice();
		}
		cost += car.getPrice();
		cost += engine.getPrice();
		
		if(cost > budget) {
			throw new ValidationException("Team is over budget by £" + (cost-budget) + "m");
		}
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
