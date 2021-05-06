package com.schnarbiesnmeowers.interview.utilities;

import static com.schnarbiesnmeowers.interview.utilities.Authorizations.*;

/**
 * 
 * @author Dylan I. Kessler
 *
 */
public enum Roles {

	ROLE_BASIC_USER(BASIC_USER_AUTH),
	ROLE_ADV_USER(ADV_USER_AUTH),
	ROLE_PREMIUM_USER(PREMIUM_USER_AUTH),
	ROLE_ADMIN(ADMIN_AUTH),
	ROLE_SUPER(SUPER_ADMIN_AUTH);
	
	private String[] authorizations;
	
	Roles(String... authorizations) {
		this.authorizations = authorizations;
	}
	
	public String[] getAuthorizations() {
		return this.authorizations;
	}
}
