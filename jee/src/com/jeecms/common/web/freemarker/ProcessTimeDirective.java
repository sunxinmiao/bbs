package com.jeecms.common.web.freemarker;

import org.apache.log4j.Logger;

import static com.jeecms.common.web.ProcessTimeFilter.START_TIME;

import java.io.IOException;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.Map;




import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateNumberModel;

/**
 * 执行时间标签
 * 
 * 需要拦截器com.jeecms.common.web.ProcessTimeFilter支持
 * 
 * @author liufang
 * 
 */
public class ProcessTimeDirective implements TemplateDirectiveModel {
	private static final Logger logger = Logger.getLogger(ProcessTimeDirective.class);
	private static final DecimalFormat FORMAT = new DecimalFormat("0.000");

	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("execute(Environment, Map, TemplateModel[], TemplateDirectiveBody) - start"); //$NON-NLS-1$
		}

		long time = getStartTime(env);
		if (time != -1) {
			time = System.currentTimeMillis() - time;
			Writer out = env.getOut();
			out.append("Processed in " + FORMAT.format(time / 1000F)
					+ " second(s)");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("execute(Environment, Map, TemplateModel[], TemplateDirectiveBody) - end"); //$NON-NLS-1$
		}
	}

	private long getStartTime(Environment env) throws TemplateModelException {
		if (logger.isDebugEnabled()) {
			logger.debug("getStartTime(Environment) - start"); //$NON-NLS-1$
		}

		TemplateModel startTime = env.getGlobalVariable(START_TIME);
		if (startTime == null) {
			logger.warn("Variable '"+START_TIME+"' not found in GlobalVariable" );
			return -1;
		}
		if (startTime instanceof TemplateNumberModel) {
			long returnlong = ((TemplateNumberModel) startTime).getAsNumber().longValue();
			if (logger.isDebugEnabled()) {
				logger.debug("getStartTime(Environment) - end"); //$NON-NLS-1$
			}
			return returnlong;
		} else {
			throw new MustNumberException(START_TIME);
		}
	}

}
