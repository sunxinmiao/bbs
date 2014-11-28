package com.jeecms.bbs.dao.impl;

import org.apache.log4j.Logger;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.jeecms.bbs.dao.BbsTopicDao;
import com.jeecms.bbs.entity.BbsTopic;
import com.jeecms.common.hibernate3.Finder;
import com.jeecms.common.hibernate3.HibernateBaseDao;
import com.jeecms.common.page.Pagination;

@Repository
public class BbsTopicDaoImpl extends HibernateBaseDao<BbsTopic, Integer>
		implements BbsTopicDao {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsTopicDaoImpl.class);

	@SuppressWarnings("unchecked")
	public List<BbsTopic> getTopTopic(Integer webId, Integer ctgId,
			Integer forumId) {
		if (logger.isDebugEnabled()) {
			logger.debug("getTopTopic(Integer, Integer, Integer) - start"); //$NON-NLS-1$
		}

		Finder f = Finder.create("from BbsTopic bean where 1=1");
		f.append(" and bean.website.id=:webId").setParam("webId", webId);
		f.append(" and bean.status>=").append(String.valueOf(BbsTopic.NORMAL));
		f.append(" and (bean.topLevel=3 ");
		f.append(" or (bean.topLevel=2 and bean.category.id=:ctgId)");
		f.setParam("ctgId", ctgId);
		f.append(" or (bean.topLevel=1 and bean.forum.id=:forumId))");
		f.setParam("forumId", forumId);
		f.append(" order by bean.topLevel desc");
		List<BbsTopic> returnList = find(f);
		if (logger.isDebugEnabled()) {
			logger.debug("getTopTopic(Integer, Integer, Integer) - end"); //$NON-NLS-1$
		}
		return returnList;
	}
	
	public Pagination getForTag(Integer siteId, Integer forumId,Integer parentPostTypeId, Integer postTypeId, Short status,
			Short primeLevel, String keyWords, String creater,
			Integer createrId, Short topLevel, int pageNo, int pageSize,String jinghua) {
		if (logger.isDebugEnabled()) {
			logger.debug("getForTag(Integer, Integer, Integer, Integer, Short, Short, String, String, Integer, Short, int, int, String) - start"); //$NON-NLS-1$
		}

		Finder f = Finder.create("select bean from BbsTopic bean where 1=1");
		f.append(" and bean.site.id=:siteId").setParam("siteId", siteId);
		if (forumId != null) {
			f.append(" and bean.forum.id=:forumId")
					.setParam("forumId", forumId);
		}
		if(parentPostTypeId!=null){
			f.append(" and bean.postType.parent.id=:parentPostTypeId")
			.setParam("parentPostTypeId", parentPostTypeId);
		}
		if(postTypeId!=null){
			f.append(" and bean.postType.id=:postTypeId")
			.setParam("postTypeId", postTypeId);
		}
		if (status == null) {
			status = 0;
		}
		f.append(" and bean.status>=:status").setParam("status", status);
		if (topLevel != null) {
			if (topLevel != 0) {
				f.append(" and bean.topLevel>=:topLevel").setParam("topLevel",
						topLevel);
			} else {
				f.append(" and bean.topLevel=0");
			}
		}
		if (primeLevel != null) {
			f.append(" and bean.primeLevel >=:primeLevel").setParam(
					"primeLevel", primeLevel);
		}
		if (!StringUtils.isBlank(keyWords)) {
			f.append(" and bean.topicText.title like :keyWords").setParam("keyWords",
					"%" + keyWords + "%");
		}
		if (!StringUtils.isBlank(creater)) {
			f.append(" and bean.creater.username like :creater").setParam(
					"creater", "%" + creater + "%");
		}
		if (createrId != null) {
			f.append(" and bean.creater.id =:createrId").setParam("createrId",
					createrId);
		}
		if("index_jing".equals(jinghua)){
			f.append(" and bean.primeLevel != 0");
		}
		f.append(" order by bean.topLevel desc,bean.sortTime desc");
		Pagination returnPagination = find(f, pageNo, pageSize);
		if (logger.isDebugEnabled()) {
			logger.debug("getForTag(Integer, Integer, Integer, Integer, Short, Short, String, String, Integer, Short, int, int, String) - end"); //$NON-NLS-1$
		}
		return returnPagination;
	}

	public Pagination getForSearchDate(Integer siteId, Integer forumId,
			Short primeLevel, Integer day, int pageNo, int pageSize) {
		if (logger.isDebugEnabled()) {
			logger.debug("getForSearchDate(Integer, Integer, Short, Integer, int, int) - start"); //$NON-NLS-1$
		}

		Finder f = Finder.create("select bean from BbsTopic bean where 1=1");
		f.append(" and bean.site.id=:siteId").setParam("siteId", siteId);
		if (forumId != null) {
			f.append(" and bean.forum.id=:forumId")
					.setParam("forumId", forumId);
		}
		f.append(" and bean.status>=0");
		if (day != null) {
			Calendar now = Calendar.getInstance();
			now.setTime(new Date(System.currentTimeMillis()));
			now.add(Calendar.DATE, -day);
			f.append(" and bean.createTime>:day")
					.setParam("day", now.getTime());
		}
		if (primeLevel != null) {
			f.append(" and bean.primeLevel >=:primeLevel").setParam(
					"primeLevel", primeLevel);
		}
		f.append(" order by bean.topLevel desc,bean.sortTime desc");
		Pagination returnPagination = find(f, pageNo, pageSize);
		if (logger.isDebugEnabled()) {
			logger.debug("getForSearchDate(Integer, Integer, Short, Integer, int, int) - end"); //$NON-NLS-1$
		}
		return returnPagination;
	}

	public Pagination getTopicByTime(Integer siteId, int pageNo, int pageSize) {
		if (logger.isDebugEnabled()) {
			logger.debug("getTopicByTime(Integer, int, int) - start"); //$NON-NLS-1$
		}

		Finder f = Finder.create("select bean from BbsTopic bean where 1=1");
		f.append(" and bean.site.id=:siteId").setParam("siteId", siteId);
		f.append(" order by bean.lastTime desc");
		Pagination returnPagination = find(f, pageNo, pageSize);
		if (logger.isDebugEnabled()) {
			logger.debug("getTopicByTime(Integer, int, int) - end"); //$NON-NLS-1$
		}
		return returnPagination;
	}

	public Pagination getMemberTopic(Integer siteId, Integer userId,
			int pageNo, int pageSize) {
		if (logger.isDebugEnabled()) {
			logger.debug("getMemberTopic(Integer, Integer, int, int) - start"); //$NON-NLS-1$
		}

		Finder f = Finder.create("select bean from BbsTopic bean where 1=1");
		f.append(" and bean.site.id=:siteId").setParam("siteId", siteId);
		f.append(" and bean.creater.id=:userId");
		f.setParam("userId", userId);
		f.append(" order by bean.lastTime desc");
		Pagination returnPagination = find(f, pageNo, pageSize);
		if (logger.isDebugEnabled()) {
			logger.debug("getMemberTopic(Integer, Integer, int, int) - end"); //$NON-NLS-1$
		}
		return returnPagination;
	}

	public Pagination getMemberReply(Integer siteId, Integer userId,
			int pageNo, int pageSize) {
		if (logger.isDebugEnabled()) {
			logger.debug("getMemberReply(Integer, Integer, int, int) - start"); //$NON-NLS-1$
		}

		Finder f = Finder.create("select bean from BbsTopic bean where 1=1");
		f.append(" and bean.site.id=:siteId").setParam("siteId", siteId);
		f.append(" and bean.haveReply like :userId").setParam("userId",
				"%," + userId + ",%");
		f.append(" order by bean.sortTime desc");
		Pagination returnPagination = find(f, pageNo, pageSize);
		if (logger.isDebugEnabled()) {
			logger.debug("getMemberReply(Integer, Integer, int, int) - end"); //$NON-NLS-1$
		}
		return returnPagination;
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

	public BbsTopic findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		BbsTopic entity = get(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public BbsTopic save(BbsTopic bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsTopic) - start"); //$NON-NLS-1$
		}

		getSession().save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsTopic) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsTopic deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		BbsTopic entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	@Override
	protected Class<BbsTopic> getEntityClass() {
		return BbsTopic.class;
	}

	@SuppressWarnings("unchecked")
	public List<BbsTopic> getListByForumId(Integer forumId) {
		if (logger.isDebugEnabled()) {
			logger.debug("getListByForumId(Integer) - start"); //$NON-NLS-1$
		}

		String hql = "from BbsTopic bean where bean.forum.id=:forumId ";
		List<BbsTopic> returnList = getSession().createQuery(hql).setInteger("forumId", forumId).setCacheable(false).list();
		if (logger.isDebugEnabled()) {
			logger.debug("getListByForumId(Integer) - end"); //$NON-NLS-1$
		}
		return returnList;
	}

	@SuppressWarnings("unchecked")
	public List<BbsTopic> getNewList(Integer count) {
		if (logger.isDebugEnabled()) {
			logger.debug("getNewList(Integer) - start"); //$NON-NLS-1$
		}

		String hql = "from BbsTopic bean order by bean.createTime desc";
		List<BbsTopic> returnList = getSession().createQuery(hql).setFirstResult(0).setMaxResults(count).setCacheable(false).list();
		if (logger.isDebugEnabled()) {
			logger.debug("getNewList(Integer) - end"); //$NON-NLS-1$
		}
		return returnList;
	}

}