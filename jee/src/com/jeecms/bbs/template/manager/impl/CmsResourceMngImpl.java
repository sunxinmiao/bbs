package com.jeecms.bbs.template.manager.impl;

import org.apache.log4j.Logger;

import static com.jeecms.bbs.web.FrontUtils.RES_EXP;
import static com.jeecms.common.web.Constants.SPT;
import static com.jeecms.common.web.Constants.UTF8;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jeecms.core.entity.CmsSite;
import com.jeecms.bbs.template.manager.CmsResourceMng;
import com.jeecms.bbs.web.FrontUtils;
import com.jeecms.common.file.FileWrap;
import com.jeecms.common.file.FileWrap.FileComparator;
import com.jeecms.common.util.Zipper.FileEntry;
import com.jeecms.common.web.springmvc.RealPathResolver;

@Service
public class CmsResourceMngImpl implements CmsResourceMng {
	private static final Logger logger = Logger.getLogger(CmsResourceMngImpl.class);

	public List<FileWrap> listFile(String path, boolean dirAndEditable) {
		if (logger.isDebugEnabled()) {
			logger.debug("listFile(String, boolean) - start"); //$NON-NLS-1$
		}

		File parent = new File(realPathResolver.get(path));
		if (parent.exists()) {
			File[] files;
			if (dirAndEditable) {
				files = parent.listFiles(filter);
			} else {
				files = parent.listFiles();
			}
			Arrays.sort(files, new FileComparator());
			List<FileWrap> list = new ArrayList<FileWrap>(files.length);
			for (File f : files) {
				list.add(new FileWrap(f, realPathResolver.get("")));
			}

			if (logger.isDebugEnabled()) {
				logger.debug("listFile(String, boolean) - end"); //$NON-NLS-1$
			}
			return list;
		} else {
			List<FileWrap> returnList = new ArrayList<FileWrap>(0);
			if (logger.isDebugEnabled()) {
				logger.debug("listFile(String, boolean) - end"); //$NON-NLS-1$
			}
			return returnList;
		}
	}

	public boolean createDir(String path, String dirName) {
		if (logger.isDebugEnabled()) {
			logger.debug("createDir(String, String) - start"); //$NON-NLS-1$
		}

		File parent = new File(realPathResolver.get(path));
		parent.mkdirs();
		File dir = new File(parent, dirName);
		boolean returnboolean = dir.mkdir();
		if (logger.isDebugEnabled()) {
			logger.debug("createDir(String, String) - end"); //$NON-NLS-1$
		}
		return returnboolean;
	}

