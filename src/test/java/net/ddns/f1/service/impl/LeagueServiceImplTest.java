package net.ddns.f1.service.impl;

import static org.junit.Assert.*;
import net.ddns.f1.FantasyF1Application;
import net.ddns.f1.repository.EventResultRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FantasyF1Application.class)
@WebAppConfiguration
public class LeagueServiceImplTest {
	
	@Autowired
	private LeagueServiceImpl service;

	@Test
	public void calculateLeagueStandingsTest() {
		service.calculateLeagueStandings();
	}

}
