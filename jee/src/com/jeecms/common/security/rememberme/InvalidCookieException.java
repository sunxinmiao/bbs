package com.jeecms.common.security.rememberme;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class InvalidCookieException extends RememberMeAuthenticationException {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(InvalidCookieException.class);

	public InvalidCookieException() {
	}

	public InvalidCookieException(String msg) {
		super(msg);
	}
}
