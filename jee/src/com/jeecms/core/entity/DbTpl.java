package com.jeecms.core.entity;

import org.apache.log4j.Logger;

import static com.jeecms.common.web.Constants.SPT;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import com.jeecms.common.web.Constants;
import com.jeecms.core.entity.base.BaseDbTpl;
import com.jeecms.core.tpl.Tpl;

public class DbTpl extends BaseDbTpl implements Tpl {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DbTpl.class);

	private static final long serialVersionUID = 1L;

	/**
	 * 获得文件夹或文件的所有父文件夹
	 * 
	 * @param path
	 * @return
	 */
	public static String[] getParentDir(String path) {
		if (logger.isDebugEnabled()) {
			logger.debug("getParentDir(String) - start"); //$NON-NLS-1$
		}

		Assert.notNull(path);
		if (!path.startsWith(SPT)) {
			throw new IllegalArgumentException("path must start with /");
		}
		List<String> list = new ArrayList<String>();
		int index = path.indexOf(SPT, 1);
		while (index >= 0) {
			list.add(path.substring(0, index));
			index = path.indexOf(SPT, index + 1);
		}
		String[] arr = new String[list.size()];
		String[] returnStringArray = list.toArray(arr);
		if (logger.isDebugEnabled()) {
			logger.debug("getParentDir(String) - end"); //$NON-NLS-1$
		}
		return returnStringArray;
	}

	public String getName() {
		if (logger.isDebugEnabled()) {
			logger.debug("getName() - start"); //$NON-NLS-1$
		}

		String returnString = getId();
		if (logger.isDebugEnabled()) {
			logger.debug("getName() - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	public String getPath() {
		if (logger.isDebugEnabled()) {
			logger.debug("getPath() - start"); //$NON-NLS-1$
		}

		String name = getId();
		String returnString = getId().substring(0, name.lastIndexOf("/"));
		if (logger.isDebugEnabled()) {
			logger.debug("getPath() - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	public String getFilename() {
		if (logger.isDebugEnabled()) {
			logger.debug("getFilename() - start"); //$NON-NLS-1$
		}

		String name = getId();
		if (!StringUtils.isBlank(name)) {
			int index = name.lastIndexOf(Constants.SPT);
			if (index != -1) {
				String returnString = name.substring(index + 1, name.length());
				if (logger.isDebugEnabled()) {
					logger.debug("getFilename() - end"); //$NON-NLS-1$
				}
				return returnString;
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getFilename() - end"); //$NON-NLS-1$
		}
		return name;
	}

	public long getLength() {
		if (logger.isDebugEnabled()) {
			logger.debug("getLength() - start"); //$NON-NLS-1$
		}

		if (isDirectory() || getSource() == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("getLength() - end"); //$NON-NLS-1$
			}
			return 128;
		} else {
			// 一个英文字符占1个字节，而一个中文则占3-4字节，这里取折中一个字符1.5个字节
			long returnlong = (long) (getSource().length() * 1.5);
			if (logger.isDebugEnabled()) {
				logger.debug("getLength() - end"); //$NON-NLS-1$
			}
			return returnlong;
		}
	}

	public int getSize() {
		if (logger.isDebugEnabled()) {
			logger.debug("getSize() - start"); //$NON-NLS-1$
		}

		int returnint = (int) (getLength() / 1024) + 1;
		if (logger.isDebugEnabled()) {
			logger.debug("getSize() - end"); //$NON-NLS-1$
		}
		return returnint;
	}

	public Date getLastModifiedDate() {
		if (logger.isDebugEnabled()) {
			logger.debug("getLastModifiedDate() - start"); //$NON-NLS-1$
		}

		Date returnDate = new Timestamp(getLastModified());
		if (logger.isDebugEnabled()) {
			logger.debug("getLastModifiedDate() - end"); //$NON-NLS-1$
		}
		return returnDate;
	}

	/* [CONSTRUCTOR MARKER BEGIN] */
	public DbTpl () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public DbTpl (java.lang.String id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public DbTpl (
		java.lang.String id,
		long lastModified,
		boolean directory) {

		super (
			id,
			lastModified,
			directory);
	}

	/* [CONSTRUCTOR MARKER END] */
}