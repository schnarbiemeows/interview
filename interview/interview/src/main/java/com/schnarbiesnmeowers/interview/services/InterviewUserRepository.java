package com.schnarbiesnmeowers.interview.services;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.schnarbiesnmeowers.interview.pojos.InterviewUser;
/**
 *
 * @author Dylan I. Kessler
 *
 */
public interface InterviewUserRepository extends JpaRepository<InterviewUser, Integer>{

	InterviewUser findUserByUserName(String userName);
	InterviewUser findUserByEmailAddr(String emailAddr);
	InterviewUser findUserByUserIdentifier(String userIdentifier);
	@Query("Select u from InterviewUser u where u.roles in (:roles)")
	List<InterviewUser> findByRoleTypes(@Param("roles") String roles);
}
