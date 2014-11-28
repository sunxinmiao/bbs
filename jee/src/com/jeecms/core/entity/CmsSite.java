package com.jeecms.core.entity;

import org.apache.log4j.Logger;

import static com.jeecms.bbs.Constants.RES_PATH;
import static com.jeecms.bbs.Constants.TPL_BASE;
import static com.jeecms.bbs.Constants.UPLOAD_PATH;
import static com.jeecms.common.web.Constants.DEFAULT;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;

import com.jeecms.core.entity.base.BaseCmsSite;

public class CmsSite extends BaseCmsSite {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(CmsSite.class);

	private static final long serialVersionUID = 1L;

	/**
	 * 获得站点url
	 * 
	 * @return
	 */
	public String getUrl() {
		if (logger.isDebugEnabled()) {
			logger.debug("getUrl() - start"); //$NON-NLS-1$
		}

		if (getStaticIndex()) {
			String returnString = getUrlStatic();
			if (logger.isDebugEnabled()) {
				logger.debug("getUrl() - end"); //$NON-NLS-1$
			}
			return returnString;
		} else {
			String returnString = getUrlDynamic();
			if (logger.isDebugEnabled()) {
				logger.debug("getUrl() - end"); //$NON-NLS-1$
			}
			return returnString;
		}
	}

	/**
	 * 获得完整路径。在给其他网站提供客户端包含时有可以使用。
	 * 
	 * @return
	 */
	public String getUrlWhole() {
		if (logger.isDebugEnabled()) {
			logger.debug("getUrlWhole() - start"); //$NON-NLS-1$
		}

		if (getStaticIndex()) {
			String returnString = getUrlBuffer(false, true, false).append("/").toString();
			if (logger.isDebugEnabled()) {
				logger.debug("getUrlWhole() - end"); //$NON-NLS-1$
			}
			return returnString;
		} else {
			String returnString = getUrlBuffer(true, true, false).append("/").toString();
			if (logger.isDebugEnabled()) {
				logger.debug("getUrlWhole() - end"); //$NON-NLS-1$
			}
			return returnString;
		}
	}

