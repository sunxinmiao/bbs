package com.jeecms.common.security;

import org.apache.log4j.Logger;

/**
 * 账号状态异常
 * 
 * @author liufang
 * 
 */
@SuppressWarnings("serial")
public class AccountStatusException extends AuthenticationException {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(AccountStatusException.class);

	public AccountStatusException() {
	}

	public AccountStatusException(String msg) {
		super(msg);
	}

	public AccountStatusException(String msg, Object extraInformation) {
		super(msg, extraInformation);
	}
}
