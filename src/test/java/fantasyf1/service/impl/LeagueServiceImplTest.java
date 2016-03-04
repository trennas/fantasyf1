package fantasyf1.service.impl;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.io.StringWriter;

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
import fantasyf1.service.impl.LeagueServiceImpl;
import fantasyf1.web.MainController;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FantasyF1Application.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class LeagueServiceImplTest {
	private MockRestServiceServer mockServer;

	@Value("${ergast-base-url}")
	private String ergastUrl;

	@Value("${season}")
	private String season;

	@Autowired
	private LeagueServiceImpl service;

	@Autowired
	RestTemplate template;

	@Autowired
	MainController controller;

    @Before
    public void setup(){
		mockServer = MockRestServiceServer.createServer(template);
    }

	@Test
	public void calculateAllResultsTest() throws Exception {
		String url = ergastUrl + season + "/1";

		StringWriter sw = new StringWriter();
		IOUtils.copy(new ClassPathResource("qual.xml").getInputStream(), sw);
		String qualXml = sw.toString();
		sw = new StringWriter();
		IOUtils.copy(new ClassPathResource("qual-null.xml").getInputStream(), sw);
		String qualNullXml = sw.toString();	
		sw = new StringWriter();
		IOUtils.copy(new ClassPathResource("race.xml").getInputStream(), sw);
		String raceXml = sw.toString();				
		sw = new StringWriter();
		IOUtils.copy(new ClassPathResource("fastestlap.xml").getInputStream(), sw);
		String fastestLapXml = sw.toString();

		mockServer.expect(requestTo(url + "/qualifying.xml")).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(qualXml, MediaType.APPLICATION_XML));

		mockServer.expect(requestTo(url + "/results.xml")).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(raceXml, MediaType.APPLICATION_XML));

		mockServer.expect(requestTo(url + "/fastest/1/drivers.xml")).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(fastestLapXml, MediaType.APPLICATION_XML));
		
		url = ergastUrl + season + "/2";
		mockServer.expect(requestTo(url + "/qualifying.xml")).andExpect(method(HttpMethod.GET))
			.andRespond(withSuccess(qualNullXml, MediaType.APPLICATION_XML));

		mockServer.expect(requestTo(url + "/results.xml")).andExpect(method(HttpMethod.GET))
			.andRespond(withSuccess(raceXml, MediaType.APPLICATION_XML));

		mockServer.expect(requestTo(url + "/fastest/1/drivers.xml")).andExpect(method(HttpMethod.GET))
			.andRespond(withSuccess(fastestLapXml, MediaType.APPLICATION_XML));
		

		service.calculateLeagueStandings();
		assertEquals(1, controller.driver(44).getFastestLaps());
		assertEquals(0, controller.driver(6).getFastestLaps());
		assertEquals(750, controller.driver(44).getTotalPoints());
		assertEquals(154, controller.driver(55).getTotalPoints());

		assertEquals(new Integer(6), controller.getBestTheoreticalTeam().getDrivers().get(0).getNumber());
		assertEquals(new Integer(9), controller.getBestTheoreticalTeam().getDrivers().get(1).getNumber());
		assertEquals(new Integer(94), controller.getBestTheoreticalTeam().getDrivers().get(2).getNumber());
		assertEquals(new Integer(6), controller.event(1).getBestTheoreticalTeam().getDrivers().get(0).getNumber());
		assertEquals(new Integer(9), controller.event(1).getBestTheoreticalTeam().getDrivers().get(1).getNumber());
		assertEquals(new Integer(94), controller.event(1).getBestTheoreticalTeam().getDrivers().get(2).getNumber());
	}

}
