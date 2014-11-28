package com.jeecms.bbs.action.directive;

import org.apache.log4j.Logger;

import static com.jeecms.common.web.freemarker.DirectiveUtils.OUT_LIST;
import static freemarker.template.ObjectWrapper.DEFAULT_WRAPPER;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.jeecms.bbs.entity.CmsFriendlink;
import com.jeecms.bbs.manager.CmsFriendlinkMng;
import com.jeecms.bbs.web.FrontUtils;
import com.jeecms.common.web.freemarker.DirectiveUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 友情链接类别列表标签
 * 
 * @author liufang
 * 
 */
public class CmsFriendlinkListDirective implements TemplateDirectiveModel {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(CmsFriendlinkListDirective.class);

	/**
	 * 输入参数，站点ID。
	 */
	public static final String PARAM_SITE_ID = "siteId";
	/**
	 * 输入参数，类别ID。
	 */
	public static final String PARAM_CTG_ID = "ctgId";
	/**
	 * 输入参数，是否显示。
	 */
	public static final String PARAM_ENABLED = "enabled";

	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("execute(Environment, Map, TemplateModel[], TemplateDirectiveBody) - start"); //$NON-NLS-1$
		}

		Integer siteId = getSiteId(params);
		if (siteId == null) {
			siteId = FrontUtils.getSite(env).getId();
		}
		Integer ctgId = getCtgId(params);
		Boolean enabled = getEnabled(params);
		if (enabled == null) {
			enabled = true;
		}
		List<CmsFriendlink> list = cmsFriendlinkMng.getList(siteId, ctgId,
				enabled);

		Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(
				params);
		paramWrap.put(OUT_LIST, DEFAULT_WRAPPER.wrap(list));
		Map<String, TemplateModel> origMap = DirectiveUtils
				.addParamsToVariable(env, paramWrap);
		body.render(env.getOut());
		DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);

		if (logger.isDebugEnabled()) {
			logger.debug("execute(Environment, Map, TemplateModel[], TemplateDirectiveBody) - end"); //$NON-NLS-1$
		}
	}

	private Integer getSiteId(Map<String, TemplateModel> params)
			throws TemplateException {
		if (logger.isDebugEnabled()) {
			logger.debug("getSiteId(Map<String,TemplateModel>) - start"); //$NON-NLS-1$
		}

		Integer returnInteger = DirectiveUtils.getInt(PARAM_SITE_ID, params);
		if (logger.isDebugEnabled()) {
			logger.debug("getSiteId(Map<String,TemplateModel>) - end"); //$NON-NLS-1$
		}
		return returnInteger;
	}

	private Integer getCtgId(Map<String, TemplateModel> params)
			throws TemplateException {
		if (logger.isDebugEnabled()) {
			logger.debug("getCtgId(Map<String,TemplateModel>) - start"); //$NON-NLS-1$
		}

		Integer returnInteger = DirectiveUtils.getInt(PARAM_CTG_ID, params);
		if (logger.isDebugEnabled()) {
			logger.debug("getCtgId(Map<String,TemplateModel>) - end"); //$NON-NLS-1$
		}
		return returnInteger;
	}

	private Boolean getEnabled(Map<String, TemplateModel> params)
			throws TemplateException {
		if (logger.isDebugEnabled()) {
			logger.debug("getEnabled(Map<String,TemplateModel>) - start"); //$NON-NLS-1$
		}

		Boolean returnBoolean = DirectiveUtils.getBool(PARAM_ENABLED, params);
		if (logger.isDebugEnabled()) {
			logger.debug("getEnabled(Map<String,TemplateModel>) - end"); //$NON-NLS-1$
		}
		return returnBoolean;
	}

	@Autowired
	private CmsFriendlinkMng cmsFriendlinkMng;
}
