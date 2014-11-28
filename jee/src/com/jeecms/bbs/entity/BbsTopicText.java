package com.jeecms.bbs.entity;

import org.apache.log4j.Logger;

import com.jeecms.bbs.entity.base.BaseBbsTopicText;



public class BbsTopicText extends BaseBbsTopicText {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsTopicText.class);

	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsTopicText () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsTopicText (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsTopicText (
		java.lang.Integer id,
		java.lang.String title) {

		super (
			id,
			title);
	}

/*[CONSTRUCTOR MARKER END]*/


}