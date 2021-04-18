package com.schnarbiesnmeowers.interview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 *
 * @author Dylan I. Kessler
 *
 */
@SpringBootApplication
@EnableFeignClients("com.schnarbiesnmeowers.interview")
@EnableDiscoveryClient
public class InterviewApplication {

	//private static final Logger applicationLogger = LogManager.getLogger("FileAppender");

	/**
	 * this is the main class for our Interview application
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(InterviewApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}