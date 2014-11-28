package com.jeecms.core.dao.impl;

import org.apache.log4j.Logger;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.jeecms.common.hibernate3.HibernateBaseDao;
import com.jeecms.core.dao.DbTplDao;
import com.jeecms.core.entity.DbTpl;

@Repository
public class DbTplDaoImpl extends HibernateBaseDao<DbTpl, String> implements
		DbTplDao {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DbTplDaoImpl.class);

	@SuppressWarnings("unchecked")
	public List<DbTpl> getStartWith(String prefix) {
		if (logger.isDebugEnabled()) {
			logger.debug("getStartWith(String) - start"); //$NON-NLS-1$
		}

		StringUtils.replace(prefix, "_", "\\_");
		prefix = prefix + "%";
		String hql = "from DbTpl bean where bean.id like ? order by bean.id";
		List<DbTpl> returnList = find(hql, prefix);
		if (logger.isDebugEnabled()) {
			logger.debug("getStartWith(String) - end"); //$NON-NLS-1$
		}
		return returnList;
	}

	@SuppressWarnings("unchecked")
	public List<DbTpl> getChild(String path, boolean isDirectory) {
		if (logger.isDebugEnabled()) {
			logger.debug("getChild(String, boolean) - start"); //$NON-NLS-1$
		}

		StringUtils.replace(path, "_", "\\_");
		path = path + "/%";
		String notLike = path + "/%";
		String hql = "from DbTpl bean"
				+ " where bean.id like ? and bean.id not like ?"
				+ " and bean.directory=? order by bean.id";
		List<DbTpl> returnList = find(hql, path, notLike, isDirectory);
		if (logger.isDebugEnabled()) {
			logger.debug("getChild(String, boolean) - end"); //$NON-NLS-1$
		}
		return returnList;
	}

	public DbTpl findById(String id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(String) - start"); //$NON-NLS-1$
		}

		DbTpl entity = get(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(String) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public DbTpl save(DbTpl bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(DbTpl) - start"); //$NON-NLS-1$
		}

		getSession().save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(DbTpl) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public DbTpl deleteById(String id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(String) - start"); //$NON-NLS-1$
		}

		DbTpl entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(String) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	@Override
	protected Class<DbTpl> getEntityClass() {
		return DbTpl.class;
	}
}