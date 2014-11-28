package com.jeecms.bbs.dao.impl;

import org.apache.log4j.Logger;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.jeecms.bbs.dao.BbsMessageDao;
import com.jeecms.bbs.entity.BbsMessage;
import com.jeecms.common.hibernate3.Finder;
import com.jeecms.common.hibernate3.HibernateBaseDao;
import com.jeecms.common.page.Pagination;

@Repository
public class BbsMessageDaoImpl extends HibernateBaseDao<BbsMessage, Integer>
		implements BbsMessageDao {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsMessageDaoImpl.class);

	public BbsMessage findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		BbsMessage entity = get(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public BbsMessage save(BbsMessage bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsMessage) - start"); //$NON-NLS-1$
		}

		getSession().save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsMessage) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsMessage deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		BbsMessage entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public BbsMessage getSendRelation(Integer userId, Integer senderId,
			Integer receiverId,Integer typeId) {
		if (logger.isDebugEnabled()) {
			logger.debug("getSendRelation(Integer, Integer, Integer, Integer) - start"); //$NON-NLS-1$
		}

		String hql = "from BbsMessage bean where bean.user.id=:userId and bean.msgType=:typeId and ((bean.sender.id=:senderId and bean.receiver.id=:receiverId) or (bean.sender.id=:receiverId and bean.receiver.id=:senderId))";
		Finder f = Finder.create(hql);
		f.setParam("userId", userId);
		f.setParam("typeId", typeId);
		f.setParam("senderId", senderId);
		f.setParam("receiverId", receiverId);
		f.setMaxResults(1);
		Query query = f.createQuery(getSession());
		BbsMessage returnBbsMessage = (BbsMessage) query.uniqueResult();
		if (logger.isDebugEnabled()) {
			logger.debug("getSendRelation(Integer, Integer, Integer, Integer) - end"); //$NON-NLS-1$
		}
		return returnBbsMessage;
	}

	public Pagination getPageByUserId(Integer userId, Integer typeId,Integer pageNo,
			Integer pageSize) {
		if (logger.isDebugEnabled()) {
			logger.debug("getPageByUserId(Integer, Integer, Integer, Integer) - start"); //$NON-NLS-1$
		}

		String hql = "from BbsMessage bean where bean.user.id=:userId and bean.msgType=:typeId";
		Finder f = Finder.create(hql);
		f.setParam("userId", userId);
		f.setParam("typeId", typeId);
		Pagination returnPagination = find(f, pageNo, pageSize);
		if (logger.isDebugEnabled()) {
			logger.debug("getPageByUserId(Integer, Integer, Integer, Integer) - end"); //$NON-NLS-1$
		}
		return returnPagination;
	}

	public List getListByUserIdStatus(Integer userId, Integer typeId,
			Boolean status) {
		if (logger.isDebugEnabled()) {
			logger.debug("getListByUserIdStatus(Integer, Integer, Boolean) - start"); //$NON-NLS-1$
		}

		String hql = "from BbsMessage bean where bean.user.id=:userId and bean.msgType=:typeId and bean.status=:status";
		Finder f = Finder.create(hql);
		f.setParam("userId", userId);
		f.setParam("typeId", typeId);
		f.setParam("status", status);
		List returnList = find(f);
		if (logger.isDebugEnabled()) {
			logger.debug("getListByUserIdStatus(Integer, Integer, Boolean) - end"); //$NON-NLS-1$
		}
		return returnList;
	}

	@Override
	protected Class<BbsMessage> getEntityClass() {
		return BbsMessage.class;
	}

}