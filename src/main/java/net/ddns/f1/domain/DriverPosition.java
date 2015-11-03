package net.ddns.f1.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class DriverPosition {
	@Getter @Setter private Driver driver;
	@Getter @Setter private int position;
	@Getter @Setter private boolean classified;
	
	public DriverPosition(Driver driver, int position, boolean classified) {
		this.driver = driver;
		this.position = position;
		this.classified = classified;
	}
}
