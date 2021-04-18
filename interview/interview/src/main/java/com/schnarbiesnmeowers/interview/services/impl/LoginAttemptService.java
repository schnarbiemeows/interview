package com.schnarbiesnmeowers.interview.services.impl;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * 
 * @author dylan
 *
 */
@Service
public class LoginAttemptService {

	private static final int MAX_NUM_TRIES = 5;
	
	private LoadingCache<String, Integer> loginAttemptsCache ;
	
	/**
	 * constructor
	 */
	public LoginAttemptService() {
		super();
		loginAttemptsCache = CacheBuilder.newBuilder().expireAfterWrite(15,TimeUnit.MINUTES)
				.maximumSize(100).build( new CacheLoader<String, Integer>() {
					public Integer load(String key) {
						return 0;
					}
				});			
	}
	
	/**
	 * remove a given user from the cache
	 * @param username
	 */
	public void evictUserFromLoginCache(String username) {
		this.loginAttemptsCache.invalidate(username);
	}
	
	/**
	 * add and/or increment a user in the cache
	 * @param username
	 */
	public void addUserToLoginCache(String username) {
		int attempts = 0;
		try {
			// TODO - refactor this to test if the key is in the map 
			attempts = this.loginAttemptsCache.get(username) + 1;
		} catch(ExecutionException e) {
			e.printStackTrace();
		}	
		this.loginAttemptsCache.put(username, attempts);
	}
	
	/**
	 * check to see if a user has exceeded their maximum number 
	 * @param username
	 * @return
	 */
	public boolean hasExceededMaxAttempts(String username) {
        try {
        	// TODO - refactor this to test if the key is in the map
            return this.loginAttemptsCache.get(username) >= MAX_NUM_TRIES;
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }
}
