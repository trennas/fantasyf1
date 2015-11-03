package net.ddns.f1.config;

import net.ddns.f1.domain.Car;
import net.ddns.f1.domain.Driver;
import net.ddns.f1.domain.Engine;
import net.ddns.f1.domain.FullName;
import net.ddns.f1.domain.Team;
import net.ddns.f1.domain.ValidationException;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DummyData {
	
	private static final Logger LOG = Logger
			.getLogger(DummyData.class);
	
	public void createDummyData() {
		Engine mercedesPower = new Engine();
		mercedesPower.setName("Mercedes");
		Engine ferrariPower = new Engine();
		ferrariPower.setName("Ferrari");
		Engine renault = new Engine();
		renault.setName("Renault");
		Engine honda = new Engine();
		honda.setName("Honda");
		
		Car mercedes = new Car();
		mercedes.setName("Mercedes");
		mercedes.setEngine(mercedesPower);
		Car ferrari = new Car();
		ferrari.setName("Ferrari");
		ferrari.setEngine(ferrariPower);
		Car lotus = new Car();
		lotus.setName("Lotus");
		lotus.setEngine(mercedesPower);
		Car williams = new Car();
		williams.setName("Williams");
		williams.setEngine(mercedesPower);
		Car redBull = new Car();
		redBull.setName("Red Bull");
		redBull.setEngine(renault);
		Car toroRosso = new Car();
		toroRosso.setName("Toro Rosso");
		toroRosso.setEngine(renault);
		Car forceIndia = new Car();
		forceIndia.setName("Force India");
		forceIndia.setEngine(mercedesPower);
		Car sauber = new Car();
		sauber.setName("Sauber");
		sauber.setEngine(ferrariPower);
		Car mclaren = new Car();
		mclaren.setName("Mclaren");
		mclaren.setEngine(honda);
		Car manor = new Car();
		manor.setName("Manor");
		manor.setEngine(ferrariPower);
		
		Driver hamilton = new Driver();
		hamilton.setName(new FullName("Lewis Hamilton"));
		hamilton.setCar(mercedes);
		Driver rosberg = new Driver();
		rosberg.setName(new FullName("Nico Rosberg"));
		rosberg.setCar(mercedes);
		Driver vettel = new Driver();
		vettel.setName(new FullName("Sebastian Vettel"));
		vettel.setCar(ferrari);
		Driver kimi = new Driver();
		kimi.setName(new FullName("Kimi Räikkönen"));
		kimi.setCar(ferrari);
		Driver ricciardo = new Driver();
		ricciardo.setName(new FullName("Daniel Riccairdo"));
		ricciardo.setCar(redBull);
		Driver kvyat = new Driver();
		kvyat.setName(new FullName("Daniil Kvyat"));
		kvyat.setCar(redBull);
		Driver massa = new Driver();
		massa.setName(new FullName("Felipe Massa"));
		massa.setCar(williams);
		Driver bottas = new Driver();
		bottas.setName(new FullName("Valtteri Bottas"));
		bottas.setCar(williams);
		Driver hulkenberg = new Driver();
		hulkenberg.setName(new FullName("Nico Hulkenberg"));
		hulkenberg.setCar(forceIndia);
		Driver perez = new Driver();
		perez.setName(new FullName("Sergio Perez"));
		perez.setCar(forceIndia);
		Driver max = new Driver();
		max.setName(new FullName("Max Verstappen"));
		max.setCar(toroRosso);
		Driver sainz = new Driver();
		sainz.setName(new FullName("Carlos Sainz"));
		sainz.setCar(toroRosso);
		Driver roman = new Driver();
		roman.setName(new FullName("Romain Grosjean"));
		roman.setCar(lotus);
		Driver pastor = new Driver();
		pastor.setName(new FullName("Pastor Maldonado"));
		pastor.setCar(lotus);
		Driver ericsson = new Driver();
		ericsson.setName(new FullName("Marcus Ericsson"));
		ericsson.setCar(sauber);
		Driver nasr = new Driver();
		nasr.setName(new FullName("Felipe Nasr"));
		nasr.setCar(sauber);
		Driver alonso = new Driver();
		alonso.setName(new FullName("Fernando Alonso"));
		alonso.setCar(mclaren);
		Driver button = new Driver();
		button.setName(new FullName("Jenson Button"));
		button.setCar(mclaren);
		Driver stevens = new Driver();
		stevens.setName(new FullName("Will Stevens"));
		stevens.setCar(manor);
		Driver merhi = new Driver();
		merhi.setName(new FullName("Roberto Merhi"));
		merhi.setCar(manor);
		
		try {
			Team mikesTeam = new Team("Fast But Bad Manors", new Driver[] {vettel, sainz}, manor, mercedesPower);
		} catch (ValidationException e) {
			LOG.info("Team Invalid: " + e.getMessage());
		}
	}
}
