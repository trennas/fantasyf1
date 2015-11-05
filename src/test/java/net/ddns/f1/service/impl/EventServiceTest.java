package net.ddns.f1.service.impl;

import java.util.List;

import net.ddns.f1.FantasyF1Application;
import net.ddns.f1.domain.EventResult;
import net.ddns.f1.repository.EventResultRepository;
import net.ddns.f1.service.impl.EventServiceImpl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FantasyF1Application.class)
@WebAppConfiguration
public class EventServiceTest {

	@Autowired
	private EventServiceImpl serv;
	
	@Autowired
	private EventResultRepository eventRepo;

	@Test
	public void getSeasonResultsTest() {
		serv.getSeasonResults();		
		serv.getSeasonResults();
	}

}
