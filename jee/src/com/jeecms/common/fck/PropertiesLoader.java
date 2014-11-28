package com.jeecms.common.fck;

import org.apache.log4j.Logger;




import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Properties;


/**
 * Manages FCKeditor.Java properties files.
 * <p>
 * It manages/loads the properties files in the following order:
 * <ol>
 * <li>the default properties as defined <a
 * href="http://java.fckeditor.net/properties.html">here</a>,
 * <li>the user-defined properties (<code>fckeditor.properties</code>) if
 * present.
 * </ol>
 * This means that user-defined properties <em>override</em> default ones. In
 * the backend it utilizes the regular {@link Properties} class.
 * </p>
 * <p>
 * Moreover, you can set properties programmatically too but make sure to
 * override them <em>before</em> the first call of that specific property.
 * 
 * @version $Id: PropertiesLoader.java,v 1.1 2011/12/26 03:47:53 Administrator Exp $
 */
public class PropertiesLoader {
	private static final Logger logger = Logger.getLogger(PropertiesLoader.class);
	private static final String DEFAULT_FILENAME = "default.properties";
	private static final String LOCAL_PROPERTIES = "/fckeditor.properties";
	private static Properties properties = new Properties();

	static {

		// 1. load library defaults
		InputStream in = PropertiesLoader.class
				.getResourceAsStream(DEFAULT_FILENAME);

		if (in == null) {
			logger.error(DEFAULT_FILENAME + " not found" );
			throw new RuntimeException(DEFAULT_FILENAME + " not found");
		} else {
			if (!(in instanceof BufferedInputStream))
				in = new BufferedInputStream(in);

			try {
				properties.load(in);
				in.close();
				logger.debug(DEFAULT_FILENAME + " loaded" );
			} catch (Exception e) {
				logger.error("Error while processing "+ DEFAULT_FILENAME);
				throw new RuntimeException("Error while processing "
						+ DEFAULT_FILENAME, e);
			}
		}

		// 2. load user defaults if present
		InputStream in2 = PropertiesLoader.class
				.getResourceAsStream(LOCAL_PROPERTIES);

		if (in2 == null) {
			logger.info(LOCAL_PROPERTIES + " not found");
		} else {

			if (!(in2 instanceof BufferedInputStream))
				in2 = new BufferedInputStream(in2);

			try {
				properties.load(in2);
				in2.close();
				logger.debug(LOCAL_PROPERTIES+" loaded");
			} catch (Exception e) {
				logger.error(LOCAL_PROPERTIES + "Error while processing {}");
				throw new RuntimeException("Error while processing "
						+ LOCAL_PROPERTIES, e);
			}

		}
	}

