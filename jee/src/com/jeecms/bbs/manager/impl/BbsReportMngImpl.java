package com.jeecms.bbs.manager.impl;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.bbs.dao.BbsReportDao;
import com.jeecms.bbs.entity.BbsReport;
import com.jeecms.bbs.manager.BbsReportMng;
import com.jeecms.common.hibernate3.Updater;
import com.jeecms.common.page.Pagination;

@Service
@Transactional
public class BbsReportMngImpl implements BbsReportMng {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsReportMngImpl.class);

	@Transactional(readOnly = true)
	public Pagination getPage(Boolean status,Integer pageNo, Integer pageSize) {
		if (logger.isDebugEnabled()) {
			logger.debug("getPage(Boolean, Integer, Integer) - start"); //$NON-NLS-1$
		}

		Pagination page = dao.getPage(status,pageNo, pageSize);

		if (logger.isDebugEnabled()) {
			logger.debug("getPage(Boolean, Integer, Integer) - end"); //$NON-NLS-1$
		}
		return page;
	}

	@Transactional(readOnly = true)
	public BbsReport findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		BbsReport entity = dao.findById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}
	
	@Transactional(readOnly = true)
	public BbsReport findByUrl(String  url) {
		if (logger.isDebugEnabled()) {
			logger.debug("findByUrl(String) - start"); //$NON-NLS-1$
		}

		BbsReport entity = dao.findByUrl(url);

		if (logger.isDebugEnabled()) {
			logger.debug("findByUrl(String) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public BbsReport save(BbsReport bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsReport) - start"); //$NON-NLS-1$
		}

		dao.save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsReport) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsReport update(BbsReport bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsReport) - start"); //$NON-NLS-1$
		}

		Updater<BbsReport> updater = new Updater<BbsReport>(bean);
		bean = dao.updateByUpdater(updater);

		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsReport) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsReport deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		BbsReport bean = dao.deleteById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsReport[] deleteByIds(Integer[] ids) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(Integer[]) - start"); //$NON-NLS-1$
		}

		BbsReport[] beans = new BbsReport[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(Integer[]) - end"); //$NON-NLS-1$
		}
		return beans;
	}

	private BbsReportDao dao;

	@Autowired
	public void setDao(BbsReportDao dao) {
		this.dao = dao;
	}

}
