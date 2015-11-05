package net.ddns.f1.config;

import java.util.ArrayList;
import java.util.List;

import net.ddns.f1.domain.Car;
import net.ddns.f1.domain.Driver;
import net.ddns.f1.domain.Engine;
import net.ddns.f1.domain.Team;
import net.ddns.f1.repository.CarRepository;
import net.ddns.f1.repository.DriverRepository;
import net.ddns.f1.repository.EngineRepository;
import net.ddns.f1.service.impl.TeamService;
import net.ddns.f1.service.impl.ValidationException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@Profile("create")
public class CreateData {

	private static final Logger LOG = Logger.getLogger(CreateData.class);

	@Autowired
	DriverRepository driverRepo;
	@Autowired
	CarRepository carRepo;
	@Autowired
	EngineRepository engineRepo;
	@Autowired
	TeamService teamService;

	@Transactional
	@Bean
	public int createDummyData() {
		LOG.info("Creating League Data...");
		engineRepo.save(new Engine("Mercedes", 30));
		engineRepo.save(new Engine("Ferrari", 20));
		engineRepo.save(new Engine("Renault", 19));
		engineRepo.save(new Engine("Honda", 14));

		carRepo.save(new Car("Mercedes", 25, engineRepo.findEngineByName("Mercedes").get(0)));
		carRepo.save(new Car("Red Bull", 20, engineRepo.findEngineByName("Renault").get(0)));
		carRepo.save(new Car("Williams", 19, engineRepo.findEngineByName("Mercedes").get(0)));
		carRepo.save(new Car("Ferrari", 18, engineRepo.findEngineByName("Ferrari").get(0)));
		carRepo.save(new Car("Mclaren", 14, engineRepo.findEngineByName("Honda").get(0)));
		carRepo.save(new Car("Force India", 13, engineRepo.findEngineByName("Mercedes").get(0)));
		carRepo.save(new Car("Toro Rosso", 12, engineRepo.findEngineByName("Renault").get(0)));
		carRepo.save(new Car("Lotus", 11, engineRepo.findEngineByName("Mercedes").get(0)));
		carRepo.save(new Car("Sauber", 8, engineRepo.findEngineByName("Ferrari").get(0)));
		carRepo.save(new Car("Manor", 2, engineRepo.findEngineByName("Ferrari").get(0)));

		driverRepo.save(new Driver("Lewis Hamilton", 44, carRepo.findCarByName("Mercedes").get(0), 27));
		driverRepo.save(new Driver("Nico Rosberg", 6, carRepo.findCarByName("Mercedes").get(0), 25));
		driverRepo.save(new Driver("Sebastian Vettel", 5, carRepo.findCarByName("Ferrari").get(0), 19));
		driverRepo.save(new Driver("Kimi Räikkönen", 7, carRepo.findCarByName("Ferrari").get(0), 17));
		driverRepo.save(new Driver("Daniel Ricciardo", 3, carRepo.findCarByName("Red Bull").get(0), 21));
		driverRepo.save(new Driver("Daniil Kvyat", 26, carRepo.findCarByName("Red Bull").get(0), 17));
		driverRepo.save(new Driver("Felipe Massa", 19, carRepo.findCarByName("Williams").get(0), 18));
		driverRepo.save(new Driver("Valtteri Bottas", 77, carRepo.findCarByName("Williams").get(0), 20));
		driverRepo.save(new Driver("Nico Hülkenberg", 27, carRepo.findCarByName("Force India").get(0), 14));
		driverRepo.save(new Driver("Sergio Pérez", 11, carRepo.findCarByName("Force India").get(0), 13));
		driverRepo.save(new Driver("Max Verstappen", 33, carRepo.findCarByName("Toro Rosso").get(0), 7));
		driverRepo.save(new Driver("Carlos Sainz", 55, carRepo.findCarByName("Toro Rosso").get(0), 6));
		driverRepo.save(new Driver("Romain Grosjean", 8, carRepo.findCarByName("Lotus").get(0), 10));
		driverRepo.save(new Driver("Pastor Maldonado", 13, carRepo.findCarByName("Lotus").get(0), 8));
		driverRepo.save(new Driver("Marcus Ericsson", 9, carRepo.findCarByName("Sauber").get(0), 5));
		driverRepo.save(new Driver("Felipe Nasr", 12, carRepo.findCarByName("Sauber").get(0), 4));
		driverRepo.save(new Driver("Fernando Alonso", 14, carRepo.findCarByName("Mclaren").get(0), 16));
		driverRepo.save(new Driver("Jenson Button", 22, carRepo.findCarByName("Mclaren").get(0), 15));
		driverRepo.save(new Driver("Will Stevzens", 28, carRepo.findCarByName("Manor").get(0), 1));
		driverRepo.save(new Driver("Roberto Merhi", 98, carRepo.findCarByName("Manor").get(0), 1));
		driverRepo.save(new Driver("Alexander Rossi", 53, carRepo.findCarByName("Manor").get(0), 1));

		try {
			final List<Driver> drivers = new ArrayList<Driver>();
			drivers.add(driverRepo.findByNumber(44).get(0));
			drivers.add(driverRepo.findByNumber(55).get(0));
			final Car car = carRepo.findCarByName("Manor").get(0);
			final Engine eng = engineRepo.findEngineByName("Mercedes").get(0);
			final Team team = new Team("Fast But Bad Manors", drivers, car, eng);
			teamService.addTeam(team);
		} catch (final ValidationException e) {
			LOG.info("Team Invalid: " + e.getMessage());
		}
		LOG.info("Complete.");
		return 0;
	}
}
