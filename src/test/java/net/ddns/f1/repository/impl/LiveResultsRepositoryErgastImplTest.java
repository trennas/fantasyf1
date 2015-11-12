package net.ddns.f1.repository.impl;

import static org.junit.Assert.assertEquals;
import net.ddns.f1.FantasyF1Application;
import net.ddns.f1.domain.EventResult;
import net.ddns.f1.repository.impl.LiveResultsRepositoryErgastImpl;

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
@ActiveProfiles("h2,create")
public class LiveResultsRepositoryErgastImplTest {

	@Autowired
	LiveResultsRepositoryErgastImpl service;

	@Test @Ignore
	public void test() {
		final EventResult result = service.fetchEventResult(17);
		assertEquals("Nico Rosberg", result.getFastestLapDriver().getName().toString());
	}

}
