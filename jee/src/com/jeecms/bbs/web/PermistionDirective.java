package com.jeecms.bbs.web;

import org.apache.log4j.Logger;

import static com.jeecms.bbs.web.AdminContextInterceptor.PERMISSION_MODEL;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.jeecms.common.web.freemarker.DirectiveUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;
import freemarker.template.TemplateSequenceModel;

/**
 * 后台管理员权限许可
 * 
 * @author liufang
 * 
 */
public class PermistionDirective implements TemplateDirectiveModel {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(PermistionDirective.class);

	/**
	 * 此url必须和perm中url一致。
	 */
	public static final String PARAM_URL = "url";

	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("execute(Environment, Map, TemplateModel[], TemplateDirectiveBody) - start"); //$NON-NLS-1$
		}

		// 此处的权限判断有可能和拦截器的不一致，有没有关系？大部分应该没有关系，因为不需要判断权限的可以不加这个标签。
		// 光一个perms可能还不够，至少还有一个是否只浏览的问题。这个是否可以不管？可以！
		// 是否控制权限这个总是要的吧？perms为null代表无需控制权限。
		String url = DirectiveUtils.getString(PARAM_URL, params);
		boolean pass = false;
		if (StringUtils.isBlank(url)) {
			// url为空，则认为有权限。
			pass = true;
		} else {
			TemplateSequenceModel perms = getPerms(env);
			if (perms == null) {
				// perms为null，则代表不需要判断权限。
				pass = true;
			} else {
				String perm;
				for (int i = 0, len = perms.size(); i < len; i++) {
					perm = ((TemplateScalarModel) perms.get(i)).getAsString();
					if (url.startsWith(perm)) {
						pass = true;
						break;
					}
				}
			}
		}
		if (pass) {
			body.render(env.getOut());
		}

		if (logger.isDebugEnabled()) {
			logger.debug("execute(Environment, Map, TemplateModel[], TemplateDirectiveBody) - end"); //$NON-NLS-1$
		}
	}

	private TemplateSequenceModel getPerms(Environment env)
			throws TemplateModelException {
		if (logger.isDebugEnabled()) {
			logger.debug("getPerms(Environment) - start"); //$NON-NLS-1$
		}

		TemplateModel model = env.getDataModel().get(PERMISSION_MODEL);
		if (model == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("getPerms(Environment) - end"); //$NON-NLS-1$
			}
			return null;
		}
		if (model instanceof TemplateSequenceModel) {
			TemplateSequenceModel returnTemplateSequenceModel = (TemplateSequenceModel) model;
			if (logger.isDebugEnabled()) {
				logger.debug("getPerms(Environment) - end"); //$NON-NLS-1$
			}
			return returnTemplateSequenceModel;
		} else {
			throw new TemplateModelException(
					"'perms' in data model not a TemplateSequenceModel");
		}

	}
}
