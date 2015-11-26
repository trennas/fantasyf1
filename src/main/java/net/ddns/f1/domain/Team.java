package net.ddns.f1.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
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
	Integer id;
	@Column(unique=true)
	private String name;
	
	private boolean theoretical;
	
	private String owner;
	
	@Column(unique=true)
	private String email;
	
	private String password;
	
	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> roles;
	
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
		this.roles = new ArrayList<String>(Arrays.asList("user"));
		this.confirmPassword = password;
		this.drivers = drivers;
		this.car = car;
		this.engine = engine;
		this.theoretical = false;
	}
	
	public Map<Integer, Integer> getPointsPerEvent() {
		if(pointsPerEvent == null) {
			pointsPerEvent = new HashMap<Integer, Integer>();
		}
		return pointsPerEvent;
	}
	
	public void setComponents(Team team) {
		this.setDrivers(team.getDrivers());
		this.setCar(team.getCar());		
		this.setEngine(team.getEngine());
		this.setTotalPoints(team.getTotalPoints());
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
