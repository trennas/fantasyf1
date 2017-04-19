package fantasyf1.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import fantasyf1.domain.AutogeneratedPK;
import fantasyf1.domain.Car;
import fantasyf1.domain.Driver;
import fantasyf1.domain.Engine;
import fantasyf1.domain.PointScorer;
import fantasyf1.domain.SeasonInformation;
import fantasyf1.repository.CarRepository;
import fantasyf1.repository.DriverRepository;
import fantasyf1.repository.EngineRepository;
import fantasyf1.repository.SeasonInformationRepository;
import fantasyf1.service.ComponentService;
import lombok.NonNull;

@Service
public class ComponentServiceImpl implements ComponentService {
	@Value("${season}")
	private String season;

	@Autowired
	private DriverRepository driverRepo;

	@Autowired
	private CarRepository carRepo;

	@Autowired
	private EngineRepository engineRepo;

	@Autowired
	private SeasonInformationRepository seasonRepo;

	@Autowired
	private ServiceUtils utils;
	
	@Override
	public Map<String, Long> getAllDriverPoints() {
		return getAllPoints(driverRepo.findAllByOrderByTotalPointsDesc());
	}

	@Override
	public Map<String, Long> getAllCarPoints() {
		return getAllPoints(carRepo.findAllByOrderByTotalPointsDesc());	
	}

	@Override
	public Map<String, Long> getAllEnginePoints() {
		return getAllPoints(engineRepo.findAllByOrderByTotalPointsDesc());
	}
	
	private Map<String, Long> getAllPoints(Iterable<? extends PointScorer> itr) {
		final Map<String, Long> components = new LinkedHashMap<>();		
		itr.forEach(component -> components.put(component.getName(), component.getTotalPoints()));
		return components;
	}

	@Override
	public Driver findDriverByName(final String name) {
		return utils.get(driverRepo.findByName(name), name);
	}

	@Override
	public Driver findDriverByNumber(final int number) {
		return utils.get(driverRepo.findByNumber(number),
				Integer.toString(number));
	}

	@Override
	public List<Driver> findDriversByStandin(final boolean standIn) {
		return driverRepo.findByStandin(standIn);
	}
	
	@Override
	public void deleteDriver(final Driver driver) {
		driverRepo.delete(driver);
	}
	
	@Override
	public void saveStandinDriver(final Driver standinDriver) {
		standinDriver.setStandin(true);
		standinDriver.setPrice(0);
		driverRepo.save(standinDriver);
	}
	
	@Override
	public Map<Integer, String> getDriverNameMap() {
		final Iterator<Driver> itr = driverRepo.findAll().iterator();
		final Map<Integer, String> driverMap = new HashMap<>();
		while(itr.hasNext()) {
			final Driver driver = itr.next();
			driverMap.put(driver.getNumber(), driver.getName());
		}
		return driverMap;
	}

	@Override
	public List<Driver> findDriversByCar(final Car car) {
		return driverRepo.findByCar(car);
	}

	@Override
	public List<Driver> findDriversByCarAndStandin(final Car car,
			final boolean standIn) {
		return driverRepo.findByCarAndStandin(car, true);
	}
	
	@Override
	public void saveDrivers(List<Driver> drivers, final boolean merge) {
		if(!merge) {
			removeDeleted(findAllDrivers(), drivers, driverRepo);
		}
		driverRepo.save(drivers);
	}
	
	private <T extends AutogeneratedPK> void removeDeleted(List<T> existingList, List<T> updatedList,
			CrudRepository<T, String> repo) {
		for(T existing : existingList) {
			boolean found = false;
			
			for(T updated : updatedList) {
				if (existing.getId() == updated.getId()) {
					found = true;
					break;
				}
			}
			
			if(!found) {
				repo.delete(existing);
			}
		}
	}
	
	@Override
	public void saveCars(List<Car> cars, final boolean merge) {
		if(!merge) {
			removeDeleted(findAllCars(), cars, carRepo);
		}
		carRepo.save(cars);
	}
	
	@Override
	public void saveEngines(List<Engine> engines, final boolean merge) {
		if(!merge) {
			removeDeleted(findAllEngines(), engines, engineRepo);
		}
		engineRepo.save(engines);
	}

	@Override
	public Car findCarById(final Integer id) {
		return utils.get(carRepo.findById(id), Integer.toString(id));
	}

	@Override
	public Car findCarByName(final String name) {
		return utils.get(carRepo.findByName(name), name);
	}

	@Override
	public List<Car> findCarsByEngine(final Engine engine) {
		return carRepo.findByEngine(engine);
	}

	@Override
	public Engine findEngineByName(final String name) {
		return utils.get(engineRepo.findByName(name), name);
	}

	@Override
	public List<Driver> findAllDrivers() {
		return IteratorUtils.toList(driverRepo.findAll().iterator());
	}

	@Override
	public List<Car> findAllCars() {
		return IteratorUtils.toList(carRepo.findAll().iterator());
	}

	@Override
	public List<Engine> findAllEngines() {
		return IteratorUtils.toList(engineRepo.findAll().iterator());
	}

	@Override
	public void saveDriver(final Driver driver) {
		driverRepo.save(driver);
	}

	@Override
	public void saveCar(final Car car) {
		carRepo.save(car);
	}

	@Override
	public void saveEngine(final Engine engine) {
		engineRepo.save(engine);
	}

	@Override
	public SeasonInformation getSeasonInformation() {
		final List<SeasonInformation> seasonInformationList = seasonRepo.findByYear(season);
		if(seasonInformationList.size() == 1) {
			return seasonInformationList.get(0);
		} else {
			return null;
		}
	}

	@Override
	public void setSeasonInformation(@NonNull final SeasonInformation seasonInformation) {
		seasonRepo.save(seasonInformation);
	}
}
