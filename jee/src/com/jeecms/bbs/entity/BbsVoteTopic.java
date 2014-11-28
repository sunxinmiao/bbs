package com.jeecms.bbs.entity;

import org.apache.log4j.Logger;

/**
 * 
 * 投票贴
 * 
 */
public class BbsVoteTopic extends BbsTopic {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsVoteTopic.class);

	/**
	 * 总票数
	 */
	private Integer totalCount;

	public void init() {
		if (logger.isDebugEnabled()) {
			logger.debug("init() - start"); //$NON-NLS-1$
		}

		super.init();
		if (totalCount == null) {
			totalCount = 0;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("init() - end"); //$NON-NLS-1$
		}
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getCategory() {
		return TOPIC_VOTE;
	}
}
