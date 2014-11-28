package com.jeecms.core.dao.impl;

import org.apache.log4j.Logger;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jeecms.common.hibernate3.HibernateBaseDao;
import com.jeecms.core.dao.FtpDao;
import com.jeecms.core.entity.Ftp;

@Repository
public class FtpDaoImpl extends HibernateBaseDao<Ftp, Integer> implements
		FtpDao {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(FtpDaoImpl.class);

	@SuppressWarnings("unchecked")
	public List<Ftp> getList() {
		if (logger.isDebugEnabled()) {
			logger.debug("getList() - start"); //$NON-NLS-1$
		}

		String hql = "from Ftp bean";
		List<Ftp> returnList = find(hql);
		if (logger.isDebugEnabled()) {
			logger.debug("getList() - end"); //$NON-NLS-1$
		}
		return returnList;
	}

	public Ftp findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		Ftp entity = get(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public Ftp save(Ftp bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(Ftp) - start"); //$NON-NLS-1$
		}

		getSession().save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(Ftp) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public Ftp deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		Ftp entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	@Override
	protected Class<Ftp> getEntityClass() {
		return Ftp.class;
	}
}