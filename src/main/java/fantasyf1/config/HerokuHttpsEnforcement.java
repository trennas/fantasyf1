package fantasyf1.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("postgres")
public class HerokuHttpsEnforcement {
	@Bean
    public Filter httpsEnforcerFilter(){
        return new HttpsEnforcer();
    }
	
	public class HttpsEnforcer implements Filter {
		public static final String X_FORWARDED_PROTO = "x-forwarded-proto";		
		private FilterConfig filterConfig;    

	    @Override
	    public void init(final FilterConfig filterConfig) throws ServletException {
	        this.filterConfig = filterConfig;
	    }

	    @Override
	    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
	    	final HttpServletRequest request = (HttpServletRequest) servletRequest;
	    	final HttpServletResponse response = (HttpServletResponse) servletResponse;
	        if (request.getHeader(X_FORWARDED_PROTO) != null) {
	            if (request.getHeader(X_FORWARDED_PROTO).indexOf("https") != 0) {
	                response.sendRedirect("https://" + request.getServerName() + request.getPathInfo());
	                return;
	            }
	        }
	        filterChain.doFilter(request, response);
	    }

	    @Override
	    public void destroy() {
	    }
	}
}
