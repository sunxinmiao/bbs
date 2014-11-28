package com.jeecms.bbs.action.directive.abs;

import org.apache.log4j.Logger;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.jeecms.bbs.manager.BbsTopicMng;
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
public abstract class AbstractTopicPageDirective implements
		TemplateDirectiveModel {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(AbstractTopicPageDirective.class);

	/**
	 * 输入参数，版块ID。
	 */
	public static final String PARAM_FORUM_ID = "forumId";
	/**
	 * 输入参数，级别精华。
	 */
	public static final String PARAM_PRIME_LEVEL = "primeLevel";
	/**
	 * 输入参数，置顶级别。
	 */
	public static final String PARAM_TOP_LEVEL = "topLevel";
	/**
	 * 输入参数，发布者。
	 */
	public static final String PARAM_CREATER = "creater";
	/**
	 * 输入参数，发布者ID。
	 */
	public static final String PARAM_CREATER_ID = "createrId";
	/**
	 * 输入参数，关键字。
	 */
	public static final String PARAM_KEY_WORDS = "keyWords";
	/**
	 * 输入参数，状态。
	 */
	public static final String PARAM_STATUS = "status";
	/**
	 * 输入参数，帖子小类。
	 */
	public static final String PARAM_POSTTYPE_ID = "typeId";
	/**
	 * 输入参数，帖子大类。
	 */
	public static final String PARAM_PARENT_POSTTYPE_ID = "parentTypeId";
	/**
	 * 输入参数，排列顺序。0：按评论时间降序；1：按评论时间升序。默认降序。
	 */
	public static final String PARAM_ORDER_BY = "orderBy";

	protected Integer getForumId(Map<String, TemplateModel> params)
			throws TemplateException {
		if (logger.isDebugEnabled()) {
			logger.debug("getForumId(Map<String,TemplateModel>) - start"); //$NON-NLS-1$
		}

		Integer returnInteger = DirectiveUtils.getInt(PARAM_FORUM_ID, params);
		if (logger.isDebugEnabled()) {
			logger.debug("getForumId(Map<String,TemplateModel>) - end"); //$NON-NLS-1$
		}
		return returnInteger;
	}

	protected String getFindType(Map<String, TemplateModel> params)
			throws TemplateException {
		if (logger.isDebugEnabled()) {
			logger.debug("getFindType(Map<String,TemplateModel>) - start"); //$NON-NLS-1$
		}

		String returnString = DirectiveUtils.getString("findType", params);
		if (logger.isDebugEnabled()) {
			logger.debug("getFindType(Map<String,TemplateModel>) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	protected Short getPrimeLevel(Map<String, TemplateModel> params)
			throws TemplateException {
		if (logger.isDebugEnabled()) {
			logger.debug("getPrimeLevel(Map<String,TemplateModel>) - start"); //$NON-NLS-1$
		}

		Short returnShort = DirectiveUtils.getShort(PARAM_PRIME_LEVEL, params);
		if (logger.isDebugEnabled()) {
			logger.debug("getPrimeLevel(Map<String,TemplateModel>) - end"); //$NON-NLS-1$
		}
		return returnShort;
	}

	protected Short getTopLevel(Map<String, TemplateModel> params)
			throws TemplateException {
		if (logger.isDebugEnabled()) {
			logger.debug("getTopLevel(Map<String,TemplateModel>) - start"); //$NON-NLS-1$
		}

		Short returnShort = DirectiveUtils.getShort(PARAM_TOP_LEVEL, params);
		if (logger.isDebugEnabled()) {
			logger.debug("getTopLevel(Map<String,TemplateModel>) - end"); //$NON-NLS-1$
		}
		return returnShort;
	}

	protected String getCreater(Map<String, TemplateModel> params)
			throws TemplateException {
		if (logger.isDebugEnabled()) {
			logger.debug("getCreater(Map<String,TemplateModel>) - start"); //$NON-NLS-1$
		}

		String returnString = DirectiveUtils.getString(PARAM_CREATER, params);
		if (logger.isDebugEnabled()) {
			logger.debug("getCreater(Map<String,TemplateModel>) - end"); //$NON-NLS-1$
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

	protected String getKeyWords(Map<String, TemplateModel> params)
			throws TemplateException {
		if (logger.isDebugEnabled()) {
			logger.debug("getKeyWords(Map<String,TemplateModel>) - start"); //$NON-NLS-1$
		}

		String returnString = DirectiveUtils.getString(PARAM_KEY_WORDS, params);
		if (logger.isDebugEnabled()) {
			logger.debug("getKeyWords(Map<String,TemplateModel>) - end"); //$NON-NLS-1$
		}
		return returnString;
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

	protected Integer getPostTypeId(Map<String, TemplateModel> params)
			throws TemplateException {
		if (logger.isDebugEnabled()) {
			logger.debug("getPostTypeId(Map<String,TemplateModel>) - start"); //$NON-NLS-1$
		}

		Integer returnInteger = DirectiveUtils.getInt(PARAM_POSTTYPE_ID, params);
		if (logger.isDebugEnabled()) {
			logger.debug("getPostTypeId(Map<String,TemplateModel>) - end"); //$NON-NLS-1$
		}
		return returnInteger;
	}

	protected Integer getParentPostTypeId(Map<String, TemplateModel> params)
			throws TemplateException {
		if (logger.isDebugEnabled()) {
			logger.debug("getParentPostTypeId(Map<String,TemplateModel>) - start"); //$NON-NLS-1$
		}

		Integer returnInteger = DirectiveUtils.getInt(PARAM_PARENT_POSTTYPE_ID, params);
		if (logger.isDebugEnabled()) {
			logger.debug("getParentPostTypeId(Map<String,TemplateModel>) - end"); //$NON-NLS-1$
		}
		return returnInteger;
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
	protected BbsTopicMng bbsTopicMng;

}
