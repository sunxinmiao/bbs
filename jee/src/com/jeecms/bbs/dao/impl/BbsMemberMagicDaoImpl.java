package com.jeecms.bbs.dao.impl;

import org.apache.log4j.Logger;

import org.springframework.stereotype.Repository;

import com.jeecms.bbs.MagicConstants;
import com.jeecms.bbs.entity.BbsMagicLog;
import com.jeecms.bbs.dao.BbsMagicLogDao;
import com.jeecms.common.hibernate3.Finder;
import com.jeecms.common.hibernate3.HibernateBaseDao;
import com.jeecms.common.page.Pagination;

@Repository
public class BbsMemberMagicDaoImpl extends HibernateBaseDao<BbsMagicLog, Integer>
		implements BbsMagicLogDao {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsMemberMagicDaoImpl.class);

	public Pagination getPage(Byte operator,Integer userId, int pageNo, int pageSize) {
		if (logger.isDebugEnabled()) {
			logger.debug("getPage(Byte, Integer, int, int) - start"); //$NON-NLS-1$
		}

		String hql = "from BbsMagicLog magiclog where 1=1 ";
		Finder finder = Finder.create(hql);
		if(operator!=null){
			finder.append(" and magiclog.operator=:operator").setParam("operator", operator);
		}
		if(MagicConstants.MAGIC_OPERATOR_GIVE==operator){
			if(userId!=null){
				finder.append(" and (magiclog.user.id=:userId or magiclog.targetUser.id=:userId)").setParam("userId", userId);
			}
		}else{
			if(userId!=null){
				finder.append(" and magiclog.user.id=:userId").setParam("userId", userId);
			}
		}
		finder.append(" order by magiclog.logTime desc");
		Pagination page = find(finder, pageNo, pageSize);

		if (logger.isDebugEnabled()) {
			logger.debug("getPage(Byte, Integer, int, int) - end"); //$NON-NLS-1$
		}
		return page;
	}

	public BbsMagicLog findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		BbsMagicLog entity = get(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public BbsMagicLog save(BbsMagicLog bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsMagicLog) - start"); //$NON-NLS-1$
		}

		getSession().save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsMagicLog) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsMagicLog deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		BbsMagicLog entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	protected Class<BbsMagicLog> getEntityClass() {
		return BbsMagicLog.class;
	}
}