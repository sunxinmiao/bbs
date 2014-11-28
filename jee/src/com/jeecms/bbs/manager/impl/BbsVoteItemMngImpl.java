package com.jeecms.bbs.manager.impl;

import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.bbs.dao.BbsVoteItemDao;
import com.jeecms.bbs.entity.BbsTopic;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.entity.BbsVoteItem;
import com.jeecms.bbs.entity.BbsVoteRecord;
import com.jeecms.bbs.entity.BbsVoteTopic;
import com.jeecms.bbs.manager.BbsVoteItemMng;
import com.jeecms.bbs.manager.BbsVoteRecordMng;
import com.jeecms.common.hibernate3.Updater;

@Service
@Transactional
public class BbsVoteItemMngImpl implements BbsVoteItemMng {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsVoteItemMngImpl.class);

	public BbsVoteItem findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		BbsVoteItem returnBbsVoteItem = dao.findById(id);
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return returnBbsVoteItem;
	}

	public List<BbsVoteItem> findByTopic(Integer topicId) {
		if (logger.isDebugEnabled()) {
			logger.debug("findByTopic(Integer) - start"); //$NON-NLS-1$
		}

		List<BbsVoteItem> returnList = dao.findByTopic(topicId);
		if (logger.isDebugEnabled()) {
			logger.debug("findByTopic(Integer) - end"); //$NON-NLS-1$
		}
		return returnList;
	}

	public BbsVoteItem save(BbsVoteItem bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsVoteItem) - start"); //$NON-NLS-1$
		}

		BbsVoteItem returnBbsVoteItem = dao.save(bean);
		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsVoteItem) - end"); //$NON-NLS-1$
		}
		return returnBbsVoteItem;
	}

	public BbsVoteItem update(BbsVoteItem bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsVoteItem) - start"); //$NON-NLS-1$
		}

		Updater<BbsVoteItem> updater = new Updater<BbsVoteItem>(bean);
		BbsVoteItem entity = dao.updateByUpdater(updater);

		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsVoteItem) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public void vote(BbsUser user, BbsVoteTopic topic, Integer[] itemIds) {
		if (logger.isDebugEnabled()) {
			logger.debug("vote(BbsUser, BbsVoteTopic, Integer[]) - start"); //$NON-NLS-1$
		}

		int count = 0;
		for (Integer id : itemIds) {
			BbsVoteItem bean = findById(id);
			bean.setVoteCount(bean.getVoteCount() + 1);
			count++;
		}
		topic.setTotalCount(topic.getTotalCount() + count);
		BbsVoteRecord record = new BbsVoteRecord();
		record.setUser(user);
		record.setTopic(topic);
		record.setVoteTime(new Date());
		bbsVoteRecordMng.save(record);

		if (logger.isDebugEnabled()) {
			logger.debug("vote(BbsUser, BbsVoteTopic, Integer[]) - end"); //$NON-NLS-1$
		}
	}

	private BbsVoteItemDao dao;
	@Autowired
	private BbsVoteRecordMng bbsVoteRecordMng;

	@Autowired
	public void setDao(BbsVoteItemDao dao) {
		this.dao = dao;
	}
}
