package com.jeecms.core.tpl;

import org.apache.log4j.Logger;

/**
 * 父目录是文件异常
 * 
 * 当移动文件或创建文件时，父目录也是文件，可以导致该异常。
 * 
 * @author liufang
 * 
 */
@SuppressWarnings("serial")
public class ParentDirIsFileExceptioin extends RuntimeException {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ParentDirIsFileExceptioin.class);

	private String parentDir;

	/**
	 * @param parentDir
	 *            The parent dir, which is a file.
	 */
	public ParentDirIsFileExceptioin(String parentDir) {
		this.parentDir = parentDir;
	}

	@Override
	public String getMessage() {
		if (logger.isDebugEnabled()) {
			logger.debug("getMessage() - start"); //$NON-NLS-1$
		}

		String returnString = "parent directory is a file: " + parentDir;
		if (logger.isDebugEnabled()) {
			logger.debug("getMessage() - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * Get the parent dir, which is a file.
	 * 
	 * @return
	 */
	public String getParentDir() {
		return parentDir;
	}
}
