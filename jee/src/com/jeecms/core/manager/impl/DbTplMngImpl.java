package com.jeecms.core.manager.impl;

import org.apache.log4j.Logger;

import static com.jeecms.common.web.Constants.SPT;
import static com.jeecms.common.web.Constants.UTF8;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.jeecms.common.web.Constants;
import com.jeecms.core.dao.DbTplDao;
import com.jeecms.core.entity.DbTpl;
import com.jeecms.core.tpl.ParentDirIsFileExceptioin;
import com.jeecms.core.tpl.Tpl;
import com.jeecms.core.tpl.TplManager;

import freemarker.cache.TemplateLoader;

@Service
@Transactional
public class DbTplMngImpl implements TemplateLoader, TplManager {
	private static final Logger logger =Logger.getLogger(DbTplMngImpl.class);

	/**
	 * @see TemplateLoader#findTemplateSource(String)
	 */
	public Object findTemplateSource(String name) throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("findTemplateSource(String) - start"); //$NON-NLS-1$
		}

		for (String ignore : ignoreLocales) {
			if (name.indexOf(ignore) != -1) {
				logger.debug("templete ignore: "+ name);
				return null;
			}
		}
		name = "/" + name;
		Tpl tpl = get(name);
		if (tpl == null) {
			logger.debug("templete not found: "+name);
			return null;
		} else if (tpl.isDirectory()) {
			logger.warn("template is a directory,not a file!");
			return null;
		} else {
			logger.debug("templete loaded: "+ name);
			return tpl;
		}
	}

	/**
	 * @see TemplateLoader#getLastModified(Object)
	 */
	public long getLastModified(Object templateSource) {
		if (logger.isDebugEnabled()) {
			logger.debug("getLastModified(Object) - start"); //$NON-NLS-1$
		}

		long returnlong = ((DbTpl) templateSource).getLastModified();
		if (logger.isDebugEnabled()) {
			logger.debug("getLastModified(Object) - end"); //$NON-NLS-1$
		}
		return returnlong;
	}

	/**
	 * @see TemplateLoader#getReader(Object, String)
	 */
	public Reader getReader(Object templateSource, String encoding)
			throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("getReader(Object, String) - start"); //$NON-NLS-1$
		}

		Reader returnReader = new StringReader(((DbTpl) templateSource).getSource());
		if (logger.isDebugEnabled()) {
			logger.debug("getReader(Object, String) - end"); //$NON-NLS-1$
		}
		return returnReader;
	}

	/**
	 * @see TemplateLoader#closeTemplateSource(Object)
	 */
	public void closeTemplateSource(Object templateSource) throws IOException {
		// do nothing.
	}

	@Transactional(readOnly = true)
	public Tpl get(String name) {
		if (logger.isDebugEnabled()) {
			logger.debug("get(String) - start"); //$NON-NLS-1$
		}

		DbTpl entity = dao.findById(name);

		if (logger.isDebugEnabled()) {
			logger.debug("get(String) - end"); //$NON-NLS-1$
		}
		return entity;
	}

	public void save(String name, String source, boolean isDirectory) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(String, String, boolean) - start"); //$NON-NLS-1$
		}

		DbTpl bean = new DbTpl();
		bean.setId(name);
		if (!isDirectory && source == null) {
			source = "";
		}
		bean.setSource(source);
		bean.setLastModified(System.currentTimeMillis());
		bean.setDirectory(isDirectory);
		dao.save(bean);
		createParentDir(name);

		if (logger.isDebugEnabled()) {
			logger.debug("save(String, String, boolean) - end"); //$NON-NLS-1$
		}
	}

	public void save(String path, MultipartFile file) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(String, MultipartFile) - start"); //$NON-NLS-1$
		}

		String name = path + SPT + file.getOriginalFilename();
		try {
			String source = new String(file.getBytes(), UTF8);
			save(name, source, false);
		} catch (UnsupportedEncodingException e) {
			logger.error("upload template error!", e);
		} catch (IOException e) {
			logger.error("upload template error!", e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("save(String, MultipartFile) - end"); //$NON-NLS-1$
		}
	}

	private void createParentDir(String name) {
		if (logger.isDebugEnabled()) {
			logger.debug("createParentDir(String) - start"); //$NON-NLS-1$
		}

		String[] dirs = DbTpl.getParentDir(name);
		DbTpl dirTpl;
		Tpl parentDir;
		for (String dir : dirs) {
			parentDir = get(dir);
			if (parentDir != null && !parentDir.isDirectory()) {
				throw new ParentDirIsFileExceptioin(
						"parent directory is a file: " + parentDir.getName());
			} else if (parentDir == null) {
				dirTpl = new DbTpl();
				dirTpl.setId(dir);
				dirTpl.setDirectory(true);
				dao.save(dirTpl);
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("createParentDir(String) - end"); //$NON-NLS-1$
		}
	}

	public void update(String name, String source) {
		if (logger.isDebugEnabled()) {
			logger.debug("update(String, String) - start"); //$NON-NLS-1$
		}

		DbTpl entity = (DbTpl) get(name);
		entity.setSource(source);
		entity.setLastModified(System.currentTimeMillis());

		if (logger.isDebugEnabled()) {
			logger.debug("update(String, String) - end"); //$NON-NLS-1$
		}
	}

	public int delete(String[] names) {
		if (logger.isDebugEnabled()) {
			logger.debug("delete(String[]) - start"); //$NON-NLS-1$
		}

		int count = 0;
		DbTpl tpl;
		for (String name : names) {
			tpl = dao.deleteById(name);
			count++;
			if (tpl.isDirectory()) {
				count += deleteByDir(tpl.getName());
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("delete(String[]) - end"); //$NON-NLS-1$
		}
		return names.length;
	}

	private int deleteByDir(String dir) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteByDir(String) - start"); //$NON-NLS-1$
		}

		dir += Constants.SPT;
		List<? extends Tpl> list = getListByPrefix(dir);
		for (Tpl tpl : list) {
			dao.deleteById(tpl.getName());
		}
		int returnint = list.size();
		if (logger.isDebugEnabled()) {
			logger.debug("deleteByDir(String) - end"); //$NON-NLS-1$
		}
		return returnint;
	}

	public List<? extends Tpl> getListByPrefix(String prefix) {
		if (logger.isDebugEnabled()) {
			logger.debug("getListByPrefix(String) - start"); //$NON-NLS-1$
		}

		List<? extends Tpl> returnList = dao.getStartWith(prefix);
		if (logger.isDebugEnabled()) {
			logger.debug("getListByPrefix(String) - end"); //$NON-NLS-1$
		}
		return returnList;
	}

	public List<String> getNameListByPrefix(String prefix) {
		if (logger.isDebugEnabled()) {
			logger.debug("getNameListByPrefix(String) - start"); //$NON-NLS-1$
		}

		// 有可能要在第一位插入一个元素
		List<String> list = new LinkedList<String>();
		for (Tpl tpl : getListByPrefix(prefix)) {
			list.add(tpl.getName());
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getNameListByPrefix(String) - end"); //$NON-NLS-1$
		}
		return list;
	}

	public List<? extends Tpl> getChild(String path) {
		if (logger.isDebugEnabled()) {
			logger.debug("getChild(String) - start"); //$NON-NLS-1$
		}

		List<DbTpl> dirs = dao.getChild(path, true);
		List<DbTpl> files = dao.getChild(path, false);
		dirs.addAll(files);

		if (logger.isDebugEnabled()) {
			logger.debug("getChild(String) - end"); //$NON-NLS-1$
		}
		return dirs;
	}

	public void rename(String orig, String dist) {
		if (logger.isDebugEnabled()) {
			logger.debug("rename(String, String) - start"); //$NON-NLS-1$
		}

		DbTpl tpl = dao.deleteById(orig);
		if (tpl == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("rename(String, String) - end"); //$NON-NLS-1$
			}
			return;
		}
		dao.deleteById(orig);
		String name = StringUtils.replace(tpl.getId(), orig, dist, 1);
		save(name, tpl.getSource(), tpl.isDirectory());
		createParentDir(name);
		if (tpl.isDirectory()) {
			List<DbTpl> list = dao.getStartWith(orig + "/");
			for (DbTpl t : list) {
				dao.deleteById(t.getId());
				name = StringUtils.replace(t.getId(), orig, dist, 1);
				save(name, t.getSource(), t.isDirectory());
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("rename(String, String) - end"); //$NON-NLS-1$
		}
	}

	private String[] ignoreLocales = { "_zh_CN.", "_zh.", "_en_US.", "_en." };

	public void setIgnoreLocales(String[] ignoreLocales) {
		this.ignoreLocales = ignoreLocales;
	}

	private DbTplDao dao;

	@Autowired
	public void setDao(DbTplDao dao) {
		this.dao = dao;
	}

}