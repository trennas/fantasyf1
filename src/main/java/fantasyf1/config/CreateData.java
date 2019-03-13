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
	public int createF1Data() {
		LOG.info("Creating 2019 League Data...");
		engineRepo.save(new Engine("Mercedes", 23));
		engineRepo.save(new Engine("Renault", 12));
		engineRepo.save(new Engine("Ferrari", 22));
		engineRepo.save(new Engine("Honda", 8));

		carRepo.save(new Car("Mercedes", 30, engineRepo.findByName("Mercedes").get(0)));
		carRepo.save(new Car("Red Bull", 18, engineRepo.findByName("Honda").get(0)));
		carRepo.save(new Car("Ferrari", 26, engineRepo.findByName("Ferrari").get(0)));
		carRepo.save(new Car("Racing Point", 12, engineRepo.findByName("Mercedes").get(0)));
		carRepo.save(new Car("Williams", 8, engineRepo.findByName("Mercedes").get(0)));
		carRepo.save(new Car("Haas F1 Team", 12, engineRepo.findByName("Ferrari").get(0)));
		carRepo.save(new Car("Renault", 11, engineRepo.findByName("Renault").get(0)));
		carRepo.save(new Car("McLaren", 9, engineRepo.findByName("Renault").get(0)));
		carRepo.save(new Car("Toro Rosso", 9, engineRepo.findByName("Honda").get(0)));
		carRepo.save(new Car("Alfa Romeo", 9, engineRepo.findByName("Ferrari").get(0)));

		driverRepo.save(new Driver("Lewis Hamilton", 44, carRepo.findByName("Mercedes").get(0), 34));
		driverRepo.save(new Driver("Valtteri Bottas", 77, carRepo.findByName("Mercedes").get(0), 23));

		driverRepo.save(new Driver("Sebastian Vettel", 5, carRepo.findByName("Ferrari").get(0), 28));
		driverRepo.save(new Driver("Charles Leclerc", 16, carRepo.findByName("Ferrari").get(0), 21));

		driverRepo.save(new Driver("Pierre Gasly", 10, carRepo.findByName("Red Bull").get(0), 14));
		driverRepo.save(new Driver("Max Verstappen", 33, carRepo.findByName("Red Bull").get(0), 20));

		driverRepo.save(new Driver("Sergio Perez", 11, carRepo.findByName("Racing Point").get(0), 9));
		driverRepo.save(new Driver("Lance Stroll", 18, carRepo.findByName("Racing Point").get(0), 8));

		driverRepo.save(new Driver("George Russell", 63, carRepo.findByName("Williams").get(0), 4));
		driverRepo.save(new Driver("Robert Kubica", 88, carRepo.findByName("Williams").get(0), 4));

		driverRepo.save(new Driver("Carlos Sainz", 55, carRepo.findByName("McLaren").get(0), 6));
		driverRepo.save(new Driver("Lando Norris", 4, carRepo.findByName("McLaren").get(0), 5));

		driverRepo.save(new Driver("Alexander Albon", 23, carRepo.findByName("Toro Rosso").get(0), 4));
		driverRepo.save(new Driver("Daniil Kvyat", 26, carRepo.findByName("Toro Rosso").get(0), 6));

		driverRepo.save(new Driver("Romain Grosjean", 8, carRepo.findByName("Haas F1 Team").get(0), 8));
		driverRepo.save(new Driver("Kevin Magnussen", 20, carRepo.findByName("Haas F1 Team").get(0), 8));

		driverRepo.save(new Driver("Nico Hulkenberg", 27, carRepo.findByName("Renault").get(0), 8));
		driverRepo.save(new Driver("Daniel Ricciardo", 3, carRepo.findByName("Renault").get(0), 9));

		driverRepo.save(new Driver("Kimi Raikkonen", 7, carRepo.findByName("Alfa Romeo").get(0), 7));
		driverRepo.save(new Driver("Antonio Giovinazzi", 99, carRepo.findByName("Alfa Romeo").get(0), 6));

		try {
			List<Driver> drivers = new ArrayList<>();
			drivers.add(driverRepo.findByName("Lewis Hamilton").get(0));
			drivers.add(driverRepo.findByName("Max Verstappen").get(0));
			drivers.add(driverRepo.findByName("Sergio Perez").get(0));
			Team team = new Team("Millimetres_Per_Sec_Type'last", "Mike Trenaman", "mike.trenaman@gmail.com", "welcome1", drivers,
					carRepo.findByName("McLaren").get(0), engineRepo.findByName("Honda").get(0));
			teamService.saveTeam(team);
		} catch (final ValidationException e) {
			LOG.info("Team Invalid: " + e.getMessage());
		}
		LOG.info("Complete.");
		return 0;
	}
}
