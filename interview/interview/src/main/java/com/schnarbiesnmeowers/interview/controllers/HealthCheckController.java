package com.schnarbiesnmeowers.interview.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/healthcheck")
public class HealthCheckController {

	private static final Logger applicationLogger = LogManager.getLogger("FileAppender");
	
	@GetMapping(path="/ping")
	public String healthcheck() {
		logAction("success");
		return "success!";
	}
	
	@GetMapping(path="/pong")
	public String healthcheck2() {
		logAction("success");
		return "pong!";
	}
	
	private static void logAction(String message) {
    	System.out.println(message);
    	applicationLogger.debug(message);
    }
}
