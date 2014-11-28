package com.jeecms.bbs.dao.impl;

import org.apache.log4j.Logger;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.jeecms.bbs.entity.BbsLoginLog;
import com.jeecms.bbs.dao.BbsLoginLogDao;
import com.jeecms.common.hibernate3.HibernateBaseDao;
import com.jeecms.common.page.Pagination;

@Repository
public class BbsLoginLogDaoImpl extends HibernateBaseDao<BbsLoginLog, Integer>
		implements BbsLoginLogDao {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsLoginLogDaoImpl.class);

	public Pagination getPage(int pageNo, int pageSize) {
		if (logger.isDebugEnabled()) {
			logger.debug("getPage(int, int) - start"); //$NON-NLS-1$
		}

		Criteria crit = createCriteria();
		Pagination page = findByCriteria(crit, pageNo, pageSize);

		if (logger.isDebugEnabled()) {
			logger.debug("getPage(int, int) - end"); //$NON-NLS-1$
		}
		return page;
	}

	public BbsLoginLog findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		BbsLoginLog entity = get(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public BbsLoginLog save(BbsLoginLog bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsLoginLog) - start"); //$NON-NLS-1$
		}

		getSession().save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsLoginLog) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsLoginLog deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		BbsLoginLog entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	protected Class<BbsLoginLog> getEntityClass() {
		return BbsLoginLog.class;
	}
}