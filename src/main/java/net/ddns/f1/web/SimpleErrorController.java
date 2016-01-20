package net.ddns.f1.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SimpleErrorController implements ErrorController {

	private static final String ERROR_PATH = "/error";

	@RequestMapping(ERROR_PATH)
	public String error(final HttpServletRequest request, final Model model) {
		model.addAttribute("message",
				request.getAttribute("javax.servlet.error.message"));
		model.addAttribute("code",
				request.getAttribute("javax.servlet.error.status_code"));
		return "error";
	}

	@Override
	public String getErrorPath() {
		return ERROR_PATH;
	}

	@RequestMapping("loginError")
	public String loginError(final Model model) {
		model.addAttribute("message", "Invalid authentication credentials.");
		model.addAttribute("returnLocation", "/login");
		return "error";
	}

	@RequestMapping("accessError")
	public String accessError(final Model model) {
		model.addAttribute("message",
				"You don't have access to view this page.");
		model.addAttribute("returnLocation", "/login");
		SecurityContextHolder.getContext().setAuthentication(null);
		return "error";
	}

}
