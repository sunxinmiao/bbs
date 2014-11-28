package com.jeecms.bbs.entity;

import org.apache.log4j.Logger;

import com.jeecms.bbs.entity.base.BaseBbsMemberMagic;



public class BbsMemberMagic extends BaseBbsMemberMagic {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsMemberMagic.class);

	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsMemberMagic () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsMemberMagic (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsMemberMagic (
		java.lang.Integer id,
		com.jeecms.bbs.entity.BbsUser user,
		com.jeecms.bbs.entity.BbsCommonMagic magic) {

		super (
			id,
			user,
			magic);
	}

/*[CONSTRUCTOR MARKER END]*/


}