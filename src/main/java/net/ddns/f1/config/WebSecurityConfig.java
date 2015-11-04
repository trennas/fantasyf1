package net.ddns.f1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;

@Configuration
@ConfigurationProperties(prefix = "auth")
@EnableWebMvcSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private String roleExpression;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests().anyRequest().permitAll();
		// http.csrf()
		// .disable()
		// .authorizeRequests()
		// .antMatchers("/css/**")
		// .permitAll()
		// .and()
		// .authorizeRequests()
		// .antMatchers("/js/**")
		// .permitAll()
		// .and()
		// .authorizeRequests()
		// .antMatchers("/image/**")
		// .permitAll()
		// .and()
		// .authorizeRequests()
		// .antMatchers("/fonts/**")
		// .permitAll()
		// .and()
		// .authorizeRequests()
		// .antMatchers("/loginError/**")
		// .permitAll()
		// .and()
		// .authorizeRequests()
		// .antMatchers("/lookupAddresses/**")
		// .permitAll()
		// .and()
		// .authorizeRequests()
		// .antMatchers("/validatePayment/**")
		// .permitAll()
		// .and()
		// .exceptionHandling()
		// .accessDeniedPage("/accessError")
		// .and()
		// .formLogin()
		// .loginPage("/login")
		// .failureUrl("/loginError")
		// .permitAll()
		// .and()
		// .logout()
		// .logoutRequestMatcher(
		// new AntPathRequestMatcher("/logout", "GET"))
		// .permitAll().and().authorizeRequests().anyRequest()
		// .access(this.roleExpression);
		// http.headers()
		// .addHeaderWriter(
		// new XFrameOptionsHeaderWriter(
		// XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN));
    }

    @Autowired
    public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
        auth
            .inMemoryAuthentication()
                .withUser("ff1").password("ff1").roles("USER");
    }

}