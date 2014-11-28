package com.jeecms.bbs.manager.impl;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.bbs.dao.BbsMagicLogDao;
import com.jeecms.bbs.entity.BbsMagicLog;
import com.jeecms.bbs.manager.BbsMagicLogMng;
import com.jeecms.common.hibernate3.Updater;
import com.jeecms.common.page.Pagination;

@Service
@Transactional
public class BbsMagicLogMngImpl implements BbsMagicLogMng {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsMagicLogMngImpl.class);

	@Transactional(readOnly = true)
	public Pagination getPage(Byte operator,Integer userId,int pageNo, int pageSize) {
		if (logger.isDebugEnabled()) {
			logger.debug("getPage(Byte, Integer, int, int) - start"); //$NON-NLS-1$
		}

		Pagination page = dao.getPage(operator,userId,pageNo, pageSize);

		if (logger.isDebugEnabled()) {
			logger.debug("getPage(Byte, Integer, int, int) - end"); //$NON-NLS-1$
		}
		return page;
	}

	@Transactional(readOnly = true)
	public BbsMagicLog findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		BbsMagicLog entity = dao.findById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public BbsMagicLog save(BbsMagicLog bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsMagicLog) - start"); //$NON-NLS-1$
		}

		dao.save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsMagicLog) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsMagicLog update(BbsMagicLog bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsMagicLog) - start"); //$NON-NLS-1$
		}

		Updater<BbsMagicLog> updater = new Updater<BbsMagicLog>(bean);
		bean = dao.updateByUpdater(updater);

		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsMagicLog) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsMagicLog deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		BbsMagicLog bean = dao.deleteById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsMagicLog[] deleteByIds(Integer[] ids) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(Integer[]) - start"); //$NON-NLS-1$
		}

		BbsMagicLog[] beans = new BbsMagicLog[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(Integer[]) - end"); //$NON-NLS-1$
		}
		return beans;
	}

	private BbsMagicLogDao dao;

	@Autowired
	public void setDao(BbsMagicLogDao dao) {
		this.dao = dao;
	}

}
