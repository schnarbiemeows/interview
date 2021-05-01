package com.schnarbiesnmeowers.interview.utilities;

/**
 * the class that defines our permissions:
 * data:select - user can view the questions, answers, question levels, and question categories
 * data:update - user can update any questions, answers, question levels, and question categories
 * data:create - user can create questions, answers, question levels, and question categories
 * data:delete - user can delete any questions, answers, question levels, and question categories
 * self:udate - user or admin can update their personal data
 * self:lvl1 - user can store basic aggregate data about their testing performance
 * self:lvl2 - user can store more option with their testing performance
 * admin:select - admin can view all admin data and user data
 * admin:update - admin can create users(non-admin)
 * admin:create - admin can create other admins
 * admin:delete - admin can delete admins
 * user:select - admin can view all user(non-admin) data
 * user:update - admin can update all user(non-admin) data
 * user:create - admin can create users(non-admin)
 * user:delete - admin can delete users(non-admin)
 * 
 * @author dylan
 *
 */
public class Authorizations {

	
	public static final String[] BASIC_USER_AUTH = { "data:select", "self:update" };
	public static final String[] ADV_USER_AUTH = { "data:select", "self:lvl1", "self:update" };
	public static final String[] PREMIUM_USER_AUTH = { "data:select", "self:lvl1", "self:lvl2", "self:update" };
	public static final String[] ADMIN_AUTH = { "data:select","data:update","data:create","data:delete", 
			"self:lvl1", "self:lvl2", "admin:select","self:update","user:select","user:update",
			"user:create","user:delete" };
	public static final String[] SUPER_ADMIN_AUTH = { "data:select","data:update","data:create","data:delete", 
			"self:lvl1", "self:lvl2", "admin:select","self:update", "admin:update","admin:create","admin:delete",
			"user:select","user:update","user:create","user:delete", };
}
