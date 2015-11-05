package net.ddns.f1.domain;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Embeddable
public class DriverPosition {
	@Getter
	@Setter
	@ManyToOne(targetEntity = Driver.class, fetch = FetchType.EAGER)
	private Driver driver;
	@Getter @Setter private int position;
	@Getter @Setter private boolean classified;

	public DriverPosition(final Driver driver, final int position, final boolean classified) {
		this.driver = driver;
		this.position = position;
		this.classified = classified;
	}
}
