package com.jeecms.core.manager.impl;

import org.apache.log4j.Logger;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.hibernate3.Updater;
import com.jeecms.core.dao.CmsSiteDao;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.manager.CmsSiteMng;
import com.jeecms.core.manager.FtpMng;

@Service
@Transactional
public class CmsSiteMngImpl implements CmsSiteMng {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(CmsSiteMngImpl.class);

//	private static final Logger log = LoggerFactory
//			.getLogger(CmsSiteMngImpl.class);

	@Transactional(readOnly = true)
	public List<CmsSite> getList() {
		if (logger.isDebugEnabled()) {
			logger.debug("getList() - start"); //$NON-NLS-1$
		}

		List<CmsSite> returnList = dao.getList(false);
		if (logger.isDebugEnabled()) {
			logger.debug("getList() - end"); //$NON-NLS-1$
		}
		return returnList;
	}

	@Transactional(readOnly = true)
	public List<CmsSite> getListFromCache() {
		if (logger.isDebugEnabled()) {
			logger.debug("getListFromCache() - start"); //$NON-NLS-1$
		}

		List<CmsSite> returnList = dao.getList(true);
		if (logger.isDebugEnabled()) {
			logger.debug("getListFromCache() - end"); //$NON-NLS-1$
		}
		return returnList;
	}

	@Transactional(readOnly = true)
	public CmsSite findByDomain(String domain, boolean cacheable) {
		if (logger.isDebugEnabled()) {
			logger.debug("findByDomain(String, boolean) - start"); //$NON-NLS-1$
		}

		CmsSite returnCmsSite = dao.findByDomain(domain, cacheable);
		if (logger.isDebugEnabled()) {
			logger.debug("findByDomain(String, boolean) - end"); //$NON-NLS-1$
		}
		return returnCmsSite;
	}

	@Transactional(readOnly = true)
	public CmsSite findById(Integer id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - start"); //$NON-NLS-1$
		}

		CmsSite entity = dao.findById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public CmsSite update(CmsSite bean, Integer uploadFtpId) {
		if (logger.isDebugEnabled()) {
			logger.debug("update(CmsSite, Integer) - start"); //$NON-NLS-1$
		}

		CmsSite entity = findById(bean.getId());
		if (uploadFtpId != null) {
			entity.setUploadFtp(ftpMng.findById(uploadFtpId));
		} else {
			entity.setUploadFtp(null);
		}
		Updater<CmsSite> updater = new Updater<CmsSite>(bean);
		entity = dao.updateByUpdater(updater);

		if (logger.isDebugEnabled()) {
			logger.debug("update(CmsSite, Integer) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public void updateTplSolution(Integer siteId, String solution) {
		if (logger.isDebugEnabled()) {
			logger.debug("updateTplSolution(Integer, String) - start"); //$NON-NLS-1$
		}

		CmsSite site = findById(siteId);
		site.setTplSolution(solution);

		if (logger.isDebugEnabled()) {
			logger.debug("updateTplSolution(Integer, String) - end"); //$NON-NLS-1$
		}
	}

	private FtpMng ftpMng;
	private CmsSiteDao dao;

	@Autowired
	public void setFtpMng(FtpMng ftpMng) {
		this.ftpMng = ftpMng;
	}

	@Autowired
	public void setDao(CmsSiteDao dao) {
		this.dao = dao;
	}

}