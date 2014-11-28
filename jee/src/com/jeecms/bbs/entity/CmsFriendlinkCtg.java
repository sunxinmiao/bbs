package com.jeecms.bbs.entity;

import org.apache.log4j.Logger;

import com.jeecms.bbs.entity.base.BaseCmsFriendlinkCtg;



public class CmsFriendlinkCtg extends BaseCmsFriendlinkCtg {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(CmsFriendlinkCtg.class);

	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public CmsFriendlinkCtg () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public CmsFriendlinkCtg (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public CmsFriendlinkCtg (
		java.lang.Integer id,
		com.jeecms.core.entity.CmsSite site,
		java.lang.String name,
		java.lang.Integer priority) {

		super (
			id,
			site,
			name,
			priority);
	}

/*[CONSTRUCTOR MARKER END]*/


}