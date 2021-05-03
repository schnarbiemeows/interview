package com.schnarbiesnmeowers.interview.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/healthcheck")
public class HealthCheckController {

	@GetMapping(path="/ping")
	public String healthcheck() {
		return "success!";
	}
}
