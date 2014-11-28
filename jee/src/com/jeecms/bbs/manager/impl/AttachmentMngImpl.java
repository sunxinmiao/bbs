package com.jeecms.bbs.manager.impl;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.hibernate3.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.bbs.dao.AttachmentDao;
import com.jeecms.bbs.entity.Attachment;
import com.jeecms.bbs.manager.AttachmentMng;

@Service
@Transactional
public class AttachmentMngImpl implements AttachmentMng {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(AttachmentMngImpl.class);

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
	public Attachment findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		Attachment entity = dao.findById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public Attachment save(Attachment bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(Attachment) - start"); //$NON-NLS-1$
		}

		dao.save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(Attachment) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public Attachment update(Attachment bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("update(Attachment) - start"); //$NON-NLS-1$
		}

		Updater<Attachment> updater = new Updater<Attachment>(bean);
		bean = dao.updateByUpdater(updater);

		if (logger.isDebugEnabled()) {
			logger.debug("update(Attachment) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public Attachment deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		Attachment bean = dao.deleteById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return bean;
	}
	
	public Attachment[] deleteByIds(Integer[] ids) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(Integer[]) - start"); //$NON-NLS-1$
		}

		Attachment[] beans = new Attachment[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(Integer[]) - end"); //$NON-NLS-1$
		}
		return beans;
	}

	private AttachmentDao dao;

	@Autowired
	public void setDao(AttachmentDao dao) {
		this.dao = dao;
	}
}