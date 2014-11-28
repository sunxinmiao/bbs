package com.jeecms.common.security.rememberme;

import org.apache.log4j.Logger;

import com.jeecms.common.security.AuthenticationException;

@SuppressWarnings("serial")
public class RememberMeAuthenticationException extends AuthenticationException {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(RememberMeAuthenticationException.class);

	public RememberMeAuthenticationException() {
	}

	public RememberMeAuthenticationException(String msg) {
		super(msg);
	}
}
