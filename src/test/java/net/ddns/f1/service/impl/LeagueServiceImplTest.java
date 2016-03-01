package net.ddns.f1.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ddns.f1.FantasyF1Application;
import net.ddns.f1.domain.EventResult;
import net.ddns.f1.domain.Position;
import net.ddns.f1.repository.DriverRepository;
import net.ddns.f1.repository.EventResultRepository;
import net.ddns.f1.service.EventService;
import net.ddns.f1.service.LeagueService;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
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
	
	@InjectMocks
    private LeagueServiceImpl testingObject;

	@Mock
    private EventService eventService;
	
	@Autowired
	private DriverRepository driverRepo;
	
	@Autowired
	private EventResultRepository eventRepo;
	
	private static final Map<String, Position> QUAL_ORDER = new HashMap<String, Position>();
	static {
		QUAL_ORDER.put("Lewis Hamilton", new Position(1, true, 44));
		QUAL_ORDER.put("Sebastian Vettel", new Position(2, true, 5));
		QUAL_ORDER.put("Raikkenen", new Position(3, true, 7));
		QUAL_ORDER.put("Ros Tos", new Position(4, true, 6));
	}
	
	private static final Map<String, Position> RACE_ORDER = new HashMap<String, Position>();
	static {
		RACE_ORDER.put("Lewis Hamilton", new Position(2, true, 44));
		RACE_ORDER.put("Sebastian Vettel", new Position(1, true, 5));
		RACE_ORDER.put("Raikkenen", new Position(4, true, 7));
		RACE_ORDER.put("Ros Tos", new Position(3, true, 6));
	}

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

	@Test
	@Ignore
	public void calculateAllResultsTest() throws Exception {
		List<EventResult> results = new ArrayList<EventResult>();
		EventResult res = new EventResult();
		res.setFastestLapDriver(driverRepo.findByNumber(44).get(0));
		res.setRound(1);
		res.setVenue("Australia");
		res.setSeason(2016);
		res.setRaceComplete(true);
		res.setQualifyingOrder(QUAL_ORDER);
		res.setRaceOrder(RACE_ORDER);
		results.add(res);
		
		eventRepo.save(res);
		
		Mockito.when(eventService.checkForNewResults(true)).thenReturn(true);
		Mockito.when(eventService.getSeasonResults()).thenReturn(results);
		//testingObject.calculateLeagueStandings();
	}

}
