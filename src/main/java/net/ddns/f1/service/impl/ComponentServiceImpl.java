package net.ddns.f1.service.impl;

import java.util.List;

import net.ddns.f1.domain.Car;
import net.ddns.f1.domain.Driver;
import net.ddns.f1.domain.Engine;
import net.ddns.f1.repository.CarRepository;
import net.ddns.f1.repository.DriverRepository;
import net.ddns.f1.repository.EngineRepository;

import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComponentsServiceImpl {
	@Autowired
	DriverRepository driverRepo;
	
	@Autowired
	CarRepository carRepo;
	
	@Autowired
	EngineRepository engineRepo;
	
	@Autowired
	ServiceUtils utils;
	
	public Driver findDriverByName(String name) throws Ff1Exception {		
		return utils.get(driverRepo.findByName(name), name);		
	}

	public List<Driver> findByStandin(boolean standIn) throws Ff1Exception {
		return driverRepo.findByStandin(standIn);
	}
	
	public Car findCarByName(String name) throws Ff1Exception {		
		return utils.get(carRepo.findByName(name), name);		
	}
	
	public Engine findEngineByName(String name) throws Ff1Exception {		
		return utils.get(engineRepo.findByName(name), name);		
	}

	public List<Driver> findAllDrivers() {
		return IteratorUtils.toList(driverRepo.findAll().iterator());
	}
	public List<Car> findAllCars() {
		return IteratorUtils.toList(carRepo.findAll().iterator());
	}
	public List<Engine> findAllDrivers() {
		return IteratorUtils.toList(driverRepo.findAll().iterator());
	}	
}
