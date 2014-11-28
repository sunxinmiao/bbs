package com.jeecms.bbs.dao.impl;

import org.apache.log4j.Logger;

import org.springframework.stereotype.Repository;

import com.jeecms.bbs.dao.BbsMessageReplyDao;
import com.jeecms.bbs.entity.BbsMessageReply;
import com.jeecms.common.hibernate3.Finder;
import com.jeecms.common.hibernate3.HibernateBaseDao;
import com.jeecms.common.page.Pagination;

@Repository
public class BbsMessageReplyDaoImpl extends
		HibernateBaseDao<BbsMessageReply, Integer> implements BbsMessageReplyDao {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsMessageReplyDaoImpl.class);

	public BbsMessageReply findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		BbsMessageReply entity = get(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public BbsMessageReply save(BbsMessageReply bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsMessageReply) - start"); //$NON-NLS-1$
		}

		getSession().save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsMessageReply) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsMessageReply deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		BbsMessageReply entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}
	
	public Pagination getPageByMsgId(Integer msgId, Integer pageNo,
			Integer pageSize) {
		if (logger.isDebugEnabled()) {
			logger.debug("getPageByMsgId(Integer, Integer, Integer) - start"); //$NON-NLS-1$
		}

		String hql = "from BbsMessageReply bean where bean.message.id=:msgId order by bean.createTime desc";
		Finder f = Finder.create(hql).setParam("msgId", msgId);
		Pagination returnPagination = find(f, pageNo, pageSize);
		if (logger.isDebugEnabled()) {
			logger.debug("getPageByMsgId(Integer, Integer, Integer) - end"); //$NON-NLS-1$
		}
		return returnPagination;
	}

	@Override
	protected Class<BbsMessageReply> getEntityClass() {
		return BbsMessageReply.class;
	}
}