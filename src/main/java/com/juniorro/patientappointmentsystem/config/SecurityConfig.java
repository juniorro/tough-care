package com.juniorro.patientappointmentsystem.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDetailsService userDetailsService;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}

	private static final String[] PUBLIC_ANT_MATCHERS = {
			"/assets/**", "/fonts/**", "/img/**", 
			"/js/**", "/login", "/home", "/about",
			"/register", "/confirm", "/contact", 
			"/recover/**", "/changePassword/**",
			"/reset/**", "/resetPassword"};
	private static final String[] SECURE_ANT_MATCHERS = {"/newUser/**", "/accessDenied"};

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers(PUBLIC_ANT_MATCHERS).permitAll().anyRequest().authenticated()
		.antMatchers(SECURE_ANT_MATCHERS).access("hasRole('ADMIN')")
		.and().exceptionHandling().accessDeniedPage("/accessDenied");

		http.csrf().disable().cors().disable().formLogin().loginPage("/login").permitAll().defaultSuccessUrl("/welcome")
				.loginProcessingUrl("/processLogin").failureUrl("/login?error");
		/*
		.logout().logoutUrl("/logout").permitAll()                                            
		.logoutSuccessUrl("/login?logout")                                       
		.invalidateHttpSession(true)                                             
		.deleteCookies("remember-me").and().rememberMe()*/
	}

}
