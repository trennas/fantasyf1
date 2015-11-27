package net.ddns.f1.domain;

import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class MinimalTeamComponent {	
	private String name;
	private Integer price;
	private Long points;
	
	public MinimalTeamComponent() {			
	}
	
	public MinimalTeamComponent(String name, int price, long points) {
		this.name = name;
		this.price = price;
		this.points = points;
	}
}
