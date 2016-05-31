package fantasyf1.config;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import fantasyf1.domain.Team;
import fantasyf1.repository.TeamRepository;

@Configuration
@EnableWebMvcSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${auth.main-page-role-expression}")
	private String roleExpression;

	@Value("${auth.myaccount-role}")
	private String myAccountRole;

	@Value("${auth.admin-role}")
	private String adminRole;
	
	@Value("${admin-user}")
	private String adminUser;
	
	@Value("${admin-password}")
	private String adminPassword;

	@Autowired
	private TeamRepository teamRepo;

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http.csrf()
				.disable()
				.authorizeRequests()
				.antMatchers("/css/**")
				.permitAll()
				.and()
				.authorizeRequests()
				.antMatchers("/js/**")
				.permitAll()
				.and()
				.authorizeRequests()
				.antMatchers("/image/**")
				.permitAll()
				.and()
				.authorizeRequests()
				.antMatchers("/fonts/**")
				.permitAll()
				.and()
				.authorizeRequests()
				.antMatchers("/loginError/**")
				.permitAll()
				.and()
				.authorizeRequests()
				.antMatchers("/error/**")
				.permitAll()
				.and()
				.authorizeRequests()
				.antMatchers("/lookupAddresses/**")
				.permitAll()
				.and()
				.exceptionHandling()
				.accessDeniedPage("/accessError")
				.and()
				.formLogin()
				.loginPage("/login")
				.failureUrl("/loginError")
				.permitAll()
				.and()
				.logout()
				.logoutRequestMatcher(
						new AntPathRequestMatcher("/logout", "GET"))
				.permitAll()

				.and()
				.authorizeRequests()
				.antMatchers("/editresult/**", "/addresult/**",
						"/deleteteam/**", "/refreshAllResults/**", "/components/**")
				.hasAuthority(adminRole)

				.and().authorizeRequests().antMatchers("/myaccount/**")
				.hasAuthority(myAccountRole).and().authorizeRequests()
				.antMatchers("/myaccount/myteam/**")
				.hasAuthority(myAccountRole).and().authorizeRequests()
				.antMatchers("/myaccount/savemyteam/**")
				.hasAuthority(myAccountRole)

				.and().authorizeRequests()
				.antMatchers("/", "/register/**", "/register/savemyteam/**")
				.access(roleExpression);

		http.headers()
				.addHeaderWriter(
						new XFrameOptionsHeaderWriter(
								XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN));
	}

	@Override
	protected void configure(final AuthenticationManagerBuilder auth)
			throws Exception {
		auth.userDetailsService(inMemoryUserDetailsManager());
	}
	
	private void createAdminUser(final List<UserDetails> users) {
		final List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(adminRole));
		users.add(new User(adminUser, adminPassword, authorities));
	}

	@Bean
	public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
		final List<UserDetails> users = new ArrayList<>();		
		createAdminUser(users);	
		
		final Iterator<Team> itr = teamRepo.findAll().iterator();
		while (itr.hasNext()) {
			final Team team = itr.next();
				final List<GrantedAuthority> authorities = new ArrayList<>();
				for (final String role : team.getRoles()) {
					authorities.add(new SimpleGrantedAuthority(role));
				}
				users.add(new User(team.getEmail(), team.getPassword(),
						authorities));
		}

		return new InMemoryUserDetailsManager(users);
	}
}