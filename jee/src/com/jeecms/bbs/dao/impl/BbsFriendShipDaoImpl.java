package com.jeecms.bbs.dao.impl;

import org.apache.log4j.Logger;

import static com.jeecms.bbs.entity.BbsFriendShip.ACCEPT;
import static com.jeecms.bbs.entity.BbsFriendShip.APPLYING;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.jeecms.bbs.dao.BbsFriendShipDao;
import com.jeecms.bbs.entity.BbsFriendShip;
import com.jeecms.common.hibernate3.Finder;
import com.jeecms.common.hibernate3.HibernateBaseDao;
import com.jeecms.common.page.Pagination;

@Repository
public class BbsFriendShipDaoImpl extends
		HibernateBaseDao<BbsFriendShip, Integer> implements BbsFriendShipDao {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsFriendShipDaoImpl.class);

	public BbsFriendShip findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		BbsFriendShip entity = get(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public BbsFriendShip save(BbsFriendShip bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsFriendShip) - start"); //$NON-NLS-1$
		}

		getSession().save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsFriendShip) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsFriendShip deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		BbsFriendShip entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}
	
	public BbsFriendShip getFriendShip(Integer userId, Integer friendId) {
		if (logger.isDebugEnabled()) {
			logger.debug("getFriendShip(Integer, Integer) - start"); //$NON-NLS-1$
		}

		String hql = "from BbsFriendShip bean where bean.user.id=:userId and bean.friend.id=:friendId";
		Finder f = Finder.create(hql).setParam("userId", userId).setParam("friendId", friendId);
		f.setMaxResults(1);
		Query query = f.createQuery(getSession());
		BbsFriendShip returnBbsFriendShip = (BbsFriendShip) query.uniqueResult();
		if (logger.isDebugEnabled()) {
			logger.debug("getFriendShip(Integer, Integer) - end"); //$NON-NLS-1$
		}
		return returnBbsFriendShip;
	}
	
	public Pagination getPageByUserId(Integer userId, Integer pageNo, Integer pageSize) {
		if (logger.isDebugEnabled()) {
			logger.debug("getPageByUserId(Integer, Integer, Integer) - start"); //$NON-NLS-1$
		}

		String hql = "from BbsFriendShip bean where bean.user.id=:userId and bean.status="+ACCEPT;
		Finder f = Finder.create(hql).setParam("userId", userId);
		Pagination returnPagination = find(f, pageNo, pageSize);
		if (logger.isDebugEnabled()) {
			logger.debug("getPageByUserId(Integer, Integer, Integer) - end"); //$NON-NLS-1$
		}
		return returnPagination;
	}
	
	public Pagination getApplyByUserId(Integer userId, Integer pageNo, Integer pageSize) {
		if (logger.isDebugEnabled()) {
			logger.debug("getApplyByUserId(Integer, Integer, Integer) - start"); //$NON-NLS-1$
		}

		String hql = "from BbsFriendShip bean where bean.friend.id=:userId and bean.status="+APPLYING;
		Finder f = Finder.create(hql).setParam("userId", userId);
		Pagination returnPagination = find(f, pageNo, pageSize);
		if (logger.isDebugEnabled()) {
			logger.debug("getApplyByUserId(Integer, Integer, Integer) - end"); //$NON-NLS-1$
		}
		return returnPagination;
	}

	@Override
	protected Class<BbsFriendShip> getEntityClass() {
		return BbsFriendShip.class;
	}
}