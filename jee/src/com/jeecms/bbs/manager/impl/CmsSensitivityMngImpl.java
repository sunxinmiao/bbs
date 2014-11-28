package com.jeecms.bbs.manager.impl;

import org.apache.log4j.Logger;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.bbs.dao.CmsSensitivityDao;
import com.jeecms.bbs.entity.CmsSensitivity;
import com.jeecms.bbs.manager.CmsSensitivityMng;
import com.jeecms.core.manager.CmsSiteMng;

@Service
@Transactional
public class CmsSensitivityMngImpl implements CmsSensitivityMng {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(CmsSensitivityMngImpl.class);

	@Transactional(readOnly = true)
	public String replaceSensitivity(Integer siteId, String s) {
		if (logger.isDebugEnabled()) {
			logger.debug("replaceSensitivity(Integer, String) - start"); //$NON-NLS-1$
		}

		if (StringUtils.isBlank(s)) {
			if (logger.isDebugEnabled()) {
				logger.debug("replaceSensitivity(Integer, String) - end"); //$NON-NLS-1$
			}
			return s;
		}
		List<CmsSensitivity> list = getList(siteId, true);
		for (CmsSensitivity sen : list) {
			s = StringUtils.replace(s, sen.getSearch(), sen.getReplacement());
		}

		if (logger.isDebugEnabled()) {
			logger.debug("replaceSensitivity(Integer, String) - end"); //$NON-NLS-1$
		}
		return s;
	}

	@Transactional(readOnly = true)
	public List<CmsSensitivity> getList(Integer siteId, boolean cacheable) {
		if (logger.isDebugEnabled()) {
			logger.debug("getList(Integer, boolean) - start"); //$NON-NLS-1$
		}

		List<CmsSensitivity> returnList = dao.getList(siteId, cacheable);
		if (logger.isDebugEnabled()) {
			logger.debug("getList(Integer, boolean) - end"); //$NON-NLS-1$
		}
		return returnList;
	}

	@Transactional(readOnly = true)
	public CmsSensitivity findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		CmsSensitivity entity = dao.findById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public CmsSensitivity save(CmsSensitivity bean, Integer siteId) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(CmsSensitivity, Integer) - start"); //$NON-NLS-1$
		}

		bean.setSite(cmsSiteMng.findById(siteId));
		dao.save(bean);

		if (logger.isDebugEnabled()) {
			logger.debug("save(CmsSensitivity, Integer) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public void updateEnsitivity(Integer[] ids, String[] searchs,
			String[] replacements) {
		if (logger.isDebugEnabled()) {
			logger.debug("updateEnsitivity(Integer[], String[], String[]) - start"); //$NON-NLS-1$
		}

		CmsSensitivity ensitivity;
		for (int i = 0, len = ids.length; i < len; i++) {
			ensitivity = findById(ids[i]);
			ensitivity.setSearch(searchs[i]);
			ensitivity.setReplacement(replacements[i]);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("updateEnsitivity(Integer[], String[], String[]) - end"); //$NON-NLS-1$
		}
	}

	public CmsSensitivity deleteById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - start"); //$NON-NLS-1$
		}

		CmsSensitivity bean = dao.deleteById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(Integer) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public CmsSensitivity[] deleteByIds(Integer[] ids) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(Integer[]) - start"); //$NON-NLS-1$
		}

		CmsSensitivity[] beans = new CmsSensitivity[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(Integer[]) - end"); //$NON-NLS-1$
		}
		return beans;
	}

	private CmsSensitivityDao dao;
	
	private CmsSiteMng cmsSiteMng;

	@Autowired
	public void setDao(CmsSensitivityDao dao) {
		this.dao = dao;
	}

	@Autowired
	public void setCmsSiteMng(CmsSiteMng cmsSiteMng) {
		this.cmsSiteMng = cmsSiteMng;
	}
	
	
}