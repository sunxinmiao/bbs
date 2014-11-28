package com.jeecms.bbs.entity;

import org.apache.log4j.Logger;

import com.jeecms.bbs.entity.base.BaseBbsMagicConfig;



public class BbsMagicConfig extends BaseBbsMagicConfig {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsMagicConfig.class);

	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsMagicConfig () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsMagicConfig (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsMagicConfig (
		java.lang.Integer id,
		boolean magicSwitch) {

		super (
			id,
			magicSwitch);
	}

/*[CONSTRUCTOR MARKER END]*/


}