package com.jeecms.bbs.manager.impl;

import org.apache.log4j.Logger;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.bbs.dao.BbsMessageReplyDao;
import com.jeecms.bbs.entity.BbsMessageReply;
import com.jeecms.bbs.manager.BbsMessageReplyMng;
import com.jeecms.common.hibernate3.Updater;
import com.jeecms.common.page.Pagination;

@Service
@Transactional
public class BbsMessageReplyMngImpl implements BbsMessageReplyMng {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsMessageReplyMngImpl.class);

	@Transactional(readOnly = true)
	public BbsMessageReply findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		BbsMessageReply entity = dao.findById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public BbsMessageReply save(BbsMessageReply bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsMessageReply) - start"); //$NON-NLS-1$
		}

		dao.save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsMessageReply) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsMessageReply update(BbsMessageReply bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsMessageReply) - start"); //$NON-NLS-1$
		}

		Updater<BbsMessageReply> updater = new Updater<BbsMessageReply>(bean);
		bean = dao.updateByUpdater(updater);

		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsMessageReply) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsMessageReply deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		BbsMessageReply bean = dao.deleteById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsMessageReply[] deleteByIds(Integer[] ids) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(Integer[]) - start"); //$NON-NLS-1$
		}

		BbsMessageReply[] beans = new BbsMessageReply[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(Integer[]) - end"); //$NON-NLS-1$
		}
		return beans;
	}
	
	public Pagination getPageByMsgId(Integer msgId, Integer pageNo,
			Integer pageSize) {
		if (logger.isDebugEnabled()) {
			logger.debug("getPageByMsgId(Integer, Integer, Integer) - start"); //$NON-NLS-1$
		}

		Pagination returnPagination = dao.getPageByMsgId(msgId, pageNo, pageSize);
		if (logger.isDebugEnabled()) {
			logger.debug("getPageByMsgId(Integer, Integer, Integer) - end"); //$NON-NLS-1$
		}
		return returnPagination;
	}

	private BbsMessageReplyDao dao;

	@Autowired
	public void setDao(BbsMessageReplyDao dao) {
		this.dao = dao;
	}
}