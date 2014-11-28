package com.jeecms.common.upload;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;


import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;

/**
 * 本地文件存储
 * 
 * @author liufang
 * 
 */
public class FileRepository implements ServletContextAware {
	private Logger logger = Logger.getLogger(FileRepository.class);

	public String storeByExt(String path, String ext, MultipartFile file)
			throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("storeByExt(String, String, MultipartFile) - start"); //$NON-NLS-1$
		}

		String filename = UploadUtils.generateFilename(path, ext);
		File dest = new File(ctx.getRealPath(filename));
		dest = UploadUtils.getUniqueFile(dest);
		store(file, dest);

		if (logger.isDebugEnabled()) {
			logger.debug("storeByExt(String, String, MultipartFile) - end"); //$NON-NLS-1$
		}
		return filename;
	}

	public String storeByFilename(String filename, MultipartFile file)
			throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("storeByFilename(String, MultipartFile) - start"); //$NON-NLS-1$
		}

		File dest = new File(ctx.getRealPath(filename));
		store(file, dest);

		if (logger.isDebugEnabled()) {
			logger.debug("storeByFilename(String, MultipartFile) - end"); //$NON-NLS-1$
		}
		return filename;
	}

	public String storeByExt(String path, String ext, File file)
			throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("storeByExt(String, String, File) - start"); //$NON-NLS-1$
		}

		String filename = UploadUtils.generateFilename(path, ext);
		File dest = new File(ctx.getRealPath(filename));
		dest = UploadUtils.getUniqueFile(dest);
		store(file, dest);

		if (logger.isDebugEnabled()) {
			logger.debug("storeByExt(String, String, File) - end"); //$NON-NLS-1$
		}
		return filename;
	}

	public String storeByFilename(String filename, File file)
			throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("storeByFilename(String, File) - start"); //$NON-NLS-1$
		}

		File dest = new File(ctx.getRealPath(filename));
		store(file, dest);

		if (logger.isDebugEnabled()) {
			logger.debug("storeByFilename(String, File) - end"); //$NON-NLS-1$
		}
		return filename;
	}

	private void store(MultipartFile file, File dest) throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("store(MultipartFile, File) - start"); //$NON-NLS-1$
		}

		try {
			UploadUtils.checkDirAndCreate(dest.getParentFile());
			file.transferTo(dest);
		} catch (IOException e) {
			logger.error("Transfer file error when upload file", e);
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("store(MultipartFile, File) - end"); //$NON-NLS-1$
		}
	}

	private void store(File file, File dest) throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("store(File, File) - start"); //$NON-NLS-1$
		}

		try {
			UploadUtils.checkDirAndCreate(dest.getParentFile());
			FileUtils.copyFile(file, dest);
		} catch (IOException e) {
			logger.error("Transfer file error when upload file", e);
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("store(File, File) - end"); //$NON-NLS-1$
		}
	}

	public File retrieve(String name) {
		if (logger.isDebugEnabled()) {
			logger.debug("retrieve(String) - start"); //$NON-NLS-1$
		}

		File returnFile = new File(ctx.getRealPath(name));
		if (logger.isDebugEnabled()) {
			logger.debug("retrieve(String) - end"); //$NON-NLS-1$
		}
		return returnFile;
	}

	private ServletContext ctx;

	public void setServletContext(ServletContext servletContext) {
		this.ctx = servletContext;
	}
}
