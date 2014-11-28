package com.jeecms.core.dao.impl;

import org.apache.log4j.Logger;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.jeecms.core.dao.CmsSiteDao;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.common.hibernate3.HibernateBaseDao;

@Repository
public class CmsSiteDaoImpl extends HibernateBaseDao<CmsSite, Integer>
		implements CmsSiteDao {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(CmsSiteDaoImpl.class);

	public int siteCount(boolean cacheable) {
		if (logger.isDebugEnabled()) {
			logger.debug("siteCount(boolean) - start"); //$NON-NLS-1$
		}

		String hql = "select count(*) from CmsSite bean";
		int returnint = ((Number) getSession().createQuery(hql).setCacheable(cacheable).iterate().next()).intValue();
		if (logger.isDebugEnabled()) {
			logger.debug("siteCount(boolean) - end"); //$NON-NLS-1$
		}
		return returnint;
	}

	@SuppressWarnings("unchecked")
	public List<CmsSite> getList(boolean cacheable) {
		if (logger.isDebugEnabled()) {
			logger.debug("getList(boolean) - start"); //$NON-NLS-1$
		}

		String hql = "from CmsSite bean order by bean.id asc";
		List<CmsSite> returnList = getSession().createQuery(hql).setCacheable(cacheable).list();
		if (logger.isDebugEnabled()) {
			logger.debug("getList(boolean) - end"); //$NON-NLS-1$
		}
		return returnList;
	}

	public CmsSite findByDomain(String domain, boolean cacheable) {
		if (logger.isDebugEnabled()) {
			logger.debug("findByDomain(String, boolean) - start"); //$NON-NLS-1$
		}

		String hql = "from CmsSite bean where domain=:domain";
		Query query = getSession().createQuery(hql).setString("domain", domain);
		query.setCacheable(cacheable);
		CmsSite returnCmsSite = (CmsSite) query.uniqueResult();
		if (logger.isDebugEnabled()) {
			logger.debug("findByDomain(String, boolean) - end"); //$NON-NLS-1$
		}
		return returnCmsSite;
	}

	public CmsSite findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		CmsSite entity = get(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public CmsSite save(CmsSite bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(CmsSite) - start"); //$NON-NLS-1$
		}

		getSession().save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(CmsSite) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public CmsSite deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		CmsSite entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public CmsSite getByDomain(String domain) {
		if (logger.isDebugEnabled()) {
			logger.debug("getByDomain(String) - start"); //$NON-NLS-1$
		}

		String hql = "from CmsSite bean where bean.domain=?";
		CmsSite returnCmsSite = findUniqueByProperty(hql, domain);
		if (logger.isDebugEnabled()) {
			logger.debug("getByDomain(String) - end"); //$NON-NLS-1$
		}
		return returnCmsSite;
	}

	@Override
	protected Class<CmsSite> getEntityClass() {
		return CmsSite.class;
	}
}