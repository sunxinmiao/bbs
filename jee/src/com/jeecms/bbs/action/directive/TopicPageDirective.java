package com.jeecms.bbs.action.directive;

import org.apache.log4j.Logger;

import static com.jeecms.common.web.freemarker.DirectiveUtils.OUT_PAGINATION;
import static freemarker.template.ObjectWrapper.DEFAULT_WRAPPER;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


import com.jeecms.bbs.action.directive.abs.AbstractTopicPageDirective;
import com.jeecms.bbs.web.FrontUtils;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.freemarker.DirectiveUtils;
import com.jeecms.common.web.freemarker.DirectiveUtils.InvokeType;
import com.jeecms.core.entity.CmsSite;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class TopicPageDirective extends AbstractTopicPageDirective {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TopicPageDirective.class);

	/**
	 * 模板名称
	 */
	public static final String TPL_NAME = "topic_page";
	/**
	 * 模板名称
	 */
	public static final String TPL_PAG = "topic";
	/**
	 * 我的主题
	 */
	public static final String TPL_MY_TOPIC = "mytopic_page";
	/**
	 * 我的主题
	 */
	public static final String TPL_MY_POST = "mypost_page";

	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("execute(Environment, Map, TemplateModel[], TemplateDirectiveBody) - start"); //$NON-NLS-1$
		}

		CmsSite site = FrontUtils.getSite(env);
		InvokeType type = DirectiveUtils.getInvokeType(params);
		String jinghua = getFindType(params);
		Pagination page = bbsTopicMng.getForTag(site.getId(),
				getForumId(params),getParentPostTypeId(params),getPostTypeId(params), getStatus(params), getPrimeLevel(params),
				getKeyWords(params), getCreater(params), getCreaterId(params),
				getTopLevel(params), FrontUtils.getPageNo(env), FrontUtils
						.getCount(params),jinghua);
		Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(
				params);
		paramWrap.put(OUT_PAGINATION, DEFAULT_WRAPPER.wrap(page));
		Map<String, TemplateModel> origMap = DirectiveUtils
				.addParamsToVariable(env, paramWrap);
		if (InvokeType.custom == type) {
			FrontUtils.includeTpl(TPL_NAME, site, params, env);
			FrontUtils.includePagination(site, params, env);
		} else if (InvokeType.sysDefined == type) {
			FrontUtils.includeTpl(TPL_MY_TOPIC, site, params, env);
			FrontUtils.includePagination(site, params, env);
		} else if (InvokeType.userDefined == type) {
			FrontUtils.includeTpl(TPL_MY_POST, site, params, env);
			FrontUtils.includePagination(site, params, env);
		} else if (InvokeType.body == type) {
			body.render(env.getOut());
			FrontUtils.includePagination(site, params, env);
		} else {
			throw new RuntimeException("invoke type not handled: " + type);
		}
		DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);

		if (logger.isDebugEnabled()) {
			logger.debug("execute(Environment, Map, TemplateModel[], TemplateDirectiveBody) - end"); //$NON-NLS-1$
		}
	}

}
