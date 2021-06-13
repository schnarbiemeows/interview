package com.schnarbiesnmeowers.interview.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.schnarbiesnmeowers.interview.pojos.PasswordReset;
/**
 *
 * @author Dylan I. Kessler
 *
 */
public interface PasswordResetRepository extends JpaRepository<PasswordReset, Integer>{

	PasswordReset findUserByEmailAddr(String emailAddr);
	PasswordReset findUserByUniqueId(String uniqueId);
}
