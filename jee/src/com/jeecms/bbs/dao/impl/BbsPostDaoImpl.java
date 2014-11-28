package com.jeecms.bbs.dao.impl;

import org.apache.log4j.Logger;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.jeecms.bbs.dao.BbsPostDao;
import com.jeecms.bbs.entity.BbsPost;
import com.jeecms.common.hibernate3.Finder;
import com.jeecms.common.hibernate3.HibernateBaseDao;
import com.jeecms.common.page.Pagination;

@Repository
public class BbsPostDaoImpl extends HibernateBaseDao<BbsPost, Integer>
		implements BbsPostDao {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsPostDaoImpl.class);

	public Pagination getForTag(Integer siteId, Integer topicId,
			Integer userId, int pageNo, int pageSize) {
		if (logger.isDebugEnabled()) {
			logger.debug("getForTag(Integer, Integer, Integer, int, int) - start"); //$NON-NLS-1$
		}

		Finder f = Finder.create("select bean from BbsPost bean where 1=1");
		f.append(" and bean.site.id=:siteId").setParam("siteId", siteId);
		if (topicId != null) {
			f.append(" and bean.topic.id=:topicId")
					.setParam("topicId", topicId);
		}
		if (userId != null) {
			f.append(" and bean.creater.id=:userId");
			f.setParam("userId", userId);
		}
		f.append(" order by bean.indexCount");
		Pagination returnPagination = find(f, pageNo, pageSize);
		if (logger.isDebugEnabled()) {
			logger.debug("getForTag(Integer, Integer, Integer, int, int) - end"); //$NON-NLS-1$
		}
		return returnPagination;
	}

	@SuppressWarnings("unchecked")
	public List<Number> getMemberReply(Integer webId, Integer memberId,
			int pageNo, int pageSize) {
		if (logger.isDebugEnabled()) {
			logger.debug("getMemberReply(Integer, Integer, int, int) - start"); //$NON-NLS-1$
		}

		Finder f = Finder
				.create("select max(bean.id) from BbsPost bean where bean.indexCount>1");
		f.append(" and bean.site.id=:webId").setParam("webId", webId);
		f.append(" and bean.creater.id=:memberId").setParam("memberId",
				memberId);
		f.append(" group by bean.topic.id order by max(bean.id) desc");
		f.setFirstResult(pageNo);
		f.setMaxResults(pageSize);
		List<Number> returnList = find(f);
		if (logger.isDebugEnabled()) {
			logger.debug("getMemberReply(Integer, Integer, int, int) - end"); //$NON-NLS-1$
		}
		return returnList;
	}

	@SuppressWarnings("unchecked")
	public List<BbsPost> getPostByTopic(Integer topicId) {
		if (logger.isDebugEnabled()) {
			logger.debug("getPostByTopic(Integer) - start"); //$NON-NLS-1$
		}

		String hql = "select bean from BbsPost bean where bean.topic.id=?";
		List<BbsPost> returnList = find(hql, topicId);
		if (logger.isDebugEnabled()) {
			logger.debug("getPostByTopic(Integer) - end"); //$NON-NLS-1$
		}
		return returnList;
	}

	public BbsPost getLastPost(Integer forumId, Integer topicId) {
		if (logger.isDebugEnabled()) {
			logger.debug("getLastPost(Integer, Integer) - start"); //$NON-NLS-1$
		}

		String hql = "select bean from BbsPost bean where bean.topic.id!=? and bean.topic.forum.id=? order by bean.createTime desc";
		BbsPost returnBbsPost = (BbsPost) findUnique(hql, topicId, forumId);
		if (logger.isDebugEnabled()) {
			logger.debug("getLastPost(Integer, Integer) - end"); //$NON-NLS-1$
		}
		return returnBbsPost;
	}

	public int getMemberReplyCount(Integer webId, Integer memberId) {
		if (logger.isDebugEnabled()) {
			logger.debug("getMemberReplyCount(Integer, Integer) - start"); //$NON-NLS-1$
		}

		StringBuilder f = new StringBuilder(
				"select count(DISTINCT bean.topic.id) from BbsPost bean where bean.indexCount>1");
		f.append(" and bean.site.id=:webId");
		f.append(" and bean.creater.id=:memberId");
		int returnint = ((Number) getSession().createQuery(f.toString()).setParameter("webId", webId).setParameter("memberId", memberId).iterate().next()).intValue();
		if (logger.isDebugEnabled()) {
			logger.debug("getMemberReplyCount(Integer, Integer) - end"); //$NON-NLS-1$
		}
		return returnint;
	}

	public int getIndexCount(Integer topicId) {
		if (logger.isDebugEnabled()) {
			logger.debug("getIndexCount(Integer) - start"); //$NON-NLS-1$
		}

		String hql = "select max(bean.indexCount) from BbsPost bean where bean.topic.id=:topicId";
		int returnint = ((Number) getSession().createQuery(hql).setParameter("topicId", topicId).iterate().next()).intValue() + 1;
		if (logger.isDebugEnabled()) {
			logger.debug("getIndexCount(Integer) - end"); //$NON-NLS-1$
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

	public BbsPost findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		BbsPost entity = get(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public BbsPost save(BbsPost bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsPost) - start"); //$NON-NLS-1$
		}

		getSession().save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsPost) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsPost deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		BbsPost entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	@Override
	protected Class<BbsPost> getEntityClass() {
		return BbsPost.class;
	}

	public void deleleByForumId(Integer forumId) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleleByForumId(Integer) - start"); //$NON-NLS-1$
		}

		String hql = "delete BbsPost bean where bean.topic.forum.id=:forumId ";
		getSession().createQuery(hql).setInteger("forumId", forumId);

		if (logger.isDebugEnabled()) {
			logger.debug("deleleByForumId(Integer) - end"); //$NON-NLS-1$
		}
	}

}