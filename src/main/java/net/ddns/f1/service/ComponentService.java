package net.ddns.f1.service;

import java.util.List;

import net.ddns.f1.domain.Car;
import net.ddns.f1.domain.Driver;
import net.ddns.f1.domain.Engine;
import net.ddns.f1.service.impl.Ff1Exception;

public interface ComponentService {
	public Driver findDriverByName(final String name) throws Ff1Exception;

	public Driver findDriverByNumber(final int number) throws Ff1Exception;

	public List<Driver> findDriversByStandin(final boolean standIn);

	public List<Driver> findDriversByCar(final Car car);

	public List<Driver> findDriversByCarAndStandin(final Car car,
			final boolean standIn) throws Ff1Exception;

	public Car findCarByName(final String name) throws Ff1Exception;

	public List<Car> findCarsByEngine(final Engine engine);

	public Engine findEngineByName(final String name) throws Ff1Exception;

	public List<Driver> findAllDrivers();

	public List<Car> findAllCars();

	public List<Engine> findAllEngines();

	public void saveDriver(final Driver driver);

	public void saveCar(final Car car);

	public void saveEngine(final Engine engine);
}
