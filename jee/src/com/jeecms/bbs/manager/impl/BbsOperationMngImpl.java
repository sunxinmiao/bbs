package com.jeecms.bbs.manager.impl;

import org.apache.log4j.Logger;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.bbs.dao.BbsOperationDao;
import com.jeecms.bbs.entity.BbsOperation;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.BbsOperationMng;
import com.jeecms.common.hibernate3.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.core.entity.CmsSite;

@Service
@Transactional
public class BbsOperationMngImpl implements BbsOperationMng {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsOperationMngImpl.class);

	public BbsOperation saveOpt(CmsSite site, BbsUser operator, String optName,
			String reason, Object target) {
		if (logger.isDebugEnabled()) {
			logger.debug("saveOpt(CmsSite, BbsUser, String, String, Object) - start"); //$NON-NLS-1$
		}

		BbsOperation opt = new BbsOperation();
		opt.setSite(site);
		opt.setOperater(operator);
		opt.setOptTime(new Timestamp(System.currentTimeMillis()));
		opt.setOptName(optName);
		opt.setOptReason(reason);
		opt.setTarget(target);
		BbsOperation returnBbsOperation = dao.save(opt);
		if (logger.isDebugEnabled()) {
			logger.debug("saveOpt(CmsSite, BbsUser, String, String, Object) - end"); //$NON-NLS-1$
		}
		return returnBbsOperation;
	}
	
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		if (logger.isDebugEnabled()) {
			logger.debug("getPage(int, int) - start"); //$NON-NLS-1$
		}

		Pagination page = dao.getPage(pageNo, pageSize);

		if (logger.isDebugEnabled()) {
			logger.debug("getPage(int, int) - end"); //$NON-NLS-1$
		}
		return page;
	}

	@Transactional(readOnly = true)
	public BbsOperation findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		BbsOperation entity = dao.findById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public BbsOperation save(BbsOperation bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsOperation) - start"); //$NON-NLS-1$
		}

		dao.save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsOperation) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsOperation update(BbsOperation bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsOperation) - start"); //$NON-NLS-1$
		}

		Updater<BbsOperation> updater = new Updater<BbsOperation>(bean);
		bean = dao.updateByUpdater(updater);

		if (logger.isDebugEnabled()) {
			logger.debug("update(BbsOperation) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsOperation deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		BbsOperation bean = dao.deleteById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return bean;
	}
	
	public BbsOperation[] deleteByIds(Integer[] ids) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(Integer[]) - start"); //$NON-NLS-1$
		}

		BbsOperation[] beans = new BbsOperation[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(Integer[]) - end"); //$NON-NLS-1$
		}
		return beans;
	}

	private BbsOperationDao dao;

	@Autowired
	public void setDao(BbsOperationDao dao) {
		this.dao = dao;
	}

}
