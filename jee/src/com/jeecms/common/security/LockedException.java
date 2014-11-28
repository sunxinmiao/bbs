package com.jeecms.common.security;

import org.apache.log4j.Logger;

/**
 * 账号被锁定异常
 * 
 * @author liufang
 * 
 */
@SuppressWarnings("serial")
public class LockedException extends AccountStatusException {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(LockedException.class);

	public LockedException() {
	}

	public LockedException(String msg) {
		super(msg);
	}

	public LockedException(String msg, Object extraInformation) {
		super(msg, extraInformation);
	}
}
