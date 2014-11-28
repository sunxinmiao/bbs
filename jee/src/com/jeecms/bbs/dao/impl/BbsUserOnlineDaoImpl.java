package com.jeecms.bbs.dao.impl;

import org.apache.log4j.Logger;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.jeecms.bbs.entity.BbsUserOnline;
import com.jeecms.bbs.dao.BbsUserOnlineDao;
import com.jeecms.common.hibernate3.Finder;
import com.jeecms.common.hibernate3.HibernateBaseDao;
import com.jeecms.common.page.Pagination;

@Repository
public class BbsUserOnlineDaoImpl extends
		HibernateBaseDao<BbsUserOnline, Integer> implements BbsUserOnlineDao {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsUserOnlineDaoImpl.class);
	
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
	
	public List<BbsUserOnline> getList() {
		if (logger.isDebugEnabled()) {
			logger.debug("getList() - start"); //$NON-NLS-1$
		}

		String hql="from BbsUserOnline online ";
		Finder finder=Finder.create(hql);
		List<BbsUserOnline> returnList = find(finder);
		if (logger.isDebugEnabled()) {
			logger.debug("getList() - end"); //$NON-NLS-1$
		}
		return returnList;
	}

	public BbsUserOnline findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		BbsUserOnline entity = get(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public BbsUserOnline save(BbsUserOnline bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsUserOnline) - start"); //$NON-NLS-1$
		}

		getSession().save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsUserOnline) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsUserOnline deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		BbsUserOnline entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	protected Class<BbsUserOnline> getEntityClass() {
		return BbsUserOnline.class;
	}
}