	/**
	 * Searches for the property with the specified key in this property list.
	 * 
	 * @see Properties#getProperty(String)
	 */
	public static String getProperty(final String key) {
		if (logger.isDebugEnabled()) {
			logger.debug("getProperty(String) - start"); //$NON-NLS-1$
		}

		String returnString = properties.getProperty(key);
		if (logger.isDebugEnabled()) {
			logger.debug("getProperty(String) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * Sets the property with the specified key in this property list.
	 * 
	 * @see Properties#setProperty(String, String)
	 */
	public static void setProperty(final String key, final String value) {
		if (logger.isDebugEnabled()) {
			logger.debug("setProperty(String, String) - start"); //$NON-NLS-1$
		}

		properties.setProperty(key, value);

		if (logger.isDebugEnabled()) {
			logger.debug("setProperty(String, String) - end"); //$NON-NLS-1$
		}
	}

	/**
	 * Returns <code>connector.resourceType.file.path</code> property
	 */
	public static String getFileResourceTypePath() {
		if (logger.isDebugEnabled()) {
			logger.debug("getFileResourceTypePath() - start"); //$NON-NLS-1$
		}

		String returnString = properties.getProperty("connector.resourceType.file.path");
		if (logger.isDebugEnabled()) {
			logger.debug("getFileResourceTypePath() - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * Returns <code>connector.resourceType.flash.path</code> property
	 */
	public static String getFlashResourceTypePath() {
		if (logger.isDebugEnabled()) {
			logger.debug("getFlashResourceTypePath() - start"); //$NON-NLS-1$
		}

		String returnString = properties.getProperty("connector.resourceType.flash.path");
		if (logger.isDebugEnabled()) {
			logger.debug("getFlashResourceTypePath() - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * Returns <code>connector.resourceType.image.path</code> property
	 */
	public static String getImageResourceTypePath() {
		if (logger.isDebugEnabled()) {
			logger.debug("getImageResourceTypePath() - start"); //$NON-NLS-1$
		}

		String returnString = properties.getProperty("connector.resourceType.image.path");
		if (logger.isDebugEnabled()) {
			logger.debug("getImageResourceTypePath() - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * Returns <code>connector.resourceType.media.path</code> property
	 */
	public static String getMediaResourceTypePath() {
		if (logger.isDebugEnabled()) {
			logger.debug("getMediaResourceTypePath() - start"); //$NON-NLS-1$
		}

		String returnString = properties.getProperty("connector.resourceType.media.path");
		if (logger.isDebugEnabled()) {
			logger.debug("getMediaResourceTypePath() - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * Returns <code>connector.resourceType.file.extensions.allowed</code>
	 * property
	 */
	public static String getFileResourceTypeAllowedExtensions() {
		if (logger.isDebugEnabled()) {
			logger.debug("getFileResourceTypeAllowedExtensions() - start"); //$NON-NLS-1$
		}

		String returnString = properties.getProperty("connector.resourceType.file.extensions.allowed");
		if (logger.isDebugEnabled()) {
			logger.debug("getFileResourceTypeAllowedExtensions() - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * Returns <code>connector.resourceType.file.extensions.denied</code>
	 * property
	 */
	public static String getFileResourceTypeDeniedExtensions() {
		if (logger.isDebugEnabled()) {
			logger.debug("getFileResourceTypeDeniedExtensions() - start"); //$NON-NLS-1$
		}

		String returnString = properties.getProperty("connector.resourceType.file.extensions.denied");
		if (logger.isDebugEnabled()) {
			logger.debug("getFileResourceTypeDeniedExtensions() - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * Returns <code>connector.resourceType.flash.extensions.allowed</code>
	 * property
	 */
	public static String getFlashResourceTypeAllowedExtensions() {
		if (logger.isDebugEnabled()) {
			logger.debug("getFlashResourceTypeAllowedExtensions() - start"); //$NON-NLS-1$
		}

		String returnString = properties.getProperty("connector.resourceType.flash.extensions.allowed");
		if (logger.isDebugEnabled()) {
			logger.debug("getFlashResourceTypeAllowedExtensions() - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * Returns <code>connector.resourceType.flash.extensions.denied</code>
	 * property
	 */
	public static String getFlashResourceTypeDeniedExtensions() {
		if (logger.isDebugEnabled()) {
			logger.debug("getFlashResourceTypeDeniedExtensions() - start"); //$NON-NLS-1$
		}

		String returnString = properties.getProperty("connector.resourceType.flash.extensions.denied");
		if (logger.isDebugEnabled()) {
			logger.debug("getFlashResourceTypeDeniedExtensions() - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * Returns <code>connector.resourceType.image.extensions.allowed</code>
	 * property
	 */
	public static String getImageResourceTypeAllowedExtensions() {
		if (logger.isDebugEnabled()) {
			logger.debug("getImageResourceTypeAllowedExtensions() - start"); //$NON-NLS-1$
		}

		String returnString = properties.getProperty("connector.resourceType.image.extensions.allowed");
		if (logger.isDebugEnabled()) {
			logger.debug("getImageResourceTypeAllowedExtensions() - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * Returns <code>connector.resourceType.image.extensions.denied</code>
	 * property
	 */
	public static String getImageResourceTypeDeniedExtensions() {
		if (logger.isDebugEnabled()) {
			logger.debug("getImageResourceTypeDeniedExtensions() - start"); //$NON-NLS-1$
		}

		String returnString = properties.getProperty("connector.resourceType.image.extensions.denied");
		if (logger.isDebugEnabled()) {
			logger.debug("getImageResourceTypeDeniedExtensions() - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * Returns <code>connector.resourceType.media.extensions.allowed</code>
	 * property
	 */
	public static String getMediaResourceTypeAllowedExtensions() {
		if (logger.isDebugEnabled()) {
			logger.debug("getMediaResourceTypeAllowedExtensions() - start"); //$NON-NLS-1$
		}

		String returnString = properties.getProperty("connector.resourceType.media.extensions.allowed");
		if (logger.isDebugEnabled()) {
			logger.debug("getMediaResourceTypeAllowedExtensions() - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * Returns <code>connector.resourceType.media.extensions.denied</code>
	 * property
	 */
	public static String getMediaResourceTypeDeniedExtensions() {
		if (logger.isDebugEnabled()) {
			logger.debug("getMediaResourceTypeDeniedExtensions() - start"); //$NON-NLS-1$
		}

		String returnString = properties.getProperty("connector.resourceType.media.extensions.denied");
		if (logger.isDebugEnabled()) {
			logger.debug("getMediaResourceTypeDeniedExtensions() - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * Returns <code>connector.userFilesPath</code> property
	 */
	public static String getUserFilesPath() {
		if (logger.isDebugEnabled()) {
			logger.debug("getUserFilesPath() - start"); //$NON-NLS-1$
		}

		String returnString = properties.getProperty("connector.userFilesPath");
		if (logger.isDebugEnabled()) {
			logger.debug("getUserFilesPath() - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * Returns <code>connector.userFilesAbsolutePath</code> property
	 */
	public static String getUserFilesAbsolutePath() {
		if (logger.isDebugEnabled()) {
			logger.debug("getUserFilesAbsolutePath() - start"); //$NON-NLS-1$
		}

		String returnString = properties.getProperty("connector.userFilesAbsolutePath");
		if (logger.isDebugEnabled()) {
			logger.debug("getUserFilesAbsolutePath() - end"); //$NON-NLS-1$
		}
		return returnString;
	}
}
