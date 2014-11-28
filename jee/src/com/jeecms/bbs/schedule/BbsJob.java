package com.jeecms.bbs.schedule;

import org.apache.log4j.Logger;



import org.springframework.beans.factory.annotation.Autowired;

import com.jeecms.bbs.manager.BbsForumMng;

public class BbsJob {
	private static final Logger logger = Logger.getLogger(BbsJob.class);

	public void execute() {
		if (logger.isDebugEnabled()) {
			logger.debug("execute() - start"); //$NON-NLS-1$
		}

		try {
			manager.updateAll_topic_today();
			logger.info("update updateAll_topic_today success!");
		} catch (Exception e) {
			// TODO: handle exception
			logger.info("update updateAll_topic_today fail!");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("execute() - end"); //$NON-NLS-1$
		}
	}

	@Autowired
	private BbsForumMng manager;

}