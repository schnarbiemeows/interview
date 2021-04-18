package com.schnarbiesnmeowers.interview.security;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
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
 */
@Component
public class JwtFailedAuth403MessageHandler extends Http403ForbiddenEntryPoint {

	private static final Logger applicationLogger = LogManager.getLogger("FileAppender");
	
	/**
	 * we need to override this method in order to give a custom output response message to the user
	 */
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
		logAction("forbidden request!");
		HttpResponse resp = new HttpResponse(HttpStatus.FORBIDDEN.value(),
				HttpStatus.FORBIDDEN,HttpStatus.FORBIDDEN.getReasonPhrase(),
				Constants.FORBIDDEN_MESSAGE);
		response.setContentType(APPLICATION_JSON_VALUE);
		response.setStatus(HttpStatus.FORBIDDEN.value());
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
