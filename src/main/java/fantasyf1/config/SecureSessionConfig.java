package fantasyf1.config;

import java.util.Collections;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.SessionTrackingMode;

import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({ "postgres" })
public class SecureSessionConfig {
	@Bean
	public ServletContextInitializer initializer() {
		return new ServletContextInitializer() {
			@Override
			public void onStartup(final ServletContext servletContext)
					throws ServletException {
				servletContext.setSessionTrackingModes(Collections
						.singleton(SessionTrackingMode.COOKIE));
				servletContext.getSessionCookieConfig().setHttpOnly(true);
				servletContext.getSessionCookieConfig().setSecure(true);
			}
		};
	}
}
