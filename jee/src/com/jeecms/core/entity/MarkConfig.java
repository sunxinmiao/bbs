package com.jeecms.core.entity;

import org.apache.log4j.Logger;

import com.jeecms.core.entity.base.BaseMarkConfig;



public class MarkConfig extends BaseMarkConfig {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(MarkConfig.class);

	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public MarkConfig () {
		super();
	}

	/**
	 * Constructor for required fields
	 */
	public MarkConfig (
		java.lang.Boolean on,
		java.lang.Integer minWidth,
		java.lang.Integer minHeight,
		java.lang.String content,
		java.lang.Integer size,
		java.lang.String color,
		java.lang.Integer alpha,
		java.lang.Integer pos,
		java.lang.Integer offsetX,
		java.lang.Integer offsetY) {

		super (
			on,
			minWidth,
			minHeight,
			content,
			size,
			color,
			alpha,
			pos,
			offsetX,
			offsetY);
	}

/*[CONSTRUCTOR MARKER END]*/


}