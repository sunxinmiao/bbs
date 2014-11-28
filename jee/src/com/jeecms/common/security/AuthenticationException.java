package com.jeecms.common.security;

import org.apache.log4j.Logger;

/**
 * 登录异常
 * 
 * @author liufang
 * 
 */
@SuppressWarnings("serial")
public class AuthenticationException extends Exception {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(AuthenticationException.class);

	private Object extraInformation;

	public AuthenticationException() {

	}

	public AuthenticationException(String msg) {
		super(msg);
	}

	public AuthenticationException(String msg, Object extraInformation) {
		super(msg);
		this.extraInformation = extraInformation;
	}

	/**
	 * Any additional information about the exception. Generally a
	 * <code>UserDetails</code> object.
	 * 
	 * @return extra information or <code>null</code>
	 */
	public Object getExtraInformation() {
		return extraInformation;
	}

	public void clearExtraInformation() {
		if (logger.isDebugEnabled()) {
			logger.debug("clearExtraInformation() - start"); //$NON-NLS-1$
		}

		this.extraInformation = null;

		if (logger.isDebugEnabled()) {
			logger.debug("clearExtraInformation() - end"); //$NON-NLS-1$
		}
	}
}
