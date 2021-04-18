package com.schnarbiesnmeowers.interview.security;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.schnarbiesnmeowers.interview.exceptions.handler.HttpResponse;
import com.schnarbiesnmeowers.interview.utilities.Constants;

/**
 * this code was modified from code provided by a course taught by
 * Roland Toussaint @ Get Arrays, LLC
 * https://getarrays.tech/#/course/jwt-springsecurity-angular
 * @author 
 *
 * the purpose of this handler is to give the user a custom message whenever they are trying to access
 * a resource that they do not have permissions to access
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

	private static final Logger applicationLogger = LogManager.getLogger("FileAppender");
	
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception)
			throws IOException, ServletException {
		logAction("user is trying to access a resource they do not have permission to access");
		HttpResponse resp = new HttpResponse(HttpStatus.UNAUTHORIZED.value(),
				HttpStatus.UNAUTHORIZED,HttpStatus.UNAUTHORIZED.getReasonPhrase(),
				Constants.ACCESS_DENIED_MESSAGE);
		response.setContentType(APPLICATION_JSON_VALUE);
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		OutputStream out = response.getOutputStream();
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(out, resp);
		out.flush();	
	}
	
	private static void logAction(String message) {
    	System.out.println(message);
    	applicationLogger.debug(message);
    }
}
