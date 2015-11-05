package net.ddns.f1.web;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Profile("!create")
public class MainController {

	private static final Logger LOG = Logger
			.getLogger(MainController.class);

	@RequestMapping("/")
	@ResponseBody
	public String mainPage() {
		return "Welcome to FF1!";
	}
}
