package com.jeecms.bbs.entity;

import org.apache.log4j.Logger;

import static com.jeecms.bbs.Constants.DAY_MILLIS;
import static com.jeecms.common.web.Constants.INDEX;

import org.apache.commons.lang.StringUtils;

import com.jeecms.bbs.entity.base.BaseBbsForum;

public class BbsForum extends BaseBbsForum {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsForum.class);

	private static final long serialVersionUID = 1L;

	public void init() {
		if (logger.isDebugEnabled()) {
			logger.debug("init() - start"); //$NON-NLS-1$
		}

		if (getPointPrime() == null) {
			setPointPrime(0);
		}
		if (getPointReply() == null) {
			setPointReply(0);
		}
		if (getPointTopic() == null) {
			setPointTopic(0);
		}
		if (getPostToday() == null) {
			setPostToday(0);
		}
		if (getPostTotal() == null) {
			setPostTotal(0);
		}
		if (getTopicLockLimit() == null) {
			setTopicLockLimit(0);
		}
		if (getTopicTotal() == null) {
			setTopicTotal(0);
		}
		if(getPrestigeAvailable()==null){
			setPrestigeAvailable(true);
		}
		if(getPointAvailable()==null){
			setPointAvailable(true);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("init() - end"); //$NON-NLS-1$
		}
	}

	/**
	 * 获得访问路径。如：http://bbs.jeecms.com/luntan/index.htm
	 * 
	 * @return
	 */
	public String getUrl() {
		if (logger.isDebugEnabled()) {
			logger.debug("getUrl() - start"); //$NON-NLS-1$
		}

		String url = getOuterUrl();
		if (!StringUtils.isBlank(url)) {
			// 外部链接
			if (url.startsWith("http://")) {
				if (logger.isDebugEnabled()) {
					logger.debug("getUrl() - end"); //$NON-NLS-1$
				}
				return url;
			} else if (url.startsWith("/")) {
				String returnString = getSite().getUrl() + url;
				if (logger.isDebugEnabled()) {
					logger.debug("getUrl() - end"); //$NON-NLS-1$
				}
				return returnString;
			} else {
				String returnString = "http://" + url;
				if (logger.isDebugEnabled()) {
					logger.debug("getUrl() - end"); //$NON-NLS-1$
				}
				return returnString;
			}
		} else {
			String returnString = getSite().getUrlBuffer(true, null, false).append("/").append(getPath()).append("/").append(INDEX).append(getSite().getDynamicSuffix()).toString();
			if (logger.isDebugEnabled()) {
				logger.debug("getUrl() - end"); //$NON-NLS-1$
			}
			return returnString;
		}
	}

	public String getRedirectUrl() {
		if (logger.isDebugEnabled()) {
			logger.debug("getRedirectUrl() - start"); //$NON-NLS-1$
		}

		String url = "/" + getPath() + "/" + INDEX
				+ getSite().getDynamicSuffix();

		if (logger.isDebugEnabled()) {
			logger.debug("getRedirectUrl() - end"); //$NON-NLS-1$
		}
		return url;
	}

	/**
	 * 是否锁定主题
	 * 
	 * @param time
	 *            主题发表时间
	 * @return
	 */
	public boolean isTopicLock(long time) {
		if (logger.isDebugEnabled()) {
			logger.debug("isTopicLock(long) - start"); //$NON-NLS-1$
		}

		if (getTopicLockLimit() == 0) {
			if (logger.isDebugEnabled()) {
				logger.debug("isTopicLock(long) - end"); //$NON-NLS-1$
			}
			return false;
		}
		boolean returnboolean = System.currentTimeMillis() - time > getTopicLockLimit() * DAY_MILLIS;
		if (logger.isDebugEnabled()) {
			logger.debug("isTopicLock(long) - end"); //$NON-NLS-1$
		}
		return returnboolean;
	}

	/* [CONSTRUCTOR MARKER BEGIN] */
	public BbsForum() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsForum(java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsForum(java.lang.Integer id,
			com.jeecms.bbs.entity.BbsCategory category,
			com.jeecms.core.entity.CmsSite site, java.lang.String path,
			java.lang.String title, java.lang.Integer topicLockLimit,
			java.lang.Integer priority, java.lang.Integer topicTotal,
			java.lang.Integer postTotal, java.lang.Integer postToday,
			java.lang.Integer pointTopic, java.lang.Integer pointReply,
			java.lang.Integer pointPrime,
			java.lang.Integer moneyTopic,
			java.lang.Integer moneyReply,
			java.lang.Integer moneyPrime) {

		super(id, category, site, path, title, topicLockLimit, priority,
				topicTotal, postTotal, postToday, pointTopic, pointReply,
				pointPrime,moneyTopic,moneyReply,moneyPrime);
	}

	/* [CONSTRUCTOR MARKER END] */

}