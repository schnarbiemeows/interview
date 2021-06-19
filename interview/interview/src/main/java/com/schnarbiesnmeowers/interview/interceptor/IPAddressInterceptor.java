package com.schnarbiesnmeowers.interview.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class IPAddressInterceptor implements HandlerInterceptor {

	private static final Logger applicationLogger = LogManager.getLogger("IpAppender");
	
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
 
        String ipAddress = request.getHeader("X-Forward-For");
        String url = request.getRequestURL().toString();
        if(ipAddress== null){
 
            ipAddress = request.getRemoteAddr();
        }
        logAction("IP address = " + ipAddress + " ----> URL = " + url);
        return true;
    }
	
	/**
	 * logging method
	 * 
	 * @param message
	 */
	private static void logAction(String message) {
		System.out.println("IPAddressInterceptor : " + message);
		applicationLogger.debug("IPAddressInterceptor : " + message);
	}
}
