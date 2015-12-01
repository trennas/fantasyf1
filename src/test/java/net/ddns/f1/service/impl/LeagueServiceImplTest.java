package net.ddns.f1.service.impl;

import static org.junit.Assert.assertTrue;

import java.util.List;

import net.ddns.f1.FantasyF1Application;
import net.ddns.f1.domain.Team;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FantasyF1Application.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class LeagueServiceImplTest {

	@Autowired
	private LeagueServiceImpl service;

	@Test
	@Ignore
	public void calculateLeagueStandingsTest() throws Exception {
		final List<Team> standings = service.calculateLeagueStandings();
		assertTrue(standings.size() > 0);
	}

}
