package com.jeecms.common.web.freemarker;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * UUID生成器
 * 
 * @author liufang
 * 
 */
public class UUIDDirective implements TemplateDirectiveModel {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(UUIDDirective.class);

	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("execute(Environment, Map, TemplateModel[], TemplateDirectiveBody) - start"); //$NON-NLS-1$
		}

		String uuid = UUID.randomUUID().toString();
		uuid = StringUtils.remove(uuid, '-');
		env.getOut().append(uuid);

		if (logger.isDebugEnabled()) {
			logger.debug("execute(Environment, Map, TemplateModel[], TemplateDirectiveBody) - end"); //$NON-NLS-1$
		}
	}
}
