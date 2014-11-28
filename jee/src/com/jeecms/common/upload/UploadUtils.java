package com.jeecms.common.upload;

import org.apache.log4j.Logger;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

import com.jeecms.common.util.Num62;

public class UploadUtils {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(UploadUtils.class);

	/**
	 * 日期格式化对象
	 */
	public static final DateFormat MONTH_FORMAT = new SimpleDateFormat(
			"/yyyyMM/ddHHmmss");

	public static String generateFilename(String path, String ext) {
		if (logger.isDebugEnabled()) {
			logger.debug("generateFilename(String, String) - start"); //$NON-NLS-1$
		}

		String returnString = path + MONTH_FORMAT.format(new Date()) + RandomStringUtils.random(4, Num62.N36_CHARS) + "." + ext;
		if (logger.isDebugEnabled()) {
			logger.debug("generateFilename(String, String) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	protected static final Pattern ILLEGAL_CURRENT_FOLDER_PATTERN = Pattern
			.compile("^[^/]|[^/]$|/\\.{1,2}|\\\\|\\||:|\\?|\\*|\"|<|>|\\p{Cntrl}");

	/**
	 * Sanitizes a filename from certain chars.<br />
	 * 
	 * This method enforces the <code>forceSingleExtension</code> property and
	 * then replaces all occurrences of \, /, |, :, ?, *, &quot;, &lt;, &gt;,
	 * control chars by _ (underscore).
	 * 
	 * @param filename
	 *            a potentially 'malicious' filename
	 * @return sanitized filename
	 */
	public static String sanitizeFileName(final String filename) {
		if (logger.isDebugEnabled()) {
			logger.debug("sanitizeFileName(String) - start"); //$NON-NLS-1$
		}

		if (StringUtils.isBlank(filename))
			return filename;

		String name = forceSingleExtension(filename);

		// Remove \ / | : ? * " < > 'Control Chars' with _
		String returnString = name.replaceAll("\\\\|/|\\||:|\\?|\\*|\"|<|>|\\p{Cntrl}", "_");
		if (logger.isDebugEnabled()) {
			logger.debug("sanitizeFileName(String) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * Sanitizes a folder name from certain chars.<br />
	 * 
	 * This method replaces all occurrences of \, /, |, :, ?, *, &quot;, &lt;,
	 * &gt;, control chars by _ (underscore).
	 * 
	 * @param folderName
	 *            a potentially 'malicious' folder name
	 * @return sanitized folder name
	 */
	public static String sanitizeFolderName(final String folderName) {
		if (logger.isDebugEnabled()) {
			logger.debug("sanitizeFolderName(String) - start"); //$NON-NLS-1$
		}

		if (StringUtils.isBlank(folderName))
			return folderName;

		// Remove . \ / | : ? * " < > 'Control Chars' with _
		String returnString = folderName.replaceAll("\\.|\\\\|/|\\||:|\\?|\\*|\"|<|>|\\p{Cntrl}", "_");
		if (logger.isDebugEnabled()) {
			logger.debug("sanitizeFolderName(String) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * Checks whether a path complies with the FCKeditor File Browser <a href="http://docs.fckeditor.net/FCKeditor_2.x/Developers_Guide/Server_Side_Integration#File_Browser_Requests"
	 * target="_blank">rules</a>.
	 * 
	 * @param path
	 *            a potentially 'malicious' path
	 * @return <code>true</code> if path complies with the rules, else
	 *         <code>false</code>
	 */
	public static boolean isValidPath(final String path) {
		if (logger.isDebugEnabled()) {
			logger.debug("isValidPath(String) - start"); //$NON-NLS-1$
		}

		if (StringUtils.isBlank(path))
			return false;

		if (ILLEGAL_CURRENT_FOLDER_PATTERN.matcher(path).find())
			return false;

		if (logger.isDebugEnabled()) {
			logger.debug("isValidPath(String) - end"); //$NON-NLS-1$
		}
		return true;
	}

	/**
	 * Replaces all dots in a filename with underscores except the last one.
	 * 
	 * @param filename
	 *            filename to sanitize
	 * @return string with a single dot only
	 */
	public static String forceSingleExtension(final String filename) {
		if (logger.isDebugEnabled()) {
			logger.debug("forceSingleExtension(String) - start"); //$NON-NLS-1$
		}

		String returnString = filename.replaceAll("\\.(?![^.]+$)", "_");
		if (logger.isDebugEnabled()) {
			logger.debug("forceSingleExtension(String) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * Checks if a filename contains more than one dot.
	 * 
	 * @param filename
	 *            filename to check
	 * @return <code>true</code> if filename contains severals dots, else
	 *         <code>false</code>
	 */
	public static boolean isSingleExtension(final String filename) {
		if (logger.isDebugEnabled()) {
			logger.debug("isSingleExtension(String) - start"); //$NON-NLS-1$
		}

		boolean returnboolean = filename.matches("[^\\.]+\\.[^\\.]+");
		if (logger.isDebugEnabled()) {
			logger.debug("isSingleExtension(String) - end"); //$NON-NLS-1$
		}
		return returnboolean;
	}

	/**
	 * Checks a directory for existence and creates it if non-existent.
	 * 
	 * @param dir
	 *            directory to check/create
	 */
	public static void checkDirAndCreate(File dir) {
		if (logger.isDebugEnabled()) {
			logger.debug("checkDirAndCreate(File) - start"); //$NON-NLS-1$
		}

		if (!dir.exists())
			dir.mkdirs();

		if (logger.isDebugEnabled()) {
			logger.debug("checkDirAndCreate(File) - end"); //$NON-NLS-1$
		}
	}

	/**
	 * Iterates over a base name and returns the first non-existent file.<br />
	 * This method extracts a file's base name, iterates over it until the first
	 * non-existent appearance with <code>basename(n).ext</code>. Where n is a
	 * positive integer starting from one.
	 * 
	 * @param file
	 *            base file
	 * @return first non-existent file
	 */
	public static File getUniqueFile(final File file) {
		if (logger.isDebugEnabled()) {
			logger.debug("getUniqueFile(File) - start"); //$NON-NLS-1$
		}

		if (!file.exists())
			return file;

		File tmpFile = new File(file.getAbsolutePath());
		File parentDir = tmpFile.getParentFile();
		int count = 1;
		String extension = FilenameUtils.getExtension(tmpFile.getName());
		String baseName = FilenameUtils.getBaseName(tmpFile.getName());
		do {
			tmpFile = new File(parentDir, baseName + "(" + count++ + ")."
					+ extension);
		} while (tmpFile.exists());

		if (logger.isDebugEnabled()) {
			logger.debug("getUniqueFile(File) - end"); //$NON-NLS-1$
		}
		return tmpFile;
	}

	public static void main(String[] args) {
		if (logger.isDebugEnabled()) {
			logger.debug("main(String[]) - start"); //$NON-NLS-1$
		}

		System.out.println(generateFilename("/base", "gif"));

		if (logger.isDebugEnabled()) {
			logger.debug("main(String[]) - end"); //$NON-NLS-1$
		}
	}

}
