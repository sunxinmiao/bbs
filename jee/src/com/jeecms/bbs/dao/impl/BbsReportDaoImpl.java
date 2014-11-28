package com.jeecms.bbs.dao.impl;

import org.apache.log4j.Logger;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jeecms.bbs.entity.BbsReport;
import com.jeecms.bbs.dao.BbsReportDao;
import com.jeecms.common.hibernate3.Finder;
import com.jeecms.common.hibernate3.HibernateBaseDao;
import com.jeecms.common.page.Pagination;

@Repository
public class BbsReportDaoImpl extends HibernateBaseDao<BbsReport, Integer>
		implements BbsReportDao {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsReportDaoImpl.class);

	public Pagination getPage(Boolean status,Integer pageNo, Integer pageSize) {
		if (logger.isDebugEnabled()) {
			logger.debug("getPage(Boolean, Integer, Integer) - start"); //$NON-NLS-1$
		}

		String hql = "from BbsReport report";
		Finder finder = Finder.create(hql);
		if(status!=null){
			if(status){
				finder.append(" where report.status=true");
			}else{
				finder.append(" where report.status=false");
			}
		}
		finder.append(" order by report.reportTime desc");
		Pagination page = find(finder, pageNo, pageSize);

		if (logger.isDebugEnabled()) {
			logger.debug("getPage(Boolean, Integer, Integer) - end"); //$NON-NLS-1$
		}
		return page;
	}
	
	public BbsReport findByUrl(String url) {
		if (logger.isDebugEnabled()) {
			logger.debug("findByUrl(String) - start"); //$NON-NLS-1$
		}

		String hql = "from BbsReport report  where report.reportUrl=:url";
		List li= getSession().createQuery(hql).setParameter("url", url).list();
		if(li!=null&&li.size()>0){
			BbsReport returnBbsReport = (BbsReport) li.get(0);
			if (logger.isDebugEnabled()) {
				logger.debug("findByUrl(String) - end"); //$NON-NLS-1$
			}
			return returnBbsReport;
		}else{
			if (logger.isDebugEnabled()) {
				logger.debug("findByUrl(String) - end"); //$NON-NLS-1$
			}
			return null;
		}
	}

	public BbsReport findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		BbsReport entity = get(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}
	

	public BbsReport save(BbsReport bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsReport) - start"); //$NON-NLS-1$
		}

		getSession().save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsReport) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsReport deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		BbsReport entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	protected Class<BbsReport> getEntityClass() {
		return BbsReport.class;
	}

	

}