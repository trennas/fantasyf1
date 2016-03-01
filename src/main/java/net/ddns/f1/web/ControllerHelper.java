package net.ddns.f1.web;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ControllerHelper {
	
	private static final Logger LOG = Logger.getLogger(ControllerHelper.class);
	
	@ExceptionHandler(value = Exception.class)
	public ModelAndView exception(Exception exception, WebRequest request) {
		final String message = exception.getClass().getName() + ": " + exception.getMessage();
		LOG.error(message);
		ModelAndView modelAndView = new ModelAndView("error");
		modelAndView.addObject("message", message);
		return modelAndView;
	}
}
