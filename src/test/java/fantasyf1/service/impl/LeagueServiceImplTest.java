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
    public void endToEndTest() throws IOException {
    	final String url = ergastUrl + season + "/1";

		StringWriter sw = new StringWriter();
		IOUtils.copy(new ClassPathResource("qual.xml").getInputStream(), sw);
		final String qualXml = sw.toString();
		sw = new StringWriter();
		IOUtils.copy(new ClassPathResource("qual-null.xml").getInputStream(), sw);
		final String qualNullXml = sw.toString();
		sw = new StringWriter();
		IOUtils.copy(new ClassPathResource("race.xml").getInputStream(), sw);
		final String raceXml = sw.toString();
		sw = new StringWriter();
		IOUtils.copy(new ClassPathResource("race-null.xml").getInputStream(), sw);
		final String raceNullXml = sw.toString();
		sw = new StringWriter();
		IOUtils.copy(new ClassPathResource("fastestlap.xml").getInputStream(), sw);
		final String fastestLapXml = sw.toString();

		sw = new StringWriter();
		IOUtils.copy(new ClassPathResource("qual2.xml").getInputStream(), sw);
		final String qual2Xml = sw.toString();
		sw = new StringWriter();
		IOUtils.copy(new ClassPathResource("race2.xml").getInputStream(), sw);
		final String race2Xml = sw.toString();
		sw = new StringWriter();
		IOUtils.copy(new ClassPathResource("fastestlap2.xml").getInputStream(), sw);
		final String fastestLap2Xml = sw.toString();

		final String url2 = ergastUrl + season + "/2";
		final String url3 = ergastUrl + season + "/3";

		mockServer.expect(requestTo(url + "/qualifying.xml")).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(qualXml, MediaType.APPLICATION_XML));

		mockServer.expect(requestTo(url + "/results.xml")).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(raceNullXml, MediaType.APPLICATION_XML));

		mockServer.expect(requestTo(url + "/fastest/1/drivers.xml")).andExpect(method(HttpMethod.GET))
		.andRespond(withSuccess(fastestLapXml, MediaType.APPLICATION_XML));

		mockServer.expect(requestTo(url2 + "/qualifying.xml")).andExpect(method(HttpMethod.GET))
			.andRespond(withSuccess(qualNullXml, MediaType.APPLICATION_XML));

		mockServer.expect(requestTo(url2 + "/results.xml")).andExpect(method(HttpMethod.GET))
			.andRespond(withSuccess(raceNullXml, MediaType.APPLICATION_XML));

		mockServer.expect(requestTo(url2 + "/fastest/1/drivers.xml")).andExpect(method(HttpMethod.GET))
			.andRespond(withSuccess(fastestLapXml, MediaType.APPLICATION_XML));

		mockServer.expect(requestTo(url + "/qualifying.xml")).andExpect(method(HttpMethod.GET))
		.andRespond(withSuccess(qualXml, MediaType.APPLICATION_XML));

		mockServer.expect(requestTo(url + "/results.xml")).andExpect(method(HttpMethod.GET))
		.andRespond(withSuccess(raceXml, MediaType.APPLICATION_XML));

		mockServer.expect(requestTo(url + "/fastest/1/drivers.xml")).andExpect(method(HttpMethod.GET))
		.andRespond(withSuccess(fastestLapXml, MediaType.APPLICATION_XML));

		mockServer.expect(requestTo(url2 + "/qualifying.xml")).andExpect(method(HttpMethod.GET))
			.andRespond(withSuccess(qualNullXml, MediaType.APPLICATION_XML));

		mockServer.expect(requestTo(url2 + "/results.xml")).andExpect(method(HttpMethod.GET))
			.andRespond(withSuccess(raceNullXml, MediaType.APPLICATION_XML));

		mockServer.expect(requestTo(url2 + "/fastest/1/drivers.xml")).andExpect(method(HttpMethod.GET))
			.andRespond(withSuccess(fastestLapXml, MediaType.APPLICATION_XML));

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
			.andRespond(withSuccess(fastestLapXml, MediaType.APPLICATION_XML));

		mockServer.expect(requestTo(url + "/qualifying.xml")).andExpect(method(HttpMethod.GET))
		.andRespond(withSuccess(qualXml, MediaType.APPLICATION_XML));

		mockServer.expect(requestTo(url + "/results.xml")).andExpect(method(HttpMethod.GET))
		.andRespond(withSuccess(raceXml, MediaType.APPLICATION_XML));

		mockServer.expect(requestTo(url + "/fastest/1/drivers.xml")).andExpect(method(HttpMethod.GET))
		.andRespond(withSuccess(fastestLapXml, MediaType.APPLICATION_XML));

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
			.andRespond(withSuccess(fastestLapXml, MediaType.APPLICATION_XML));

		controller.updateResults();

		assertFalse(controller.event(1).isRaceComplete());

		controller.updateResults();

		assertTrue(controller.event(1).isRaceComplete());

		assertEquals(1, controller.driver(3).getFastestLaps());
		assertEquals(0, controller.driver(6).getFastestLaps());

		assertEquals(600, controller.driver(44).getTotalPoints());
		assertEquals(232, controller.driver(55).getTotalPoints());

		assertTrue(containsDriver(8, controller.getBestTheoreticalTeam().getDrivers()));
		assertTrue(containsDriver(6, controller.getBestTheoreticalTeam().getDrivers()));
		assertTrue(containsDriver(94, controller.getBestTheoreticalTeam().getDrivers()));

		assertTrue(containsDriver(8, controller.getBestTheoreticalTeam().getDrivers()));
		assertTrue(containsDriver(6, controller.getBestTheoreticalTeam().getDrivers()));
		assertTrue(containsDriver(94, controller.getBestTheoreticalTeam().getDrivers()));

		controller.updateResults();

		assertEquals(1200, controller.driver(44).getTotalPoints());
		assertEquals(2, controller.driver(3).getFastestLaps());

		controller.refreshAllResults();

		assertEquals(1200, controller.driver(44).getTotalPoints());
		assertEquals(464, controller.driver(55).getTotalPoints());
		assertEquals(2, controller.driver(3).getFastestLaps());
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
