package com.jeecms.core.entity;

import org.apache.log4j.Logger;

import com.jeecms.core.entity.base.BaseUnifiedUser;



public class UnifiedUser extends BaseUnifiedUser {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(UnifiedUser.class);

	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public UnifiedUser () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public UnifiedUser (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public UnifiedUser (
		java.lang.Integer id,
		java.lang.String username,
		java.lang.String password,
		java.util.Date registerTime,
		java.lang.String registerIp,
		java.lang.Integer loginCount) {

		super (
			id,
			username,
			password,
			registerTime,
			registerIp,
			loginCount);
	}

/*[CONSTRUCTOR MARKER END]*/


}