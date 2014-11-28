package com.jeecms.common.web;

import org.apache.log4j.Logger;




import freemarker.template.TemplateDirectiveModel;

public abstract class WebDirective implements TemplateDirectiveModel {
	/**
	 * 日志。可以被子类使用。
	 */
	protected final Logger logger = Logger.getLogger(getClass());
	/**
	 * 输出参数：列表数据
	 */
	public static final String OUT_LIST = "tag_list";
	/**
	 * 输出参数：分页数据
	 */
	public static final String OUT_PAGINATION = "tag_pagination";
}
