package com.jeecms.core.entity;

import org.apache.log4j.Logger;

import java.sql.Timestamp;
import java.util.Date;

import com.jeecms.core.entity.base.BaseAuthentication;

public class Authentication extends BaseAuthentication {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(Authentication.class);

	private static final long serialVersionUID = 1L;

	public void init() {
		if (logger.isDebugEnabled()) {
			logger.debug("init() - start"); //$NON-NLS-1$
		}

		Date now = new Timestamp(System.currentTimeMillis());
		setLoginTime(now);
		setUpdateTime(now);

		if (logger.isDebugEnabled()) {
			logger.debug("init() - end"); //$NON-NLS-1$
		}
	}

	/* [CONSTRUCTOR MARKER BEGIN] */
	public Authentication () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Authentication (java.lang.String id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public Authentication (
		java.lang.String id,
		java.lang.Integer uid,
		java.lang.String username,
		java.util.Date loginTime,
		java.lang.String loginIp,
		java.util.Date updateTime) {

		super (
			id,
			uid,
			username,
			loginTime,
			loginIp,
			updateTime);
	}

	/* [CONSTRUCTOR MARKER END] */

}