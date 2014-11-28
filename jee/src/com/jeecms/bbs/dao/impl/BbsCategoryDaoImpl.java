package com.jeecms.bbs.dao.impl;

import org.apache.log4j.Logger;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jeecms.bbs.dao.BbsCategoryDao;
import com.jeecms.bbs.entity.BbsCategory;
import com.jeecms.common.hibernate3.HibernateBaseDao;

@Repository
public class BbsCategoryDaoImpl extends HibernateBaseDao<BbsCategory, Integer> implements
		BbsCategoryDao {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsCategoryDaoImpl.class);

	@SuppressWarnings("unchecked")
	public List<BbsCategory> getList(Integer webId) {
		if (logger.isDebugEnabled()) {
			logger.debug("getList(Integer) - start"); //$NON-NLS-1$
		}

		String hql = "select bean from BbsCategory bean where bean.site.id=? order by bean.priority";
		List<BbsCategory> returnList = find(hql, webId);
		if (logger.isDebugEnabled()) {
			logger.debug("getList(Integer) - end"); //$NON-NLS-1$
		}
		return returnList;
	}

	public int countPath(Integer webId, String path) {
		if (logger.isDebugEnabled()) {
			logger.debug("countPath(Integer, String) - start"); //$NON-NLS-1$
		}

		String hql = "select count(*) from BbsCategory bean where bean.site.id=? and bean.path=?";
		int returnint = ((Number) getSession().createQuery(hql).setParameter(0, webId).setParameter(1, path).iterate().next()).intValue();
		if (logger.isDebugEnabled()) {
			logger.debug("countPath(Integer, String) - end"); //$NON-NLS-1$
		}
		return returnint;
	}

	public BbsCategory getByPath(Integer webId, String path) {
		if (logger.isDebugEnabled()) {
			logger.debug("getByPath(Integer, String) - start"); //$NON-NLS-1$
		}

		String hql = "select bean from BbsCategory bean where bean.site.id=? and bean.path=?";
		BbsCategory returnBbsCategory = (BbsCategory) findUnique(hql, webId, path);
		if (logger.isDebugEnabled()) {
			logger.debug("getByPath(Integer, String) - end"); //$NON-NLS-1$
		}
		return returnBbsCategory;
	}
	
	public BbsCategory findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		BbsCategory entity = get(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}
	
	public BbsCategory save(BbsCategory bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsCategory) - start"); //$NON-NLS-1$
		}

		getSession().save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsCategory) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsCategory deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		BbsCategory entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	@Override
	protected Class<BbsCategory> getEntityClass() {
		return BbsCategory.class;
	}
}