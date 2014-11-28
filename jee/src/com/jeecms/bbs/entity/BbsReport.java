package com.jeecms.bbs.entity;

import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.Set;

import com.jeecms.bbs.entity.base.BaseBbsReport;



public class BbsReport extends BaseBbsReport {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsReport.class);

	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsReport () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsReport (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsReport (
		java.lang.Integer id,
		java.lang.String reportUrl,
		java.util.Date reportTime) {

		super (
			id,
			reportUrl,
			reportTime);
	}
	public BbsReportExt getReportExt(){
		if (logger.isDebugEnabled()) {
			logger.debug("getReportExt() - start"); //$NON-NLS-1$
		}

		Set<BbsReportExt>sets=getBbsReportExtSet(); 
		Iterator<BbsReportExt>it=sets.iterator();
		BbsReportExt returnBbsReportExt = it.next();
		if (logger.isDebugEnabled()) {
			logger.debug("getReportExt() - end"); //$NON-NLS-1$
		}
		return returnBbsReportExt;
	}

/*[CONSTRUCTOR MARKER END]*/


}