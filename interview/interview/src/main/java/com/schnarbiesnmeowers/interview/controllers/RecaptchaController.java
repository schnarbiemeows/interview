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
import com.schnarbiesnmeowers.interview.exceptions.InvalidReCaptchaException;
import com.schnarbiesnmeowers.interview.exceptions.ReCaptchaInvalidException;

@RestController
@RequestMapping(path="/recaptcha")
public class RecaptchaController {

	@Autowired
	private RecaptchaBusiness recaptchaBusiness;
	
	@PostMapping(path = "/post")
	public ResponseEntity<GoogleResponseDTO> validateRecaptcha(@Valid @RequestBody GoogleRequestDTO data) throws InvalidReCaptchaException, ReCaptchaInvalidException {
		try {
			GoogleResponseDTO response = recaptchaBusiness.validateRecaptcha(data.getResponse());
		    return ResponseEntity.status(HttpStatus.CREATED).body(response);
		} catch(InvalidReCaptchaException e1) {
			throw e1;
		} catch(ReCaptchaInvalidException e2) {
			throw e2;
		}
	}

}
