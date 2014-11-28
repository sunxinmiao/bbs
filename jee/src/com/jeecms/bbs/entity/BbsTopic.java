package com.jeecms.bbs.entity;

import org.apache.log4j.Logger;

import static com.jeecms.bbs.web.FrontUtils.replaceSensitivity;
import static com.jeecms.bbs.Constants.DAY_MILLIS;

import java.sql.Timestamp;
import java.util.Date;

import com.jeecms.bbs.entity.base.BaseBbsTopic;

public class BbsTopic extends BaseBbsTopic {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbsTopic.class);

	private static final long serialVersionUID = 1L;
	/**
	 * 正常状态
	 */
	public static final short NORMAL = 0;
	/**
	 * 屏蔽状态
	 */
	public static final short SHIELD = -1;
	/**
	 * 锁定状态
	 */
	public static final short LOCKED = 1;
	
	
	/**
	 * 普通帖
	 */
	public static final short TOPIC_NORMAL = 100;
	
	/**
	 * 投票帖
	 */
	public static final short TOPIC_VOTE = 101;
	
	/**
	 * 前台状态
	 * 
	 * @return 3:锁;2:旧;1:新
	 */
	public short getFrontStatus() {
		if (logger.isDebugEnabled()) {
			logger.debug("getFrontStatus() - start"); //$NON-NLS-1$
		}

		if (isLocked()) {
			if (logger.isDebugEnabled()) {
				logger.debug("getFrontStatus() - end"); //$NON-NLS-1$
			}
			return 3;
		} else if (System.currentTimeMillis() - getLastTime().getTime() > DAY_MILLIS) {
			if (logger.isDebugEnabled()) {
				logger.debug("getFrontStatus() - end"); //$NON-NLS-1$
			}
			return 2;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("getFrontStatus() - end"); //$NON-NLS-1$
			}
			return 1;
		}
	}

	/**
	 * 是否热帖
	 * 
	 * @return
	 */
	public boolean isHot() {
		if (logger.isDebugEnabled()) {
			logger.debug("isHot() - start"); //$NON-NLS-1$
		}

		boolean returnboolean = getReplyCount() >= 30;
		if (logger.isDebugEnabled()) {
			logger.debug("isHot() - end"); //$NON-NLS-1$
		}
		return returnboolean;
	}

	/**
	 * 是否锁定
	 * 
	 * @return
	 */
	public boolean isLocked() {
		if (logger.isDebugEnabled()) {
			logger.debug("isLocked() - start"); //$NON-NLS-1$
		}

		boolean returnboolean = getStatus() == LOCKED || getForum().isTopicLock(getCreateTime().getTime());
		if (logger.isDebugEnabled()) {
			logger.debug("isLocked() - end"); //$NON-NLS-1$
		}
		return returnboolean;
	}

	/**
	 * 是否屏蔽
	 * 
	 * @return
	 */
	public boolean isShield() {
		if (logger.isDebugEnabled()) {
			logger.debug("isShield() - start"); //$NON-NLS-1$
		}

		boolean returnboolean = getStatus() == SHIELD;
		if (logger.isDebugEnabled()) {
			logger.debug("isShield() - end"); //$NON-NLS-1$
		}
		return returnboolean;
	}

	/**
	 * 样式是否有效
	 * 
	 * @return
	 */
	public boolean isStyle() {
		if (logger.isDebugEnabled()) {
			logger.debug("isStyle() - start"); //$NON-NLS-1$
		}

		Date d = getStyleTime();
		if (d == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("isStyle() - end"); //$NON-NLS-1$
			}
			return true;
		}
		long time = d.getTime();
		boolean returnboolean = time - System.currentTimeMillis() > 0;
		if (logger.isDebugEnabled()) {
			logger.debug("isStyle() - end"); //$NON-NLS-1$
		}
		return returnboolean;
	}

	public String getUrl() {
		if (logger.isDebugEnabled()) {
			logger.debug("getUrl() - start"); //$NON-NLS-1$
		}

		String returnString = getSite().getUrlBuffer(true, null, false).append("/").append(getForum().getPath()).append("/").append(getId()).append(getSite().getDynamicSuffix()).toString();
		if (logger.isDebugEnabled()) {
			logger.debug("getUrl() - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * 获得访问路径前缀。如：http://bbs.jeecms.com/luntan/2
	 * 
	 * @return
	 */
	public StringBuilder getUrlPerfix() {
		if (logger.isDebugEnabled()) {
			logger.debug("getUrlPerfix() - start"); //$NON-NLS-1$
		}

		StringBuilder returnStringBuilder = getSite().getUrlBuffer(true, null, false).append("/").append(getForum().getPath()).append("/").append(getId());
		if (logger.isDebugEnabled()) {
			logger.debug("getUrlPerfix() - end"); //$NON-NLS-1$
		}
		return returnStringBuilder;
	}

	public String getRedirectUrl() {
		if (logger.isDebugEnabled()) {
			logger.debug("getRedirectUrl() - start"); //$NON-NLS-1$
		}

		String path = getForum().getPath();
		String url = "/" + path + "/" + getId() + getSite().getDynamicSuffix();

		if (logger.isDebugEnabled()) {
			logger.debug("getRedirectUrl() - end"); //$NON-NLS-1$
		}
		return url;
	}

	public void init() {
		if (logger.isDebugEnabled()) {
			logger.debug("init() - start"); //$NON-NLS-1$
		}

		Date now = new Timestamp(System.currentTimeMillis());
		if (getCreateTime() == null) {
			setCreateTime(now);
		}
		if (getLastTime() == null) {
			setLastTime(now);
		}
		if (getPrimeLevel() == null) {
			setPrimeLevel(NORMAL);
		}
		if (getSortTime() == null) {
			setSortTime(now);
		}
		if (getViewCount() == null) {
			setViewCount(0L);
		}
		if (getReplyCount() == null) {
			setReplyCount(0);
		}
		if (getTopLevel() == null) {
			setTopLevel(NORMAL);
		}
		if (getStyleBold() == null) {
			setStyleBold(false);
		}
		if (getStatus() == null) {
			setStatus(NORMAL);
		}
		if (getModeratorReply() == null) {
			setModeratorReply(false);
		}
		if(getAllayReply()==null){
			setAllayReply(true);
		}
		if(getHasSofeed()==null){
			setHasSofeed(false);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("init() - end"); //$NON-NLS-1$
		}
	}
	
	public String getTitle(){
		if (logger.isDebugEnabled()) {
			logger.debug("getTitle() - start"); //$NON-NLS-1$
		}

		BbsTopicText text = getTopicText();
		if (text == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("getTitle() - end"); //$NON-NLS-1$
			}
			return null;
		} else {
			String returnString = replaceSensitivity(text.getTitle());
			if (logger.isDebugEnabled()) {
				logger.debug("getTitle() - end"); //$NON-NLS-1$
			}
			return returnString;
		}
	}
	
	public void setTitle(String title) {
		if (logger.isDebugEnabled()) {
			logger.debug("setTitle(String) - start"); //$NON-NLS-1$
		}

		BbsTopicText text = getTopicText();
		if (text != null) {
			text.setTitle(title);
		} 

		if (logger.isDebugEnabled()) {
			logger.debug("setTitle(String) - end"); //$NON-NLS-1$
		}
	}
	public void setTopicTitle(String title) {
		if (logger.isDebugEnabled()) {
			logger.debug("setTopicTitle(String) - start"); //$NON-NLS-1$
		}

		super.setTitle(title);

		if (logger.isDebugEnabled()) {
			logger.debug("setTopicTitle(String) - end"); //$NON-NLS-1$
		}
	}
	
	
	public Integer getCategory(){
		return TOPIC_NORMAL;
	}
	
	/* [CONSTRUCTOR MARKER BEGIN] */
	public BbsTopic () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsTopic (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsTopic (
		java.lang.Integer id,
		com.jeecms.core.entity.CmsSite site,
		com.jeecms.bbs.entity.BbsForum forum,
		com.jeecms.bbs.entity.BbsUser creater,
		com.jeecms.bbs.entity.BbsUser lastReply,
		java.util.Date createTime,
		java.util.Date lastTime,
		java.util.Date sortTime,
		java.lang.Long viewCount,
		java.lang.Integer replyCount,
		java.lang.Short topLevel,
		java.lang.Short primeLevel,
		java.lang.Boolean styleBold,
		java.lang.Boolean styleItalic,
		java.lang.Short status,
		java.lang.Boolean affix,
		java.lang.Boolean moderatorReply) {

		super (
			id,
			site,
			forum,
			creater,
			lastReply,
			createTime,
			lastTime,
			sortTime,
			viewCount,
			replyCount,
			topLevel,
			primeLevel,
			styleBold,
			styleItalic,
			status,
			affix,
			moderatorReply);
	}
	/* [CONSTRUCTOR MARKER END] */

}