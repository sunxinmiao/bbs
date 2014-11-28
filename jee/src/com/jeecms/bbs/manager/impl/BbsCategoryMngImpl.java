package com.jeecms.bbs.manager.impl;

import org.apache.log4j.Logger;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.bbs.dao.BbsCategoryDao;
import com.jeecms.bbs.entity.BbsCategory;
import com.jeecms.bbs.manager.BbsCategoryMng;
import com.jeecms.common.hibernate3.Updater;

@Service
@Transactional
public class BbsCategoryMngImpl implements BbsCategoryMng {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsCategoryMngImpl.class);

	@Transactional(readOnly = true)
	public List<BbsCategory> getList(Integer siteId) {
		if (logger.isDebugEnabled()) {
			logger.debug("getList(Integer) - start"); //$NON-NLS-1$
		}

		List<BbsCategory> list = dao.getList(siteId);

		if (logger.isDebugEnabled()) {
			logger.debug("getList(Integer) - end"); //$NON-NLS-1$
		}
		return list;
	}

	@Transactional(readOnly = true)
	public BbsCategory findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		BbsCategory category = dao.findById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return category;
	}

	public BbsCategory save(BbsCategory bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsCategory) - start"); //$NON-NLS-1$
		}

		dao.save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsCategory) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsCategory update(BbsCategory bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsCategory) - start"); //$NON-NLS-1$
		}

		Updater<BbsCategory> updater = new Updater<BbsCategory>(bean);
		BbsCategory entity = dao.updateByUpdater(updater);

		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsCategory) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public BbsCategory deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		BbsCategory bean = dao.deleteById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsCategory[] deleteByIds(Integer[] ids) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(Integer[]) - start"); //$NON-NLS-1$
		}

		BbsCategory[] beans = new BbsCategory[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(Integer[]) - end"); //$NON-NLS-1$
		}
		return beans;
	}

	public BbsCategory[] updatePriority(Integer[] ids, Integer[] priority) {
		if (logger.isDebugEnabled()) {
			logger.debug("updatePriority(Integer[], Integer[]) - start"); //$NON-NLS-1$
		}

		int len = ids.length;
		BbsCategory[] beans = new BbsCategory[len];
		for (int i = 0; i < len; i++) {
			beans[i] = findById(ids[i]);
			beans[i].setPriority(priority[i]);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("updatePriority(Integer[], Integer[]) - end"); //$NON-NLS-1$
		}
		return beans;
	}

	private BbsCategoryDao dao;

	@Autowired
	public void setDao(BbsCategoryDao dao) {
		this.dao = dao;
	}
}
