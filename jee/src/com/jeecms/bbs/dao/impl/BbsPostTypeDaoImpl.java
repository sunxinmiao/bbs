package com.jeecms.bbs.dao.impl;

import org.apache.log4j.Logger;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jeecms.bbs.dao.BbsPostTypeDao;
import com.jeecms.bbs.entity.BbsPostType;
import com.jeecms.common.hibernate3.Finder;
import com.jeecms.common.hibernate3.HibernateBaseDao;
import com.jeecms.common.page.Pagination;
@Repository
public class BbsPostTypeDaoImpl extends HibernateBaseDao<BbsPostType, Integer>
		implements BbsPostTypeDao {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsPostTypeDaoImpl.class);

	public Pagination getPage(Integer siteId,Integer forumId,Integer parentId, int pageNo, int pageSize) {
		if (logger.isDebugEnabled()) {
			logger.debug("getPage(Integer, Integer, Integer, int, int) - start"); //$NON-NLS-1$
		}

		String hql = "from BbsPostType postType where 1=1 ";
		Finder finder = Finder.create(hql);
		if (siteId != null) {
			finder.append("  and postType.site.id=:siteId").setParam("siteId",
					siteId);
		}
		if(forumId!=null){
			finder.append("  and postType.forum.id=:forumId").setParam("forumId",
					forumId);
		}
		if(parentId!=null){
			finder.append("  and postType.parent.id=:parentId").setParam("parentId",
					parentId);
		}else{
			finder.append("  and postType.parent is null");
		}
		finder.append(" order by postType.priority asc");
		Pagination returnPagination = find(finder, pageNo, pageSize);
		if (logger.isDebugEnabled()) {
			logger.debug("getPage(Integer, Integer, Integer, int, int) - end"); //$NON-NLS-1$
		}
		return returnPagination;
	}
	
	public List getList(Integer siteId,Integer forumId,Integer parentId) {
		if (logger.isDebugEnabled()) {
			logger.debug("getList(Integer, Integer, Integer) - start"); //$NON-NLS-1$
		}

		String hql = "from BbsPostType postType ";
		Finder finder = Finder.create(hql);
		if (siteId != null) {
			finder.append(" where postType.site.id=:siteId").setParam("siteId",
					siteId);
		}
		if(forumId!=null){
			finder.append("  and postType.forum.id=:forumId").setParam("forumId",
					forumId);
		}
		if(parentId!=null){
			finder.append("  and postType.parent.id=:parentId").setParam("parentId",
					parentId);
		}else{
			finder.append("  and postType.parent is null");
		}
		finder.append(" order by postType.priority asc");
		List returnList = find(finder);
		if (logger.isDebugEnabled()) {
			logger.debug("getList(Integer, Integer, Integer) - end"); //$NON-NLS-1$
		}
		return returnList;
	}

	public BbsPostType findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		BbsPostType entity = get(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public BbsPostType save(BbsPostType bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsPostType) - start"); //$NON-NLS-1$
		}

		getSession().save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsPostType) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsPostType deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		BbsPostType entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	@Override
	protected Class<BbsPostType> getEntityClass() {
		return BbsPostType.class;
	}
}