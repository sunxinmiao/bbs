package com.jeecms.core.manager.impl;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.upload.UploadUtils;
import com.jeecms.core.dao.DbFileDao;
import com.jeecms.core.entity.DbFile;
import com.jeecms.core.manager.DbFileMng;

@Service
@Transactional
public class DbFileMngImpl implements DbFileMng {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DbFileMngImpl.class);

	@Transactional(readOnly = true)
	public DbFile findById(String id) {
		if (logger.isDebugEnabled()) {
			logger.debug("findById(String) - start"); //$NON-NLS-1$
		}

		DbFile entity = dao.findById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("findById(String) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public String storeByExt(String path, String ext, InputStream in)
			throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("storeByExt(String, String, InputStream) - start"); //$NON-NLS-1$
		}

		String filename;
		DbFile file;
		// 判断文件是否存在
		do {
			filename = UploadUtils.generateFilename(path, ext);
			file = findById(filename);
		} while (file != null);
		save(filename, in);

		if (logger.isDebugEnabled()) {
			logger.debug("storeByExt(String, String, InputStream) - end"); //$NON-NLS-1$
		}
		return filename;
	}

	public String storeByFilename(String filename, InputStream in)
			throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("storeByFilename(String, InputStream) - start"); //$NON-NLS-1$
		}

		DbFile file = findById(filename);
		if (file != null) {
			update(file, in);
		} else {
			save(filename, in);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("storeByFilename(String, InputStream) - end"); //$NON-NLS-1$
		}
		return filename;
	}

	public File retrieve(String name) throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("retrieve(String) - start"); //$NON-NLS-1$
		}

		// 此方法依赖于文件名是唯一的，否则有可能出现问题。
		String path = System.getProperty("java.io.tmpdir");
		File file = new File(path, name);
		file = UploadUtils.getUniqueFile(file);
		DbFile df = findById(name);
		FileUtils.writeByteArrayToFile(file, df.getContent());

		if (logger.isDebugEnabled()) {
			logger.debug("retrieve(String) - end"); //$NON-NLS-1$
		}
		return file;
	}

	public boolean restore(String name, File file)
			throws FileNotFoundException, IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("restore(String, File) - start"); //$NON-NLS-1$
		}

		storeByFilename(name, new FileInputStream(file));
		file.deleteOnExit();

		if (logger.isDebugEnabled()) {
			logger.debug("restore(String, File) - end"); //$NON-NLS-1$
		}
		return true;
	}

	private DbFile update(DbFile file, InputStream in) throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("update(DbFile, InputStream) - start"); //$NON-NLS-1$
		}

		byte[] content = IOUtils.toByteArray(in);
		file.setContent(content);
		file.setLastModified(System.currentTimeMillis());
		file.setLength(content.length);
		in.close();

		if (logger.isDebugEnabled()) {
			logger.debug("update(DbFile, InputStream) - end"); //$NON-NLS-1$
		}
		return file;
	}

	private DbFile save(String filename, InputStream in) throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("save(String, InputStream) - start"); //$NON-NLS-1$
		}

		byte[] content = IOUtils.toByteArray(in);
		DbFile file = new DbFile(filename, content.length, System
				.currentTimeMillis(), content);
		dao.save(file);
		in.close();

		if (logger.isDebugEnabled()) {
			logger.debug("save(String, InputStream) - end"); //$NON-NLS-1$
		}
		return file;
	}

	public DbFile deleteById(String id) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(String) - start"); //$NON-NLS-1$
		}

		DbFile bean = dao.deleteById(id);

		if (logger.isDebugEnabled()) {
			logger.debug("deleteById(String) - end"); //$NON-NLS-1$
		}
		return bean;
	}

	public DbFile[] deleteByIds(String[] ids) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(String[]) - start"); //$NON-NLS-1$
		}

		DbFile[] beans = new DbFile[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("deleteByIds(String[]) - end"); //$NON-NLS-1$
		}
		return beans;
	}

	private DbFileDao dao;

	@Autowired
	public void setDao(DbFileDao dao) {
		this.dao = dao;
	}
}