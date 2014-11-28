package com.jeecms.bbs.dao.impl;

import org.apache.log4j.Logger;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.jeecms.common.hibernate3.HibernateBaseDao;
import com.jeecms.common.page.Pagination;
import com.jeecms.bbs.dao.AttachmentDao;
import com.jeecms.bbs.entity.Attachment;

@Repository
public class AttachmentDaoImpl extends HibernateBaseDao<Attachment, Integer> implements AttachmentDao {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(AttachmentDaoImpl.class);

	public Pagination getPage(int pageNo, int pageSize) {
		if (logger.isDebugEnabled()) {
			logger.debug("getPage(int, int) - start"); //$NON-NLS-1$
		}

		Criteria crit = createCriteria();
		Pagination page = findByCriteria(crit, pageNo, pageSize);

		if (logger.isDebugEnabled()) {
			logger.debug("getPage(int, int) - end"); //$NON-NLS-1$
		}
		return page;
	}

	public Attachment findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		Attachment entity = get(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public Attachment save(Attachment bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(Attachment) - start"); //$NON-NLS-1$
		}

		getSession().save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(Attachment) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public Attachment deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		Attachment entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}
	
	@Override
	protected Class<Attachment> getEntityClass() {
		return Attachment.class;
	}
}