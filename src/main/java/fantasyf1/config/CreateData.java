package fantasyf1.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import fantasyf1.domain.Car;
import fantasyf1.domain.Driver;
import fantasyf1.domain.Engine;
import fantasyf1.domain.Team;
import fantasyf1.repository.CarRepository;
import fantasyf1.repository.DriverRepository;
import fantasyf1.repository.EngineRepository;
import fantasyf1.service.TeamService;
import fantasyf1.service.impl.ValidationException;

@Configuration
@Profile("create")
public class CreateData {

	private static final Logger LOG = Logger.getLogger(CreateData.class);

	@Autowired
	private DriverRepository driverRepo;
	@Autowired
	private CarRepository carRepo;
	@Autowired
	private EngineRepository engineRepo;
	@Autowired
	private TeamService teamService;

	@Value("${auth.myaccount-role}")
	private String myAccountRole;

	@Bean
	public int create2016Data() {
		LOG.info("Creating 2017 League Data...");
		engineRepo.save(new Engine("Mercedes", 30));
		engineRepo.save(new Engine("Ferrari", 25));
		engineRepo.save(new Engine("Renault", 17));
		engineRepo.save(new Engine("Honda", 8));

		carRepo.save(new Car("Mercedes", 35, engineRepo.findByName("Mercedes").get(0)));
		carRepo.save(new Car("Ferrari", 30, engineRepo.findByName("Ferrari").get(0)));
		carRepo.save(new Car("Williams", 27, engineRepo.findByName("Mercedes").get(0)));
		carRepo.save(new Car("Red Bull", 25, engineRepo.findByName("Renault").get(0)));
		carRepo.save(new Car("Force India", 23, engineRepo.findByName("Mercedes").get(0)));
		carRepo.save(new Car("Renault", 21, engineRepo.findByName("Renault").get(0)));
		carRepo.save(new Car("Toro Rosso", 19, engineRepo.findByName("Ferrari").get(0)));
		carRepo.save(new Car("Sauber", 17, engineRepo.findByName("Ferrari").get(0)));
		carRepo.save(new Car("McLaren", 16, engineRepo.findByName("Honda").get(0)));
		carRepo.save(new Car("Haas F1 Team", 15, engineRepo.findByName("Ferrari").get(0)));

		driverRepo.save(new Driver("Lewis Hamilton", 44, carRepo.findByName("Mercedes").get(0), 40));
		driverRepo.save(new Driver("Valtteri Bottas", 77, carRepo.findByName("Mercedes").get(0), 36));

		driverRepo.save(new Driver("Daniel Ricciardo", 3, carRepo.findByName("Red Bull").get(0), 31));
		driverRepo.save(new Driver("Max Verstappen", 33, carRepo.findByName("Red Bull").get(0), 31));

		driverRepo.save(new Driver("Sebastian Vettel", 5, carRepo.findByName("Ferrari").get(0), 30));
		driverRepo.save(new Driver("Kimi Raikkonen", 7, carRepo.findByName("Ferrari").get(0), 29));

		driverRepo.save(new Driver("Sergio Perez", 11, carRepo.findByName("Force India").get(0), 25));
		driverRepo.save(new Driver("Esteban Ocon", 31, carRepo.findByName("Force India").get(0), 24));

		driverRepo.save(new Driver("Felipe Massa", 19, carRepo.findByName("Williams").get(0), 24));
		driverRepo.save(new Driver("Lance Stroll", 18, carRepo.findByName("Williams").get(0), 23));

		driverRepo.save(new Driver("Fernando Alonso", 14, carRepo.findByName("McLaren").get(0), 21));
		driverRepo.save(new Driver("Stoffel Vandoorne", 47, carRepo.findByName("McLaren").get(0), 20));

		driverRepo.save(new Driver("Carlos Sainz", 55, carRepo.findByName("Toro Rosso").get(0), 19));
		driverRepo.save(new Driver("Daniil Kvyat", 26, carRepo.findByName("Toro Rosso").get(0), 18));

		driverRepo.save(new Driver("Romain Grosjean", 8, carRepo.findByName("Haas F1 Team").get(0), 17));
		driverRepo.save(new Driver("Kevin Magnussen", 20, carRepo.findByName("Haas F1 Team").get(0), 17));

		driverRepo.save(new Driver("Nico Hulkenberg", 27, carRepo.findByName("Renault").get(0), 16));
		driverRepo.save(new Driver("Jolyon Palmer", 30, carRepo.findByName("Renault").get(0), 15));

		driverRepo.save(new Driver("Marcus Ericsson", 9, carRepo.findByName("Sauber").get(0), 14));
		driverRepo.save(new Driver("Pascal Wehrlein", 94, carRepo.findByName("Sauber").get(0), 13));

//		try {
//			List<Driver> drivers = new ArrayList<>();
//			drivers.add(driverRepo.findByName("Lewis Hamilton").get(0));
//			drivers.add(driverRepo.findByName("Daniil Kvyat").get(0));
//			drivers.add(driverRepo.findByName("Fernando Alonso").get(0));
//			Team team = new Team("Brawlers Lifters", "Brawler", "alan.bates2@baesystems.com", "welcome1", drivers,
//					carRepo.findByName("Sauber").get(0), engineRepo.findByName("Honda").get(0));
//			teamService.saveTeam(team);
//		} catch (final ValidationException e) {
//			LOG.info("Team Invalid: " + e.getMessage());
//		}

		LOG.info("Complete.");
		return 0;
	}
}
