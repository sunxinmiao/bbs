package com.jeecms.bbs.dao.impl;

import org.apache.log4j.Logger;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jeecms.bbs.dao.BbsVoteItemDao;
import com.jeecms.bbs.entity.BbsVoteItem;
import com.jeecms.common.hibernate3.Finder;
import com.jeecms.common.hibernate3.HibernateBaseDao;

@Repository
public class BbsVoteItemDaoImpl extends HibernateBaseDao<BbsVoteItem, Integer> implements
		BbsVoteItemDao {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsVoteItemDaoImpl.class);

	public BbsVoteItem findById(Integer id){
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		BbsVoteItem entity = get(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}
	
	@SuppressWarnings("unchecked")
	public List<BbsVoteItem> findByTopic(Integer topicId) {
		if (logger.isDebugEnabled()) {
			logger.debug("findByTopic(Integer) - start"); //$NON-NLS-1$
		}

		String hql = "from BbsVoteItem bean where bean.topic.id=:topicId";
		Finder f = Finder.create(hql);
		f.setParam("topicId", topicId);
		List<BbsVoteItem> returnList = find(f);
		if (logger.isDebugEnabled()) {
			logger.debug("findByTopic(Integer) - end"); //$NON-NLS-1$
		}
		return returnList;
	}

	public BbsVoteItem save(BbsVoteItem bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsVoteItem) - start"); //$NON-NLS-1$
		}

		getSession().save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsVoteItem) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	protected Class<BbsVoteItem> getEntityClass() {
		return BbsVoteItem.class;
	}

}
