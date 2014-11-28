package com.jeecms.core.tpl;

import org.apache.log4j.Logger;

import static com.jeecms.common.web.Constants.UTF8;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.jeecms.common.web.springmvc.RealPathResolver;

public class FileTplManagerImpl implements TplManager {
	private static Logger logger = Logger.getLogger(FileTplManagerImpl.class);

	public int delete(String[] names) {
		if (logger.isDebugEnabled()) {
			logger.debug("delete(String[]) - start"); //$NON-NLS-1$
		}

		File f;
		int count = 0;
		for (String name : names) {
			f = new File(realPathResolver.get(name));
			if (f.isDirectory()) {
				if (FileUtils.deleteQuietly(f)) {
					count++;
				}
			} else {
				if (f.delete()) {
					count++;
				}
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("delete(String[]) - end"); //$NON-NLS-1$
		}
		return count;
	}

	public Tpl get(String name) {
		if (logger.isDebugEnabled()) {
			logger.debug("get(String) - start"); //$NON-NLS-1$
		}

		File f = new File(realPathResolver.get(name));
		if (f.exists()) {
			Tpl returnTpl = new FileTpl(f, root);
			if (logger.isDebugEnabled()) {
				logger.debug("get(String) - end"); //$NON-NLS-1$
			}
			return returnTpl;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("get(String) - end"); //$NON-NLS-1$
			}
			return null;
		}
	}

	public List<Tpl> getListByPrefix(String prefix) {
		if (logger.isDebugEnabled()) {
			logger.debug("getListByPrefix(String) - start"); //$NON-NLS-1$
		}

		File f = new File(realPathResolver.get(prefix));
		String name = prefix.substring(prefix.lastIndexOf("/") + 1);
		File parent;
		if (prefix.endsWith("/")) {
			name = "";
			parent = f;
		} else {
			parent = f.getParentFile();
		}
		if (parent.exists()) {
			File[] files = parent.listFiles(new PrefixFilter(name));
			if (files != null) {
				List<Tpl> list = new ArrayList<Tpl>();
				for (File file : files) {
					list.add(new FileTpl(file, root));
				}

				if (logger.isDebugEnabled()) {
					logger.debug("getListByPrefix(String) - end"); //$NON-NLS-1$
				}
				return list;
			} else {
				List<Tpl> returnList = new ArrayList<Tpl>(0);
				if (logger.isDebugEnabled()) {
					logger.debug("getListByPrefix(String) - end"); //$NON-NLS-1$
				}
				return returnList;
			}
		} else {
			List<Tpl> returnList = new ArrayList<Tpl>(0);
			if (logger.isDebugEnabled()) {
				logger.debug("getListByPrefix(String) - end"); //$NON-NLS-1$
			}
			return returnList;
		}
	}

	public List<String> getNameListByPrefix(String prefix) {
		if (logger.isDebugEnabled()) {
			logger.debug("getNameListByPrefix(String) - start"); //$NON-NLS-1$
		}

		List<Tpl> list = getListByPrefix(prefix);
		List<String> result = new ArrayList<String>(list.size());
		for (Tpl tpl : list) {
			result.add(tpl.getName());
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getNameListByPrefix(String) - end"); //$NON-NLS-1$
		}
		return result;
	}

	public List<Tpl> getChild(String path) {
		if (logger.isDebugEnabled()) {
			logger.debug("getChild(String) - start"); //$NON-NLS-1$
		}

		File file = new File(realPathResolver.get(path));
		File[] child = file.listFiles();
		if (child != null) {
			List<Tpl> list = new ArrayList<Tpl>(child.length);
			for (File f : child) {
				list.add(new FileTpl(f, root));
			}

			if (logger.isDebugEnabled()) {
				logger.debug("getChild(String) - end"); //$NON-NLS-1$
			}
			return list;
		} else {
			List<Tpl> returnList = new ArrayList<Tpl>(0);
			if (logger.isDebugEnabled()) {
				logger.debug("getChild(String) - end"); //$NON-NLS-1$
			}
			return returnList;
		}
	}

	public void rename(String orig, String dist) {
		if (logger.isDebugEnabled()) {
			logger.debug("rename(String, String) - start"); //$NON-NLS-1$
		}

		String os = realPathResolver.get(orig);
		File of = new File(os);
		String ds = realPathResolver.get(dist);
		File df = new File(ds);
		try {
			if (of.isDirectory()) {
				FileUtils.moveDirectory(of, df);
			} else {
				FileUtils.moveFile(of, df);
			}
		} catch (IOException e) {
			logger.error("Move template error: " + orig + " to " + dist, e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("rename(String, String) - end"); //$NON-NLS-1$
		}
	}

	public void save(String name, String source, boolean isDirectory) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(String, String, boolean) - start"); //$NON-NLS-1$
		}

		String real = realPathResolver.get(name);
		File f = new File(real);
		if (isDirectory) {
			f.mkdirs();
		} else {
			try {
				FileUtils.writeStringToFile(f, source, UTF8);
			} catch (IOException e) {
				logger.error("Save template error: " + name, e);
				throw new RuntimeException(e);
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("save(String, String, boolean) - end"); //$NON-NLS-1$
		}
	}

	public void save(String path, MultipartFile file) {
		if (logger.isDebugEnabled()) {
			logger.debug("save(String, MultipartFile) - start"); //$NON-NLS-1$
		}

		File f = new File(realPathResolver.get(path), file
				.getOriginalFilename());
		try {
			file.transferTo(f);
		} catch (IllegalStateException e) {
			logger.error("upload template error!", e);
		} catch (IOException e) {
			logger.error("upload template error!", e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("save(String, MultipartFile) - end"); //$NON-NLS-1$
		}
	}

	public void update(String name, String source) {
		if (logger.isDebugEnabled()) {
			logger.debug("update(String, String) - start"); //$NON-NLS-1$
		}

		String real = realPathResolver.get(name);
		File f = new File(real);
		try {
			FileUtils.writeStringToFile(f, source, UTF8);
		} catch (IOException e) {
			logger.error("Save template error: " + name, e);
			throw new RuntimeException(e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("update(String, String) - end"); //$NON-NLS-1$
		}
	}

	private String root;

	private RealPathResolver realPathResolver;

	@Autowired
	public void setRealPathResolver(RealPathResolver realPathResolver) {
		if (logger.isDebugEnabled()) {
			logger.debug("setRealPathResolver(RealPathResolver) - start"); //$NON-NLS-1$
		}

		this.realPathResolver = realPathResolver;
		root = realPathResolver.get("");

		if (logger.isDebugEnabled()) {
			logger.debug("setRealPathResolver(RealPathResolver) - end"); //$NON-NLS-1$
		}
	}

	private static class PrefixFilter implements FileFilter {
		private String prefix;

		public PrefixFilter(String prefix) {
			this.prefix = prefix;
		}

		public boolean accept(File file) {
			if (logger.isDebugEnabled()) {
				logger.debug("accept(File) - start"); //$NON-NLS-1$
			}

			String name = file.getName();
			boolean returnboolean = file.isFile() && name.startsWith(prefix);
			if (logger.isDebugEnabled()) {
				logger.debug("accept(File) - end"); //$NON-NLS-1$
			}
			return returnboolean;
		}
	}

}
