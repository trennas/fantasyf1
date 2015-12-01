package net.ddns.f1.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import net.ddns.f1.domain.Driver;
import net.ddns.f1.domain.Team;
import net.ddns.f1.domain.TheoreticalTeam;
import net.ddns.f1.repository.TeamRepository;
import net.ddns.f1.repository.TheoreticalTeamRepository;
import net.ddns.f1.service.TeamService;

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
public class TeamServiceImpl implements TeamService {

	private static final Logger LOG = Logger.getLogger(TeamServiceImpl.class);
	@Autowired
	private TeamRepository teamRepo;

	@Autowired
	private TheoreticalTeamRepository theoreticalTeamRepo;

	@Autowired
	private InMemoryUserDetailsManager inMemoryUserDetailsManager;

	@Autowired
	private Environment environment;

	@Autowired
	ServiceUtils utils;

	@Value("${best-theoretical-team-name}")
	private String bestTheoreticalTeamName;
	@Value("${auth.myaccount-role}")
	private String myAccountRole;
	@Value("${budget}")
	private int budget;
	@Value("${team-name-regex}")
	private String teamNameRegex;
	@Value("${email-regex}")
	private String emailRegex;
	@Value("#{new java.text.SimpleDateFormat(\"${dateFormat}\").parse(\"${season-start-date-time}\")}")
	private Date seasonStartDateTime;
	@Value("${num-drivers-per-team}")
	private int numDriversPerTeam;

	@Override
	public List<Team> getAllRealTeams() {
		return teamRepo.findByTheoretical(false);
	}

	@Override
	public boolean seasonStarted() {
		return new Date().after(seasonStartDateTime);
	}

	private boolean dataCreationProfile() {
		final String[] profiles = environment.getActiveProfiles();
		for (final String profile : profiles) {
			if (profile.equals("create")) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void saveTeam(final Team team) throws ValidationException {
		final boolean newTeam = team.getId() == null;
		team.setTheoretical(false);
		if (seasonStarted()) {
			if (newTeam) {
				if (!dataCreationProfile()) {
					throw new ValidationException(
							"New teams cannot be added once the season has started");
				}
			} else {
				final Team prevTeam = teamRepo.findById(team.getId()).get(0);
				if (!prevTeam.getDrivers().get(0).getName()
						.equals(team.getDrivers().get(0).getName())
						|| !prevTeam.getDrivers().get(1).getName()
								.equals(team.getDrivers().get(1).getName())
						|| !prevTeam.getCar().getName()
								.equals(team.getCar().getName())
						|| !prevTeam.getEngine().getName()
								.equals(team.getEngine().getName())) {
					throw new ValidationException(
							"Your team cannot be altered once the season has started");
				}
			}
		}
		validateTeam(team, newTeam);
		teamRepo.save(team);
		setCredentials(team);
	}

	private void setCredentials(final Team team) {
		if (team.getRoles() == null || team.getRoles().size() == 0) {
			final List<String> roles = Arrays.asList(myAccountRole);
			team.setRoles(roles);
		}
		try {
			inMemoryUserDetailsManager.deleteUser(team.getEmail());
			final List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			for (final String role : team.getRoles()) {
				authorities.add(new SimpleGrantedAuthority(role));
			}
			inMemoryUserDetailsManager.createUser(new User(team.getEmail(),
					team.getPassword(), authorities));
		} catch (final Exception e) {
			LOG.error("Unable to set credentials for " + team.getEmail());
		}
	}

	@Override
	public void validateTeam(final Team team, final boolean newTeam)
			throws ValidationException {
		if (team == null) {
			throw new ValidationException("Team is null");
		}

		if (team.getOwner() == null || team.getOwner().isEmpty()) {
			throw new ValidationException("Your name is required");
		}
		if (team.getEmail() == null || team.getEmail().isEmpty()) {
			throw new ValidationException("Your E-Mail address is required");
		} else if (!team.getEmail().matches(emailRegex)) {
			throw new ValidationException("Invalid E-Mail address");
		}
		if (team.getPassword() == null || team.getPassword().isEmpty()) {
			throw new ValidationException("Password cannot be empty");
		} else if (team.getConfirmPassword() == null
				|| !team.getConfirmPassword().equals(team.getPassword())) {
			throw new ValidationException("Passwords don't match");
		}
		if (team.getName() == null || team.getName().isEmpty()) {
			throw new ValidationException("A team name is required");
		}
		if (!team.getName().matches(teamNameRegex)) {
			throw new ValidationException(
					"Name must match the following regex: " + teamNameRegex);
		} else if (team.getName().equalsIgnoreCase(bestTheoreticalTeamName)) {
			throw new ValidationException("That team name is reserved");
		} else {
			final List<Team> existingTeams = teamRepo
					.findByName(team.getName());
			for (final Team existingTeam : existingTeams) {
				if (newTeam || existingTeam.getId() != team.getId()) {
					throw new ValidationException(
							"A team already exists with that name");
				}
			}
		}

		final List<Team> existingTeams = teamRepo.findByEmail(team.getEmail());
		for (final Team existingTeam : existingTeams) {
			if (newTeam || existingTeam.getId() != team.getId()) {
				throw new ValidationException(
						"A team has already been registered with that E-Mail address");
			}
		}
		validateTeamComponents(team);
	}

	@Override
	public void validateTeamComponents(final Team team)
			throws ValidationException {
		int cost = 0;
		if (team.getDrivers() == null
				|| team.getDrivers().size() < numDriversPerTeam) {
			throw new ValidationException("You must select "
					+ numDriversPerTeam + " drivers");
		}
		if (team.getCar() == null) {
			throw new ValidationException("You must select a car");
		}
		if (team.getEngine() == null) {
			throw new ValidationException("You must select an engine");
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

	@Override
	public void delete(final int id) {
		try {
			inMemoryUserDetailsManager.deleteUser(findById(id).getEmail());
			teamRepo.delete(id);
		} catch (final Ff1Exception e) {
			LOG.error("Couldn't delete team id: " + id
					+ " as it couldn't be determine. " + e.getMessage());
		}
	}

	@Override
	public Team findByEmail(final String email) throws Ff1Exception {
		return utils.get(teamRepo.findByEmail(email), email);
	}

	@Override
	public Team findById(final int id) throws Ff1Exception {
		return utils.get(teamRepo.findById(id), Integer.toBinaryString(id));
	}

	@Override
	public List<Team> findAll() {
		return IteratorUtils.toList(teamRepo.findAll().iterator());
	}

	@Override
	public void deleteAllTheoreticalTeams() {
		theoreticalTeamRepo.deleteAll();
	}

	@Override
	public TheoreticalTeam findTheoreticalTeamByName(final String name)
			throws Ff1Exception {
		return utils.get(theoreticalTeamRepo.findByName(name), name);
	}

	@Override
	public void saveTheoreticalTeam(final TheoreticalTeam team) {
		theoreticalTeamRepo.save(team);
	}
}
