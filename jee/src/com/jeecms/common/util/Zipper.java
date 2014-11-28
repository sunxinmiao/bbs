package com.jeecms.common.util;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;


import org.springframework.util.Assert;

/**
 * 用于制作zip压缩包
 * 
 * @author liufang
 * 
 */
public class Zipper {
	private static final Logger logger = Logger.getLogger(Zipper.class);

	/**
	 * 制作压缩包
	 * 
	 */
	public static void zip(OutputStream out, List<FileEntry> fileEntrys,
			String encoding) {
		if (logger.isDebugEnabled()) {
			logger.debug("zip(OutputStream, List<FileEntry>, String) - start"); //$NON-NLS-1$
		}

		new Zipper(out, fileEntrys, encoding);

		if (logger.isDebugEnabled()) {
			logger.debug("zip(OutputStream, List<FileEntry>, String) - end"); //$NON-NLS-1$
		}
	}

	/**
	 * 制作压缩包
	 * 
	 */
	public static void zip(OutputStream out, List<FileEntry> fileEntrys) {
		if (logger.isDebugEnabled()) {
			logger.debug("zip(OutputStream, List<FileEntry>) - start"); //$NON-NLS-1$
		}

		new Zipper(out, fileEntrys, null);

		if (logger.isDebugEnabled()) {
			logger.debug("zip(OutputStream, List<FileEntry>) - end"); //$NON-NLS-1$
		}
	}

	/**
	 * 创建Zipper对象
	 * 
	 * @param out
	 *            输出流
	 * @param filter
	 *            文件过滤，不过滤可以为null。
	 * @param srcFilename
	 *            源文件名。可以有多个源文件，如果源文件是目录，那么所有子目录都将被包含。
	 */
	protected Zipper(OutputStream out, List<FileEntry> fileEntrys,
			String encoding) {
		Assert.notEmpty(fileEntrys);
		long begin = System.currentTimeMillis();
		logger.debug("开始制作压缩包");
		try {
			try {
				zipOut = new ZipOutputStream(out);
				if (!StringUtils.isBlank(encoding)) {
					logger.debug("using encoding: "+encoding);
					zipOut.setEncoding(encoding);
				} else {
					logger.debug("using default encoding");
				}
				for (FileEntry fe : fileEntrys) {
					zip(fe.getFile(), fe.getFilter(), fe.getZipEntry(), fe
							.getPrefix());
				}
			} finally {
				zipOut.close();
			}
		} catch (IOException e) {
			logger.error("Zipper(OutputStream, List<FileEntry>, String)", e); //$NON-NLS-1$

			throw new RuntimeException("制作压缩包时，出现IO异常！", e);
		}
		long end = System.currentTimeMillis();
		logger.info("制作压缩包成功。耗时：" +(end - begin)+"ms。" );
	}

	/**
	 * 压缩文件
	 * 
	 * @param srcFile
	 *            源文件
	 * @param pentry
	 *            父ZipEntry
	 * @throws IOException
	 */
	private void zip(File srcFile, FilenameFilter filter, ZipEntry pentry,
			String prefix) throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("zip(File, FilenameFilter, ZipEntry, String) - start"); //$NON-NLS-1$
		}

		ZipEntry entry;
		if (srcFile.isDirectory()) {
			if (pentry == null) {
				entry = new ZipEntry(srcFile.getName());
			} else {
				entry = new ZipEntry(pentry.getName() + "/" + srcFile.getName());
			}
			File[] files = srcFile.listFiles(filter);
			for (File f : files) {
				zip(f, filter, entry, prefix);
			}
		} else {
			if (pentry == null) {
				entry = new ZipEntry(prefix + srcFile.getName());
			} else {
				entry = new ZipEntry(pentry.getName() + "/" + prefix
						+ srcFile.getName());
			}
			FileInputStream in;
			try {
				logger.debug("读取文件："+ srcFile.getAbsolutePath());
				in = new FileInputStream(srcFile);
				try {
					zipOut.putNextEntry(entry);
					int len;
					while ((len = in.read(buf)) > 0) {
						zipOut.write(buf, 0, len);
					}
					zipOut.closeEntry();
				} finally {
					in.close();
				}
			} catch (FileNotFoundException e) {
				logger.error("zip(File, FilenameFilter, ZipEntry, String)", e); //$NON-NLS-1$

				throw new RuntimeException("制作压缩包时，源文件不存在："
						+ srcFile.getAbsolutePath(), e);
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("zip(File, FilenameFilter, ZipEntry, String) - end"); //$NON-NLS-1$
		}
	}

	private byte[] buf = new byte[1024];
	private ZipOutputStream zipOut;

	public static class FileEntry {
		private FilenameFilter filter;
		private String parent;
		private File file;
		private String prefix;

		public FileEntry(String parent, String prefix, File file,
				FilenameFilter filter) {
			this.parent = parent;
			this.prefix = prefix;
			this.file = file;
			this.filter = filter;
		}

		public FileEntry(String parent, File file) {
			this.parent = parent;
			this.file = file;
		}

		public FileEntry(String parent, String prefix, File file) {
			this(parent, prefix, file, null);
		}

		public ZipEntry getZipEntry() {
			if (logger.isDebugEnabled()) {
				logger.debug("getZipEntry() - start"); //$NON-NLS-1$
			}

			if (StringUtils.isBlank(parent)) {
				if (logger.isDebugEnabled()) {
					logger.debug("getZipEntry() - end"); //$NON-NLS-1$
				}
				return null;
			} else {
				ZipEntry returnZipEntry = new ZipEntry(parent);
				if (logger.isDebugEnabled()) {
					logger.debug("getZipEntry() - end"); //$NON-NLS-1$
				}
				return returnZipEntry;
			}
		}

		public FilenameFilter getFilter() {
			return filter;
		}

		public void setFilter(FilenameFilter filter) {
			this.filter = filter;
		}

		public String getParent() {
			return parent;
		}

		public void setParent(String parent) {
			this.parent = parent;
		}

		public File getFile() {
			return file;
		}

		public void setFile(File file) {
			this.file = file;
		}

		public String getPrefix() {
			if (logger.isDebugEnabled()) {
				logger.debug("getPrefix() - start"); //$NON-NLS-1$
			}

			if (prefix == null) {
				if (logger.isDebugEnabled()) {
					logger.debug("getPrefix() - end"); //$NON-NLS-1$
				}
				return "";
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("getPrefix() - end"); //$NON-NLS-1$
				}
				return prefix;
			}
		}

		public void setPrefix(String prefix) {
			this.prefix = prefix;
		}
	}
}
