package net.ddns.f1.web;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
public class MainController {
	
	private static final Logger LOG = Logger
			.getLogger(MainController.class);

	@RequestMapping("/")
	@ResponseBody
	public String mainPage() {
		RestTemplate restTemplate = new RestTemplate();
		String qualResults = restTemplate.getForObject("http://ergast.com/api/f1/current/last/qualifying", String.class);
		String raceResults = restTemplate.getForObject("http://ergast.com/api/f1/current/last/results", String.class);
		LOG.info(qualResults);
		LOG.info(raceResults);
		return "Welcome to FF1!";
	}
}
