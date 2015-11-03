package net.ddns.f1.config;

import java.util.ArrayList;
import java.util.List;

import net.ddns.f1.domain.Car;
import net.ddns.f1.domain.Driver;
import net.ddns.f1.domain.Engine;
import net.ddns.f1.domain.FullName;
import net.ddns.f1.domain.Team;
import net.ddns.f1.domain.ValidationException;
import net.ddns.f1.repository.CarRepository;
import net.ddns.f1.repository.DriverRepository;
import net.ddns.f1.repository.EngineRepository;
import net.ddns.f1.repository.TeamRepository;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class DummyData {

	private static final Logger LOG = Logger.getLogger(DummyData.class);

	@Autowired
	DriverRepository driverRepo;
	@Autowired
	CarRepository carRepo;
	@Autowired
	EngineRepository engineRepo;
	@Autowired
	TeamRepository teamRepo;

	@Transactional
	@Bean
	public int createDummyData() {
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

		driverRepo.save(new Driver(new FullName("Lewis Hamilton"), carRepo.findCarByName("Mercedes").get(0), 27));
		driverRepo.save(new Driver(new FullName("Nico Rosberg"), carRepo.findCarByName("Mercedes").get(0), 25));
		driverRepo.save(new Driver(new FullName("Sebastian Vettel"), carRepo.findCarByName("Ferrari").get(0), 19));
		driverRepo.save(new Driver(new FullName("Kimi Räikkönen"), carRepo.findCarByName("Ferrari").get(0), 17));
		driverRepo.save(new Driver(new FullName("Daniel Ricciardo"), carRepo.findCarByName("Red Bull").get(0), 21));
		driverRepo.save(new Driver(new FullName("Daniil Kvyat"), carRepo.findCarByName("Red Bull").get(0), 17));
		driverRepo.save(new Driver(new FullName("Felipe Massa"), carRepo.findCarByName("Williams").get(0), 18));
		driverRepo.save(new Driver(new FullName("Valtteri Bottas"), carRepo.findCarByName("Williams").get(0), 20));
		driverRepo.save(new Driver(new FullName("Nico Hulkenberg"), carRepo.findCarByName("Force India").get(0), 14));
		driverRepo.save(new Driver(new FullName("Sergio Perez"), carRepo.findCarByName("Force India").get(0), 13));
		driverRepo.save(new Driver(new FullName("Max Verstappen"), carRepo.findCarByName("Toro Rosso").get(0), 7));
		driverRepo.save(new Driver(new FullName("Carlos Sainz"), carRepo.findCarByName("Toro Rosso").get(0), 6));
		driverRepo.save(new Driver(new FullName("Romain Grosjean"), carRepo.findCarByName("Lotus").get(0), 10));
		driverRepo.save(new Driver(new FullName("Pastor Maldonado"), carRepo.findCarByName("Lotus").get(0), 8));
		driverRepo.save(new Driver(new FullName("Marcus Ericsson"), carRepo.findCarByName("Sauber").get(0), 5));
		driverRepo.save(new Driver(new FullName("Felipe Nasr"), carRepo.findCarByName("Sauber").get(0), 4));
		driverRepo.save(new Driver(new FullName("Fernando Alonso"), carRepo.findCarByName("Mclaren").get(0), 16));
		driverRepo.save(new Driver(new FullName("Jenson Button"), carRepo.findCarByName("Mclaren").get(0), 15));
		driverRepo.save(new Driver(new FullName("Will Stevens"), carRepo.findCarByName("Manor").get(0), 1));
		driverRepo.save(new Driver(new FullName("Roberto Merhi"), carRepo.findCarByName("Manor").get(0), 1));
		driverRepo.save(new Driver(new FullName("Alexander Rossi"), carRepo.findCarByName("Manor").get(0), 1));

		try {
			final List<Driver> drivers = new ArrayList<Driver>();
			drivers.add(driverRepo.findDriverByName(new FullName("Sebastian Vettel")).get(0));
			drivers.add(driverRepo.findDriverByName(new FullName("Carlos Sainz")).get(0));
			final Car car = carRepo.findCarByName("Manor").get(0);
			final Engine eng = engineRepo.findEngineByName("Mercedes").get(0);
			teamRepo.save(new Team("Fast But Bad Manors", drivers, car, eng));
		} catch (final ValidationException e) {
			LOG.info("Team Invalid: " + e.getMessage());
		}
		return 0;
	}
}
