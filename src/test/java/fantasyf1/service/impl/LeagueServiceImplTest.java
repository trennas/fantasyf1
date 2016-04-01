package fantasyf1.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import fantasyf1.FantasyF1Application;
import fantasyf1.domain.MinimalTeamComponent;
import fantasyf1.web.MainController;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FantasyF1Application.class)
@WebAppConfiguration
@ActiveProfiles({"test", "create"})
public class LeagueServiceImplTest {
	private MockRestServiceServer mockServer;

	@Value("${ergast-base-url}")
	private String ergastUrl;

	@Value("${season}")
	private String season;

	@Autowired
	RestTemplate template;

	@Autowired
	MainController controller;

    @Before
    public void setup(){
		mockServer = MockRestServiceServer.createServer(template);
    }

    @Test
    public void integrationTest() throws IOException {
    	final String url  = ergastUrl + season + "/1";
		final String url2 = ergastUrl + season + "/2";
		final String url3 = ergastUrl + season + "/3";
    	
    	StringWriter sw = new StringWriter();
		IOUtils.copy(new ClassPathResource("qual-null.xml").getInputStream(), sw);
		final String qualNullXml = sw.toString();
		sw = new StringWriter();
		IOUtils.copy(new ClassPathResource("race-null.xml").getInputStream(), sw);
		final String raceNullXml = sw.toString();
    	
		sw = new StringWriter();
		IOUtils.copy(new ClassPathResource("qual.xml").getInputStream(), sw);
		final String qual1Xml = sw.toString();		
		sw = new StringWriter();
		IOUtils.copy(new ClassPathResource("race.xml").getInputStream(), sw);
		final String race1Xml = sw.toString();		
		sw = new StringWriter();
		IOUtils.copy(new ClassPathResource("fastestlap.xml").getInputStream(), sw);
		final String fastestLap1Xml = sw.toString();

		sw = new StringWriter();
		IOUtils.copy(new ClassPathResource("qual2.xml").getInputStream(), sw);
		final String qual2Xml = sw.toString();
		sw = new StringWriter();
		IOUtils.copy(new ClassPathResource("race2.xml").getInputStream(), sw);
		final String race2Xml = sw.toString();
		sw = new StringWriter();
		IOUtils.copy(new ClassPathResource("fastestlap2.xml").getInputStream(), sw);
		final String fastestLap2Xml = sw.toString();

		mockServer.expect(requestTo(url + "/qualifying.xml")).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(qual1Xml, MediaType.APPLICATION_XML));

