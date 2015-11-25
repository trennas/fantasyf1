package net.ddns.f1.config;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.ddns.f1.domain.Team;
import net.ddns.f1.repository.TeamRepository;

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

@Configuration
@EnableWebMvcSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${auth.main-page-role-expression}")
	private String roleExpression;
	
	@Value("${auth.myaccount-role}")
	private String myAccountRole;
	
	@Value("${auth.admin-role}")
	private String adminRole;
	
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
		 
		 .and().authorizeRequests().antMatchers("/editresult/**", "/refreshAllResults/**")
		 .hasAuthority(adminRole)
		 
		 .and().authorizeRequests().antMatchers("/myaccount/**")
		 .hasAuthority(myAccountRole)
		 .and().authorizeRequests().antMatchers("/myaccount/myteam/**")
		 .hasAuthority(myAccountRole)
		 .and().authorizeRequests().antMatchers("/myaccount/savemyteam/**")
		 .hasAuthority(myAccountRole)
		 
		 .and().authorizeRequests().antMatchers("/", "/register/**", "/register/savemyteam/**")
		 .access(roleExpression);
		 
		 http.headers()
		 .addHeaderWriter(
		 new XFrameOptionsHeaderWriter(
		 XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN));
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(inMemoryUserDetailsManager());
    }
    
    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {    	
        final List<UserDetails> users = new ArrayList<UserDetails>();
        
        Iterator<Team> itr = teamRepo.findAll().iterator();
        while(itr.hasNext()) {
        	Team team = itr.next();
        	List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			for(String role : team.getRoles()) {
				authorities.add(new SimpleGrantedAuthority(role));
			}
        	users.add(new User(team.getEmail(), team.getPassword(), authorities));
        }
        
        return new InMemoryUserDetailsManager(users);
    }
}