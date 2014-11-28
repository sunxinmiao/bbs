package com.jeecms.core.dao.impl;

import org.apache.log4j.Logger;

import org.springframework.stereotype.Repository;

import com.jeecms.common.hibernate3.HibernateBaseDao;
import com.jeecms.core.dao.DbFileDao;
import com.jeecms.core.entity.DbFile;

@Repository
public class DbFileDaoImpl extends HibernateBaseDao<DbFile, String> implements
		DbFileDao {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DbFileDaoImpl.class);

	public DbFile findById(String id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(String) - start"); //$NON-NLS-1$
		}

		DbFile entity = get(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(String) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public DbFile save(DbFile bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(DbFile) - start"); //$NON-NLS-1$
		}

		getSession().save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(DbFile) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public DbFile deleteById(String id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(String) - start"); //$NON-NLS-1$
		}

		DbFile entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(String) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	@Override
	protected Class<DbFile> getEntityClass() {
		return DbFile.class;
	}
}