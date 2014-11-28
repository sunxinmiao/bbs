package com.jeecms.bbs.dao.impl;

import org.apache.log4j.Logger;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.jeecms.bbs.dao.BbsUserDao;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.common.hibernate3.Finder;
import com.jeecms.common.hibernate3.HibernateBaseDao;
import com.jeecms.common.page.Pagination;

@Repository
public class BbsUserDaoImpl extends HibernateBaseDao<BbsUser, Integer>
		implements BbsUserDao {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsUserDaoImpl.class);

	public Pagination getPage(String username, String email, Integer groupId,
			Boolean disabled, Boolean admin, Long pointMin,
			Long pointMax, Long prestigeMin, Long prestigeMax,
			Integer orderBy, int pageNo, int pageSize) {
		if (logger.isDebugEnabled()) {
			logger.debug("getPage(String, String, Integer, Boolean, Boolean, Long, Long, Long, Long, Integer, int, int) - start"); //$NON-NLS-1$
		}

		Finder f = Finder.create("select bean from BbsUser bean");
		if (orderBy != null && !orderBy.equals(0)) {
			f.append(" left join bean.userOnline online ");
		}
		f.append(" where 1=1");
		if (!StringUtils.isBlank(username)) {
			f.append(" and bean.username like :username");
			f.setParam("username", "%" + username + "%");
		}
		if (!StringUtils.isBlank(email)) {
			f.append(" and bean.email like :email");
			f.setParam("email", "%" + email + "%");
		}
		if (groupId != null) {
			f.append(" and bean.group.id=:groupId");
			f.setParam("groupId", groupId);
		}
		if (disabled != null) {
			f.append(" and bean.disabled=:disabled");
			f.setParam("disabled", disabled);
		}
		if (admin != null) {
			f.append(" and bean.admin=:admin");
			f.setParam("admin", admin);
		}
		if(pointMin!=null){
			f.append(" and bean.point>=:pointMin");
			f.setParam("pointMin", pointMin);
		}
		if(pointMax!=null){
			f.append(" and bean.point<=:pointMax");
			f.setParam("pointMax", pointMax);
		}
		if(prestigeMin!=null){
			f.append(" and bean.prestige>=:prestigeMin");
			f.setParam("prestigeMin", prestigeMin);
		}
		if(prestigeMax!=null){
			f.append(" and bean.prestige<=:prestigeMax");
			f.setParam("prestigeMax", prestigeMax);
		}
		if (orderBy != null && !orderBy.equals(0)) {
			if (orderBy.equals(1)) {
				/**
				 * 今日活跃度降序
				 */
				f.append(" order by online.onlineDay desc");
			} else if (orderBy.equals(2)) {
				/**
				 * 今日活跃度升序
				 */
				f.append(" order by online.onlineDay asc");
			} else if (orderBy.equals(3)) {
				/**
				 * 本周活跃度降序
				 */
				f.append(" order by online.onlineWeek desc");
			} else if (orderBy.equals(4)) {
				/**
				 * 本周活跃度升序
				 */
				f.append(" order by online.onlineWeek asc");
			} else if (orderBy.equals(5)) {
				/**
				 * 本月活跃度降序
				 */
				f.append(" order by online.onlineMonth desc");
			} else if (orderBy.equals(6)) {
				/**
				 * 本月活跃度升序
				 */
				f.append(" order by online.onlineMonth asc");
			} else if (orderBy.equals(7)) {
				/**
				 * 本年活跃度降序
				 */
				f.append(" order by online.onlineYear desc");
			} else if (orderBy.equals(8)) {
				/**
				 * 本年活跃度升序
				 */
				f.append(" order by online.onlineYear asc");
			} else if (orderBy.equals(9)) {
				/**
				 * 积分降序
				 */
				f.append(" order by bean.point desc");
			} else if (orderBy.equals(10)) {
				/**
				 * 积分升序
				 */
				f.append(" order by bean.point asc");
			} else if (orderBy.equals(11)) {
				/**
				 * 威望降序
				 */
				f.append(" order by bean.prestige desc");
			} else if (orderBy.equals(12)) {
				/**
				 * 威望升序
				 */
				f.append(" order by bean.prestige asc");
			}
		} else {
			/**
			 * 没有指定排序方式
			 */
			f.append(" order by bean.id desc");
		}
		Pagination returnPagination = find(f, pageNo, pageSize);
		if (logger.isDebugEnabled()) {
			logger.debug("getPage(String, String, Integer, Boolean, Boolean, Long, Long, Long, Long, Integer, int, int) - end"); //$NON-NLS-1$
		}
		return returnPagination;
	}
	
	@SuppressWarnings("unchecked")
	public List<BbsUser> getSuggestMember(String username, Integer count) {
		if (logger.isDebugEnabled()) {
			logger.debug("getSuggestMember(String, Integer) - start"); //$NON-NLS-1$
		}

		Finder f = Finder.create("select bean from BbsUser bean where bean.username like :username and bean.admin=false");
		f.setParam("username", username+"%");
		Query query = f.createQuery(getSession());
		query.setMaxResults(count);
		List<BbsUser> returnList = query.list();
		if (logger.isDebugEnabled()) {
			logger.debug("getSuggestMember(String, Integer) - end"); //$NON-NLS-1$
		}
		return returnList;
	}

	@SuppressWarnings("unchecked")
	public List<BbsUser> getAdminList(Integer siteId, Boolean allChannel,
			Boolean disabled, Integer rank) {
		if (logger.isDebugEnabled()) {
			logger.debug("getAdminList(Integer, Boolean, Boolean, Integer) - start"); //$NON-NLS-1$
		}

		Finder f = Finder.create("select bean from BbsUser");
		f.append(" bean join bean.userSites us");
		f.append(" where us.site.id=:siteId");
		f.setParam("siteId", siteId);
		if (allChannel != null) {
			f.append(" and us.allChannel=:allChannel");
			f.setParam("allChannel", allChannel);
		}
		if (disabled != null) {
			f.append(" and bean.disabled=:disabled");
			f.setParam("disabled", disabled);
		}
		if (rank != null) {
			f.append(" and bean.rank<=:rank");
			f.setParam("rank", rank);
		}
		f.append(" and bean.admin=true");
		f.append(" order by bean.id asc");
		List<BbsUser> returnList = find(f);
		if (logger.isDebugEnabled()) {
			logger.debug("getAdminList(Integer, Boolean, Boolean, Integer) - end"); //$NON-NLS-1$
		}
		return returnList;
	}

	public BbsUser findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		BbsUser entity = get(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public BbsUser findByUsername(String username) {
		if (logger.isDebugEnabled()) {
			logger.debug("findByUsername(String) - start"); //$NON-NLS-1$
		}

		BbsUser returnBbsUser = findUniqueByProperty("username", username);
		if (logger.isDebugEnabled()) {
			logger.debug("findByUsername(String) - end"); //$NON-NLS-1$
		}
		return returnBbsUser;
	}
	
	public int countByUsername(String username) {
		if (logger.isDebugEnabled()) {
			logger.debug("countByUsername(String) - start"); //$NON-NLS-1$
		}

		String hql = "select count(*) from BbsUser bean where bean.username=:username";
		Query query = getSession().createQuery(hql);
		query.setParameter("username", username);
		int returnint = ((Number) query.iterate().next()).intValue();
		if (logger.isDebugEnabled()) {
			logger.debug("countByUsername(String) - end"); //$NON-NLS-1$
		}
		return returnint;
	}

	public int countByEmail(String email) {
		if (logger.isDebugEnabled()) {
			logger.debug("countByEmail(String) - start"); //$NON-NLS-1$
		}

		String hql = "select count(*) from BbsUser bean where bean.email=:email";
		Query query = getSession().createQuery(hql);
		query.setParameter("email", email);
		int returnint = ((Number) query.iterate().next()).intValue();
		if (logger.isDebugEnabled()) {
			logger.debug("countByEmail(String) - end"); //$NON-NLS-1$
		}
		return returnint;
	}

	public BbsUser save(BbsUser bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsUser) - start"); //$NON-NLS-1$
		}

		getSession().save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsUser) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsUser deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		BbsUser entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	@Override
	protected Class<BbsUser> getEntityClass() {
		return BbsUser.class;
	}
}