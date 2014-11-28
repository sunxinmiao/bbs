package com.jeecms.bbs.action.directive.abs;

import org.apache.log4j.Logger;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.jeecms.bbs.manager.BbsCommonMagicMng;
import com.jeecms.bbs.manager.BbsMagicConfigMng;
import com.jeecms.bbs.manager.BbsPostMng;
import com.jeecms.common.web.freemarker.DirectiveUtils;

import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 主题列表标签基类
 * 
 * @author liufang
 * 
 */
public abstract class AbstractPostPageDirective implements
		TemplateDirectiveModel {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(AbstractPostPageDirective.class);

	/**
	 * 输入参数，主题ID。
	 */
	public static final String PARAM_TOPIC_ID = "topicId";
	/**
	 * 输入参数，每页条数。
	 */
	public static final String PARAM_PER_COUNT = "preCount";
	/**
	 * 输入参数，发布者ID。
	 */
	public static final String PARAM_CREATER_ID = "createrId";
	/**
	 * 输入参数，个人样式。
	 */
	public static final String PARAM_POST_STYLE = "postStyle";
	/**
	 * 输入参数，状态。
	 */
	public static final String PARAM_STATUS = "status";
	/**
	 * 输入参数，排列顺序。0：按评论时间降序；1：按评论时间升序。默认降序。
	 */
	public static final String PARAM_ORDER_BY = "orderBy";

	protected Integer getTopicId(Map<String, TemplateModel> params)
			throws TemplateException {
		if (logger.isDebugEnabled()) {
			logger.debug("getTopicId(Map<String,TemplateModel>) - start"); //$NON-NLS-1$
		}

		Integer returnInteger = DirectiveUtils.getInt(PARAM_TOPIC_ID, params);
		if (logger.isDebugEnabled()) {
			logger.debug("getTopicId(Map<String,TemplateModel>) - end"); //$NON-NLS-1$
		}
		return returnInteger;
	}

	protected Integer getPreGount(Map<String, TemplateModel> params)
			throws TemplateException {
		if (logger.isDebugEnabled()) {
			logger.debug("getPreGount(Map<String,TemplateModel>) - start"); //$NON-NLS-1$
		}

		Integer returnInteger = DirectiveUtils.getInt(PARAM_PER_COUNT, params);
		if (logger.isDebugEnabled()) {
			logger.debug("getPreGount(Map<String,TemplateModel>) - end"); //$NON-NLS-1$
		}
		return returnInteger;
	}

	protected String getPostStyle(Map<String, TemplateModel> params)
			throws TemplateException {
		if (logger.isDebugEnabled()) {
			logger.debug("getPostStyle(Map<String,TemplateModel>) - start"); //$NON-NLS-1$
		}

		String returnString = DirectiveUtils.getString(PARAM_POST_STYLE, params);
		if (logger.isDebugEnabled()) {
			logger.debug("getPostStyle(Map<String,TemplateModel>) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	protected Integer getCreaterId(Map<String, TemplateModel> params)
			throws TemplateException {
		if (logger.isDebugEnabled()) {
			logger.debug("getCreaterId(Map<String,TemplateModel>) - start"); //$NON-NLS-1$
		}

		Integer returnInteger = DirectiveUtils.getInt(PARAM_CREATER_ID, params);
		if (logger.isDebugEnabled()) {
			logger.debug("getCreaterId(Map<String,TemplateModel>) - end"); //$NON-NLS-1$
		}
		return returnInteger;
	}

	protected Short getStatus(Map<String, TemplateModel> params)
			throws TemplateException {
		if (logger.isDebugEnabled()) {
			logger.debug("getStatus(Map<String,TemplateModel>) - start"); //$NON-NLS-1$
		}

		Short returnShort = DirectiveUtils.getShort(PARAM_STATUS, params);
		if (logger.isDebugEnabled()) {
			logger.debug("getStatus(Map<String,TemplateModel>) - end"); //$NON-NLS-1$
		}
		return returnShort;
	}

	protected boolean getDesc(Map<String, TemplateModel> params)
			throws TemplateException {
		if (logger.isDebugEnabled()) {
			logger.debug("getDesc(Map<String,TemplateModel>) - start"); //$NON-NLS-1$
		}

		Integer orderBy = DirectiveUtils.getInt(PARAM_ORDER_BY, params);
		if (orderBy == null || orderBy == 0) {
			if (logger.isDebugEnabled()) {
				logger.debug("getDesc(Map<String,TemplateModel>) - end"); //$NON-NLS-1$
			}
			return true;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("getDesc(Map<String,TemplateModel>) - end"); //$NON-NLS-1$
			}
			return false;
		}
	}

	@Autowired
	protected BbsPostMng bbsPostMng;
	@Autowired
	protected BbsMagicConfigMng magicConfigMng;
	@Autowired
	protected BbsCommonMagicMng magicMng;

}
