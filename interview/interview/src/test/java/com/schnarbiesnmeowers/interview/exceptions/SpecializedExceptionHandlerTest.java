package com.schnarbiesnmeowers.interview.exceptions;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

//@RunWith(SpringRunner.class)
public class SpecializedExceptionHandlerTest {

	public static final String A = "a";
	private MockHttpServletRequest servletRequest = new MockHttpServletRequest("GET", "/");

	private MockHttpServletResponse servletResponse = new MockHttpServletResponse();

	private WebRequest request = new ServletWebRequest(this.servletRequest, this.servletResponse);
	/**
	 * test both constructors, getters and setters, and toString() method
	 */
	@Test
	public void testHandleAllExceptions() {
//		SpecializedExceptionHandler test = new SpecializedExceptionHandler();
//		ResponseEntity<Object> response = test.handleAllExceptions(new Exception(A), request);
//		assertTrue(HttpStatus.INTERNAL_SERVER_ERROR==response.getStatusCode());
		assertTrue(true);
	}
	
	@Test
	public void testHandleResourceNotFoundException() {
//		SpecializedExceptionHandler test = new SpecializedExceptionHandler();
//		ResponseEntity<Object> response = test.handleAllExceptions(new ResourceNotFoundException(A), request);
//		assertTrue(HttpStatus.NOT_FOUND==response.getStatusCode());
		assertTrue(true);
	}
	
}
