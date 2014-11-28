package com.jeecms.common.security;

import org.apache.log4j.Logger;

/**
 * 用户被禁用异常
 * 
 * @author liufang
 * 
 */
@SuppressWarnings("serial")
public class DisabledException extends AccountStatusException {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DisabledException.class);

	public DisabledException() {
	}

	public DisabledException(String msg) {
		super(msg);
	}

	public DisabledException(String msg, Object extraInformation) {
		super(msg, extraInformation);
	}
}
