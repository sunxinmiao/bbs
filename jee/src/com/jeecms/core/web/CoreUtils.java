package com.jeecms.core.web;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * 提供Core中公用的方法
 * 
 * @author liufang
 * 
 */
public class CoreUtils {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(CoreUtils.class);

	/**
	 * 去除模板前缀
	 * 
	 * @param list
	 *            模板列表
	 * @param prefix
	 *            模板前缀
	 * @param include
	 *            需包含的模板
	 * @param excludes
	 *            需去除的模板
	 * @return
	 */
	public static List<String> tplTrim(List<String> list, String prefix,
			String include, String... excludes) {
		if (logger.isDebugEnabled()) {
			logger.debug("tplTrim(List<String>, String, String, String) - start"); //$NON-NLS-1$
		}

		List<String> result = new ArrayList<String>(list.size());
		if (!StringUtils.isBlank(include) && !list.contains(include)) {
			if (!tplContain(excludes, include)) {
				result.add(include.substring(prefix.length()));
			}
		}
		for (String t : list) {
			if (!tplContain(excludes, t)) {
				result.add(t.substring(prefix.length()));
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("tplTrim(List<String>, String, String, String) - end"); //$NON-NLS-1$
		}
		return result;
	}

	/**
	 * 检查tpl是否存在于excludes里面。tpl
	 * 
	 * @param excludes
	 * @param tpl
	 * @return
	 */
	private static boolean tplContain(String[] excludes, String tpl) {
		if (logger.isDebugEnabled()) {
			logger.debug("tplContain(String[], String) - start"); //$NON-NLS-1$
		}

		int start = tpl.lastIndexOf("/");
		int end = tpl.lastIndexOf(".");
		if (start == -1 || end == -1) {
			throw new RuntimeException("tpl not contain '/' or '.':" + tpl);
		}
		String name = tpl.substring(start + 1, end);
		for (String e : excludes) {
			if (e.equals(name)) {
				if (logger.isDebugEnabled()) {
					logger.debug("tplContain(String[], String) - end"); //$NON-NLS-1$
				}
				return true;
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("tplContain(String[], String) - end"); //$NON-NLS-1$
		}
		return false;
	}
}
