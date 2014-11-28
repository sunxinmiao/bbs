package com.jeecms.bbs.entity;

import org.apache.log4j.Logger;

import com.jeecms.bbs.entity.base.BaseBbsUserOnline;



public class BbsUserOnline extends BaseBbsUserOnline {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsUserOnline.class);

	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsUserOnline () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsUserOnline (java.lang.Integer id) {
		super(id);
	}
	public void initial(){
		if (logger.isDebugEnabled()) {
			logger.debug("initial() - start"); //$NON-NLS-1$
		}

		setOnlineDay(0d);
		setOnlineLatest(0d);
		setOnlineMonth(0d);
		setOnlineWeek(0d);
		setOnlineYear(0d);
		setOnlineTotal(0d);

		if (logger.isDebugEnabled()) {
			logger.debug("initial() - end"); //$NON-NLS-1$
		}
	}
	public void updateOnline(Double time){
		if (logger.isDebugEnabled()) {
			logger.debug("updateOnline(Double) - start"); //$NON-NLS-1$
		}

		setOnlineDay(getOnlineDay()+time);
		setOnlineLatest(getOnlineLatest()+time);
		setOnlineMonth(getOnlineMonth()+time);
		setOnlineTotal(getOnlineTotal()+time);
		setOnlineWeek(getOnlineWeek()+time);
		setOnlineYear(getOnlineYear()+time);

		if (logger.isDebugEnabled()) {
			logger.debug("updateOnline(Double) - end"); //$NON-NLS-1$
		}
	}

/*[CONSTRUCTOR MARKER END]*/


}