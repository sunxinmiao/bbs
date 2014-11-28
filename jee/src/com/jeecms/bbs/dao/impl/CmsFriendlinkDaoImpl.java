package com.jeecms.bbs.dao.impl;

import org.apache.log4j.Logger;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jeecms.bbs.dao.CmsFriendlinkDao;
import com.jeecms.bbs.entity.CmsFriendlink;
import com.jeecms.common.hibernate3.Finder;
import com.jeecms.common.hibernate3.HibernateBaseDao;

@Repository
public class CmsFriendlinkDaoImpl extends
		HibernateBaseDao<CmsFriendlink, Integer> implements CmsFriendlinkDao {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(CmsFriendlinkDaoImpl.class);

	@SuppressWarnings("unchecked")
	public List<CmsFriendlink> getList(Integer siteId, Integer ctgId,
			Boolean enabled) {
		if (logger.isDebugEnabled()) {
			logger.debug("getList(Integer, Integer, Boolean) - start"); //$NON-NLS-1$
		}

		Finder f = Finder.create("from CmsFriendlink bean where 1=1");
		if (enabled != null) {
			f.append(" and bean.enabled=:enabled");
			f.setParam("enabled", enabled);
		}
		if (siteId != null) {
			f.append(" and bean.site.id=:siteId");
			f.setParam("siteId", siteId);
		}
		if (ctgId != null) {
			f.append(" and bean.category.id=:ctgId");
			f.setParam("ctgId", ctgId);
		}
		f.append(" order by bean.priority asc");
		List<CmsFriendlink> returnList = find(f);
		if (logger.isDebugEnabled()) {
			logger.debug("getList(Integer, Integer, Boolean) - end"); //$NON-NLS-1$
		}
		return returnList;
	}

	public int countByCtgId(Integer ctgId) {
		if (logger.isDebugEnabled()) {
			logger.debug("countByCtgId(Integer) - start"); //$NON-NLS-1$
		}

		String hql = "select count(*) from CmsFriendlink bean where bean.category.id=:ctgId";
		int returnint = ((Number) getSession().createQuery(hql).setParameter("ctgId", ctgId).iterate().next()).intValue();
		if (logger.isDebugEnabled()) {
			logger.debug("countByCtgId(Integer) - end"); //$NON-NLS-1$
		}
		return returnint;
	}

	public CmsFriendlink findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		CmsFriendlink entity = get(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public CmsFriendlink save(CmsFriendlink bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(CmsFriendlink) - start"); //$NON-NLS-1$
		}

		getSession().save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(CmsFriendlink) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public CmsFriendlink deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		CmsFriendlink entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	@Override
	protected Class<CmsFriendlink> getEntityClass() {
		return CmsFriendlink.class;
	}
}