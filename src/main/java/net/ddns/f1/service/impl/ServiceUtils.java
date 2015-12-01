package net.ddns.f1.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class ServiceUtils {
	private static final Logger LOG = Logger.getLogger(ServiceUtils.class);

	public <T> T get(final List<T> res, final String searchCriteria)
			throws Ff1Exception {
		if (res.size() == 0) {
			return res.get(0);
		} else if (res.size() > 1) {
			final String message = "Multiple items ("
					+ res.getClass().getName() + ") found for searchCriteria: "
					+ searchCriteria;
			LOG.error(message);
			throw new Ff1Exception(message);
		} else {
			return null;
		}
	}
}
