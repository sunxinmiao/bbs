package com.jeecms.common.file;

import org.apache.log4j.Logger;

import static com.jeecms.common.web.Constants.SPT;

import java.io.File;
import java.io.FileFilter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.io.FilenameUtils;

import com.jeecms.common.image.ImageUtils;

/**
 * 文件包装类
 * 
 * 用于前台显示文件信息
 * 
 * @author liufang
 * 
 */
public class FileWrap {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(FileWrap.class);

	/**
	 * 可编辑的后缀名
	 */
	public static final String[] EDITABLE_EXT = new String[] { "html", "htm",
			"css", "js", "txt" };
	private File file;
	private String rootPath;
	private FileFilter filter;
	private List<FileWrap> child;
	private String filename;

	/**
	 * 构造器
	 * 
	 * @param file
	 *            待包装的文件
	 */
	public FileWrap(File file) {
		this(file, null);
	}

	/**
	 * 构造器
	 * 
	 * @param file
	 *            待包装的文件
	 * @param rootPath
	 *            根路径，用于计算相对路径
	 */
	public FileWrap(File file, String rootPath) {
		this(file, rootPath, null);
	}

	/**
	 * 构造器
	 * 
	 * @param file
	 *            待包装的文件
	 * @param rootPath
	 *            根路径，用于计算相对路径
	 * @param filter
	 *            文件过滤器，应用于所有子文件
	 */
	public FileWrap(File file, String rootPath, FileFilter filter) {
		this.file = file;
		this.rootPath = rootPath;
		this.filter = filter;
	}

