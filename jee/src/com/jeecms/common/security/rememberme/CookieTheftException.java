package com.jeecms.common.security.rememberme;

import org.apache.log4j.Logger;

/**
 * @author Luke Taylor
 * @version $Id: CookieTheftException.java,v 1.1 2011/12/26 03:47:58 Administrator Exp $
 */
@SuppressWarnings("serial")
public class CookieTheftException extends RememberMeAuthenticationException {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(CookieTheftException.class);

	public CookieTheftException(String message) {
		super(message);
	}
}
