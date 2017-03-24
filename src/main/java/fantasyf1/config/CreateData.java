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
		LOG.info("Creating 2017 League Data...");
		engineRepo.save(new Engine("Mercedes", 30));
		engineRepo.save(new Engine("Renault", 19));
		engineRepo.save(new Engine("Ferrari", 15));
		engineRepo.save(new Engine("Honda", 5));

		carRepo.save(new Car("Mercedes", 34, engineRepo.findByName("Mercedes").get(0)));
		carRepo.save(new Car("Red Bull", 23, engineRepo.findByName("Renault").get(0)));
		carRepo.save(new Car("Ferrari", 20, engineRepo.findByName("Ferrari").get(0)));
		carRepo.save(new Car("Force India", 15, engineRepo.findByName("Mercedes").get(0)));
		carRepo.save(new Car("Williams", 14, engineRepo.findByName("Mercedes").get(0)));
		carRepo.save(new Car("McLaren", 12, engineRepo.findByName("Honda").get(0)));
		carRepo.save(new Car("Toro Rosso", 11, engineRepo.findByName("Renault").get(0)));
		carRepo.save(new Car("Haas F1 Team", 10, engineRepo.findByName("Ferrari").get(0)));
		carRepo.save(new Car("Renault", 9, engineRepo.findByName("Renault").get(0)));		
		carRepo.save(new Car("Sauber", 8, engineRepo.findByName("Ferrari").get(0)));

		driverRepo.save(new Driver("Lewis Hamilton", 44, carRepo.findByName("Mercedes").get(0), 38));
		driverRepo.save(new Driver("Valtteri Bottas", 77, carRepo.findByName("Mercedes").get(0), 36));

		driverRepo.save(new Driver("Daniel Ricciardo", 3, carRepo.findByName("Red Bull").get(0), 26));
		driverRepo.save(new Driver("Max Verstappen", 33, carRepo.findByName("Red Bull").get(0), 24));

		driverRepo.save(new Driver("Sebastian Vettel", 5, carRepo.findByName("Ferrari").get(0), 22));
		driverRepo.save(new Driver("Kimi Raikkonen", 7, carRepo.findByName("Ferrari").get(0), 20));

		driverRepo.save(new Driver("Sergio Perez", 11, carRepo.findByName("Force India").get(0), 16));
		driverRepo.save(new Driver("Esteban Ocon", 31, carRepo.findByName("Force India").get(0), 14));

		driverRepo.save(new Driver("Felipe Massa", 19, carRepo.findByName("Williams").get(0), 12));
		driverRepo.save(new Driver("Lance Stroll", 18, carRepo.findByName("Williams").get(0), 10));

		driverRepo.save(new Driver("Fernando Alonso", 14, carRepo.findByName("McLaren").get(0), 12));
		driverRepo.save(new Driver("Stoffel Vandoorne", 2, carRepo.findByName("McLaren").get(0), 10));

		driverRepo.save(new Driver("Carlos Sainz", 55, carRepo.findByName("Toro Rosso").get(0), 11));
		driverRepo.save(new Driver("Daniil Kvyat", 26, carRepo.findByName("Toro Rosso").get(0), 10));

		driverRepo.save(new Driver("Romain Grosjean", 8, carRepo.findByName("Haas F1 Team").get(0), 10));
		driverRepo.save(new Driver("Kevin Magnussen", 20, carRepo.findByName("Haas F1 Team").get(0), 9));

		driverRepo.save(new Driver("Nico Hulkenberg", 27, carRepo.findByName("Renault").get(0), 8));
		driverRepo.save(new Driver("Jolyon Palmer", 30, carRepo.findByName("Renault").get(0), 7));

		driverRepo.save(new Driver("Marcus Ericsson", 9, carRepo.findByName("Sauber").get(0), 6));
		driverRepo.save(new Driver("Pascal Wehrlein", 94, carRepo.findByName("Sauber").get(0), 5));

		try {
			List<Driver> drivers = new ArrayList<>();
			drivers.add(driverRepo.findByName("Lewis Hamilton").get(0));
			drivers.add(driverRepo.findByName("Sebastian Vettel").get(0));
			drivers.add(driverRepo.findByName("Nico Hulkenberg").get(0));
			Team team = new Team("Millimetres_Per_Sec_Type'last", "Mike Trenaman", "mike.trenaman@gmail.com", "welcome1", drivers,
					carRepo.findByName("Toro Rosso").get(0), engineRepo.findByName("Honda").get(0));
			teamService.saveTeam(team);
		} catch (final ValidationException e) {
			LOG.info("Team Invalid: " + e.getMessage());
		}

		LOG.info("Complete.");
		return 0;
	}
}
