package com.jeecms.bbs.manager.impl;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.bbs.dao.BbsMemberMagicDao;
import com.jeecms.bbs.entity.BbsMemberMagic;
import com.jeecms.bbs.manager.BbsMemberMagicMng;
import com.jeecms.common.hibernate3.Updater;
import com.jeecms.common.page.Pagination;

@Service
@Transactional
public class BbsMemberMagicMngImpl implements BbsMemberMagicMng {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsMemberMagicMngImpl.class);

	@Transactional(readOnly = true)
	public Pagination getPage(Integer userId, int pageNo, int pageSize) {
		if (logger.isDebugEnabled()) {
			logger.debug("getPage(Integer, int, int) - start"); //$NON-NLS-1$
		}

		Pagination page = dao.getPage(userId, pageNo, pageSize);

		if (logger.isDebugEnabled()) {
			logger.debug("getPage(Integer, int, int) - end"); //$NON-NLS-1$
		}
		return page;
	}

	@Transactional(readOnly = true)
	public BbsMemberMagic findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		BbsMemberMagic entity = dao.findById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public BbsMemberMagic save(BbsMemberMagic bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsMemberMagic) - start"); //$NON-NLS-1$
		}

		dao.save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsMemberMagic) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsMemberMagic update(BbsMemberMagic bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsMemberMagic) - start"); //$NON-NLS-1$
		}

		Updater<BbsMemberMagic> updater = new Updater<BbsMemberMagic>(bean);
		bean = dao.updateByUpdater(updater);

		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsMemberMagic) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsMemberMagic deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		BbsMemberMagic bean = dao.deleteById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsMemberMagic[] deleteByIds(Integer[] ids) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(Integer[]) - start"); //$NON-NLS-1$
		}

		BbsMemberMagic[] beans = new BbsMemberMagic[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(Integer[]) - end"); //$NON-NLS-1$
		}
		return beans;
	}

	private BbsMemberMagicDao dao;

	@Autowired
	public void setDao(BbsMemberMagicDao dao) {
		this.dao = dao;
	}

}
