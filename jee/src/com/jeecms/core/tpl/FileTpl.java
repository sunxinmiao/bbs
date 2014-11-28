package com.jeecms.core.tpl;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.io.FileUtils;

import com.jeecms.common.web.Constants;

public class FileTpl implements Tpl {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(FileTpl.class);

	private File file;
	// 应用的根目录
	private String root;

	public FileTpl(File file, String root) {
		this.file = file;
		this.root = root;
	}

	public String getName() {
		if (logger.isDebugEnabled()) {
			logger.debug("getName() - start"); //$NON-NLS-1$
		}

		String ap = file.getAbsolutePath().substring(root.length());
		ap = ap.replace(File.separatorChar, '/');

		if (logger.isDebugEnabled()) {
			logger.debug("getName() - end"); //$NON-NLS-1$
		}
		return ap;
	}

	public String getPath() {
		if (logger.isDebugEnabled()) {
			logger.debug("getPath() - start"); //$NON-NLS-1$
		}

		String name = getName();
		String returnString = name.substring(0, name.lastIndexOf('/'));
		if (logger.isDebugEnabled()) {
			logger.debug("getPath() - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	public String getFilename() {
		if (logger.isDebugEnabled()) {
			logger.debug("getFilename() - start"); //$NON-NLS-1$
		}

		String returnString = file.getName();
		if (logger.isDebugEnabled()) {
			logger.debug("getFilename() - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	public String getSource() {
		if (logger.isDebugEnabled()) {
			logger.debug("getSource() - start"); //$NON-NLS-1$
		}

		if (file.isDirectory()) {
			if (logger.isDebugEnabled()) {
				logger.debug("getSource() - end"); //$NON-NLS-1$
			}
			return null;
		}
		try {
			String returnString = FileUtils.readFileToString(this.file, Constants.UTF8);
			if (logger.isDebugEnabled()) {
				logger.debug("getSource() - end"); //$NON-NLS-1$
			}
			return returnString;
		} catch (IOException e) {
			logger.error("getSource()", e); //$NON-NLS-1$

			throw new RuntimeException(e);
		}
	}

	public long getLastModified() {
		if (logger.isDebugEnabled()) {
			logger.debug("getLastModified() - start"); //$NON-NLS-1$
		}

		long returnlong = file.lastModified();
		if (logger.isDebugEnabled()) {
			logger.debug("getLastModified() - end"); //$NON-NLS-1$
		}
		return returnlong;
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

	public long getLength() {
		if (logger.isDebugEnabled()) {
			logger.debug("getLength() - start"); //$NON-NLS-1$
		}

		long returnlong = file.length();
		if (logger.isDebugEnabled()) {
			logger.debug("getLength() - end"); //$NON-NLS-1$
		}
		return returnlong;
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

	public boolean isDirectory() {
		if (logger.isDebugEnabled()) {
			logger.debug("isDirectory() - start"); //$NON-NLS-1$
		}

		boolean returnboolean = file.isDirectory();
		if (logger.isDebugEnabled()) {
			logger.debug("isDirectory() - end"); //$NON-NLS-1$
		}
		return returnboolean;
	}
}
