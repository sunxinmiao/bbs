package com.jeecms.bbs.manager.impl;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.bbs.dao.BbsMagicConfigDao;
import com.jeecms.bbs.entity.BbsMagicConfig;
import com.jeecms.bbs.manager.BbsMagicConfigMng;
import com.jeecms.common.hibernate3.Updater;

@Service
@Transactional
public class BbsMagicConfigMngImpl implements BbsMagicConfigMng {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsMagicConfigMngImpl.class);

	@Transactional(readOnly = true)
	public BbsMagicConfig findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		BbsMagicConfig config = dao.findById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return config;
	}

	public BbsMagicConfig update(BbsMagicConfig bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsMagicConfig) - start"); //$NON-NLS-1$
		}

		BbsMagicConfig entity = findById(bean.getId());
		Updater<BbsMagicConfig> updater = new Updater<BbsMagicConfig>(bean);
		entity = dao.updateByUpdater(updater);

		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsMagicConfig) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	private BbsMagicConfigDao dao;

	@Autowired
	public void setDao(BbsMagicConfigDao dao) {
		this.dao = dao;
	}

}
