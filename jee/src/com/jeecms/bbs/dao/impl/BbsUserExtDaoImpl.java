package com.jeecms.bbs.dao.impl;

import org.apache.log4j.Logger;

import org.springframework.stereotype.Repository;

import com.jeecms.bbs.dao.BbsUserExtDao;
import com.jeecms.bbs.entity.BbsUserExt;
import com.jeecms.common.hibernate3.HibernateBaseDao;

@Repository
public class BbsUserExtDaoImpl extends HibernateBaseDao<BbsUserExt, Integer> implements BbsUserExtDao {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsUserExtDaoImpl.class);

	public BbsUserExt findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		BbsUserExt entity = get(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public BbsUserExt save(BbsUserExt bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsUserExt) - start"); //$NON-NLS-1$
		}

		getSession().save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsUserExt) - end"); //$NON-NLS-1$
		}
		return bean;
	}
	
	@Override
	protected Class<BbsUserExt> getEntityClass() {
		return BbsUserExt.class;
	}
}