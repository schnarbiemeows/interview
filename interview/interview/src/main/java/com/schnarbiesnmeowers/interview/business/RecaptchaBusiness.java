package com.schnarbiesnmeowers.interview.business;

import java.net.URI;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestOperations;

import com.schnarbiesnmeowers.interview.dtos.GoogleResponseDTO;
import com.schnarbiesnmeowers.interview.exceptions.InvalidReCaptchaException;
import com.schnarbiesnmeowers.interview.exceptions.ReCaptchaInvalidException;

@Component
public class RecaptchaBusiness {

	private static final Logger applicationLogger = LogManager.getLogger("FileAppender");
	private static Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");
	
	@Value("${google.url}")
	private String google_url;
	
	@Value("${recaptcha.secret}")
	private String secret;
	
	@Autowired
    private RestOperations restTemplate;
	
	public GoogleResponseDTO validateRecaptcha(String response) throws InvalidReCaptchaException, ReCaptchaInvalidException {
		logAction("validating recaptcha");
		GoogleResponseDTO googleResponse = null;
		if(!responseSanityCheck(response)) {
			logAction("recaptcha failed sanity check");
            throw new InvalidReCaptchaException("Response contains invalid characters");
        }
		URI verifyUri = URI.create(String.format(google_url+"?secret=%s&response=%s",secret, response));
		googleResponse = restTemplate.getForObject(verifyUri, GoogleResponseDTO.class);
		if(!googleResponse.isSuccess()) {
			logAction("recaptcha failed the backend call to google");
            throw new ReCaptchaInvalidException("reCaptcha was not successfully validated");
        } 
		logAction("recaptcha validated");
		return googleResponse;
	}
	
	private boolean responseSanityCheck(String response) {
        return StringUtils.hasLength(response) && RESPONSE_PATTERN.matcher(response).matches();
    }
	private static void logAction(String message) {
    	System.out.println(message);
    	applicationLogger.debug(message);
    }
}
