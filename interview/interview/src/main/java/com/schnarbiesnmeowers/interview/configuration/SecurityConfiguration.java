package com.schnarbiesnmeowers.interview.configuration;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.google.common.collect.ImmutableList;
import com.schnarbiesnmeowers.interview.security.JwtAccessDeniedHandler;
import com.schnarbiesnmeowers.interview.security.JwtAuthorizationFilter;
import com.schnarbiesnmeowers.interview.security.JwtFailedAuth403MessageHandler;
import com.schnarbiesnmeowers.interview.utilities.Constants;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private JwtAuthorizationFilter jwtAuthorizationFilter;
	private JwtAccessDeniedHandler jwtAccessDeniedHandler;
	private JwtFailedAuth403MessageHandler jwtAuthenticationEntryPoint;
	private UserDetailsService userDetailsService;
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	private static final Logger applicationLogger = LogManager.getLogger("FileAppender");
	
	@Value("${cors.urls}")
	private String cors_urls;
	
	@Autowired
	public SecurityConfiguration(JwtAuthorizationFilter jwtAuthorizationFilter,
			JwtAccessDeniedHandler jwtAccessDeniedHandler, 
			JwtFailedAuth403MessageHandler jwtAuthenticationEntryPoint,
			@Qualifier("UserDetailsService")UserDetailsService userDetailsService, 
			BCryptPasswordEncoder bCryptPasswordEncoder) {
		super();
		this.jwtAuthorizationFilter = jwtAuthorizationFilter;
		this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
		this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
		this.userDetailsService = userDetailsService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(this.userDetailsService)
		.passwordEncoder(this.bCryptPasswordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		/**
		 * tell it not to use cross-site request forgery
		 * tell it to use cross origin resource sharing
		 * configure a stateless session policy(no sessions)
		 * antMatchers : for these urls, you don't have to authenticate
		 */
		http.csrf().disable()
		.cors()
		.and()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.authorizeRequests().antMatchers(Constants.PUBLIC_URLS).permitAll()
		.anyRequest().authenticated()
		.and()
		.exceptionHandling().accessDeniedHandler(this.jwtAccessDeniedHandler)
		.authenticationEntryPoint(this.jwtAuthenticationEntryPoint)
		.and()
		.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        String[] urlsArray = cors_urls.split(",");
        System.out.println(urlsArray);
        configuration.setAllowedOrigins(Arrays.asList(urlsArray));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(ImmutableList.of("Authorization", "Cache-Control", "Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
	
	
	private static void logAction(String message) {
    	System.out.println(message);
    	applicationLogger.debug(message);
    }
}
