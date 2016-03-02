package net.ddns.f1.service.impl;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Ignore;
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

import net.ddns.f1.FantasyF1Application;

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

    @Before
    public void setup(){
		mockServer = MockRestServiceServer.createServer(template);
    }

	@Test
	@Ignore
	public void calculateAllResultsTest() throws Exception {
		String url = ergastUrl + season + "/1";

		final StringWriter qualWriter = new StringWriter();
		IOUtils.copy(new ClassPathResource("qual.xml").getInputStream(), qualWriter);
		final StringWriter raceWriter = new StringWriter();
		IOUtils.copy(new ClassPathResource("race.xml").getInputStream(), raceWriter);
		final StringWriter fastWriter = new StringWriter();
		IOUtils.copy(new ClassPathResource("fastestlap.xml").getInputStream(), fastWriter);

		mockServer.expect(requestTo(url + "/qualifying.xml")).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(qualWriter.toString(), MediaType.APPLICATION_XML));

		mockServer.expect(requestTo(url + "/results.xml")).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(raceWriter.toString(), MediaType.APPLICATION_XML));

		mockServer.expect(requestTo(url + "/fastest/1/drivers.xml")).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(fastWriter.toString(), MediaType.APPLICATION_XML));

		service.calculateLeagueStandings();
		assertTrue(true);
	}

}
