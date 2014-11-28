package com.jeecms.bbs.manager.impl;

import org.apache.log4j.Logger;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.bbs.dao.BbsVoteRecordDao;
import com.jeecms.bbs.entity.BbsVoteRecord;
import com.jeecms.bbs.manager.BbsVoteRecordMng;

@Service
@Transactional
public class BbsVoteRecordMngImpl implements BbsVoteRecordMng {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsVoteRecordMngImpl.class);

	public BbsVoteRecord findRecord(Integer userId, Integer topicId) {
		if (logger.isDebugEnabled()) {
			logger.debug("findRecord(Integer, Integer) - start"); //$NON-NLS-1$
		}

		BbsVoteRecord returnBbsVoteRecord = dao.findRecord(userId, topicId);
		if (logger.isDebugEnabled()) {
			logger.debug("findRecord(Integer, Integer) - end"); //$NON-NLS-1$
		}
		return returnBbsVoteRecord;
	}

	public BbsVoteRecord save(BbsVoteRecord bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsVoteRecord) - start"); //$NON-NLS-1$
		}

		BbsVoteRecord returnBbsVoteRecord = dao.save(bean);
		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsVoteRecord) - end"); //$NON-NLS-1$
		}
		return returnBbsVoteRecord;
	}

	private BbsVoteRecordDao dao;

	@Autowired
	public void setDao(BbsVoteRecordDao dao) {
		this.dao = dao;
	}
}
