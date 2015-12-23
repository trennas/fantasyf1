package net.ddns.f1.service;

import java.util.List;

import net.ddns.f1.domain.Car;
import net.ddns.f1.domain.Driver;
import net.ddns.f1.domain.Engine;

public interface ComponentService {
	public Driver findDriverByName(final String name);

	public Driver findDriverByNumber(final int number);

	public List<Driver> findDriversByStandin(final boolean standIn);

	public List<Driver> findDriversByCar(final Car car);

	public List<Driver> findDriversByCarAndStandin(final Car car,
			final boolean standIn);
	
	public void saveDrivers(List<Driver> drivers);

	public Car findCarByName(final String name);

	public List<Car> findCarsByEngine(final Engine engine);

	public Engine findEngineByName(final String name);

	public List<Driver> findAllDrivers();

	public List<Car> findAllCars();
	
	public void saveCars(List<Car> cars);

	public List<Engine> findAllEngines();
	
	public void saveEngines(List<Engine> engines);

	public void saveDriver(final Driver driver);

	public void saveCar(final Car car);

	public void saveEngine(final Engine engine);
}
