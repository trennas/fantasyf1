package net.ddns.f1.domain;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Engine implements Comparable<Engine>, PointScorer {
	@Id
	private String name;
	private int price;
	
	private long totalPoints;	
	@ElementCollection(fetch = FetchType.EAGER)
	private Map<Integer, Integer> pointsPerEvent = new LinkedHashMap<Integer, Integer>();
	
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

	@Override
	public int compareTo(Engine o) {
		return this.name.compareToIgnoreCase(o.getName());
	}
}
