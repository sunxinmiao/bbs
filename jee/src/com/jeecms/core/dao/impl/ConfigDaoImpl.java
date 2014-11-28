package com.jeecms.core.dao.impl;

import org.apache.log4j.Logger;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jeecms.common.hibernate3.HibernateBaseDao;
import com.jeecms.core.dao.ConfigDao;
import com.jeecms.core.entity.Config;

@Repository
public class ConfigDaoImpl extends HibernateBaseDao<Config, String> implements
		ConfigDao {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ConfigDaoImpl.class);

	@SuppressWarnings("unchecked")
	public List<Config> getList() {
		if (logger.isDebugEnabled()) {
			logger.debug("getList() - start"); //$NON-NLS-1$
		}

		String hql = "from Config";
		List<Config> returnList = find(hql);
		if (logger.isDebugEnabled()) {
			logger.debug("getList() - end"); //$NON-NLS-1$
		}
		return returnList;
	}

	public Config findById(String id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(String) - start"); //$NON-NLS-1$
		}

		Config entity = get(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(String) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public Config save(Config bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(Config) - start"); //$NON-NLS-1$
		}

		getSession().save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(Config) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public Config deleteById(String id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(String) - start"); //$NON-NLS-1$
		}

		Config entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(String) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	@Override
	protected Class<Config> getEntityClass() {
		return Config.class;
	}
}