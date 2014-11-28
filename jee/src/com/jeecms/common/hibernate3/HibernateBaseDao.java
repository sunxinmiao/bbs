package com.jeecms.common.hibernate3;

import org.apache.log4j.Logger;

import static org.hibernate.EntityMode.POJO;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.util.Assert;

import com.jeecms.common.util.MyBeanUtils;

/**
 * hibernate DAO基类
 * 
 * 提供hql分页查询，拷贝更新等一些常用功能。
 * 
 * @author liufang
 * 
 * @param <T>
 *            entity class
 * @param <ID>
 *            entity id
 */
public abstract class HibernateBaseDao<T, ID extends Serializable> extends
		HibernateSimpleDao {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(HibernateBaseDao.class);

	/**
	 * @see Session.get(Class,Serializable)
	 * @param id
	 * @return 持久化对象
	 */
	protected T get(ID id) {
		if (logger.isDebugEnabled()) {
			logger.debug("get(ID) - start"); //$NON-NLS-1$
		}

		T returnT = get(id, false);
		if (logger.isDebugEnabled()) {
			logger.debug("get(ID) - end"); //$NON-NLS-1$
		}
		return returnT;
	}

	/**
	 * @see Session.get(Class,Serializable,LockMode)
	 * @param id
	 *            对象ID
	 * @param lock
	 *            是否锁定，使用LockMode.UPGRADE
	 * @return 持久化对象
	 */
	@SuppressWarnings("unchecked")
	protected T get(ID id, boolean lock) {
		if (logger.isDebugEnabled()) {
			logger.debug("get(ID, boolean) - start"); //$NON-NLS-1$
		}

		T entity;
		if (lock) {
			entity = (T) getSession().get(getEntityClass(), id,
					LockMode.UPGRADE);
		} else {
			entity = (T) getSession().get(getEntityClass(), id);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("get(ID, boolean) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	/**
	 * 按属性查找对象列表
	 */
	@SuppressWarnings("unchecked")
	protected List<T> findByProperty(String property, Object value) {
		if (logger.isDebugEnabled()) {
			logger.debug("findByProperty(String, Object) - start"); //$NON-NLS-1$
		}

		Assert.hasText(property);
		List<T> returnList = createCriteria(Restrictions.eq(property, value)).list();
		if (logger.isDebugEnabled()) {
			logger.debug("findByProperty(String, Object) - end"); //$NON-NLS-1$
		}
		return returnList;
	}

	/**
	 * 按属性查找唯一对象
	 */
	@SuppressWarnings("unchecked")
	protected T findUniqueByProperty(String property, Object value) {
		if (logger.isDebugEnabled()) {
			logger.debug("findUniqueByProperty(String, Object) - start"); //$NON-NLS-1$
		}

		Assert.hasText(property);
		Assert.notNull(value);
		T returnT = (T) createCriteria(Restrictions.eq(property, value)).uniqueResult();
		if (logger.isDebugEnabled()) {
			logger.debug("findUniqueByProperty(String, Object) - end"); //$NON-NLS-1$
		}
		return returnT;
	}

	/**
	 * 按属性统计记录数
	 * 
	 * @param property
	 * @param value
	 * @return
	 */
	protected int countByProperty(String property, Object value) {
		if (logger.isDebugEnabled()) {
			logger.debug("countByProperty(String, Object) - start"); //$NON-NLS-1$
		}

		Assert.hasText(property);
		Assert.notNull(value);
		int returnint = ((Number) (createCriteria(Restrictions.eq(property, value)).setProjection(Projections.rowCount()).uniqueResult())).intValue();
		if (logger.isDebugEnabled()) {
			logger.debug("countByProperty(String, Object) - end"); //$NON-NLS-1$
		}
		return returnint;
	}

	/**
	 * 按Criterion查询列表数据.
	 * 
	 * @param criterion
	 *            数量可变的Criterion.
	 */
	@SuppressWarnings("unchecked")
	protected List findByCriteria(Criterion... criterion) {
		if (logger.isDebugEnabled()) {
			logger.debug("findByCriteria(Criterion) - start"); //$NON-NLS-1$
		}

		List returnList = createCriteria(criterion).list();
		if (logger.isDebugEnabled()) {
			logger.debug("findByCriteria(Criterion) - end"); //$NON-NLS-1$
		}
		return returnList;
	}

	/**
	 * 通过Updater更新对象
	 * 
	 * @param updater
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T updateByUpdater(Updater<T> updater) {
		if (logger.isDebugEnabled()) {
			logger.debug("updateByUpdater(Updater<T>) - start"); //$NON-NLS-1$
		}

		ClassMetadata cm = sessionFactory.getClassMetadata(getEntityClass());
		T bean = updater.getBean();
		T po = (T) getSession().get(getEntityClass(),
				cm.getIdentifier(bean, POJO));
		updaterCopyToPersistentObject(updater, po, cm);

		if (logger.isDebugEnabled()) {
			logger.debug("updateByUpdater(Updater<T>) - end"); //$NON-NLS-1$
		}
		return po;
	}

	/**
	 * 将更新对象拷贝至实体对象，并处理many-to-one的更新。
	 * 
	 * @param updater
	 * @param po
	 */
	private void updaterCopyToPersistentObject(Updater<T> updater, T po,
			ClassMetadata cm) {
		if (logger.isDebugEnabled()) {
			logger.debug("updaterCopyToPersistentObject(Updater<T>, T, ClassMetadata) - start"); //$NON-NLS-1$
		}

		String[] propNames = cm.getPropertyNames();
		String identifierName = cm.getIdentifierPropertyName();
		T bean = updater.getBean();
		Object value;
		for (String propName : propNames) {
			if (propName.equals(identifierName)) {
				continue;
			}
			try {
				value = MyBeanUtils.getSimpleProperty(bean, propName);
				if (!updater.isUpdate(propName, value)) {
					continue;
				}
				cm.setPropertyValue(po, propName, value, POJO);
			} catch (Exception e) {
				logger.error("updaterCopyToPersistentObject(Updater<T>, T, ClassMetadata)", e); //$NON-NLS-1$

				throw new RuntimeException(
						"copy property to persistent object failed: '"
								+ propName + "'", e);
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("updaterCopyToPersistentObject(Updater<T>, T, ClassMetadata) - end"); //$NON-NLS-1$
		}
	}

	/**
	 * 根据Criterion条件创建Criteria,后续可进行更多处理,辅助函数.
	 */
	protected Criteria createCriteria(Criterion... criterions) {
		if (logger.isDebugEnabled()) {
			logger.debug("createCriteria(Criterion) - start"); //$NON-NLS-1$
		}

		Criteria criteria = getSession().createCriteria(getEntityClass());
		for (Criterion c : criterions) {
			criteria.add(c);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("createCriteria(Criterion) - end"); //$NON-NLS-1$
		}
		return criteria;
	}

	/**
	 * 获得Dao对于的实体类
	 * 
	 * @return
	 */
	abstract protected Class<T> getEntityClass();
}
