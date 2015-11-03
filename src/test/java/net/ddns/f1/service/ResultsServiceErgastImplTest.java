package net.ddns.f1.service;

import static org.junit.Assert.assertEquals;
import net.ddns.f1.FantasyF1Application;
import net.ddns.f1.domain.EventResult;
import net.ddns.f1.service.impl.ResultsServiceErgastImpl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FantasyF1Application.class)
@WebAppConfiguration
public class ResultsServiceErgastImplTest {

	@Autowired
	ResultsServiceErgastImpl service;

	@Test
	public void test() {
		final EventResult result = service.getEventResult(17);
		assertEquals("Nico Rosberg", result.getFastestLapDriver().getName().toString());
	}

}
