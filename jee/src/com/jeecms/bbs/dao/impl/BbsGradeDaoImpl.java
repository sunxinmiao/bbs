package com.jeecms.bbs.dao.impl;

import org.apache.log4j.Logger;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.jeecms.bbs.dao.BbsGradeDao;
import com.jeecms.bbs.entity.BbsGrade;
import com.jeecms.common.hibernate3.HibernateBaseDao;
import com.jeecms.common.page.Pagination;

@Repository
public class BbsGradeDaoImpl extends HibernateBaseDao<BbsGrade, Integer>
		implements BbsGradeDao {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsGradeDaoImpl.class);

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

	public BbsGrade findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		BbsGrade entity = get(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public BbsGrade save(BbsGrade bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsGrade) - start"); //$NON-NLS-1$
		}

		getSession().save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsGrade) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsGrade deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		BbsGrade entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	@Override
	protected Class<BbsGrade> getEntityClass() {
		return BbsGrade.class;
	}
}