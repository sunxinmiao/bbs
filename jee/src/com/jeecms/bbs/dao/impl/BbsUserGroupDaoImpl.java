package com.jeecms.bbs.dao.impl;

import org.apache.log4j.Logger;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.jeecms.bbs.dao.BbsUserGroupDao;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.entity.BbsUserGroup;
import com.jeecms.common.hibernate3.HibernateBaseDao;
import com.jeecms.common.page.Pagination;

@Repository
public class BbsUserGroupDaoImpl extends
		HibernateBaseDao<BbsUserGroup, Integer> implements BbsUserGroupDao {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsUserGroupDaoImpl.class);

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

	@SuppressWarnings("unchecked")
	public List<BbsUserGroup> getList(Integer siteId) {
		if (logger.isDebugEnabled()) {
			logger.debug("getList(Integer) - start"); //$NON-NLS-1$
		}

		String hql = "select bean from BbsUserGroup bean where bean.site.id=? order by bean.type, bean.point";
		List<BbsUserGroup> returnList = find(hql, siteId);
		if (logger.isDebugEnabled()) {
			logger.debug("getList(Integer) - end"); //$NON-NLS-1$
		}
		return returnList;
	}

	public BbsUserGroup getRegDef() {
		if (logger.isDebugEnabled()) {
			logger.debug("getRegDef() - start"); //$NON-NLS-1$
		}

		String hql = "select bean from BbsUserGroup bean where bean.default=true";
		BbsUserGroup returnBbsUserGroup = (BbsUserGroup) findUnique(hql);
		if (logger.isDebugEnabled()) {
			logger.debug("getRegDef() - end"); //$NON-NLS-1$
		}
		return returnBbsUserGroup;
	}

	public BbsUserGroup findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		BbsUserGroup entity = get(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public BbsUserGroup save(BbsUserGroup bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsUserGroup) - start"); //$NON-NLS-1$
		}

		getSession().save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsUserGroup) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsUserGroup deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		BbsUserGroup entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	@Override
	protected Class<BbsUserGroup> getEntityClass() {
		return BbsUserGroup.class;
	}

	/* (non-Javadoc)
	 * @see com.jeecms.bbs.dao.BbsUserGroupDao#findNearByPoint()
	 */
	public BbsUserGroup findNearByPoint(Long point) {
		if (logger.isDebugEnabled()) {
			logger.debug("findNearByPoint(Long) - start"); //$NON-NLS-1$
		}

		// TODO Auto-generated method stub
		String hql = "from BbsUserGroup bean where bean.point <= :point order by bean.point desc ";
		List<BbsUserGroup> groups = getSession().createQuery(hql).setLong("point", point).setCacheable(false).list();
		BbsUserGroup group = null;
		if(groups.get(0).getPoint()==0){
			String hql1 = "from BbsUserGroup bean where bean.default = 1";
			group = (BbsUserGroup)getSession().createQuery(hql1).uniqueResult();
		}else{
			group = groups.get(0);
		}
		

		if (logger.isDebugEnabled()) {
			logger.debug("findNearByPoint(Long) - end"); //$NON-NLS-1$
		}
		return group;
	}
}