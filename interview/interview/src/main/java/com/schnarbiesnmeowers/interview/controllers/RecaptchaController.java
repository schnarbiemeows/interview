package com.schnarbiesnmeowers.interview.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schnarbiesnmeowers.interview.business.RecaptchaBusiness;
import com.schnarbiesnmeowers.interview.dtos.GoogleRequestDTO;
import com.schnarbiesnmeowers.interview.dtos.GoogleResponseDTO;

@RestController
@RequestMapping(path="/recaptcha")
public class RecaptchaController {

	@Autowired
	private RecaptchaBusiness recaptchaBusiness;
	
	@PostMapping(path = "/post")
	public ResponseEntity<GoogleResponseDTO> validateRecaptcha(@Valid @RequestBody GoogleRequestDTO data) throws Exception {
		try {
			GoogleResponseDTO response = recaptchaBusiness.validateRecaptcha(data.getResponse());
		    return ResponseEntity.status(HttpStatus.CREATED).body(response);
		} catch (Exception e) {
			throw e;
		}
	}

}