	public String getUrlDynamic() {
		if (logger.isDebugEnabled()) {
			logger.debug("getUrlDynamic() - start"); //$NON-NLS-1$
		}

		String returnString = getUrlBuffer(true, null, false).append("/").toString();
		if (logger.isDebugEnabled()) {
			logger.debug("getUrlDynamic() - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	public String getUrlStatic() {
		if (logger.isDebugEnabled()) {
			logger.debug("getUrlStatic() - start"); //$NON-NLS-1$
		}

		String returnString = getUrlBuffer(false, null, true).append("/").toString();
		if (logger.isDebugEnabled()) {
			logger.debug("getUrlStatic() - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	public StringBuilder getUrlBuffer(boolean dynamic, Boolean whole,
			boolean forIndex) {
		if (logger.isDebugEnabled()) {
			logger.debug("getUrlBuffer(boolean, Boolean, boolean) - start"); //$NON-NLS-1$
		}

		boolean relative = whole != null ? !whole : getRelativePath();
		String ctx = getContextPath();
		StringBuilder url = new StringBuilder();
		if (!relative) {
			url.append(getProtocol()).append(getDomain());
			if (getPort() != null) {
				url.append(":").append(getPort());
			}
		}
		if (!StringUtils.isBlank(ctx)) {
			url.append(ctx);
		}
		if (dynamic) {
			String servlet = getServletPoint();
			if (!StringUtils.isBlank(servlet)) {
				url.append(servlet);
			}
		} else {
			if (!forIndex) {
				String staticDir = getStaticDir();
				if (!StringUtils.isBlank(staticDir)) {
					url.append(staticDir);
				}
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getUrlBuffer(boolean, Boolean, boolean) - end"); //$NON-NLS-1$
		}
		return url;
	}

	/**
	 * 获得模板路径。如：/WEB-INF/t/cms/www
	 * 
	 * @return
	 */
	public String getTplPath() {
		if (logger.isDebugEnabled()) {
			logger.debug("getTplPath() - start"); //$NON-NLS-1$
		}

		String returnString = TPL_BASE + "/" + getPath();
		if (logger.isDebugEnabled()) {
			logger.debug("getTplPath() - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * 获得模板方案路径。如：/WEB-INF/t/cms/www/default
	 * 
	 * @return
	 */
	public String getSolutionPath() {
		if (logger.isDebugEnabled()) {
			logger.debug("getSolutionPath() - start"); //$NON-NLS-1$
		}

		String returnString = TPL_BASE + "/" + getPath() + "/" + getTplSolution();
		if (logger.isDebugEnabled()) {
			logger.debug("getSolutionPath() - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * 获得模板资源路径。如：/r/cms/www
	 * 
	 * @return
	 */
	public String getResPath() {
		if (logger.isDebugEnabled()) {
			logger.debug("getResPath() - start"); //$NON-NLS-1$
		}

		String returnString = RES_PATH + "/" + getPath();
		if (logger.isDebugEnabled()) {
			logger.debug("getResPath() - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * 获得上传路径。如：/u/jeecms/path
	 * 
	 * @return
	 */
	public String getUploadPath() {
		if (logger.isDebugEnabled()) {
			logger.debug("getUploadPath() - start"); //$NON-NLS-1$
		}

		String returnString = UPLOAD_PATH + getPath();
		if (logger.isDebugEnabled()) {
			logger.debug("getUploadPath() - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * 获得上传访问前缀。
	 * 
	 * 根据配置识别上传至数据、FTP和本地
	 * 
	 * @return
	 */
	public String getUploadBase() {
		if (logger.isDebugEnabled()) {
			logger.debug("getUploadBase() - start"); //$NON-NLS-1$
		}

		CmsConfig config = getConfig();
		String ctx = config.getContextPath();
		if (config.getUploadToDb()) {
			if (!StringUtils.isBlank(ctx)) {
				String returnString = ctx + config.getDbFileUri();
				if (logger.isDebugEnabled()) {
					logger.debug("getUploadBase() - end"); //$NON-NLS-1$
				}
				return returnString;
			} else {
				String returnString = config.getDbFileUri();
				if (logger.isDebugEnabled()) {
					logger.debug("getUploadBase() - end"); //$NON-NLS-1$
				}
				return returnString;
			}
		} else if (getUploadFtp() != null) {
			String returnString = getUploadFtp().getUrl();
			if (logger.isDebugEnabled()) {
				logger.debug("getUploadBase() - end"); //$NON-NLS-1$
			}
			return returnString;
		} else {
			if (!StringUtils.isBlank(ctx)) {
				if (logger.isDebugEnabled()) {
					logger.debug("getUploadBase() - end"); //$NON-NLS-1$
				}
				return ctx;
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("getUploadBase() - end"); //$NON-NLS-1$
				}
				return "";
			}
		}
	}

	public String getServletPoint() {
		if (logger.isDebugEnabled()) {
			logger.debug("getServletPoint() - start"); //$NON-NLS-1$
		}

		CmsConfig config = getConfig();
		if (config != null) {
			String returnString = config.getServletPoint();
			if (logger.isDebugEnabled()) {
				logger.debug("getServletPoint() - end"); //$NON-NLS-1$
			}
			return returnString;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("getServletPoint() - end"); //$NON-NLS-1$
			}
			return null;
		}
	}

	public String getContextPath() {
		if (logger.isDebugEnabled()) {
			logger.debug("getContextPath() - start"); //$NON-NLS-1$
		}

		CmsConfig config = getConfig();
		if (config != null) {
			String returnString = config.getContextPath();
			if (logger.isDebugEnabled()) {
				logger.debug("getContextPath() - end"); //$NON-NLS-1$
			}
			return returnString;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("getContextPath() - end"); //$NON-NLS-1$
			}
			return null;
		}
	}

	public Integer getPort() {
		if (logger.isDebugEnabled()) {
			logger.debug("getPort() - start"); //$NON-NLS-1$
		}

		CmsConfig config = getConfig();
		if (config != null) {
			Integer returnInteger = config.getPort();
			if (logger.isDebugEnabled()) {
				logger.debug("getPort() - end"); //$NON-NLS-1$
			}
			return returnInteger;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("getPort() - end"); //$NON-NLS-1$
			}
			return null;
		}
	}

	public String getDefImg() {
		if (logger.isDebugEnabled()) {
			logger.debug("getDefImg() - start"); //$NON-NLS-1$
		}

		CmsConfig config = getConfig();
		if (config != null) {
			String returnString = config.getDefImg();
			if (logger.isDebugEnabled()) {
				logger.debug("getDefImg() - end"); //$NON-NLS-1$
			}
			return returnString;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("getDefImg() - end"); //$NON-NLS-1$
			}
			return null;
		}
	}

	public String getLoginUrl() {
		if (logger.isDebugEnabled()) {
			logger.debug("getLoginUrl() - start"); //$NON-NLS-1$
		}

		CmsConfig config = getConfig();
		if (config != null) {
			String returnString = config.getLoginUrl();
			if (logger.isDebugEnabled()) {
				logger.debug("getLoginUrl() - end"); //$NON-NLS-1$
			}
			return returnString;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("getLoginUrl() - end"); //$NON-NLS-1$
			}
			return null;
		}
	}

	public String getProcessUrl() {
		if (logger.isDebugEnabled()) {
			logger.debug("getProcessUrl() - start"); //$NON-NLS-1$
		}

		CmsConfig config = getConfig();
		if (config != null) {
			String returnString = config.getProcessUrl();
			if (logger.isDebugEnabled()) {
				logger.debug("getProcessUrl() - end"); //$NON-NLS-1$
			}
			return returnString;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("getProcessUrl() - end"); //$NON-NLS-1$
			}
			return null;
		}
	}

	public static Integer[] fetchIds(Collection<CmsSite> sites) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchIds(Collection<CmsSite>) - start"); //$NON-NLS-1$
		}

		if (sites == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("fetchIds(Collection<CmsSite>) - end"); //$NON-NLS-1$
			}
			return null;
		}
		Integer[] ids = new Integer[sites.size()];
		int i = 0;
		for (CmsSite s : sites) {
			ids[i++] = s.getId();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("fetchIds(Collection<CmsSite>) - end"); //$NON-NLS-1$
		}
		return ids;
	}

	public void init() {
		if (logger.isDebugEnabled()) {
			logger.debug("init() - start"); //$NON-NLS-1$
		}

		if (StringUtils.isBlank(getProtocol())) {
			setProtocol("http://");
		}
		if (StringUtils.isBlank(getTplSolution())) {
			setTplSolution(DEFAULT);
		}
		if (getFinalStep() == null) {
			byte step = 2;
			setFinalStep(step);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("init() - end"); //$NON-NLS-1$
		}
	}

	/* [CONSTRUCTOR MARKER BEGIN] */
	public CmsSite() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public CmsSite(java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public CmsSite(java.lang.Integer id,
			com.jeecms.core.entity.CmsConfig config, java.lang.String domain,
			java.lang.String path, java.lang.String name,
			java.lang.String protocol, java.lang.String dynamicSuffix,
			java.lang.String staticSuffix, java.lang.Boolean indexToRoot,
			java.lang.Boolean staticIndex, java.lang.String localeAdmin,
			java.lang.String localeFront, java.lang.String tplSolution,
			java.lang.Byte finalStep, java.lang.Byte afterCheck,
			java.lang.Boolean relativePath, java.lang.Boolean resycleOn) {

		super(id, config, domain, path, name, protocol, dynamicSuffix,
				staticSuffix, indexToRoot, staticIndex, localeAdmin,
				localeFront, tplSolution, finalStep, afterCheck, relativePath,
				resycleOn);
	}

	/* [CONSTRUCTOR MARKER END] */

}