package net.ddns.f1.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.ddns.f1.domain.Driver;
import net.ddns.f1.domain.Team;
import net.ddns.f1.repository.TeamRepository;

import org.apache.commons.collections4.IteratorUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class TeamService {
	
	private static final Logger LOG = Logger
			.getLogger(TeamService.class);

	@Autowired
	private TeamRepository teamRepo;
	
	@Autowired
	private InMemoryUserDetailsManager inMemoryUserDetailsManager;
	
	@Autowired
	private Environment environment;

	@Value("${budget}")
	private int budget;
	@Value("${team-name-regex}")
	private String teamNameRegex;
	@Value("#{new java.text.SimpleDateFormat(\"${dateFormat}\").parse(\"${season-start-date-time}\")}")    
	private Date seasonStartDateTime;
	@Value("${num-drivers-per-team}")
	private int numDriversPerTeam;

	public List<Team> getAllTeams() {
		final Iterable<Team> i = teamRepo.findAll();
		return IteratorUtils.toList(i.iterator());
	}
	
	public boolean seasonStarted() {
		return new Date().after(seasonStartDateTime);
	}
	
	private boolean dataCreationProfile() {
		String[] profiles = environment.getActiveProfiles();
		for(String profile : profiles) {
			if(profile.equals("create")) {
				return true;
			}
		}
		return false;
	}

	public void saveTeam(final Team team) throws ValidationException {
		boolean newTeam = team.getId() == null;
		if(seasonStarted()) {
			if(newTeam) {
				if(!dataCreationProfile()) {
					throw new ValidationException("New teams cannot be added once the season has started");
				}
			} else {
				Team prevTeam = teamRepo.findById(team.getId()).get(0);
				if(!prevTeam.getDrivers().get(0).getName().equals(team.getDrivers().get(0).getName()) ||
				   !prevTeam.getDrivers().get(1).getName().equals(team.getDrivers().get(1).getName()) ||
				   !prevTeam.getCar().getName().equals(team.getCar().getName()) ||
				   !prevTeam.getEngine().getName().equals(team.getEngine().getName())) {
					throw new ValidationException("Your team cannot be altered once the season has started");
				}
			}
		}
		validateTeam(team, newTeam);
		teamRepo.save(team);
		setCredentials(team);
	}
	
	private void setCredentials(Team team) {
		try {
			inMemoryUserDetailsManager.deleteUser(team.getEmail());
			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			for(String role : team.getRoles()) {
				authorities.add(new SimpleGrantedAuthority(role));
			}
			inMemoryUserDetailsManager.createUser(new User(team.getEmail(), team.getPassword(), authorities));
		} catch (Exception e) {
			LOG.error("Unable to set credentials for " + team.getEmail());
		}
	}

	public void validateTeam(final Team team, boolean newTeam) throws ValidationException {
		int cost = 0;
		if(team == null) {
			throw new ValidationException(
					"Team is null");
		}
		if(team.getDrivers() == null || team.getDrivers().size() < numDriversPerTeam) {
			throw new ValidationException(
					"You must select " + numDriversPerTeam + " drivers");
		}
		if(team.getCar() == null) {
			throw new ValidationException("You must select a car");
		}
		if(team.getEngine() == null) {
			throw new ValidationException("You must select an engine");
		}
		if(team.getPassword() == null || team.getPassword().isEmpty()) {
			throw new ValidationException(
					"Password cannot be empty");
		} else if(team.getConfirmPassword() == null || !team.getConfirmPassword().equals(team.getPassword())) {
			throw new ValidationException(
					"Passwords don't match");
		}
		if (team.getName() == null || team.getName().isEmpty()) {
			throw new ValidationException(
				"A team name is required");
		}
		if (!team.getName().matches(teamNameRegex)) {
			throw new ValidationException(
					"Name must match the following regex: " + teamNameRegex);
		} else {			
			final List<Team> existingTeams = teamRepo.findByName(team.getName());
			if (newTeam) {
				if(existingTeams.size() > 0) {
					throw new ValidationException(
							"A team already exists with that name");
				}
			} else {
				Team existingTeam = teamRepo.findById(team.getId()).get(0);
				if(!existingTeam.getName().equals(team.getName())) {
					if(existingTeams.size() > 0) {
						throw new ValidationException(
							"A team already exists with that name");
					}
				}
			}
		}

		final List<Driver> drivers = team.getDrivers();

		if (drivers.get(0).equals(drivers.get(1))) {
			throw new ValidationException("Both drivers are the same");
		}

		if (team.getCar().getEngine().equals(team.getEngine())) {
			throw new ValidationException("Car cannot use chosen engine");
		}

		for (final Driver driver : drivers) {
			if (driver.getCar().getEngine().equals(team.getEngine())) {
				throw new ValidationException("Driver (" + driver.getName()
						+ ") cannot use chosen engine");
			}
			cost += driver.getPrice();
		}
		cost += team.getCar().getPrice();
		cost += team.getEngine().getPrice();

		if (cost > budget) {
			throw new ValidationException("Team is over budget by £"
					+ (cost - budget) + "m");
		}
	}
}
