package net.ddns.f1.service.impl;

import java.util.List;

import net.ddns.f1.domain.Driver;
import net.ddns.f1.domain.Team;
import net.ddns.f1.repository.impl.TeamRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TeamService {

	@Autowired
	TeamRepository teamRepo;

	@Value("${budget}")
	private int budget;
	@Value("${team-name-regex}")
	private String teamNameRegex;

	public void addTeam(final Team team) throws ValidationException {
		validateTeam(team);
		teamRepo.save(team);
	}

	public void validateTeam(final Team team) throws ValidationException {
		int cost = 0;

		if (!team.getName().matches(teamNameRegex)) {
			throw new ValidationException(
					"Name must match the following regex: " + teamNameRegex);
		} else {
			final List<Team> existingTeams = teamRepo.findTeamByName(team
					.getName());
			if (existingTeams.size() > 0) {
				throw new ValidationException(
						"A team already exists with that name");
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
