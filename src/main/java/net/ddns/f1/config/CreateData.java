package net.ddns.f1.config;

import java.util.ArrayList;
import java.util.List;

import net.ddns.f1.domain.Car;
import net.ddns.f1.domain.Correction;
import net.ddns.f1.domain.Driver;
import net.ddns.f1.domain.Engine;
import net.ddns.f1.domain.Team;
import net.ddns.f1.repository.CarRepository;
import net.ddns.f1.repository.CorrectionRepository;
import net.ddns.f1.repository.DriverRepository;
import net.ddns.f1.repository.EngineRepository;
import net.ddns.f1.service.impl.TeamServiceImpl;
import net.ddns.f1.service.impl.ValidationException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("create")
@Configuration
public class CreateData {

	private static final Logger LOG = Logger.getLogger(CreateData.class);

	@Autowired
	DriverRepository driverRepo;
	@Autowired
	CarRepository carRepo;
	@Autowired
	EngineRepository engineRepo;
	@Autowired
	CorrectionRepository correctionRepo;
	@Autowired
	TeamServiceImpl teamService;

	@Value("${auth.myaccount-role}")
	private String myAccountRole;

	@Value("${auth.admin-role}")
	private String adminRole;
	
	@Profile("createAdmin")
	@Bean
	public int admin() {
		Team team = new Team(null, null,
				"mike.trenaman@gmail.com", "welcome1", null, null, null);
		team.getRoles().add(adminRole);
		teamService.saveTeamNoValidation(team);
		return 0;
	}

