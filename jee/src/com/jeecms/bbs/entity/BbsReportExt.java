package com.jeecms.bbs.entity;

import org.apache.log4j.Logger;

import com.jeecms.bbs.entity.base.BaseBbsReportExt;



public class BbsReportExt extends BaseBbsReportExt {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsReportExt.class);

	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsReportExt () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsReportExt (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsReportExt (
		java.lang.Integer id,
		com.jeecms.bbs.entity.BbsUser reportUser,
		com.jeecms.bbs.entity.BbsReport report,
		java.util.Date reportTime) {

		super (
			id,
			reportUser,
			report,
			reportTime);
	}

/*[CONSTRUCTOR MARKER END]*/


}