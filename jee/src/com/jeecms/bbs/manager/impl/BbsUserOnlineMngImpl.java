package com.jeecms.bbs.manager.impl;

import org.apache.log4j.Logger;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.bbs.dao.BbsUserOnlineDao;
import com.jeecms.bbs.entity.BbsUserOnline;
import com.jeecms.bbs.manager.BbsUserOnlineMng;
import com.jeecms.common.hibernate3.Updater;
import com.jeecms.common.page.Pagination;

@Service
@Transactional
public class BbsUserOnlineMngImpl implements BbsUserOnlineMng {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsUserOnlineMngImpl.class);

	@Transactional(readOnly = true)
	public List<BbsUserOnline> getList() {
		if (logger.isDebugEnabled()) {
			logger.debug("getList() - start"); //$NON-NLS-1$
		}

		List<BbsUserOnline> returnList = dao.getList();
		if (logger.isDebugEnabled()) {
			logger.debug("getList() - end"); //$NON-NLS-1$
		}
		return returnList;
	}
	
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
	public BbsUserOnline findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		BbsUserOnline entity = dao.findById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public BbsUserOnline save(BbsUserOnline bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsUserOnline) - start"); //$NON-NLS-1$
		}

		dao.save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsUserOnline) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsUserOnline update(BbsUserOnline bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsUserOnline) - start"); //$NON-NLS-1$
		}

		Updater<BbsUserOnline> updater = new Updater<BbsUserOnline>(bean);
		bean = dao.updateByUpdater(updater);

		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsUserOnline) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsUserOnline deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		BbsUserOnline bean = dao.deleteById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsUserOnline[] deleteByIds(Integer[] ids) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(Integer[]) - start"); //$NON-NLS-1$
		}

		BbsUserOnline[] beans = new BbsUserOnline[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(Integer[]) - end"); //$NON-NLS-1$
		}
		return beans;
	}

	private BbsUserOnlineDao dao;

	@Autowired
	public void setDao(BbsUserOnlineDao dao) {
		this.dao = dao;
	}

}
