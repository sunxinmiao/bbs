package com.jeecms.common.security;

import org.apache.log4j.Logger;

/**
 * 用户名没有找到异常
 * 
 * @author liufang
 * 
 */
@SuppressWarnings("serial")
public class UsernameNotFoundException extends AuthenticationException {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(UsernameNotFoundException.class);

	public UsernameNotFoundException() {
	}

	public UsernameNotFoundException(String msg) {
		super(msg);
	}
}