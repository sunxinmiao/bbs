package com.jeecms.bbs.dao.impl;

import org.apache.log4j.Logger;

import org.springframework.stereotype.Repository;

import com.jeecms.bbs.dao.BbsCreditExchangeDao;
import com.jeecms.bbs.entity.BbsCreditExchange;
import com.jeecms.common.hibernate3.HibernateBaseDao;

@Repository
public class BbsCreditExchangeDaoImpl extends
		HibernateBaseDao<BbsCreditExchange, Integer> implements
		BbsCreditExchangeDao {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsCreditExchangeDaoImpl.class);

	public BbsCreditExchange findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		BbsCreditExchange entity = get(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public BbsCreditExchange save(BbsCreditExchange bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsCreditExchange) - start"); //$NON-NLS-1$
		}

		getSession().save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsCreditExchange) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsCreditExchange deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		BbsCreditExchange entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	@Override
	protected Class<BbsCreditExchange> getEntityClass() {
		return BbsCreditExchange.class;
	}
}