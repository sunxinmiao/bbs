package com.jeecms.common.hibernate3;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.impl.CriteriaImpl;
import org.hibernate.transform.ResultTransformer;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.MyBeanUtils;

/**
 * hibernate DAO基类
 * 
 * 提供hql分页查询，不带泛型，与具体实体类无关。
 * 
 * @author liufang
 * 
 */
public abstract class HibernateSimpleDao {
	/**
	 * 日志，可用于子类
	 */
	protected Logger logger = Logger.getLogger(getClass());
	/**
	 * HIBERNATE 的 order 属性
	 */
	protected static final String ORDER_ENTRIES = "orderEntries";

	/**
	 * 通过HQL查询对象列表
	 * 
	 * @param hql
	 *            hql语句
	 * @param values
	 *            数量可变的参数
	 */
	@SuppressWarnings("unchecked")
	protected List find(String hql, Object... values) {
		if (logger.isDebugEnabled()) {
			logger.debug("find(String, Object) - start"); //$NON-NLS-1$
		}

		List returnList = createQuery(hql, values).list();
		if (logger.isDebugEnabled()) {
			logger.debug("find(String, Object) - end"); //$NON-NLS-1$
		}
	return returnList;
	}

	/**
	 * 通过HQL查询唯一对象
	 */
	protected Object findUnique(String hql, Object... values) {
		if (logger.isDebugEnabled()) {
			logger.debug("findUnique(String, Object) - start"); //$NON-NLS-1$
		}

		Object returnObject = createQuery(hql, values).setMaxResults(1).uniqueResult();
		if (logger.isDebugEnabled()) {
			logger.debug("findUnique(String, Object) - end"); //$NON-NLS-1$
		}
		return returnObject;
	}

	/**
	 * 通过Finder获得分页数据
	 * 
	 * @param finder
	 *            Finder对象
	 * @param pageNo
	 *            页码
	 * @param pageSize
	 *            每页条数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected Pagination find(Finder finder, int pageNo, int pageSize) {
		if (logger.isDebugEnabled()) {
			logger.debug("find(Finder, int, int) - start"); //$NON-NLS-1$
		}

		int totalCount = countQueryResult(finder);
		Pagination p = new Pagination(pageNo, pageSize, totalCount);
		if (totalCount < 1) {
			p.setList(new ArrayList());

			if (logger.isDebugEnabled()) {
				logger.debug("find(Finder, int, int) - end"); //$NON-NLS-1$
			}
			return p;
		}
		Query query = getSession().createQuery(finder.getOrigHql());
		finder.setParamsToQuery(query);
		query.setFirstResult(p.getFirstResult());
		query.setMaxResults(p.getPageSize());
		if (finder.isCacheable()) {
			query.setCacheable(true);
		}
		List list = query.list();
		p.setList(list);

		if (logger.isDebugEnabled()) {
			logger.debug("find(Finder, int, int) - end"); //$NON-NLS-1$
		}
		return p;
	}

	/**
	 * 通过Finder获得列表数据
	 * 
	 * @param finder
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected List find(Finder finder) {
		if (logger.isDebugEnabled()) {
			logger.debug("find(Finder) - start"); //$NON-NLS-1$
		}

		Query query = finder.createQuery(getSession());
		List list = query.list();

		if (logger.isDebugEnabled()) {
			logger.debug("find(Finder) - end"); //$NON-NLS-1$
		}
		return list;
	}

	/**
	 * 根据查询函数与参数列表创建Query对象,后续可进行更多处理,辅助函数.
	 */
	protected Query createQuery(String queryString, Object... values) {
		if (logger.isDebugEnabled()) {
			logger.debug("createQuery(String, Object) - start"); //$NON-NLS-1$
		}

		Assert.hasText(queryString);
		Query queryObject = getSession().createQuery(queryString);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				queryObject.setParameter(i, values[i]);
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("createQuery(String, Object) - end"); //$NON-NLS-1$
		}
		return queryObject;
	}

	/**
	 * 通过Criteria获得分页数据
	 * 
	 * @param crit
	 * @param pageNo
	 * @param pageSize
	 * @param projection
	 * @param orders
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected Pagination findByCriteria(Criteria crit, int pageNo, int pageSize) {
		if (logger.isDebugEnabled()) {
			logger.debug("findByCriteria(Criteria, int, int) - start"); //$NON-NLS-1$
		}

		CriteriaImpl impl = (CriteriaImpl) crit;
		// 先把Projection、ResultTransformer、OrderBy取出来,清空三者后再执行Count操作
		Projection projection = impl.getProjection();
		ResultTransformer transformer = impl.getResultTransformer();
		List<CriteriaImpl.OrderEntry> orderEntries;
		try {
			orderEntries = (List) MyBeanUtils
					.getFieldValue(impl, ORDER_ENTRIES);
			MyBeanUtils.setFieldValue(impl, ORDER_ENTRIES, new ArrayList());
		} catch (Exception e) {
			logger.error("findByCriteria(Criteria, int, int)", e); //$NON-NLS-1$

			throw new RuntimeException(
					"cannot read/write 'orderEntries' from CriteriaImpl", e);
		}

		int totalCount = ((Number) crit.setProjection(Projections.rowCount())
				.uniqueResult()).intValue();
		Pagination p = new Pagination(pageNo, pageSize, totalCount);
		if (totalCount < 1) {
			p.setList(new ArrayList());

			if (logger.isDebugEnabled()) {
				logger.debug("findByCriteria(Criteria, int, int) - end"); //$NON-NLS-1$
			}
			return p;
		}

		// 将之前的Projection,ResultTransformer和OrderBy条件重新设回去
		crit.setProjection(projection);
		if (projection == null) {
			crit.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		}
		if (transformer != null) {
			crit.setResultTransformer(transformer);
		}
		try {
			MyBeanUtils.setFieldValue(impl, ORDER_ENTRIES, orderEntries);
		} catch (Exception e) {
			logger.error("findByCriteria(Criteria, int, int)", e); //$NON-NLS-1$

			throw new RuntimeException(
					"set 'orderEntries' to CriteriaImpl faild", e);
		}
		crit.setFirstResult(p.getFirstResult());
		crit.setMaxResults(p.getPageSize());
		p.setList(crit.list());

		if (logger.isDebugEnabled()) {
			logger.debug("findByCriteria(Criteria, int, int) - end"); //$NON-NLS-1$
		}
		return p;
	}

	/**
	 * 获得Finder的记录总数
	 * 
	 * @param finder
	 * @return
	 */
	protected int countQueryResult(Finder finder) {
		if (logger.isDebugEnabled()) {
			logger.debug("countQueryResult(Finder) - start"); //$NON-NLS-1$
		}

		Query query = getSession().createQuery(finder.getRowCountHql());
		finder.setParamsToQuery(query);
		if (finder.isCacheable()) {
			query.setCacheable(true);
		}
		int returnint = ((Number) query.iterate().next()).intValue();
		if (logger.isDebugEnabled()) {
			logger.debug("countQueryResult(Finder) - end"); //$NON-NLS-1$
		}
		return returnint;
	}

	protected SessionFactory sessionFactory;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected Session getSession() {
		if (logger.isDebugEnabled()) {
			logger.debug("getSession() - start"); //$NON-NLS-1$
		}

		Session returnSession = sessionFactory.getCurrentSession();
		if (logger.isDebugEnabled()) {
			logger.debug("getSession() - end"); //$NON-NLS-1$
		}
		return returnSession;
	}
}
