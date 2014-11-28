package com.jeecms.common.web.freemarker;

import org.apache.log4j.Logger;

import freemarker.template.TemplateModelException;

/**
 * 非数字参数异常
 * 
 * @author liufang
 * 
 */
@SuppressWarnings("serial")
public class MustStringException extends TemplateModelException {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(MustStringException.class);

	public MustStringException(String paramName) {
		super("The \"" + paramName + "\" parameter must be a string.");
	}
}
