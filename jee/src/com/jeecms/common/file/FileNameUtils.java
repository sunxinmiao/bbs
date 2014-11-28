package com.jeecms.common.file;

import org.apache.log4j.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.RandomStringUtils;

import com.jeecms.common.util.Num62;

/**
 * 文件名生成帮助类
 * 
 * @author liufang
 * 
 */
public class FileNameUtils {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(FileNameUtils.class);

	/**
	 * 日期格式化对象，将当前日期格式化成yyyyMM格式，用于生成目录。
	 */
	public static final DateFormat pathDf = new SimpleDateFormat("yyyyMM");
	/**
	 * 日期格式化对象，将当前日期格式化成ddHHmmss格式，用于生成文件名。
	 */
	public static final DateFormat nameDf = new SimpleDateFormat("ddHHmmss");

	/**
	 * 生成当前年月格式的文件路径
	 * 
	 * yyyyMM 200806
	 * 
	 * @return
	 */
	public static String genPathName() {
		if (logger.isDebugEnabled()) {
			logger.debug("genPathName() - start"); //$NON-NLS-1$
		}

		String returnString = pathDf.format(new Date());
		if (logger.isDebugEnabled()) {
			logger.debug("genPathName() - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * 生产以当前日、时间开头加4位随机数的文件名
	 * 
	 * ddHHmmss 03102230
	 * 
	 * @return 10位长度文件名
	 */
	public static String genFileName() {
		if (logger.isDebugEnabled()) {
			logger.debug("genFileName() - start"); //$NON-NLS-1$
		}

		String returnString = nameDf.format(new Date()) + RandomStringUtils.random(4, Num62.N36_CHARS);
		if (logger.isDebugEnabled()) {
			logger.debug("genFileName() - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * 生产以当前时间开头加4位随机数的文件名
	 * 
	 * @param ext
	 *            文件名后缀，不带'.'
	 * @return 10位长度文件名+文件后缀
	 */
	public static String genFileName(String ext) {
		if (logger.isDebugEnabled()) {
			logger.debug("genFileName(String) - start"); //$NON-NLS-1$
		}

		String returnString = genFileName() + "." + ext;
		if (logger.isDebugEnabled()) {
			logger.debug("genFileName(String) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	public static void main(String[] args) {
		if (logger.isDebugEnabled()) {
			logger.debug("main(String[]) - start"); //$NON-NLS-1$
		}

		System.out.println(genPathName());
		System.out.println(genFileName());

		if (logger.isDebugEnabled()) {
			logger.debug("main(String[]) - end"); //$NON-NLS-1$
		}
	}
}
