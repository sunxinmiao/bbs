package com.jeecms.bbs.entity;

import org.apache.log4j.Logger;

import org.apache.commons.lang.StringUtils;

import com.jeecms.bbs.entity.base.BaseBbsUserExt;

public class BbsUserExt extends BaseBbsUserExt {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsUserExt.class);

	private static final long serialVersionUID = 1L;

	public void blankToNull() {
		if (logger.isDebugEnabled()) {
			logger.debug("blankToNull() - start"); //$NON-NLS-1$
		}

		// 将空串设置为null，便于前台处理。
		if (StringUtils.isBlank(getRealname())) {
			setRealname(null);
		}
		if (StringUtils.isBlank(getIntro())) {
			setIntro(null);
		}
		if (StringUtils.isBlank(getComefrom())) {
			setComefrom(null);
		}
		if (StringUtils.isBlank(getMoble())) {
			setMoble(null);
		}
		if (StringUtils.isBlank(getPhone())) {
			setPhone(null);
		}
		if (StringUtils.isBlank(getMsn())) {
			setMsn(null);
		}
		if (StringUtils.isBlank(getQq())) {
			setQq(null);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("blankToNull() - end"); //$NON-NLS-1$
		}
	}

	/* [CONSTRUCTOR MARKER BEGIN] */
	public BbsUserExt() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsUserExt(java.lang.Integer id) {
		super(id);
	}

	/* [CONSTRUCTOR MARKER END] */

}