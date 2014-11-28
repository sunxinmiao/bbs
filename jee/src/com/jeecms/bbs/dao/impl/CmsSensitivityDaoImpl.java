package com.jeecms.bbs.dao.impl;

import org.apache.log4j.Logger;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jeecms.bbs.dao.CmsSensitivityDao;
import com.jeecms.bbs.entity.CmsSensitivity;
import com.jeecms.common.hibernate3.HibernateBaseDao;

@Repository
public class CmsSensitivityDaoImpl extends
		HibernateBaseDao<CmsSensitivity, Integer> implements CmsSensitivityDao {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(CmsSensitivityDaoImpl.class);

	@SuppressWarnings("unchecked")
	public List<CmsSensitivity> getList(Integer siteId, boolean cacheable) {
		if (logger.isDebugEnabled()) {
			logger.debug("getList(Integer, boolean) - start"); //$NON-NLS-1$
		}

		String hql = "from CmsSensitivity bean where bean.site.id=:siteId order by bean.id desc";
		List<CmsSensitivity> returnList = getSession().createQuery(hql).setParameter("siteId", siteId).setCacheable(cacheable).list();
		if (logger.isDebugEnabled()) {
			logger.debug("getList(Integer, boolean) - end"); //$NON-NLS-1$
		}
		return returnList;
	}

	public CmsSensitivity findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		CmsSensitivity entity = get(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public CmsSensitivity save(CmsSensitivity bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(CmsSensitivity) - start"); //$NON-NLS-1$
		}

		getSession().save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(CmsSensitivity) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public CmsSensitivity deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		CmsSensitivity entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	@Override
	protected Class<CmsSensitivity> getEntityClass() {
		return CmsSensitivity.class;
	}
}