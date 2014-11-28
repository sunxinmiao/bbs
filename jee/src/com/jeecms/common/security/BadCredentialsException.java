package com.jeecms.common.security;

import org.apache.log4j.Logger;

/**
 * 认证信息错误异常。如：密码错误。
 * 
 * @author liufang
 * 
 */
@SuppressWarnings("serial")
public class BadCredentialsException extends AuthenticationException {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BadCredentialsException.class);

	public BadCredentialsException() {
	}

	public BadCredentialsException(String msg) {
		super(msg);
	}
}
