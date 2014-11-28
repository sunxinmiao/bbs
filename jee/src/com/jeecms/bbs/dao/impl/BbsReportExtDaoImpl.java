package com.jeecms.bbs.dao.impl;

import org.apache.log4j.Logger;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jeecms.bbs.entity.BbsReportExt;
import com.jeecms.bbs.dao.BbsReportExtDao;
import com.jeecms.common.hibernate3.Finder;
import com.jeecms.common.hibernate3.HibernateBaseDao;
import com.jeecms.common.page.Pagination;

@Repository
public class BbsReportExtDaoImpl extends
		HibernateBaseDao<BbsReportExt, Integer> implements BbsReportExtDao {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsReportExtDaoImpl.class);

	public Pagination getPage(Integer reportId,Integer pageNo, Integer pageSize) {
		if (logger.isDebugEnabled()) {
			logger.debug("getPage(Integer, Integer, Integer) - start"); //$NON-NLS-1$
		}

		String hql = "from BbsReportExt reportext ";
		Finder finder = Finder.create(hql);
		if(reportId!=null){
			finder.append(" where reportext.report.id=:reportId").setParam("reportId",reportId);
		}
		finder.append(" order by reportext.reportTime asc");
		Pagination page = find(finder, pageNo, pageSize);

		if (logger.isDebugEnabled()) {
			logger.debug("getPage(Integer, Integer, Integer) - end"); //$NON-NLS-1$
		}
		return page;
	}
	
	public BbsReportExt findByReportUid(Integer reportId, Integer userId) {
		if (logger.isDebugEnabled()) {
			logger.debug("findByReportUid(Integer, Integer) - start"); //$NON-NLS-1$
		}

		String hql = "from BbsReportExt ext  where ext.report.id=:reportId and ext.reportUser.id=:userId";
		List li= getSession().createQuery(hql).setParameter("reportId", reportId).setParameter("userId", userId).list();
		if(li!=null&&li.size()>0){
			BbsReportExt returnBbsReportExt = (BbsReportExt) li.get(0);
			if (logger.isDebugEnabled()) {
				logger.debug("findByReportUid(Integer, Integer) - end"); //$NON-NLS-1$
			}
			return returnBbsReportExt;
		}else{
			if (logger.isDebugEnabled()) {
				logger.debug("findByReportUid(Integer, Integer) - end"); //$NON-NLS-1$
			}
			return null;
		}
	}

	public BbsReportExt findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		BbsReportExt entity = get(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public BbsReportExt save(BbsReportExt bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsReportExt) - start"); //$NON-NLS-1$
		}

		getSession().save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsReportExt) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsReportExt deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		BbsReportExt entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	protected Class<BbsReportExt> getEntityClass() {
		return BbsReportExt.class;
	}

	

}