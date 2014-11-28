package com.jeecms.core.entity;

import org.apache.log4j.Logger;

import com.jeecms.core.entity.base.BaseDbFile;



public class DbFile extends BaseDbFile {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DbFile.class);

	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public DbFile () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public DbFile (java.lang.String id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public DbFile (
		java.lang.String id,
		java.lang.Integer length,
		java.lang.Long lastModified,
		byte[] content) {

		super (
			id,
			length,
			lastModified,
			content);
	}

/*[CONSTRUCTOR MARKER END]*/


}