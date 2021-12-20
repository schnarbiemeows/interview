package com.schnarbiesnmeowers.interview.utilities;

import com.schnarbiesnmeowers.interview.dtos.InterviewUserDTO;

public class HelperUtility {

	public static InterviewUserDTO generateRandomInterviewUserDTO() {
		InterviewUserDTO record = new InterviewUserDTO();
		record.setEmailAddr(Randomizer.randomString(20));
		record.setFirstName(Randomizer.randomString(20));
		record.setUserActive(Randomizer.randomBoolean());
		record.setUserNotLocked(Randomizer.randomBoolean());
		record.setJoinDate(Randomizer.randomDate());
		record.setLastLoginDate(Randomizer.randomDate());
		record.setLastLoginDateDisplay(Randomizer.randomDate());
		record.setLastName(Randomizer.randomString(20));
		record.setPassword(Randomizer.randomString(20));
		record.setProfileImage(Randomizer.randomString(20));
		record.setUserIdentifier(Randomizer.randomString(20));
		record.setUserName(Randomizer.randomString(20));
		return record;
	}

	public static void addRolesAndAuthorizationsToUser(Roles role, InterviewUserDTO user) {
		user.setRoles(role.name());
		user.setAuthorizations(role.getAuthorizations());
	}
}
