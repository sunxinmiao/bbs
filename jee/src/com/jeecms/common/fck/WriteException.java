package com.jeecms.common.fck;

import org.apache.log4j.Logger;

/**
 * Thrown to indicate that an error has occurred during some write action.
 * 
 * @version $Id: WriteException.java,v 1.1 2011/12/26 03:47:53 Administrator Exp $
 */
@SuppressWarnings("serial")
public class WriteException extends Exception {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(WriteException.class);
}