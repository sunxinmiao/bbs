package com.jeecms.bbs.manager.impl;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.bbs.dao.BbsCreditExchangeDao;
import com.jeecms.bbs.entity.BbsCreditExchange;
import com.jeecms.bbs.manager.BbsCreditExchangeMng;
import com.jeecms.common.hibernate3.Updater;

@Service
@Transactional
public class BbsCreditExchangeMngImpl implements BbsCreditExchangeMng {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsCreditExchangeMngImpl.class);

	@Transactional(readOnly = true)
	public BbsCreditExchange findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		BbsCreditExchange config = dao.findById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return config;
	}

	public BbsCreditExchange update(BbsCreditExchange bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsCreditExchange) - start"); //$NON-NLS-1$
		}

		BbsCreditExchange entity = findById(bean.getId());
		Updater<BbsCreditExchange> updater = new Updater<BbsCreditExchange>(
				bean);
		entity = dao.updateByUpdater(updater);

		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsCreditExchange) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	private BbsCreditExchangeDao dao;

	@Autowired
	public void setDao(BbsCreditExchangeDao dao) {
		this.dao = dao;
	}

}
