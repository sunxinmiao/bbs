package com.jeecms.bbs.manager.impl;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.bbs.dao.BbsReportExtDao;
import com.jeecms.bbs.entity.BbsReportExt;
import com.jeecms.bbs.manager.BbsReportExtMng;
import com.jeecms.common.hibernate3.Updater;
import com.jeecms.common.page.Pagination;

@Service
@Transactional
public class BbsReportExtMngImpl implements BbsReportExtMng {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsReportExtMngImpl.class);

	@Transactional(readOnly = true)
	public Pagination getPage(Integer reportId,Integer pageNo, Integer pageSize) {
		if (logger.isDebugEnabled()) {
			logger.debug("getPage(Integer, Integer, Integer) - start"); //$NON-NLS-1$
		}

		Pagination page = dao.getPage(reportId,pageNo, pageSize);

		if (logger.isDebugEnabled()) {
			logger.debug("getPage(Integer, Integer, Integer) - end"); //$NON-NLS-1$
		}
		return page;
	}

	@Transactional(readOnly = true)
	public BbsReportExt findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		BbsReportExt entity = dao.findById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}
	
	@Transactional(readOnly = true)
	public BbsReportExt findByReportUid(Integer reportId, Integer userId) {
		if (logger.isDebugEnabled()) {
			logger.debug("findByReportUid(Integer, Integer) - start"); //$NON-NLS-1$
		}
		

		BbsReportExt returnBbsReportExt = dao.findByReportUid(reportId, userId);
		if (logger.isDebugEnabled()) {
			logger.debug("findByReportUid(Integer, Integer) - end"); //$NON-NLS-1$
		}
		return returnBbsReportExt;
	}

	public BbsReportExt save(BbsReportExt bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsReportExt) - start"); //$NON-NLS-1$
		}

		dao.save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsReportExt) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsReportExt update(BbsReportExt bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsReportExt) - start"); //$NON-NLS-1$
		}

		Updater<BbsReportExt> updater = new Updater<BbsReportExt>(bean);
		bean = dao.updateByUpdater(updater);

		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsReportExt) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsReportExt deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		BbsReportExt bean = dao.deleteById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsReportExt[] deleteByIds(Integer[] ids) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(Integer[]) - start"); //$NON-NLS-1$
		}

		BbsReportExt[] beans = new BbsReportExt[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(Integer[]) - end"); //$NON-NLS-1$
		}
		return beans;
	}

	private BbsReportExtDao dao;

	@Autowired
	public void setDao(BbsReportExtDao dao) {
		this.dao = dao;
	}

	

}
