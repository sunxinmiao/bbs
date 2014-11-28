package com.jeecms.common.security;

import org.apache.log4j.Logger;

/**
 * 账号过期异常。如：改账号只缴纳了一年的费用，一年后没有续费。
 * 
 * @author liufang
 * 
 */
@SuppressWarnings("serial")
public class AccountExpiredException extends AccountStatusException {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(AccountExpiredException.class);

	public AccountExpiredException() {
	}

	public AccountExpiredException(String msg) {
		super(msg);
	}

	public AccountExpiredException(String msg, Object extraInformation) {
		super(msg, extraInformation);
	}
}
