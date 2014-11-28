package com.jeecms.common.web.freemarker;

import org.apache.log4j.Logger;

import freemarker.template.TemplateModelException;

/**
 * 缺少必须参数异常
 * 
 * @author liufang
 * 
 */
@SuppressWarnings("serial")
public class ParamsRequiredException extends TemplateModelException {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ParamsRequiredException.class);

	public ParamsRequiredException(String paramName) {
		super("The required \"" + paramName + "\" paramter is missing.");
	}
}
