package com.jeecms.bbs.entity;

import org.apache.log4j.Logger;

import com.jeecms.bbs.entity.base.BaseBbsPostType;



public class BbsPostType extends BaseBbsPostType {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsPostType.class);

	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsPostType () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsPostType (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsPostType (
		java.lang.Integer id,
		com.jeecms.core.entity.CmsSite site,
		com.jeecms.bbs.entity.BbsForum forum) {

		super (
			id,
			site,
			forum);
	}

/*[CONSTRUCTOR MARKER END]*/


}