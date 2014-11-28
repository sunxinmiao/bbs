package com.jeecms.bbs.dao.impl;

import org.apache.log4j.Logger;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.jeecms.bbs.dao.BbsForumDao;
import com.jeecms.bbs.entity.BbsForum;
import com.jeecms.common.hibernate3.HibernateBaseDao;
import com.jeecms.common.page.Pagination;

@Repository
public class BbsForumDaoImpl extends HibernateBaseDao<BbsForum, Integer>
		implements BbsForumDao {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsForumDaoImpl.class);

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
	
	@SuppressWarnings("unchecked")
	public List<BbsForum> getList(Integer siteId) {
		if (logger.isDebugEnabled()) {
			logger.debug("getList(Integer) - start"); //$NON-NLS-1$
		}

		String hql = "select bean from BbsForum bean inner join bean.category ctg where bean.site.id=? order by ctg.priority, bean.priority";
		List<BbsForum> returnList = find(hql, siteId);
		if (logger.isDebugEnabled()) {
			logger.debug("getList(Integer) - end"); //$NON-NLS-1$
		}
		return returnList;
	}

	@SuppressWarnings("unchecked")
	public List<BbsForum> getList(Integer siteId, Integer categoryId) {
		if (logger.isDebugEnabled()) {
			logger.debug("getList(Integer, Integer) - start"); //$NON-NLS-1$
		}

		String hql = "from BbsForum bean where bean.site.id=? and bean.category.id=? order by bean.priority";
		List<BbsForum> returnList = find(hql, siteId, categoryId);
		if (logger.isDebugEnabled()) {
			logger.debug("getList(Integer, Integer) - end"); //$NON-NLS-1$
		}
		return returnList;
	}

	public int countPath(Integer siteId, String path) {
		if (logger.isDebugEnabled()) {
			logger.debug("countPath(Integer, String) - start"); //$NON-NLS-1$
		}

		String hql = "select count(*) from BbsForum bean where bean.site.id=? and bean.path=?";
		int returnint = ((Number) getSession().createQuery(hql).setParameter(0, siteId).setParameter(1, path).iterate().next()).intValue();
		if (logger.isDebugEnabled()) {
			logger.debug("countPath(Integer, String) - end"); //$NON-NLS-1$
		}
		return returnint;
	}

	public BbsForum getByPath(Integer siteId, String path) {
		if (logger.isDebugEnabled()) {
			logger.debug("getByPath(Integer, String) - start"); //$NON-NLS-1$
		}

		String hql = "select bean from BbsForum bean where bean.site.id=? and bean.path=?";
		BbsForum returnBbsForum = (BbsForum) findUnique(hql, siteId, path);
		if (logger.isDebugEnabled()) {
			logger.debug("getByPath(Integer, String) - end"); //$NON-NLS-1$
		}
		return returnBbsForum;
	}

	public BbsForum findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		BbsForum entity = get(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public BbsForum save(BbsForum bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsForum) - start"); //$NON-NLS-1$
		}

		getSession().save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsForum) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsForum deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		BbsForum entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	@Override
	protected Class<BbsForum> getEntityClass() {
		return BbsForum.class;
	}

	public void updateAll_topic_today() {
		if (logger.isDebugEnabled()) {
			logger.debug("updateAll_topic_today() - start"); //$NON-NLS-1$
		}

		// TODO Auto-generated method stub
		String hql = "update BbsForum forum set forum.postToday=0";
		getSession().createQuery(hql).executeUpdate();

		if (logger.isDebugEnabled()) {
			logger.debug("updateAll_topic_today() - end"); //$NON-NLS-1$
		}
	}
}