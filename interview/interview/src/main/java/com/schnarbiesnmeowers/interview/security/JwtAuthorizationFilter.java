package com.schnarbiesnmeowers.interview.security;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.common.net.HttpHeaders;
import com.schnarbiesnmeowers.interview.utilities.Constants;

/**
 * this code was modified from code provided by a course taught by
 * Roland Toussaint @ Get Arrays, LLC
 * https://getarrays.tech/#/course/jwt-springsecurity-angular
 * @author 
 *
 */
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

	private static final Logger applicationLogger = LogManager.getLogger("IpAppender");
	
	/**
	 * 
	 */
	private JwtTokenProvider jwtTokenProvider;
	
	/**
	 * 
	 * @param jwtTokenProvider
	 */
	public JwtAuthorizationFilter(JwtTokenProvider jwtTokenProvider) {
		super();
		this.jwtTokenProvider = jwtTokenProvider;
	}


	/**
	 * 
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		/**
		 *  all server requests actually consist of 2 request, the first one is an OPTIONS request
		 *  to get server information. If this request is one of those, let it pass through
		 */
		logAction("inside doFilterInternal");
		if(request.getMethod().equalsIgnoreCase(Constants.OPTIONS_HTTP_METHOD)) {
			logAction("request is an OPTION");
			response.setStatus(HttpStatus.OK.value());
		} else {
			/**
			 * otherwise, if there is no AUTHORIZATION header, or if it is not a bearer token
			 * return nothing
			 */
			String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
			if(authHeader == null || !authHeader.startsWith(Constants.TOKEN_PREFIX)) {
				logAction("authHeader = null OR is not a Bearer token");
				filterChain.doFilter(request, response);
				return;
			}
			String token = authHeader.replace(Constants.TOKEN_PREFIX, "");
			String username = this.jwtTokenProvider.getSubject(token);
			/**
			 * the SecurityContextHolder.getContext() code is only needed if we are using an HttpSession
			 */
			if (this.jwtTokenProvider.isThisAValidToken(username,
					token) /* && SecurityContextHolder.getContext().getAuthentication() == null */ ) {
				// get the authorizations
				logAction("token is valid");
				List<GrantedAuthority> authorizations = this.jwtTokenProvider.getAuthorizations(token);
				// get the authentications
				Authentication userAuth = this.jwtTokenProvider.getUserAuthentication(username, authorizations, request);
				// again, only needed if we are using a session
				SecurityContextHolder.getContext().setAuthentication(userAuth);
			} else {
				logAction("token is NOT valid");
				// again, only needed if we are using a session
				SecurityContextHolder.clearContext();
			}
		}
		filterChain.doFilter(request, response);
	}

	private static void logAction(String message) {
    	System.out.println("JwtAuthorizationFilter : " + message);
    	applicationLogger.debug("JwtAuthorizationFilter : " + message);
    }
}
