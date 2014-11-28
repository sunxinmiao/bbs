package com.jeecms.bbs.entity;

import org.apache.log4j.Logger;

import com.jeecms.bbs.entity.base.BaseBbsLoginLog;



public class BbsLoginLog extends BaseBbsLoginLog {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsLoginLog.class);

	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsLoginLog () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsLoginLog (java.lang.Integer id) {
		super(id);
	}

/*[CONSTRUCTOR MARKER END]*/


}