package net.ddns.f1.domain;

import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class MinimalTeamComponent implements Comparable<MinimalTeamComponent> {
	private String name;
	private Integer price;
	private Long points;
	private Integer number;

	public MinimalTeamComponent() {
	}

	public MinimalTeamComponent(final String name, final int price,
			final long points) {
		this.name = name;
		this.price = price;
		this.points = points;
	}
	
	public MinimalTeamComponent(final String name, final int price,
			final long points, Integer number) {
		this.name = name;
		this.price = price;
		this.points = points;
		this.number = number;
	}

	@Override
	public int compareTo(final MinimalTeamComponent o) {
		return name.compareToIgnoreCase(o.getName());
	}
}
