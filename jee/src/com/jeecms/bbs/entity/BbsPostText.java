package com.jeecms.bbs.entity;

import org.apache.log4j.Logger;

import com.jeecms.bbs.entity.base.BaseBbsPostText;



public class BbsPostText extends BaseBbsPostText {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsPostText.class);

	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsPostText () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsPostText (java.lang.Integer id) {
		super(id);
	}

/*[CONSTRUCTOR MARKER END]*/


}