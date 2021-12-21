package com.schnarbiesnmeowers.interview.controllers;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


//@WebMvcTest(HealthCheckController.class)
////@RunWith(SpringRunner.class)
//@Spring_X_BootTest
//@WebMvcTest
//@AutoConfigureMockMvc
//@TestPropertySource(properties = "spring.datasource.hikari.maximum-pool-size=1")
public class HealthCheckControllerTest {

//	public static final String SUCCESS = "success!";
//	
//	@Autowired
//	private MockMvc mockMvc;
//	
//	@Test
//	public void itemBasicTest() throws Exception {
//		RequestBuilder request = MockMvcRequestBuilders
//										.get("/healthcheck/ping")
//										.accept(MediaType.APPLICATION_JSON);
//		MvcResult mvcResult = mockMvc.perform(request)
//										.andExpect(status().isOk())
//										.andReturn();
//		assertEquals(SUCCESS,mvcResult.getResponse().getContentAsString());
//	}
}
