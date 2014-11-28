package com.jeecms.core.dao.impl;

import org.apache.log4j.Logger;

import java.util.Date;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.jeecms.common.hibernate3.HibernateBaseDao;
import com.jeecms.common.page.Pagination;
import com.jeecms.core.dao.AuthenticationDao;
import com.jeecms.core.entity.Authentication;

@Repository
public class AuthenticationDaoImpl extends
		HibernateBaseDao<Authentication, String> implements AuthenticationDao {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(AuthenticationDaoImpl.class);

	public int deleteExpire(Date d) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteExpire(Date) - start"); //$NON-NLS-1$
		}

		String hql = "delete Authentication bean where bean.updateTime <= :d";
		int returnint = getSession().createQuery(hql).setTimestamp("d", d).executeUpdate();
		if (logger.isDebugEnabled()) {
			logger.debug("deleteExpire(Date) - end"); //$NON-NLS-1$
		}
		return returnint;
	}

	public Authentication getByUserId(Long userId) {
		if (logger.isDebugEnabled()) {
			logger.debug("getByUserId(Long) - start"); //$NON-NLS-1$
		}

		String hql = "from Authentication bean where bean.uid=?";
		Authentication returnAuthentication = (Authentication) findUnique(hql, userId);
		if (logger.isDebugEnabled()) {
			logger.debug("getByUserId(Long) - end"); //$NON-NLS-1$
		}
		return returnAuthentication;
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

	public Authentication findById(String id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(String) - start"); //$NON-NLS-1$
		}

		Authentication entity = get(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(String) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public Authentication save(Authentication bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(Authentication) - start"); //$NON-NLS-1$
		}

		getSession().save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(Authentication) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public Authentication deleteById(String id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(String) - start"); //$NON-NLS-1$
		}

		Authentication entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(String) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	@Override
	protected Class<Authentication> getEntityClass() {
		return Authentication.class;
	}
}