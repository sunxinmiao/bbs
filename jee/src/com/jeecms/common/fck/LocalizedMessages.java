package com.jeecms.common.fck;

import org.apache.log4j.Logger;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.LocaleResolver;

import com.jeecms.common.web.springmvc.MessageResolver;

/**
 * Provides access to localized messages (properties).
 * <p>
 * Localized messages are loaded for a particular locale from a HTTP request.
 * The locale is resolved by the current {@link LocaleResolver}
 * instance/singleton. If a locale or a bundle for a locale cannot be found,
 * default messages are used.
 * </p>
 * <p>
 * Note: Loaded messages are cached per locale, any subsequent call of the same
 * locale will be served by the cache instead of another resource bundle
 * retrieval.
 * </p>
 * 
 * @version $Id: LocalizedMessages.java,v 1.1 2011/12/26 03:47:53 Administrator Exp $
 */
public class LocalizedMessages {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(LocalizedMessages.class);

	/**
	 * Searches for the message with the specified key in this message list.
	 * 
	 * @see Properties#getProperty(String)
	 */
	private static String getMessage(HttpServletRequest request, String key,
			Object... args) {
		if (logger.isDebugEnabled()) {
			logger.debug("getMessage(HttpServletRequest, String, Object) - start"); //$NON-NLS-1$
		}

		String returnString = MessageResolver.getMessage(request, key, args);
		if (logger.isDebugEnabled()) {
			logger.debug("getMessage(HttpServletRequest, String, Object) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * Returns localized <code>fck.editor.compatibleBrowser.yes</code> property.
	 */
	public static String getCompatibleBrowserYes(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("getCompatibleBrowserYes(HttpServletRequest) - start"); //$NON-NLS-1$
		}

		String returnString = getMessage(request, "fck.editor.compatibleBrowser.yes");
		if (logger.isDebugEnabled()) {
			logger.debug("getCompatibleBrowserYes(HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return returnString; //$NON-NLS-1$
	}

	/** Returns localized <code>fck.editor.compatibleBrowser.no</code> property. */
	public static String getCompatibleBrowserNo(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("getCompatibleBrowserNo(HttpServletRequest) - start"); //$NON-NLS-1$
		}

		String returnString = getMessage(request, "fck.editor.compatibleBrowser.no");
		if (logger.isDebugEnabled()) {
			logger.debug("getCompatibleBrowserNo(HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return returnString; //$NON-NLS-1$
	}

	/**
	 * Returns localized <code>fck.connector.fileUpload.enabled</code> property.
	 */
	public static String getFileUploadEnabled(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("getFileUploadEnabled(HttpServletRequest) - start"); //$NON-NLS-1$
		}

		String returnString = getMessage(request, "fck.connector.fileUpload.enabled");
		if (logger.isDebugEnabled()) {
			logger.debug("getFileUploadEnabled(HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return returnString; //$NON-NLS-1$
	}

	/**
	 * Returns localized <code>fck.connector.fileUpload.disabled</code>
	 * property.
	 */
	public static String getFileUploadDisabled(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("getFileUploadDisabled(HttpServletRequest) - start"); //$NON-NLS-1$
		}

		String returnString = getMessage(request, "fck.connector.fileUpload.disabled");
		if (logger.isDebugEnabled()) {
			logger.debug("getFileUploadDisabled(HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return returnString; //$NON-NLS-1$
	}

	/**
	 * Returns localized <code>fck.connector.file_renamed_warning</code>
	 * property.
	 * 
	 * @param newFileName
	 *            the new filename of the warning
	 * @return localized message with new filename
	 */
	public static String getFileRenamedWarning(HttpServletRequest request,
			String newFileName) {
		if (logger.isDebugEnabled()) {
			logger.debug("getFileRenamedWarning(HttpServletRequest, String) - start"); //$NON-NLS-1$
		}

		String returnString = getMessage(request, "fck.connector.fileUpload.file_renamed_warning", newFileName);
		if (logger.isDebugEnabled()) {
			logger.debug("getFileRenamedWarning(HttpServletRequest, String) - end"); //$NON-NLS-1$
		}
		return returnString; //$NON-NLS-1$
	}

	/**
	 * Returns localized
	 * <code>fck.connector.fileUpload.invalid_file_type_specified</code>
	 * property.
	 */
	public static String getInvalidFileTypeSpecified(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("getInvalidFileTypeSpecified(HttpServletRequest) - start"); //$NON-NLS-1$
		}

		String returnString = getMessage(request, "fck.connector.fileUpload.invalid_file_type_specified");
		if (logger.isDebugEnabled()) {
			logger.debug("getInvalidFileTypeSpecified(HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return returnString; //$NON-NLS-1$
	}

	/**
	 * Returns localized <code>fck.connector.fileUpload.write_error</code>
	 * property.
	 */
	public static String getFileUploadWriteError(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("getFileUploadWriteError(HttpServletRequest) - start"); //$NON-NLS-1$
		}

		String returnString = getMessage(request, "fck.connector.fileUpload.write_error");
		if (logger.isDebugEnabled()) {
			logger.debug("getFileUploadWriteError(HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return returnString; //$NON-NLS-1$
	}

	/**
	 * Returns localized <code>fck.connector.getResources.enabled</code>
	 * property.
	 */
	public static String getGetResourcesEnabled(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("getGetResourcesEnabled(HttpServletRequest) - start"); //$NON-NLS-1$
		}

		String returnString = getMessage(request, "fck.connector.getResources.enabled");
		if (logger.isDebugEnabled()) {
			logger.debug("getGetResourcesEnabled(HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return returnString; //$NON-NLS-1$
	}

	/**
	 * Returns localized <code>fck.connector.getResources.disabled</code>
	 * property.
	 */
	public static String getGetResourcesDisabled(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("getGetResourcesDisabled(HttpServletRequest) - start"); //$NON-NLS-1$
		}

		String returnString = getMessage(request, "fck.connector.getResources.disabled");
		if (logger.isDebugEnabled()) {
			logger.debug("getGetResourcesDisabled(HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return returnString; //$NON-NLS-1$
	}

	/**
	 * Returns localized <code>fck.connector.getResources.read_error</code>
	 * property.
	 */
	public static String getGetResourcesReadError(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("getGetResourcesReadError(HttpServletRequest) - start"); //$NON-NLS-1$
		}

		String returnString = getMessage(request, "fck.connector.getResources.read_error");
		if (logger.isDebugEnabled()) {
			logger.debug("getGetResourcesReadError(HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return returnString; //$NON-NLS-1$
	}

	/**
	 * Returns localized <code>fck.connector.createFolder.enabled</code>
	 * property.
	 */
	public static String getCreateFolderEnabled(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("getCreateFolderEnabled(HttpServletRequest) - start"); //$NON-NLS-1$
		}

		String returnString = getMessage(request, "fck.connector.createFolder.enabled");
		if (logger.isDebugEnabled()) {
			logger.debug("getCreateFolderEnabled(HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return returnString; //$NON-NLS-1$
	}

	/**
	 * Returns localized <code>fck.connector.createFolder.disabled</code>
	 * property.
	 */
	public static String getCreateFolderDisabled(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("getCreateFolderDisabled(HttpServletRequest) - start"); //$NON-NLS-1$
		}

		String returnString = getMessage(request, "fck.connector.createFolder.disabled");
		if (logger.isDebugEnabled()) {
			logger.debug("getCreateFolderDisabled(HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return returnString; //$NON-NLS-1$
	}

	/**
	 * Returns localized <code>fck.connector.invalid_command_specified</code>
	 * property.
	 */
	public static String getInvalidCommandSpecified(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("getInvalidCommandSpecified(HttpServletRequest) - start"); //$NON-NLS-1$
		}

		String returnString = getMessage(request, "fck.connector.invalid_command_specified");
		if (logger.isDebugEnabled()) {
			logger.debug("getInvalidCommandSpecified(HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return returnString; //$NON-NLS-1$
	}

	/**
	 * Returns localized
	 * <code>fck.connector.createFolder.folder_already_exists_error</code>
	 * property.
	 */
	public static String getFolderAlreadyExistsError(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("getFolderAlreadyExistsError(HttpServletRequest) - start"); //$NON-NLS-1$
		}

		String returnString = getMessage(request, "fck.connector.createFolder.folder_already_exists_error");
		if (logger.isDebugEnabled()) {
			logger.debug("getFolderAlreadyExistsError(HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return returnString; //$NON-NLS-1$
	}

	/**
	 * Returns localized
	 * <code>fck.connector.createFolder.invalid_new_folder_name_specified</code>
	 * property.
	 */
	public static String getInvalidNewFolderNameSpecified(
			HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("getInvalidNewFolderNameSpecified(HttpServletRequest) - start"); //$NON-NLS-1$
		}

		String returnString = getMessage(request, "fck.connector.createFolder.invalid_new_folder_name_specified");
		if (logger.isDebugEnabled()) {
			logger.debug("getInvalidNewFolderNameSpecified(HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return returnString; //$NON-NLS-1$
	}

	/**
	 * Returns localized <code>fck.connector.createFolder.write_error</code>
	 * property.
	 */
	public static String getCreateFolderWriteError(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("getCreateFolderWriteError(HttpServletRequest) - start"); //$NON-NLS-1$
		}

		String returnString = getMessage(request, "fck.connector.createFolder.write_error");
		if (logger.isDebugEnabled()) {
			logger.debug("getCreateFolderWriteError(HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return returnString; //$NON-NLS-1$
	}

	/**
	 * Returns localized
	 * <code>fck.connector.invalid_resource_type_specified</code> property.
	 */
	public static String getInvalidResouceTypeSpecified(
			HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("getInvalidResouceTypeSpecified(HttpServletRequest) - start"); //$NON-NLS-1$
		}

		String returnString = getMessage(request, "fck.connector.invalid_resource_type_specified");
		if (logger.isDebugEnabled()) {
			logger.debug("getInvalidResouceTypeSpecified(HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return returnString; //$NON-NLS-1$
	}

	/**
	 * Returns localized
	 * <code>fck.connector.invalid_current_folder_specified</code> property.
	 */
	public static String getInvalidCurrentFolderSpecified(
			HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("getInvalidCurrentFolderSpecified(HttpServletRequest) - start"); //$NON-NLS-1$
		}

		String returnString = getMessage(request, "fck.connector.invalid_current_folder_specified");
		if (logger.isDebugEnabled()) {
			logger.debug("getInvalidCurrentFolderSpecified(HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return returnString; //$NON-NLS-1$
	}

}
