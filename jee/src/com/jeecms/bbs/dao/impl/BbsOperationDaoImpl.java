package com.jeecms.bbs.dao.impl;

import org.apache.log4j.Logger;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.jeecms.bbs.entity.BbsOperation;
import com.jeecms.bbs.dao.BbsOperationDao;
import com.jeecms.common.hibernate3.HibernateBaseDao;
import com.jeecms.common.page.Pagination;

@Repository
public class BbsOperationDaoImpl extends
		HibernateBaseDao<BbsOperation, Integer> implements BbsOperationDao {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsOperationDaoImpl.class);

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

	public BbsOperation findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		BbsOperation entity = get(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public BbsOperation save(BbsOperation bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsOperation) - start"); //$NON-NLS-1$
		}

		getSession().save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsOperation) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsOperation deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		BbsOperation entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	@Override
	protected Class<BbsOperation> getEntityClass() {
		return BbsOperation.class;
	}
}