	@Profile("create2015")
	@Bean
	public int createDummyData() {
		LOG.info("Creating League Data...");
		engineRepo.save(new Engine("Mercedes", 30));
		engineRepo.save(new Engine("Ferrari", 20));
		engineRepo.save(new Engine("Renault", 19));
		engineRepo.save(new Engine("Honda", 14));

		carRepo.save(new Car("Mercedes", 25, engineRepo.findByName("Mercedes")
				.get(0)));
		carRepo.save(new Car("Red Bull", 20, engineRepo.findByName("Renault")
				.get(0)));
		carRepo.save(new Car("Williams", 19, engineRepo.findByName("Mercedes")
				.get(0)));
		carRepo.save(new Car("Ferrari", 18, engineRepo.findByName("Ferrari")
				.get(0)));
		carRepo.save(new Car("Mclaren", 14, engineRepo.findByName("Honda").get(
				0)));
		carRepo.save(new Car("Force India", 13, engineRepo.findByName(
				"Mercedes").get(0)));
		carRepo.save(new Car("Toro Rosso", 12, engineRepo.findByName("Renault")
				.get(0)));
		carRepo.save(new Car("Lotus", 11, engineRepo.findByName("Mercedes")
				.get(0)));
		carRepo.save(new Car("Sauber", 8, engineRepo.findByName("Ferrari").get(
				0)));
		carRepo.save(new Car("Manor", 2, engineRepo.findByName("Ferrari")
				.get(0)));

		driverRepo.save(new Driver("Lewis Hamilton", 44, carRepo.findByName(
				"Mercedes").get(0), 27, false));
		driverRepo.save(new Driver("Nico Rosberg", 6, carRepo.findByName(
				"Mercedes").get(0), 25, false));
		driverRepo.save(new Driver("Sebastian Vettel", 5, carRepo.findByName(
				"Ferrari").get(0), 19, false));
		driverRepo.save(new Driver("Kimi Raikkonen", 7, carRepo.findByName(
				"Ferrari").get(0), 17, false));
		driverRepo.save(new Driver("Daniel Ricciardo", 3, carRepo.findByName(
				"Red Bull").get(0), 21, false));
		driverRepo.save(new Driver("Daniil Kvyat", 26, carRepo.findByName(
				"Red Bull").get(0), 17, false));
		driverRepo.save(new Driver("Felipe Massa", 19, carRepo.findByName(
				"Williams").get(0), 18, false));
		driverRepo.save(new Driver("Valtteri Bottas", 77, carRepo.findByName(
				"Williams").get(0), 20, false));
		driverRepo.save(new Driver("Nico Hulkenberg", 27, carRepo.findByName(
				"Force India").get(0), 14, false));
		driverRepo.save(new Driver("Sergio Perez", 11, carRepo.findByName(
				"Force India").get(0), 13, false));
		driverRepo.save(new Driver("Max Verstappen", 33, carRepo.findByName(
				"Toro Rosso").get(0), 7, false));
		driverRepo.save(new Driver("Carlos Sainz", 55, carRepo.findByName(
				"Toro Rosso").get(0), 6, false));
		driverRepo.save(new Driver("Romain Grosjean", 8, carRepo.findByName(
				"Lotus").get(0), 10, false));
		driverRepo.save(new Driver("Pastor Maldonado", 13, carRepo.findByName(
				"Lotus").get(0), 8, false));
		driverRepo.save(new Driver("Marcus Ericsson", 9, carRepo.findByName(
				"Sauber").get(0), 5, false));
		driverRepo.save(new Driver("Felipe Nasr", 12, carRepo.findByName(
				"Sauber").get(0), 4, false));
		driverRepo.save(new Driver("Fernando Alonso", 14, carRepo.findByName(
				"Mclaren").get(0), 16, false));
		driverRepo.save(new Driver("Jenson Button", 22, carRepo.findByName(
				"Mclaren").get(0), 15, false));
		driverRepo.save(new Driver("Will Stevens", 28, carRepo.findByName(
				"Manor").get(0), 1, false));
		driverRepo.save(new Driver("Roberto Merhi", 98, carRepo.findByName(
				"Manor").get(0), 1, false));

		driverRepo.save(new Driver("Kevin Magnussen", 20, carRepo.findByName(
				"Mclaren").get(0), 0, true));
		driverRepo.save(new Driver("Alexander Rossi", 53, carRepo.findByName(
				"Manor").get(0), 0, true));

		Correction correction = new Correction(2, driverRepo.findByNumber(98).get(0).getName(), 98, 19, true,
				15, true);
		List<String> remarks = new ArrayList<String>();
		remarks.add("Roberto Merhi not classified in qualifying (Q1 time was outside 107%)");
		correction.setRemarks(remarks);
		correctionRepo.save(correction);

		correctionRepo.save(new Correction(3, driverRepo.findByNumber(33).get(0).getName(), 33, 13, true, 17,
				false));
		correctionRepo.save(new Correction(10, driverRepo.findByNumber(28).get(0).getName(), 28, 20, true, 16,
				false));

		correction = new Correction(12, driverRepo.findByNumber(33).get(0).getName(), 33, 20, true, 12, true);
		remarks = new ArrayList<String>();
		remarks.add("Max Verstappen did not set a qualifying time");
		correction.setRemarks(remarks);
		correctionRepo.save(correction);

		correctionRepo.save(new Correction(12, driverRepo.findByNumber(6).get(0).getName(), 6, 4, true, 17,
				false));
		correctionRepo.save(new Correction(12, driverRepo.findByNumber(14).get(0).getName(), 14, 17, true, 18,
				false));

		correction = new Correction(14, driverRepo.findByNumber(53).get(0).getName(), 53, 20, true, 18, true);
		remarks = new ArrayList<String>();
		remarks.add("Alexander Rossi not classified in qualifying (Q1 time was outside 107%)");
		correction.setRemarks(remarks);
		correctionRepo.save(correction);

		correctionRepo.save(new Correction(14, driverRepo.findByNumber(12).get(0).getName(), 12, 18, true, 20,
				true));

		try {
			List<Driver> drivers = new ArrayList<Driver>();
			drivers.add(driverRepo.findByName("Lewis Hamilton").get(0));
			drivers.add(driverRepo.findByName("Felipe Nasr").get(0));
			Team team = new Team("Arrowmaker", "Pete Garratt",
					"peter.garratt@baesystems.com", "welcome1", drivers,
					carRepo.findByName("Sauber").get(0), engineRepo.findByName(
							"Renault").get(0));
			teamService.saveTeam(team);

			drivers = new ArrayList<Driver>();
			drivers.add(driverRepo.findByName("Lewis Hamilton").get(0));
			drivers.add(driverRepo.findByName("Daniil Kvyat").get(0));
			team = new Team("Brawlsy's Lifters", "Alan Bates",
					"alan.bates2@baesystems.com", "welcome1", drivers, carRepo
							.findByName("Manor").get(0), engineRepo.findByName(
							"Honda").get(0));
			teamService.saveTeam(team);

			drivers = new ArrayList<Driver>();
			drivers.add(driverRepo.findByName("Sebastian Vettel").get(0));
			drivers.add(driverRepo.findByName("Will Stevens").get(0));
			team = new Team("Can't be any worse than last year!",
					"Andy Rousell", "andrew.rousell@baesystems.com",
					"welcome1", drivers, carRepo.findByName("Sauber").get(0),
					engineRepo.findByName("Mercedes").get(0));
			teamService.saveTeam(team);

			drivers = new ArrayList<Driver>();
			drivers.add(driverRepo.findByName("Nico Rosberg").get(0));
			drivers.add(driverRepo.findByName("Nico Hulkenberg").get(0));
			team = new Team("Dodgy Sparkplugs", "Matt Horsley",
					"matt@durge.org", "welcome1", drivers, carRepo.findByName(
							"Manor").get(0), engineRepo.findByName("Renault")
							.get(0));
			teamService.saveTeam(team);

			drivers = new ArrayList<Driver>();
			drivers.add(driverRepo.findByName("Sebastian Vettel").get(0));
			drivers.add(driverRepo.findByName("Carlos Sainz").get(0));
			team = new Team("Fast But Bad Manors", "Mike Trenaman",
					"mike.trenaman@gmail.com", "welcome1", drivers, carRepo
							.findByName("Manor").get(0), engineRepo.findByName(
							"Mercedes").get(0));
			team.getRoles().add(adminRole);
			teamService.saveTeam(team);

			drivers = new ArrayList<Driver>();
			drivers.add(driverRepo.findByName("Nico Rosberg").get(0));
			drivers.add(driverRepo.findByName("Sebastian Vettel").get(0));
			team = new Team("FIRO de formaggio", "Gary Comer",
					"gary.comer@baesystems.com", "welcome1", drivers, carRepo
							.findByName("Manor").get(0), engineRepo.findByName(
							"Honda").get(0));
			teamService.saveTeam(team);

			drivers = new ArrayList<Driver>();
			drivers.add(driverRepo.findByName("Nico Rosberg").get(0));
			drivers.add(driverRepo.findByName("Pastor Maldonado").get(0));
			team = new Team("Fishy Fliers", "Dave Mccune",
					"david.mccune@baesystems.com", "welcome1", drivers, carRepo
							.findByName("Force India").get(0), engineRepo
							.findByName("Honda").get(0));
			teamService.saveTeam(team);

			drivers = new ArrayList<Driver>();
			drivers.add(driverRepo.findByName("Daniel Ricciardo").get(0));
			drivers.add(driverRepo.findByName("Will Stevens").get(0));
			team = new Team("Sweet & Sauber", "Jane Trenaman",
					"jane.trenaman@yahoo.co.uk", "welcome1", drivers, carRepo
							.findByName("Sauber").get(0), engineRepo
							.findByName("Mercedes").get(0));
			teamService.saveTeam(team);

		} catch (final ValidationException e) {
			LOG.info("Team Invalid: " + e.getMessage());
		}
		LOG.info("Complete.");
		return 0;
	}
}
