package com.jeecms.core.dao.impl;

import org.apache.log4j.Logger;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.jeecms.common.hibernate3.HibernateBaseDao;
import com.jeecms.common.page.Pagination;
import com.jeecms.core.dao.UnifiedUserDao;
import com.jeecms.core.entity.UnifiedUser;

@Repository
public class UnifiedUserDaoImpl extends HibernateBaseDao<UnifiedUser, Integer>
		implements UnifiedUserDao {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(UnifiedUserDaoImpl.class);

	public UnifiedUser getByUsername(String username) {
		if (logger.isDebugEnabled()) {
			logger.debug("getByUsername(String) - start"); //$NON-NLS-1$
		}

		UnifiedUser returnUnifiedUser = findUniqueByProperty("username", username);
		if (logger.isDebugEnabled()) {
			logger.debug("getByUsername(String) - end"); //$NON-NLS-1$
		}
		return returnUnifiedUser;
	}

	public List<UnifiedUser> getByEmail(String email) {
		if (logger.isDebugEnabled()) {
			logger.debug("getByEmail(String) - start"); //$NON-NLS-1$
		}

		List<UnifiedUser> returnList = findByProperty("email", email);
		if (logger.isDebugEnabled()) {
			logger.debug("getByEmail(String) - end"); //$NON-NLS-1$
		}
		return returnList;
	}

	public int countByEmail(String email) {
		if (logger.isDebugEnabled()) {
			logger.debug("countByEmail(String) - start"); //$NON-NLS-1$
		}

		String hql = "select count(*) from UnifiedUser bean where bean.email=:email";
		Query query = getSession().createQuery(hql);
		query.setParameter("email", email);
		int returnint = ((Number) query.iterate().next()).intValue();
		if (logger.isDebugEnabled()) {
			logger.debug("countByEmail(String) - end"); //$NON-NLS-1$
		}
		return returnint;
	}

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

	public UnifiedUser findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		UnifiedUser entity = get(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public UnifiedUser save(UnifiedUser bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(UnifiedUser) - start"); //$NON-NLS-1$
		}

		getSession().save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(UnifiedUser) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public UnifiedUser deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		UnifiedUser entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	@Override
	protected Class<UnifiedUser> getEntityClass() {
		return UnifiedUser.class;
	}
}