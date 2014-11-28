package com.jeecms.core.manager.impl;

import org.apache.log4j.Logger;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.hibernate3.Updater;
import com.jeecms.core.dao.FtpDao;
import com.jeecms.core.entity.Ftp;
import com.jeecms.core.manager.FtpMng;

@Service
@Transactional
public class FtpMngImpl implements FtpMng {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(FtpMngImpl.class);

	@Transactional(readOnly = true)
	public List<Ftp> getList() {
		if (logger.isDebugEnabled()) {
			logger.debug("getList() - start"); //$NON-NLS-1$
		}

		List<Ftp> returnList = dao.getList();
		if (logger.isDebugEnabled()) {
			logger.debug("getList() - end"); //$NON-NLS-1$
		}
		return returnList;
	}

	@Transactional(readOnly = true)
	public Ftp findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		Ftp entity = dao.findById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public Ftp save(Ftp bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(Ftp) - start"); //$NON-NLS-1$
		}

		dao.save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(Ftp) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public Ftp update(Ftp bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("update(Ftp) - start"); //$NON-NLS-1$
		}

		Updater<Ftp> updater = new Updater<Ftp>(bean);
		Ftp entity = dao.updateByUpdater(updater);

		if (logger.isDebugEnabled()) {
			logger.debug("update(Ftp) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public Ftp deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		Ftp bean = dao.deleteById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public Ftp[] deleteByIds(Integer[] ids) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(Integer[]) - start"); //$NON-NLS-1$
		}

		Ftp[] beans = new Ftp[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(Integer[]) - end"); //$NON-NLS-1$
		}
		return beans;
	}

	private FtpDao dao;

	@Autowired
	public void setDao(FtpDao dao) {
		this.dao = dao;
	}
}