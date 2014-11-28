package com.jeecms.bbs.dao.impl;

import org.apache.log4j.Logger;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.bbs.dao.BbsCommonMagicDao;
import com.jeecms.bbs.entity.BbsCommonMagic;
import com.jeecms.common.hibernate3.Finder;
import com.jeecms.common.hibernate3.HibernateBaseDao;

@Repository
public class BbsCommonMagicDaoImpl extends
		HibernateBaseDao<BbsCommonMagic, Integer> implements BbsCommonMagicDao {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsCommonMagicDaoImpl.class);

	@Transactional(readOnly=true)
	public List<BbsCommonMagic> getList(){
		if (logger.isDebugEnabled()) {
			logger.debug("getList() - start"); //$NON-NLS-1$
		}

		String hql="from BbsCommonMagic magic order by displayorder";
		List<BbsCommonMagic> returnList = getSession().createQuery(hql).list();
		if (logger.isDebugEnabled()) {
			logger.debug("getList() - end"); //$NON-NLS-1$
		}
		return returnList;
	}
	@Transactional(readOnly=true)
	public BbsCommonMagic findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		BbsCommonMagic entity = get(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}
	@Transactional(readOnly=true)
	public BbsCommonMagic findByIdentifier(String mid) {
		if (logger.isDebugEnabled()) {
			logger.debug("findByIdentifier(String) - start"); //$NON-NLS-1$
		}

		String hql="from BbsCommonMagic magic where magic.identifier=:mid";
		Finder finder=Finder.create(hql).setParam("mid", mid);
		List<BbsCommonMagic>li=find(finder);
		if(li!=null&&li.size()>0){
			BbsCommonMagic returnBbsCommonMagic = li.get(0);
			if (logger.isDebugEnabled()) {
				logger.debug("findByIdentifier(String) - end"); //$NON-NLS-1$
			}
			return returnBbsCommonMagic;
		}else{
			if (logger.isDebugEnabled()) {
				logger.debug("findByIdentifier(String) - end"); //$NON-NLS-1$
			}
			return null;
		}
	}

	public BbsCommonMagic save(BbsCommonMagic bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsCommonMagic) - start"); //$NON-NLS-1$
		}

		getSession().save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(BbsCommonMagic) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public BbsCommonMagic deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		BbsCommonMagic entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	@Override
	protected Class<BbsCommonMagic> getEntityClass() {
		return BbsCommonMagic.class;
	}

}