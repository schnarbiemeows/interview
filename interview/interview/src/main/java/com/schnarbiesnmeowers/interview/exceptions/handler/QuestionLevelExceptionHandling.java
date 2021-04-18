package com.schnarbiesnmeowers.interview.exceptions.handler;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class QuestionLevelExceptionHandling implements ErrorController {
	private static final Logger applicationLogger = LogManager.getLogger("FileAppender");

	public static final String ERROR_PATH = "/error";
	
	private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus,
                httpStatus.getReasonPhrase().toUpperCase(), message), httpStatus);
    }
	
	@RequestMapping(ERROR_PATH)
    public ResponseEntity<HttpResponse> notFound404() {
        return createHttpResponse(NOT_FOUND, "There is no mapping for this URL");
    }
	
	@Override
    public String getErrorPath() {
    	/**
    	 * we are implementing Spring's ErrorController
    	 * here, we are overriding the natural URl that results on a 404 error
    	 * and then above, we have a method to handle this
    	 * we have to add {"/","/user"} in the UserController in order for this to work
    	 */
        return ERROR_PATH;
    }
	
	private static void logAction(String message) {
    	System.out.println(message);
    	applicationLogger.debug(message);
    }
}
