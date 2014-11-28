package com.jeecms.bbs.entity;

import org.apache.log4j.Logger;

import com.jeecms.bbs.entity.base.BaseBbsVoteItem;



public class BbsVoteItem extends BaseBbsVoteItem {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsVoteItem.class);

	private static final long serialVersionUID = 1L;
	
	public int getPercent() {
		if (logger.isDebugEnabled()) {
			logger.debug("getPercent() - start"); //$NON-NLS-1$
		}

		Integer totalCount = getTopic().getTotalCount();
		if (totalCount != null && totalCount != 0) {
			int returnint = (getVoteCount() * 100) / totalCount;
			if (logger.isDebugEnabled()) {
				logger.debug("getPercent() - end"); //$NON-NLS-1$
			}
			return returnint;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("getPercent() - end"); //$NON-NLS-1$
			}
			return 0;
		}
	}
	
	public void init(){
		if (logger.isDebugEnabled()) {
			logger.debug("init() - start"); //$NON-NLS-1$
		}

		if(getVoteCount()==null){
			setVoteCount(0);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("init() - end"); //$NON-NLS-1$
		}
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsVoteItem () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsVoteItem (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsVoteItem (
		java.lang.Integer id,
		com.jeecms.bbs.entity.BbsVoteTopic topic,
		java.lang.String name,
		java.lang.Integer voteCount) {

		super (
			id,
			topic,
			name,
			voteCount);
	}

/*[CONSTRUCTOR MARKER END]*/


}