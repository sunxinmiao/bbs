package com.jeecms.core.dao.impl;

import org.apache.log4j.Logger;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jeecms.common.hibernate3.HibernateBaseDao;
import com.jeecms.core.dao.CmsConfigDao;
import com.jeecms.core.entity.CmsConfig;

@Repository
public class CmsConfigDaoImpl extends HibernateBaseDao<CmsConfig, Integer>
		implements CmsConfigDao {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(CmsConfigDaoImpl.class);

	public CmsConfig findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		CmsConfig entity = get(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	protected Class<CmsConfig> getEntityClass() {
		return CmsConfig.class;
	}
	@SuppressWarnings("unchecked")
	public List<CmsConfig> getList() {
		if (logger.isDebugEnabled()) {
			logger.debug("getList() - start"); //$NON-NLS-1$
		}

		String hql = "from CmsConfig";
		List<CmsConfig> returnList = find(hql);
		if (logger.isDebugEnabled()) {
			logger.debug("getList() - end"); //$NON-NLS-1$
		}
		return returnList;
	}
}