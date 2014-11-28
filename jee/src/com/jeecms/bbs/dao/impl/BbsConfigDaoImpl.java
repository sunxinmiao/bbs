package com.jeecms.bbs.dao.impl;

import org.apache.log4j.Logger;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.jeecms.bbs.dao.BbsConfigDao;
import com.jeecms.bbs.entity.BbsConfig;
import com.jeecms.common.hibernate3.HibernateBaseDao;

@Repository
public class BbsConfigDaoImpl extends HibernateBaseDao<BbsConfig, Integer> implements
		BbsConfigDao {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsConfigDaoImpl.class);

	public void clearTodayData() {
		if (logger.isDebugEnabled()) {
			logger.debug("clearTodayData() - start"); //$NON-NLS-1$
		}

		// 最高发贴数
		String hql = "update BbsConfig bean set bean.postMax=bean.postToday, bean.postMaxDate=:currentDate where bean.postToday>bean.postMax";
		getSession().createQuery(hql).setParameter("currentDate", new Date())
				.executeUpdate();
		// 昨日发帖数
		hql = "update BbsConfig bean set bean.postYesterday=bean.postToday";
		getSession().createQuery(hql).executeUpdate();
		// 今日发帖数清零
		hql = "update BbsConfig bean set bean.postToday=0";
		getSession().createQuery(hql).executeUpdate();

		if (logger.isDebugEnabled()) {
			logger.debug("clearTodayData() - end"); //$NON-NLS-1$
		}
	}
	
	public BbsConfig findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		BbsConfig entity = get(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}
	
	public BbsConfig save(BbsConfig bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsConfig) - start"); //$NON-NLS-1$
		}

		getSession().save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsConfig) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsConfig deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		BbsConfig entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	@Override
	protected Class<BbsConfig> getEntityClass() {
		return BbsConfig.class;
	}
}