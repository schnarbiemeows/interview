package com.schnarbiesnmeowers.interview.security;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static java.util.Arrays.stream;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.schnarbiesnmeowers.interview.utilities.Constants;

/**
 * this code was modified from code provided by a course taught by
 * Roland Tousssaint @ Get Arrays, LLC
 * https://getarrays.tech/#/course/jwt-springsecurity-angular
 * @author 
 *
 */
@Component
public class JwtTokenProvider {
	
	@Value("${jwt.secret}")
	private String secret;

	/**
	 * method to generate the JWT token
	 * @param principal
	 * @return
	 */
	public String generateJwtToken(UserPrincipal principal) {
		String[] claims = getClaimsFromPrincipal(principal); 
		Date newDate = new Date();
		return JWT.create().withIssuer(Constants.COMPANY)
				.withAudience(Constants.COMPANY_ADMINISTRATION)
				.withIssuedAt(new Date())
				.withSubject(principal.getUsername())
				.withArrayClaim(Constants.AUTHORITIES, claims)
				.withExpiresAt(new Date(newDate.getTime()+Constants.EXPIRATION_TIME))
				.sign(Algorithm.HMAC512(secret.getBytes()));
	}

	/**
	 * method to get the Authorizations from a UserPrincipal object
	 * @param principal
	 * @return
	 */
	private String[] getClaimsFromPrincipal(UserPrincipal principal) {
		List<String> authorizations = new ArrayList();
		for(GrantedAuthority authority : principal.getAuthorities()) {
			authorizations.add(authority.getAuthority());
		}
		return authorizations.toArray(new String[0]);
	}
	
	/**
	 * method to get a user's granted Authorizations from the token
	 * - this method is only called by the JwTAuthorizationFilter
	 * @param token
	 * @return
	 */
	public List<GrantedAuthority> getAuthorizations(String token) {
		String[] claims = getClaimsFromToken(token);
		return stream(claims).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
	}
	
	/**
	 * helper method to get a user's granted Authorizations from the token
	 * @param token
	 * @return
	 */
	private String[] getClaimsFromToken(String token) {
		JWTVerifier verifier = getVerifier();
		return verifier.verify(token).getClaim(Constants.AUTHORITIES).asArray(String.class);
	}

	/**
	 * helper method to get the JWTVerifier
	 * the JWTVerifier is needed to unpack the token, verify it, and get items from it
	 * @returns JWTVerifier
	 */
	private JWTVerifier getVerifier() {
		JWTVerifier verifier;
		try {
			Algorithm algorithm = HMAC512(secret);
			verifier = JWT.require(algorithm).withIssuer(Constants.COMPANY).build();
		} catch(JWTVerificationException e) {
			throw new JWTVerificationException(Constants.TOKEN_CANNOT_BE_VERIFIED);
		}
		return verifier;
	}
	
	/**
	 * method to return a User's valid Authorization after a token has been validated
	 * this method is only called by the JwtAuthorizationFilter for setting this user's
	 * information in the SecurityContext
	 * @param username
	 * @param authorizations
	 * @param request
	 * @returns UsernamePasswordAuthenticationToken
	 */
	public Authentication getUserAuthentication(String username, List<GrantedAuthority> authorizations, HttpServletRequest request) {
		UsernamePasswordAuthenticationToken usernamePasswordAuthToken = 
				new UsernamePasswordAuthenticationToken(username,null, authorizations);
		usernamePasswordAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		return usernamePasswordAuthToken;
	}
	
	/**
	 * method to check if this token is still valid, or has expired
	 * @param username
	 * @param token
	 * @return
	 */
	public boolean isThisAValidToken(String username, String token) {
		JWTVerifier verifier = getVerifier();
		return StringUtils.isNotEmpty(username) && !isTokenExpired(verifier, token);
	}

	/**
	 * method to check if this token has expired
	 * @param verifier
	 * @param token
	 * @return
	 */
	private boolean isTokenExpired(JWTVerifier verifier, String token) {
		Date expirationDate = verifier.verify(token).getExpiresAt();
		return expirationDate.before(new Date());
	}
	
	/**
	 * method to get the token's Subject
	 * @param token
	 * @return
	 */
	public String getSubject(String token) {
		JWTVerifier verifier = getVerifier();
		return verifier.verify(token).getSubject();
	}
}
