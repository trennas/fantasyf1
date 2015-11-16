package net.ddns.f1.domain;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Transient;

import lombok.Data;
import net.ddns.f1.service.impl.ValidationException;

@Entity
@Data
public class Team  implements Comparable<Team>, PointScorer {
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	int id;
	private String name;	
	private String owner;	
	private String email;
	private String password;
	private String roles;
	
	@Transient
	private String confirmPassword;
	
	@ManyToMany(targetEntity = Driver.class, fetch = FetchType.EAGER)
	@OrderColumn
	private List<Driver> drivers;
	@OneToOne(targetEntity = Car.class, fetch = FetchType.EAGER)
	private Car car;
	@OneToOne(targetEntity = Engine.class, fetch = FetchType.EAGER)
	private Engine engine;

	private long totalPoints;	
	@ElementCollection(fetch = FetchType.EAGER)
	private Map<Integer, Integer> pointsPerEvent = new LinkedHashMap<Integer, Integer>();

	public Team() {
	}

	public Team(final String name, final String owner, final String email, final String password, final List<Driver> drivers, final Car car,
			final Engine engine) throws ValidationException {
		this.name = name;
		this.owner = owner;
		this.email = email;
		this.password = password;
		this.roles = "user";
		this.confirmPassword = password;
		this.drivers = drivers;
		this.car = car;
		this.engine = engine;
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public int compareTo(Team otherTeam) {
		if (this.totalPoints > otherTeam.getTotalPoints()) {
			return -1;
		} else if (this.totalPoints < otherTeam.getTotalPoints()) {
			return 1;
		} else {
			return 0;
		}
	}
}
