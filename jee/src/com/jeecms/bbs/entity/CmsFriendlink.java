package com.jeecms.bbs.entity;

import org.apache.log4j.Logger;

import org.apache.commons.lang.StringUtils;

import com.jeecms.bbs.entity.base.BaseCmsFriendlink;

public class CmsFriendlink extends BaseCmsFriendlink {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(CmsFriendlink.class);

	private static final long serialVersionUID = 1L;

	public void init() {
		if (logger.isDebugEnabled()) {
			logger.debug("init() - start"); //$NON-NLS-1$
		}

		if (getPriority() == null) {
			setPriority(10);
		}
		if (getViews() == null) {
			setViews(0);
		}
		if (getEnabled() == null) {
			setEnabled(true);
		}
		blankToNull();

		if (logger.isDebugEnabled()) {
			logger.debug("init() - end"); //$NON-NLS-1$
		}
	}

	public void blankToNull() {
		if (logger.isDebugEnabled()) {
			logger.debug("blankToNull() - start"); //$NON-NLS-1$
		}

		if (StringUtils.isBlank(getLogo())) {
			setLogo(null);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("blankToNull() - end"); //$NON-NLS-1$
		}
	}

	/* [CONSTRUCTOR MARKER BEGIN] */
	public CmsFriendlink () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public CmsFriendlink (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public CmsFriendlink (
		java.lang.Integer id,
		com.jeecms.bbs.entity.CmsFriendlinkCtg category,
		com.jeecms.core.entity.CmsSite site,
		java.lang.String name,
		java.lang.String domain,
		java.lang.Integer views,
		java.lang.Integer priority,
		java.lang.Boolean enabled) {

		super (
			id,
			category,
			site,
			name,
			domain,
			views,
			priority,
			enabled);
	}

	/* [CONSTRUCTOR MARKER END] */

}