	/**
	 * 是否允许编辑
	 * 
	 * @param ext
	 *            文件扩展名
	 * @return
	 */
	public static boolean editableExt(String ext) {
		if (logger.isDebugEnabled()) {
			logger.debug("editableExt(String) - start"); //$NON-NLS-1$
		}

		ext = ext.toLowerCase(Locale.ENGLISH);
		for (String s : EDITABLE_EXT) {
			if (s.equals(ext)) {
				if (logger.isDebugEnabled()) {
					logger.debug("editableExt(String) - end"); //$NON-NLS-1$
				}
				return true;
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("editableExt(String) - end"); //$NON-NLS-1$
		}
		return false;
	}

	/**
	 * 获得完整文件名，根据根路径(rootPath)。
	 * 
	 * @return
	 */
	public String getName() {
		if (logger.isDebugEnabled()) {
			logger.debug("getName() - start"); //$NON-NLS-1$
		}

		String path = file.getAbsolutePath();
		String relPath = path.substring(rootPath.length());
		String returnString = relPath.replace(File.separator, SPT);
		if (logger.isDebugEnabled()) {
			logger.debug("getName() - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * 获得文件路径，不包含文件名的路径。
	 * 
	 * @return
	 */
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

	/**
	 * 获得文件名，不包含路径的文件名。
	 * 
	 * 如没有指定名称，则返回文件自身的名称。
	 * 
	 * @return
	 */
	public String getFilename() {
		if (logger.isDebugEnabled()) {
			logger.debug("getFilename() - start"); //$NON-NLS-1$
		}

		String returnString = filename != null ? filename : file.getName();
		if (logger.isDebugEnabled()) {
			logger.debug("getFilename() - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * 扩展名
	 * 
	 * @return
	 */
	public String getExtension() {
		if (logger.isDebugEnabled()) {
			logger.debug("getExtension() - start"); //$NON-NLS-1$
		}

		String returnString = FilenameUtils.getExtension(file.getName());
		if (logger.isDebugEnabled()) {
			logger.debug("getExtension() - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * 最后修改时间。长整型(long)。
	 * 
	 * @return
	 */
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

	/**
	 * 最后修改时间。日期型(Timestamp)。
	 * 
	 * @return
	 */
	public Date getLastModifiedDate() {
		if (logger.isDebugEnabled()) {
			logger.debug("getLastModifiedDate() - start"); //$NON-NLS-1$
		}

		Date returnDate = new Timestamp(file.lastModified());
		if (logger.isDebugEnabled()) {
			logger.debug("getLastModifiedDate() - end"); //$NON-NLS-1$
		}
		return returnDate;
	}

	/**
	 * 文件大小，单位KB。
	 * 
	 * @return
	 */
	public long getSize() {
		if (logger.isDebugEnabled()) {
			logger.debug("getSize() - start"); //$NON-NLS-1$
		}

		long returnlong = file.length() / 1024 + 1;
		if (logger.isDebugEnabled()) {
			logger.debug("getSize() - end"); //$NON-NLS-1$
		}
		return returnlong;
	}

	/**
	 * 获得文件的图标名称
	 * <ul>
	 * <li>directory = folder</li>
	 * <li>jpg,jpeg = jpg</li>
	 * <li>gif = gif</li>
	 * <li>html,htm = html</li>
	 * <li>swf = swf</li>
	 * <li>txt = txt</li>
	 * <li>其他 = unknow</li>
	 * </ul>
	 * 
	 * @return
	 */
	public String getIco() {
		if (logger.isDebugEnabled()) {
			logger.debug("getIco() - start"); //$NON-NLS-1$
		}

		if (file.isDirectory()) {
			if (logger.isDebugEnabled()) {
				logger.debug("getIco() - end"); //$NON-NLS-1$
			}
			return "folder";
		}
		String ext = getExtension().toLowerCase();
		if (ext.equals("jpg") || ext.equals("jpeg")) {
			if (logger.isDebugEnabled()) {
				logger.debug("getIco() - end"); //$NON-NLS-1$
			}
			return "jpg";
		} else if (ext.equals("png")) {
			if (logger.isDebugEnabled()) {
				logger.debug("getIco() - end"); //$NON-NLS-1$
			}
			return "png";
		} else if (ext.equals("gif")) {
			if (logger.isDebugEnabled()) {
				logger.debug("getIco() - end"); //$NON-NLS-1$
			}
			return "gif";
		} else if (ext.equals("html") || ext.equals("htm")) {
			if (logger.isDebugEnabled()) {
				logger.debug("getIco() - end"); //$NON-NLS-1$
			}
			return "html";
		} else if (ext.equals("swf")) {
			if (logger.isDebugEnabled()) {
				logger.debug("getIco() - end"); //$NON-NLS-1$
			}
			return "swf";
		} else if (ext.equals("txt")) {
			if (logger.isDebugEnabled()) {
				logger.debug("getIco() - end"); //$NON-NLS-1$
			}
			return "txt";
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("getIco() - end"); //$NON-NLS-1$
			}
			return "unknow";
		}
	}

	/**
	 * 获得下级目录
	 * 
	 * 如果没有指定下级目录，则返回文件夹自身的下级文件，并运用FileFilter。
	 * 
	 * @return
	 */
	public List<FileWrap> getChild() {
		if (logger.isDebugEnabled()) {
			logger.debug("getChild() - start"); //$NON-NLS-1$
		}

		if (this.child != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("getChild() - end"); //$NON-NLS-1$
			}
			return this.child;
		}
		File[] files;
		if (filter == null) {
			files = getFile().listFiles();
		} else {
			files = getFile().listFiles(filter);
		}
		List<FileWrap> list = new ArrayList<FileWrap>();
		if (files != null) {
			Arrays.sort(files, new FileComparator());
			for (File f : files) {
				FileWrap fw = new FileWrap(f, rootPath, filter);
				list.add(fw);
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getChild() - end"); //$NON-NLS-1$
		}
		return list;
	}

	/**
	 * 获得被包装的文件
	 * 
	 * @return
	 */
	public File getFile() {
		return this.file;
	}

	/**
	 * 是否图片
	 * 
	 * @return
	 */
	public boolean isImage() {
		if (logger.isDebugEnabled()) {
			logger.debug("isImage() - start"); //$NON-NLS-1$
		}

		if (isDirectory()) {
			if (logger.isDebugEnabled()) {
				logger.debug("isImage() - end"); //$NON-NLS-1$
			}
			return false;
		}
		String ext = getExtension();
		boolean returnboolean = ImageUtils.isValidImageExt(ext);
		if (logger.isDebugEnabled()) {
			logger.debug("isImage() - end"); //$NON-NLS-1$
		}
		return returnboolean;
	}

	/**
	 * 是否可编辑
	 * 
	 * @return
	 */
	public boolean isEditable() {
		if (logger.isDebugEnabled()) {
			logger.debug("isEditable() - start"); //$NON-NLS-1$
		}

		if (isDirectory()) {
			if (logger.isDebugEnabled()) {
				logger.debug("isEditable() - end"); //$NON-NLS-1$
			}
			return false;
		}
		String ext = getExtension().toLowerCase();
		for (String s : EDITABLE_EXT) {
			if (s.equals(ext)) {
				if (logger.isDebugEnabled()) {
					logger.debug("isEditable() - end"); //$NON-NLS-1$
				}
				return true;
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("isEditable() - end"); //$NON-NLS-1$
		}
		return false;
	}

	/**
	 * 是否目录
	 * 
	 * @return
	 */
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

	/**
	 * 是否文件
	 * 
	 * @return
	 */
	public boolean isFile() {
		if (logger.isDebugEnabled()) {
			logger.debug("isFile() - start"); //$NON-NLS-1$
		}

		boolean returnboolean = file.isFile();
		if (logger.isDebugEnabled()) {
			logger.debug("isFile() - end"); //$NON-NLS-1$
		}
		return returnboolean;
	}

	/**
	 * 设置待包装的文件
	 * 
	 * @param file
	 */
	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * 指定名称
	 * 
	 * @param name
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * 指定下级目录
	 * 
	 * @param child
	 */
	public void setChild(List<FileWrap> child) {
		this.child = child;
	}

	/**
	 * 文件比较器，文件夹靠前排。
	 * 
	 * @author liufang
	 * 
	 */
	public static class FileComparator implements Comparator<File> {
		public int compare(File o1, File o2) {
			if (logger.isDebugEnabled()) {
				logger.debug("compare(File, File) - start"); //$NON-NLS-1$
			}

			if (o1.isDirectory() && !o2.isDirectory()) {
				int returnint = -1;
				if (logger.isDebugEnabled()) {
					logger.debug("compare(File, File) - end"); //$NON-NLS-1$
				}
				return returnint;
			} else if (!o1.isDirectory() && o2.isDirectory()) {
				if (logger.isDebugEnabled()) {
					logger.debug("compare(File, File) - end"); //$NON-NLS-1$
				}
				return 1;
			} else {
				int returnint = o1.compareTo(o2);
				if (logger.isDebugEnabled()) {
					logger.debug("compare(File, File) - end"); //$NON-NLS-1$
				}
				return returnint;
			}
		}
	}
}
