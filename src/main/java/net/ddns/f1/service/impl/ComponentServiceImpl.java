package net.ddns.f1.service.impl;

import java.util.List;

import net.ddns.f1.domain.Car;
import net.ddns.f1.domain.Driver;
import net.ddns.f1.domain.Engine;
import net.ddns.f1.repository.CarRepository;
import net.ddns.f1.repository.DriverRepository;
import net.ddns.f1.repository.EngineRepository;
import net.ddns.f1.service.ComponentService;

import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public class ComponentServiceImpl implements ComponentService {
	@Autowired
	DriverRepository driverRepo;

	@Autowired
	CarRepository carRepo;

	@Autowired
	EngineRepository engineRepo;

	@Autowired
	ServiceUtils utils;

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
	public List<Driver> findDriversByCar(final Car car) {
		return driverRepo.findByCar(car);
	}

	@Override
	public List<Driver> findDriversByCarAndStandin(final Car car,
			final boolean standIn) {
		return driverRepo.findByCarAndStandin(car, true);
	}
	
	@Override
	public void saveDrivers(List<Driver> drivers) {
		removeDeleted(findAllDrivers(), drivers, driverRepo);
		driverRepo.save(drivers);
	}
	
	private <T> void removeDeleted(List<T> existingList, List<T> updatedList, CrudRepository<T, String> repo) {
		for(T existing : existingList) {
			boolean found = false;
			
			for(T updated : updatedList) {
				if(existing.equals(updated)) {
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
	public void saveCars(List<Car> cars) {
		removeDeleted(findAllCars(), cars, carRepo);
		carRepo.save(cars);
	}
	
	@Override
	public void saveEngines(List<Engine> engines) {
		removeDeleted(findAllEngines(), engines, engineRepo);
		engineRepo.save(engines);
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
}
