package com.jeecms.common.web.freemarker;

import org.apache.log4j.Logger;

import freemarker.template.TemplateModelException;

/**
 * 非布尔参数异常
 * 
 * @author liufang
 * 
 */
@SuppressWarnings("serial")
public class MustDateException extends TemplateModelException {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(MustDateException.class);

	public MustDateException(String paramName) {
		super("The \"" + paramName + "\" parameter must be a date.");
	}
}
