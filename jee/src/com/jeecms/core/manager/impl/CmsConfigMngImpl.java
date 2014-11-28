package com.jeecms.core.manager.impl;

import org.apache.log4j.Logger;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.hibernate3.Updater;
import com.jeecms.core.dao.CmsConfigDao;
import com.jeecms.core.entity.CmsConfig;
import com.jeecms.core.manager.CmsConfigMng;

@Service
@Transactional
public class CmsConfigMngImpl implements CmsConfigMng {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(CmsConfigMngImpl.class);

	@Transactional(readOnly = true)
	public CmsConfig get() {
		if (logger.isDebugEnabled()) {
			logger.debug("get() - start"); //$NON-NLS-1$
		}

		CmsConfig entity = dao.findById(1);

		if (logger.isDebugEnabled()) {
			logger.debug("get() - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public void updateCountCopyTime(Date d) {
		if (logger.isDebugEnabled()) {
			logger.debug("updateCountCopyTime(Date) - start"); //$NON-NLS-1$
		}

		dao.findById(1).setCountCopyTime(d);

		if (logger.isDebugEnabled()) {
			logger.debug("updateCountCopyTime(Date) - end"); //$NON-NLS-1$
		}
	}

	public void updateCountClearTime(Date d) {
		if (logger.isDebugEnabled()) {
			logger.debug("updateCountClearTime(Date) - start"); //$NON-NLS-1$
		}

		dao.findById(1).setCountClearTime(d);

		if (logger.isDebugEnabled()) {
			logger.debug("updateCountClearTime(Date) - end"); //$NON-NLS-1$
		}
	}

	public CmsConfig update(CmsConfig bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("update(CmsConfig) - start"); //$NON-NLS-1$
		}

		Updater<CmsConfig> updater = new Updater<CmsConfig>(bean);
		CmsConfig entity = dao.updateByUpdater(updater);
		entity.blankToNull();

		if (logger.isDebugEnabled()) {
			logger.debug("update(CmsConfig) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	private CmsConfigDao dao;

	@Autowired
	public void setDao(CmsConfigDao dao) {
		this.dao = dao;
	}
}