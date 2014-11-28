package com.jeecms.bbs.dao.impl;

import org.apache.log4j.Logger;

import org.springframework.stereotype.Repository;

import com.jeecms.bbs.dao.BbsMagicConfigDao;
import com.jeecms.bbs.entity.BbsMagicConfig;
import com.jeecms.common.hibernate3.HibernateBaseDao;

@Repository
public class BbsMagicConfigDaoImpl extends
		HibernateBaseDao<BbsMagicConfig, Integer> implements BbsMagicConfigDao {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsMagicConfigDaoImpl.class);

	public BbsMagicConfig findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		BbsMagicConfig entity = get(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public BbsMagicConfig save(BbsMagicConfig bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsMagicConfig) - start"); //$NON-NLS-1$
		}

		getSession().save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsMagicConfig) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsMagicConfig deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		BbsMagicConfig entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	@Override
	protected Class<BbsMagicConfig> getEntityClass() {
		return BbsMagicConfig.class;
	}
}