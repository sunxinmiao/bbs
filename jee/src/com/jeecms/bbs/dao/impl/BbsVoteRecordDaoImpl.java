package com.jeecms.bbs.dao.impl;

import org.apache.log4j.Logger;


import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.jeecms.bbs.dao.BbsVoteRecordDao;
import com.jeecms.bbs.entity.BbsVoteRecord;
import com.jeecms.common.hibernate3.HibernateBaseDao;

@Repository
public class BbsVoteRecordDaoImpl extends HibernateBaseDao<BbsVoteRecord, Integer> implements
		BbsVoteRecordDao {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsVoteRecordDaoImpl.class);

	public BbsVoteRecord findRecord(Integer userId, Integer topicId){
		if (logger.isDebugEnabled()) {
			logger.debug("findRecord(Integer, Integer) - start"); //$NON-NLS-1$
		}

		String hql = "from BbsVoteRecord bean where bean.user.id=:userId and bean.topic.id=:topicId";
		Query query = getSession().createQuery(hql);
		query.setParameter("userId", userId);
		query.setParameter("topicId", topicId);
		query.setMaxResults(1);
		BbsVoteRecord returnBbsVoteRecord = (BbsVoteRecord) query.uniqueResult();
		if (logger.isDebugEnabled()) {
			logger.debug("findRecord(Integer, Integer) - end"); //$NON-NLS-1$
		}
		return returnBbsVoteRecord;
	}

	public BbsVoteRecord save(BbsVoteRecord bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsVoteRecord) - start"); //$NON-NLS-1$
		}

		getSession().save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsVoteRecord) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	protected Class<BbsVoteRecord> getEntityClass() {
		return BbsVoteRecord.class;
	}

}