		mockServer.expect(requestTo(url + "/results.xml")).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(raceNullXml, MediaType.APPLICATION_XML));

		mockServer.expect(requestTo(url + "/fastest/1/drivers.xml")).andExpect(method(HttpMethod.GET))
		.andRespond(withSuccess(fastestLap1Xml, MediaType.APPLICATION_XML));

		mockServer.expect(requestTo(url2 + "/qualifying.xml")).andExpect(method(HttpMethod.GET))
			.andRespond(withSuccess(qualNullXml, MediaType.APPLICATION_XML));

		mockServer.expect(requestTo(url2 + "/results.xml")).andExpect(method(HttpMethod.GET))
			.andRespond(withSuccess(raceNullXml, MediaType.APPLICATION_XML));

		mockServer.expect(requestTo(url2 + "/fastest/1/drivers.xml")).andExpect(method(HttpMethod.GET))
			.andRespond(withSuccess(fastestLap1Xml, MediaType.APPLICATION_XML));

		mockServer.expect(requestTo(url + "/qualifying.xml")).andExpect(method(HttpMethod.GET))
		.andRespond(withSuccess(qual1Xml, MediaType.APPLICATION_XML));

		mockServer.expect(requestTo(url + "/results.xml")).andExpect(method(HttpMethod.GET))
		.andRespond(withSuccess(race1Xml, MediaType.APPLICATION_XML));

		mockServer.expect(requestTo(url + "/fastest/1/drivers.xml")).andExpect(method(HttpMethod.GET))
		.andRespond(withSuccess(fastestLap1Xml, MediaType.APPLICATION_XML));

		mockServer.expect(requestTo(url2 + "/qualifying.xml")).andExpect(method(HttpMethod.GET))
			.andRespond(withSuccess(qualNullXml, MediaType.APPLICATION_XML));

		mockServer.expect(requestTo(url2 + "/results.xml")).andExpect(method(HttpMethod.GET))
			.andRespond(withSuccess(raceNullXml, MediaType.APPLICATION_XML));

		mockServer.expect(requestTo(url2 + "/fastest/1/drivers.xml")).andExpect(method(HttpMethod.GET))
			.andRespond(withSuccess(fastestLap1Xml, MediaType.APPLICATION_XML));

		mockServer.expect(requestTo(url2 + "/qualifying.xml")).andExpect(method(HttpMethod.GET))
			.andRespond(withSuccess(qual2Xml, MediaType.APPLICATION_XML));

		mockServer.expect(requestTo(url2 + "/results.xml")).andExpect(method(HttpMethod.GET))
			.andRespond(withSuccess(race2Xml, MediaType.APPLICATION_XML));

		mockServer.expect(requestTo(url2 + "/fastest/1/drivers.xml")).andExpect(method(HttpMethod.GET))
			.andRespond(withSuccess(fastestLap2Xml, MediaType.APPLICATION_XML));

		mockServer.expect(requestTo(url3 + "/qualifying.xml")).andExpect(method(HttpMethod.GET))
		.andRespond(withSuccess(qualNullXml, MediaType.APPLICATION_XML));

		mockServer.expect(requestTo(url3 + "/results.xml")).andExpect(method(HttpMethod.GET))
			.andRespond(withSuccess(raceNullXml, MediaType.APPLICATION_XML));

		mockServer.expect(requestTo(url3 + "/fastest/1/drivers.xml")).andExpect(method(HttpMethod.GET))
			.andRespond(withSuccess(fastestLap1Xml, MediaType.APPLICATION_XML));

		mockServer.expect(requestTo(url + "/qualifying.xml")).andExpect(method(HttpMethod.GET))
		.andRespond(withSuccess(qual1Xml, MediaType.APPLICATION_XML));

		mockServer.expect(requestTo(url + "/results.xml")).andExpect(method(HttpMethod.GET))
		.andRespond(withSuccess(race1Xml, MediaType.APPLICATION_XML));

		mockServer.expect(requestTo(url + "/fastest/1/drivers.xml")).andExpect(method(HttpMethod.GET))
		.andRespond(withSuccess(fastestLap1Xml, MediaType.APPLICATION_XML));

		mockServer.expect(requestTo(url2 + "/qualifying.xml")).andExpect(method(HttpMethod.GET))
		.andRespond(withSuccess(qual2Xml, MediaType.APPLICATION_XML));

		mockServer.expect(requestTo(url2 + "/results.xml")).andExpect(method(HttpMethod.GET))
			.andRespond(withSuccess(race2Xml, MediaType.APPLICATION_XML));

		mockServer.expect(requestTo(url2 + "/fastest/1/drivers.xml")).andExpect(method(HttpMethod.GET))
			.andRespond(withSuccess(fastestLap2Xml, MediaType.APPLICATION_XML));

		mockServer.expect(requestTo(url3 + "/qualifying.xml")).andExpect(method(HttpMethod.GET))
		.andRespond(withSuccess(qualNullXml, MediaType.APPLICATION_XML));

		mockServer.expect(requestTo(url3 + "/results.xml")).andExpect(method(HttpMethod.GET))
			.andRespond(withSuccess(raceNullXml, MediaType.APPLICATION_XML));

		mockServer.expect(requestTo(url3 + "/fastest/1/drivers.xml")).andExpect(method(HttpMethod.GET))
			.andRespond(withSuccess(fastestLap1Xml, MediaType.APPLICATION_XML));

		// Race 1, Qual complete but race not complete
		controller.updateResults();

		assertFalse(controller.event(1).isRaceComplete());
		
		assertEquals(0, controller.driver(3).getFastestLaps());
		assertEquals(0, controller.driver(55).getFastestLaps());
		assertEquals(0, controller.driver(44).getFastestLaps());
		
		assertEquals(200, controller.driver(44).getTotalPoints());
		assertEquals(160, controller.driver(6).getTotalPoints());		
		assertEquals(104, controller.driver(7).getTotalPoints());		
		assertEquals(68, controller.driver(3).getTotalPoints());
		assertEquals(128, controller.driver(5).getTotalPoints());
		assertEquals(72, controller.driver(55).getTotalPoints());
		
		assertTrue(containsDriver(33, controller.getBestTheoreticalTeamForRound(1).getDrivers()));
		assertTrue(containsDriver(55, controller.getBestTheoreticalTeamForRound(1).getDrivers()));
		assertTrue(containsDriver(14, controller.getBestTheoreticalTeamForRound(1).getDrivers()));
		assertEquals("Toro Rosso", controller.getBestTheoreticalTeamForRound(1).getCar().getName());
		assertEquals("Mercedes", controller.getBestTheoreticalTeamForRound(1).getEngine().getName());
		assertTrue(containsDriver(33, controller.getBestTheoreticalTeam().getDrivers()));
		assertTrue(containsDriver(55, controller.getBestTheoreticalTeam().getDrivers()));
		assertTrue(containsDriver(14, controller.getBestTheoreticalTeam().getDrivers()));
		assertEquals("Toro Rosso", controller.getBestTheoreticalTeam().getCar().getName());
		assertEquals("Mercedes", controller.getBestTheoreticalTeam().getEngine().getName());

		// Race 1, Qual and race complete
		controller.updateResults();

		assertTrue(controller.event(1).isRaceComplete());
		
		assertEquals(1, controller.driver(3).getFastestLaps());
		assertEquals(0, controller.driver(55).getFastestLaps());
		assertEquals(0, controller.driver(44).getFastestLaps());
		
		assertEquals(600, controller.driver(44).getTotalPoints());
		assertEquals(660, controller.driver(6).getTotalPoints());		
		assertEquals(104, controller.driver(7).getTotalPoints());		
		assertEquals(378, controller.driver(3).getTotalPoints());
		assertEquals(448, controller.driver(5).getTotalPoints());
		assertEquals(232, controller.driver(55).getTotalPoints());
		assertEquals(52, controller.driver(14).getTotalPoints());
		
		assertTrue(containsDriver(8, controller.getBestTheoreticalTeamForRound(1).getDrivers()));
		assertTrue(containsDriver(6, controller.getBestTheoreticalTeamForRound(1).getDrivers()));
		assertTrue(containsDriver(94, controller.getBestTheoreticalTeamForRound(1).getDrivers()));
		assertEquals("Mercedes", controller.getBestTheoreticalTeamForRound(1).getCar().getName());
		assertEquals("Honda", controller.getBestTheoreticalTeamForRound(1).getEngine().getName());
		assertTrue(containsDriver(8, controller.getBestTheoreticalTeam().getDrivers()));
		assertTrue(containsDriver(6, controller.getBestTheoreticalTeam().getDrivers()));
		assertTrue(containsDriver(94, controller.getBestTheoreticalTeam().getDrivers()));
		assertEquals("Mercedes", controller.getBestTheoreticalTeam().getCar().getName());
		assertEquals("Honda", controller.getBestTheoreticalTeam().getEngine().getName());

		// Race 2, Qual and race complete
		controller.updateResults();
		checkFinalResults();

		// Refresh everything, should all stay the same
		controller.refreshAllResults();
		checkFinalResults();
    }
    
    private void checkFinalResults() {
    	assertEquals(4, controller.event(2).getRemarks().size());
    	assertEquals("Fernando Alonso did not participate in qualifying", controller.event(2).getRemarks().get(0));
    	assertEquals("Fernando Alonso did not participate in the race", controller.event(2).getRemarks().get(1));
    	assertEquals("Fernando Alonso scores qualifying points from stand-in driver Stoffel Vandoorne", controller.event(2).getRemarks().get(2));
    	assertEquals("Fernando Alonso scores race points from stand-in driver Stoffel Vandoorne", controller.event(2).getRemarks().get(3));
    	
    	assertTrue(controller.event(1).isRaceComplete());
		assertTrue(controller.event(2).isRaceComplete());
		
		assertEquals(1, controller.driver(3).getFastestLaps());
		assertEquals(1, controller.driver(55).getFastestLaps());
		assertEquals(0, controller.driver(44).getFastestLaps());
		
		assertEquals(1048, controller.driver(44).getTotalPoints());
		assertEquals(1024, controller.driver(6).getTotalPoints());		
		assertEquals(664, controller.driver(7).getTotalPoints());
		assertEquals(666, controller.driver(3).getTotalPoints());
		assertEquals(1148, controller.driver(5).getTotalPoints());
		assertEquals(514, controller.driver(55).getTotalPoints());
		assertEquals(104, controller.driver(14).getTotalPoints());
		
		assertTrue(containsDriver(8, controller.getBestTheoreticalTeamForRound(1).getDrivers()));
		assertTrue(containsDriver(6, controller.getBestTheoreticalTeamForRound(1).getDrivers()));
		assertTrue(containsDriver(94, controller.getBestTheoreticalTeamForRound(1).getDrivers()));
		assertEquals("Mercedes", controller.getBestTheoreticalTeamForRound(1).getCar().getName());
		assertEquals("Honda", controller.getBestTheoreticalTeamForRound(1).getEngine().getName());
		assertTrue(containsDriver(5, controller.getBestTheoreticalTeamForRound(2).getDrivers()));
		assertTrue(containsDriver(55, controller.getBestTheoreticalTeamForRound(2).getDrivers()));
		assertTrue(containsDriver(8, controller.getBestTheoreticalTeamForRound(2).getDrivers()));
		assertEquals("Ferrari", controller.getBestTheoreticalTeamForRound(2).getCar().getName());
		assertEquals("Honda", controller.getBestTheoreticalTeamForRound(2).getEngine().getName());
		assertTrue(containsDriver(5, controller.getBestTheoreticalTeam().getDrivers()));
		assertTrue(containsDriver(55, controller.getBestTheoreticalTeam().getDrivers()));
		assertTrue(containsDriver(8, controller.getBestTheoreticalTeam().getDrivers()));
		assertEquals("Mclaren", controller.getBestTheoreticalTeam().getCar().getName());
		assertEquals("Ferrari", controller.getBestTheoreticalTeam().getEngine().getName());
    }

	private boolean containsDriver(final int number, final List<MinimalTeamComponent> drivers) {
		for (final MinimalTeamComponent driver : drivers) {
			if(driver.getNumber() == number) {
				return true;
			}
		}
		return false;
	}

}
