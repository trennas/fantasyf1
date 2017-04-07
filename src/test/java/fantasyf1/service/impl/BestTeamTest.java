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

import fantasyf1.FantasyF1Application;
import fantasyf1.domain.MinimalTeamComponent;
import fantasyf1.web.MainController;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FantasyF1Application.class)
@WebAppConfiguration
@ActiveProfiles({"create", "h2"})
public class BestTeamTest {
	private MockRestServiceServer mockServer;

	@Value("${ergast-base-url}")
	private String ergastUrl;

	@Value("${season}")
	private String season;

	@Autowired
	private RestTemplate template;

	@Autowired
	private MainController controller;

    @Before
    public void setup(){
		mockServer = MockRestServiceServer.createServer(template);
    }

    @Test
    public void bestTeamTest() throws IOException {
    	final String url  = ergastUrl + season + "/1";
    	final String url2 = ergastUrl + season + "/2";
		final String seasonDataUrl  = ergastUrl + season;

    	StringWriter sw = new StringWriter();
		IOUtils.copy(new ClassPathResource("qual-null.xml").getInputStream(), sw);
		final String qualNullXml = sw.toString();
		sw = new StringWriter();
		IOUtils.copy(new ClassPathResource("race-null.xml").getInputStream(), sw);
		final String raceNullXml = sw.toString();

		sw = new StringWriter();
		IOUtils.copy(new ClassPathResource("bestteamqual.xml").getInputStream(), sw);
		final String qualXml = sw.toString();
		sw = new StringWriter();
		IOUtils.copy(new ClassPathResource("bestteamrace.xml").getInputStream(), sw);
		final String raceXml = sw.toString();
		sw = new StringWriter();
		IOUtils.copy(new ClassPathResource("bestteamfastestlap.xml").getInputStream(), sw);
		final String fastestLapXml = sw.toString();
		sw = new StringWriter();
		IOUtils.copy(new ClassPathResource("season-data.xml").getInputStream(), sw);
		final String seasonDataXml = sw.toString();

		// Race 1 Qual Complete, Race Complete
		mockServer.expect(requestTo(seasonDataUrl)).andExpect(method(HttpMethod.GET))
		.andRespond(withSuccess(seasonDataXml, MediaType.APPLICATION_XML));
		
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

		controller.updateResults();
    }
}
