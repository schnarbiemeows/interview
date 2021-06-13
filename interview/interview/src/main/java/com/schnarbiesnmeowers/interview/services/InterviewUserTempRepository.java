package com.schnarbiesnmeowers.interview.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.schnarbiesnmeowers.interview.pojos.InterviewUserTemp;
/**
 *
 * @author Dylan I. Kessler
 *
 */
public interface InterviewUserTempRepository extends JpaRepository<InterviewUserTemp, Integer>{

	InterviewUserTemp findUserByUserName(String userName);
	InterviewUserTemp findUserByUniqueId(String uniqueId);
}
