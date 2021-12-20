package com.schnarbiesnmeowers.interview.exceptions.handler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.schnarbiesnmeowers.interview.business.InterviewUserBusiness;
import com.schnarbiesnmeowers.interview.controllers.InterviewUserController;
import com.schnarbiesnmeowers.interview.security.JwtTokenProvider;
import com.schnarbiesnmeowers.interview.services.UserService;
import com.schnarbiesnmeowers.interview.services.impl.UserServiceImpl;

//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
//@AutoConfigureMockMvc
public class InterviewUserExceptionHandlingTest {

//	@Autowired
//	private MockMvc mockMvc;
//	
//	@Test
//	public void testException() {
//		this.mockMvc = MockMvcBuilders.standaloneSetup(new InterviewUserController(new InterviewUserBusiness(), 
//				new UserServiceImpl(null, null, null, null, null, null), 
//				null, new JwtTokenProvider()))
//				.setControllerAdvice(InterviewUserExceptionHandling.class)
//                .build();
//		
//	}
}