	public void saveFile(String path, MultipartFile file)
			throws IllegalStateException, IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("saveFile(String, MultipartFile) - start"); //$NON-NLS-1$
		}

		File dest = new File(realPathResolver.get(path), file
				.getOriginalFilename());
		file.transferTo(dest);

		if (logger.isDebugEnabled()) {
			logger.debug("saveFile(String, MultipartFile) - end"); //$NON-NLS-1$
		}
	}

	public void createFile(String path, String filename, String data)
			throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("createFile(String, String, String) - start"); //$NON-NLS-1$
		}

		File parent = new File(realPathResolver.get(path));
		parent.mkdirs();
		File file = new File(parent, filename);
		FileUtils.writeStringToFile(file, data, UTF8);

		if (logger.isDebugEnabled()) {
			logger.debug("createFile(String, String, String) - end"); //$NON-NLS-1$
		}
	}

	public String readFile(String name) throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("readFile(String) - start"); //$NON-NLS-1$
		}

		File file = new File(realPathResolver.get(name));
		String returnString = FileUtils.readFileToString(file, UTF8);
		if (logger.isDebugEnabled()) {
			logger.debug("readFile(String) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	public void updateFile(String name, String data) throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("updateFile(String, String) - start"); //$NON-NLS-1$
		}

		File file = new File(realPathResolver.get(name));
		FileUtils.writeStringToFile(file, data, UTF8);

		if (logger.isDebugEnabled()) {
			logger.debug("updateFile(String, String) - end"); //$NON-NLS-1$
		}
	}

	public int delete(String[] names) {
		if (logger.isDebugEnabled()) {
			logger.debug("delete(String[]) - start"); //$NON-NLS-1$
		}

		int count = 0;
		File f;
		for (String name : names) {
			f = new File(realPathResolver.get(name));
			if (FileUtils.deleteQuietly(f)) {
				count++;
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("delete(String[]) - end"); //$NON-NLS-1$
		}
		return count;
	}

	public void rename(String origName, String destName) {
		if (logger.isDebugEnabled()) {
			logger.debug("rename(String, String) - start"); //$NON-NLS-1$
		}

		File orig = new File(realPathResolver.get(origName));
		File dest = new File(realPathResolver.get(destName));
		orig.renameTo(dest);

		if (logger.isDebugEnabled()) {
			logger.debug("rename(String, String) - end"); //$NON-NLS-1$
		}
	}

	public void copyTplAndRes(CmsSite from, CmsSite to) throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("copyTplAndRes(CmsSite, CmsSite) - start"); //$NON-NLS-1$
		}

		String fromSol = from.getTplSolution();
		String toSol = to.getTplSolution();
		File tplFrom = new File(realPathResolver.get(from.getTplPath()),
				fromSol);
		File tplTo = new File(realPathResolver.get(to.getTplPath()), toSol);
		FileUtils.copyDirectory(tplFrom, tplTo);
		File resFrom = new File(realPathResolver.get(from.getResPath()),
				fromSol);
		if (resFrom.exists()) {
			File resTo = new File(realPathResolver.get(to.getResPath()), toSol);
			FileUtils.copyDirectory(resFrom, resTo);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("copyTplAndRes(CmsSite, CmsSite) - end"); //$NON-NLS-1$
		}
	}

	public void delTplAndRes(CmsSite site) throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("delTplAndRes(CmsSite) - start"); //$NON-NLS-1$
		}

		File tpl = new File(realPathResolver.get(site.getTplPath()));
		File res = new File(realPathResolver.get(site.getResPath()));
		FileUtils.deleteDirectory(tpl);
		FileUtils.deleteDirectory(res);

		if (logger.isDebugEnabled()) {
			logger.debug("delTplAndRes(CmsSite) - end"); //$NON-NLS-1$
		}
	}

	public String[] getSolutions(String path) {
		if (logger.isDebugEnabled()) {
			logger.debug("getSolutions(String) - start"); //$NON-NLS-1$
		}

		File tpl = new File(realPathResolver.get(path));
		String[] returnStringArray = tpl.list(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				if (logger.isDebugEnabled()) {
					logger.debug("$FilenameFilter.accept(File, String) - start"); //$NON-NLS-1$
				}

				boolean returnboolean = dir.isDirectory();
				if (logger.isDebugEnabled()) {
					logger.debug("$FilenameFilter.accept(File, String) - end"); //$NON-NLS-1$
				}
				return returnboolean;
			}
		});
		if (logger.isDebugEnabled()) {
			logger.debug("getSolutions(String) - end"); //$NON-NLS-1$
		}
		return returnStringArray;
	}

	public List<FileEntry> export(CmsSite site, String solution) {
		if (logger.isDebugEnabled()) {
			logger.debug("export(CmsSite, String) - start"); //$NON-NLS-1$
		}

		List<FileEntry> fileEntrys = new ArrayList<FileEntry>();
		File tpl = new File(realPathResolver.get(site.getTplPath()), solution);
		fileEntrys.add(new FileEntry("", "", tpl));
		File res = new File(realPathResolver.get(site.getResPath()), solution);
		if (res.exists()) {
			for (File r : res.listFiles()) {
				fileEntrys.add(new FileEntry(FrontUtils.RES_EXP, r));
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("export(CmsSite, String) - end"); //$NON-NLS-1$
		}
		return fileEntrys;
	}

	@SuppressWarnings("unchecked")
	public void imoport(File file, CmsSite site) throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("imoport(File, CmsSite) - start"); //$NON-NLS-1$
		}

		String resRoot = site.getResPath();
		String tplRoot = site.getTplPath();
		// 用默认编码或UTF-8编码解压会乱码！windows7的原因吗？
		ZipFile zip = new ZipFile(file, "GBK");
		ZipEntry entry;
		String name;
		String filename;
		File outFile;
		File pfile;
		byte[] buf = new byte[1024];
		int len;
		InputStream is = null;
		OutputStream os = null;
		String solution = null;

		Enumeration<ZipEntry> en = zip.getEntries();
		while (en.hasMoreElements()) {
			name = en.nextElement().getName();
			if (!name.startsWith(RES_EXP)) {
				solution = name.substring(0, name.indexOf("/"));
				break;
			}
		}
		if (solution == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("imoport(File, CmsSite) - end"); //$NON-NLS-1$
			}
			return;
		}
		en = zip.getEntries();
		while (en.hasMoreElements()) {
			entry = en.nextElement();
			if (!entry.isDirectory()) {
				name = entry.getName();
				logger.debug("unzip file："+name);
				// 模板还是资源
				if (name.startsWith(RES_EXP)) {
					filename = resRoot + "/" + solution
							+ name.substring(RES_EXP.length());
				} else {
					filename = tplRoot + SPT + name;
				}
				logger.debug("解压地址："+filename);
				outFile = new File(realPathResolver.get(filename));
				pfile = outFile.getParentFile();
				if (!pfile.exists()) {
					pfile.mkdirs();
				}
				try {
					is = zip.getInputStream(entry);
					os = new FileOutputStream(outFile);
					while ((len = is.read(buf)) != -1) {
						os.write(buf, 0, len);
					}
				} finally {
					if (is != null) {
						is.close();
						is = null;
					}
					if (os != null) {
						os.close();
						os = null;
					}
				}
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("imoport(File, CmsSite) - end"); //$NON-NLS-1$
		}
	}

	// 文件夹和可编辑文件则显示
	private FileFilter filter = new FileFilter() {
		public boolean accept(File file) {
			if (logger.isDebugEnabled()) {
				logger.debug("$FileFilter.accept(File) - start"); //$NON-NLS-1$
			}

			boolean returnboolean = file.isDirectory() || FileWrap.editableExt(FilenameUtils.getExtension(file.getName()));
			if (logger.isDebugEnabled()) {
				logger.debug("$FileFilter.accept(File) - end"); //$NON-NLS-1$
			}
			return returnboolean;
		}
	};

	private RealPathResolver realPathResolver;

	@Autowired
	public void setRealPathResolver(RealPathResolver realPathResolver) {
		this.realPathResolver = realPathResolver;
	}
}
