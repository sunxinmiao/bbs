package com.jeecms.bbs.dao.impl;

import org.apache.log4j.Logger;

import org.springframework.stereotype.Repository;

import com.jeecms.bbs.entity.BbsMemberMagic;
import com.jeecms.bbs.dao.BbsMemberMagicDao;
import com.jeecms.common.hibernate3.Finder;
import com.jeecms.common.hibernate3.HibernateBaseDao;
import com.jeecms.common.page.Pagination;

@Repository
public class BbsMagicLogDaoImpl extends
		HibernateBaseDao<BbsMemberMagic, Integer> implements BbsMemberMagicDao {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsMagicLogDaoImpl.class);

	public Pagination getPage(Integer userId, int pageNo, int pageSize) {
		if (logger.isDebugEnabled()) {
			logger.debug("getPage(Integer, int, int) - start"); //$NON-NLS-1$
		}

		String hql = "from BbsMemberMagic magic ";
		Finder finder = Finder.create(hql);
		Pagination page = find(finder, pageNo, pageSize);

		if (logger.isDebugEnabled()) {
			logger.debug("getPage(Integer, int, int) - end"); //$NON-NLS-1$
		}
		return page;
	}

	public BbsMemberMagic findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		BbsMemberMagic entity = get(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public BbsMemberMagic save(BbsMemberMagic bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsMemberMagic) - start"); //$NON-NLS-1$
		}

		getSession().save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsMemberMagic) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsMemberMagic deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		BbsMemberMagic entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	protected Class<BbsMemberMagic> getEntityClass() {
		return BbsMemberMagic.class;
	}
}