package com.schnarbiesnmeowers.interview.exceptions;

import java.util.Date;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * class for specialized exception handlers
 * @author Dylan I. Kessler
 *
 */
@ControllerAdvice
@RestController
public class SpecializedExceptionHandler extends ResponseEntityExceptionHandler {

	//private static final Logger applicationLogger = LogManager.getLogger("FileAppender");

	/**
	 * override of the generic Exception handler
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(),
				request.getDescription(false));
		return new ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * handle when a given resource cannot be located
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler(ResourceNotFoundException.class)
	public final ResponseEntity<Object> handleUserNotFoundException(ResourceNotFoundException ex, WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(),
				request.getDescription(false));
		return new ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND);
	}

	/**
	 * handle validation fail for a given parameter
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), "Validation Failed",
				ex.getBindingResult().toString());
		return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
}
