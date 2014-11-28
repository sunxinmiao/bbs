package com.jeecms.bbs.manager.impl;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.bbs.dao.BbsLoginLogDao;
import com.jeecms.bbs.entity.BbsLoginLog;
import com.jeecms.bbs.manager.BbsLoginLogMng;
import com.jeecms.common.hibernate3.Updater;
import com.jeecms.common.page.Pagination;

@Service
@Transactional
public class BbsLoginLogMngImpl implements BbsLoginLogMng {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsLoginLogMngImpl.class);

	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		if (logger.isDebugEnabled()) {
			logger.debug("getPage(int, int) - start"); //$NON-NLS-1$
		}

		Pagination page = dao.getPage(pageNo, pageSize);

		if (logger.isDebugEnabled()) {
			logger.debug("getPage(int, int) - end"); //$NON-NLS-1$
		}
		return page;
	}

	@Transactional(readOnly = true)
	public BbsLoginLog findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		BbsLoginLog entity = dao.findById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public BbsLoginLog save(BbsLoginLog bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsLoginLog) - start"); //$NON-NLS-1$
		}

		dao.save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsLoginLog) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsLoginLog update(BbsLoginLog bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsLoginLog) - start"); //$NON-NLS-1$
		}

		Updater<BbsLoginLog> updater = new Updater<BbsLoginLog>(bean);
		bean = dao.updateByUpdater(updater);

		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsLoginLog) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsLoginLog deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		BbsLoginLog bean = dao.deleteById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsLoginLog[] deleteByIds(Integer[] ids) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(Integer[]) - start"); //$NON-NLS-1$
		}

		BbsLoginLog[] beans = new BbsLoginLog[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(Integer[]) - end"); //$NON-NLS-1$
		}
		return beans;
	}

	private BbsLoginLogDao dao;

	@Autowired
	public void setDao(BbsLoginLogDao dao) {
		this.dao = dao;
	}

}
