package com.jeecms.bbs.dao.impl;

import org.apache.log4j.Logger;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jeecms.bbs.dao.CmsFriendlinkCtgDao;
import com.jeecms.bbs.entity.CmsFriendlinkCtg;
import com.jeecms.common.hibernate3.Finder;
import com.jeecms.common.hibernate3.HibernateBaseDao;

@Repository
public class CmsFriendlinkCtgDaoImpl extends
		HibernateBaseDao<CmsFriendlinkCtg, Integer> implements
		CmsFriendlinkCtgDao {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(CmsFriendlinkCtgDaoImpl.class);

	@SuppressWarnings("unchecked")
	public List<CmsFriendlinkCtg> getList(Integer siteId) {
		if (logger.isDebugEnabled()) {
			logger.debug("getList(Integer) - start"); //$NON-NLS-1$
		}

		Finder f = Finder.create("from CmsFriendlinkCtg bean");
		if (siteId != null) {
			f.append(" where bean.site.id=:siteId");
			f.setParam("siteId", siteId);
		}
		f.append(" order by bean.priority asc");
		List<CmsFriendlinkCtg> returnList = find(f);
		if (logger.isDebugEnabled()) {
			logger.debug("getList(Integer) - end"); //$NON-NLS-1$
		}
		return returnList;
	}

	public int countBySiteId(Integer siteId) {
		if (logger.isDebugEnabled()) {
			logger.debug("countBySiteId(Integer) - start"); //$NON-NLS-1$
		}

		String hql = "select count(*) from CmsFriendlinkCtg bean where bean.site.id=:siteId";
		int returnint = ((Number) getSession().createQuery(hql).setParameter("siteId", siteId).iterate().next()).intValue();
		if (logger.isDebugEnabled()) {
			logger.debug("countBySiteId(Integer) - end"); //$NON-NLS-1$
		}
		return returnint;
	}

	public CmsFriendlinkCtg findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		CmsFriendlinkCtg entity = get(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public CmsFriendlinkCtg save(CmsFriendlinkCtg bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(CmsFriendlinkCtg) - start"); //$NON-NLS-1$
		}

		getSession().save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(CmsFriendlinkCtg) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public CmsFriendlinkCtg deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		CmsFriendlinkCtg entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	@Override
	protected Class<CmsFriendlinkCtg> getEntityClass() {
		return CmsFriendlinkCtg.class;
	}
}