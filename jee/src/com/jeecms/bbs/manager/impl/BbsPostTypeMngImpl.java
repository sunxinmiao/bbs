package com.jeecms.bbs.manager.impl;

import org.apache.log4j.Logger;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.bbs.dao.BbsPostTypeDao;
import com.jeecms.bbs.entity.BbsPostType;
import com.jeecms.bbs.manager.BbsPostTypeMng;
import com.jeecms.common.hibernate3.Updater;
import com.jeecms.common.page.Pagination;

@Service
@Transactional
public class BbsPostTypeMngImpl implements BbsPostTypeMng {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsPostTypeMngImpl.class);

	@Transactional(readOnly = true)
	public Pagination getPage(Integer siteId,Integer forumId,Integer parentId,int pageNo, int pageSize) {
		if (logger.isDebugEnabled()) {
			logger.debug("getPage(Integer, Integer, Integer, int, int) - start"); //$NON-NLS-1$
		}

		Pagination page = dao.getPage(siteId,forumId,parentId,pageNo, pageSize);

		if (logger.isDebugEnabled()) {
			logger.debug("getPage(Integer, Integer, Integer, int, int) - end"); //$NON-NLS-1$
		}
		return page;
	}
	
	@Transactional(readOnly = true)
	public List getList(Integer siteId,Integer forumId,Integer parentId) {
		if (logger.isDebugEnabled()) {
			logger.debug("getList(Integer, Integer, Integer) - start"); //$NON-NLS-1$
		}

		List list = dao.getList(siteId,forumId,parentId);

		if (logger.isDebugEnabled()) {
			logger.debug("getList(Integer, Integer, Integer) - end"); //$NON-NLS-1$
		}
		return list;
	}

	@Transactional(readOnly = true)
	public BbsPostType findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		BbsPostType entity = dao.findById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public BbsPostType save(BbsPostType bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsPostType) - start"); //$NON-NLS-1$
		}

		dao.save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsPostType) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsPostType update(BbsPostType bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsPostType) - start"); //$NON-NLS-1$
		}

		Updater<BbsPostType> updater = new Updater<BbsPostType>(bean);
		bean = dao.updateByUpdater(updater);

		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsPostType) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsPostType deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		BbsPostType bean = dao.deleteById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsPostType[] deleteByIds(Integer[] ids) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(Integer[]) - start"); //$NON-NLS-1$
		}

		BbsPostType[] beans = new BbsPostType[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(Integer[]) - end"); //$NON-NLS-1$
		}
		return beans;
	}

	private BbsPostTypeDao dao;

	@Autowired
	public void setDao(BbsPostTypeDao dao) {
		this.dao = dao;
	}

}
