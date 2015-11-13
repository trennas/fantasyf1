package net.ddns.f1.config;

import java.util.Iterator;

import net.ddns.f1.domain.Team;
import net.ddns.f1.repository.TeamRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebMvcSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${auth.role-expression}")
	private String roleExpression;
	
	@Value("${auth.admin-role}")
	private String adminExpression;
	
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
		 .and().authorizeRequests().antMatchers("/editresult/**")
		 .hasRole(this.adminExpression)
		 .and().authorizeRequests().antMatchers("/")
		 .access(this.roleExpression);
		 http.headers()
		 .addHeaderWriter(
		 new XFrameOptionsHeaderWriter(
		 XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN));
    }

    @Autowired
    public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
        auth
            .inMemoryAuthentication()
                .withUser("admin").password("thorn47wibble62").roles(adminExpression);
        Iterator<Team> itr = teamRepo.findAll().iterator();
        while(itr.hasNext()) {
        	Team team = itr.next();
        	auth.inMemoryAuthentication().withUser(team.getEmail()).password(team.getPassword()).roles("USER");
        }
    }

}