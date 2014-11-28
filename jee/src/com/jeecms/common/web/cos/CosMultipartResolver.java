package com.jeecms.common.web.cos;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;



import org.springframework.core.io.Resource;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.util.WebUtils;

/**
 * {@link MultipartResolver} implementation for Jason Hunter's <a
 * href="http://www.servlets.com/cos">COS (com.oreilly.servlet)</a>. Works with
 * a COS MultipartRequest underneath.
 * 
 * <p>
 * Provides "maxUploadSize" and "defaultEncoding" settings as bean properties;
 * see respective MultipartRequest constructor parameters for details. Default
 * maximum file size is unlimited; fallback encoding is the platform's default.
 * 
 * @author Juergen Hoeller
 * @since 06.10.2003
 * @see CosMultipartHttpServletRequest
 * @see com.CosMultipartRequest.servlet.MultipartRequest
 */
public class CosMultipartResolver implements MultipartResolver,
		ServletContextAware {

	/**
	 * Constant identifier for the mulipart content type :
	 * 'multipart/form-data'.
	 */
	public static final String MULTIPART_CONTENT_TYPE = "multipart/form-data";

	/** Logger available to subclasses */
	protected final Logger logger = Logger.getLogger(getClass());

	private int maxUploadSize = Integer.MAX_VALUE;

	private String defaultEncoding = WebUtils.DEFAULT_CHARACTER_ENCODING;

	private File uploadTempDir;

	/**
	 * Constructor for use as bean. Determines the servlet container's temporary
	 * directory via the ServletContext passed in as through the
	 * ServletContextAware interface (typically by a WebApplicationContext).
	 * 
	 * @see #setServletContext
	 * @see org.springframework.web.context.ServletContextAware
	 * @see org.springframework.web.context.WebApplicationContext
	 */
	public CosMultipartResolver() {
	}

	/**
	 * Constructor for standalone usage. Determines the servlet container's
	 * temporary directory via the given ServletContext.
	 * 
	 * @param servletContext
	 *            the ServletContext to use (must not be <code>null</code>)
	 * @throws IllegalArgumentException
	 *             if the supplied {@link ServletContext} is <code>null</code>
	 */
	public CosMultipartResolver(ServletContext servletContext) {
		this.uploadTempDir = WebUtils.getTempDir(servletContext);
	}

	/**
	 * Set the maximum allowed size (in bytes) before uploads are refused. -1
	 * indicates no limit (the default).
	 * 
	 * @param maxUploadSize
	 *            the maximum file size allowed
	 */
	public void setMaxUploadSize(int maxUploadSize) {
		this.maxUploadSize = maxUploadSize;
	}

	/**
	 * Return the maximum allowed size (in bytes) before uploads are refused.
	 */
	protected int getMaxUploadSize() {
		return maxUploadSize;
	}

	/**
	 * Set the default character encoding to use for parsing requests, to be
	 * applied to headers of individual parts and to form fields. Default is
	 * ISO-8859-1, according to the Servlet spec.
	 * <p>
	 * If the request specifies a character encoding itself, the request
	 * encoding will override this setting. This also allows for generically
	 * overriding the character encoding in a filter that invokes the
	 * ServletRequest.setCharacterEncoding method.
	 * 
	 * @param defaultEncoding
	 *            the character encoding to use
	 * @see #determineEncoding
	 * @see javax.servlet.ServletRequest#getCharacterEncoding
	 * @see javax.servlet.ServletRequest#setCharacterEncoding
	 * @see WebUtils#DEFAULT_CHARACTER_ENCODING
	 */
	public void setDefaultEncoding(String defaultEncoding) {
		this.defaultEncoding = defaultEncoding;
	}

	/**
	 * Return the default character encoding to use for parsing requests.
	 */
	protected String getDefaultEncoding() {
		return defaultEncoding;
	}

	/**
	 * Set the temporary directory where uploaded files get stored. Default is
	 * the servlet container's temporary directory for the web application.
	 * 
	 * @see org.springframework.web.util.WebUtils#TEMP_DIR_CONTEXT_ATTRIBUTE
	 */
	public void setUploadTempDir(Resource uploadTempDir) throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("setUploadTempDir(Resource) - start"); //$NON-NLS-1$
		}

		if (!uploadTempDir.exists() && !uploadTempDir.getFile().mkdirs()) {
			throw new IllegalArgumentException("Given uploadTempDir ["
					+ uploadTempDir + "] could not be created");
		}
		this.uploadTempDir = uploadTempDir.getFile();

		if (logger.isDebugEnabled()) {
			logger.debug("setUploadTempDir(Resource) - end"); //$NON-NLS-1$
		}
	}

	/**
	 * Return the temporary directory where uploaded files get stored.
	 */
	protected File getUploadTempDir() {
		return uploadTempDir;
	}

	public void setServletContext(ServletContext servletContext) {
		if (logger.isDebugEnabled()) {
			logger.debug("setServletContext(ServletContext) - start"); //$NON-NLS-1$
		}

		if (this.uploadTempDir == null) {
			this.uploadTempDir = WebUtils.getTempDir(servletContext);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("setServletContext(ServletContext) - end"); //$NON-NLS-1$
		}
	}

	public boolean isMultipart(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("isMultipart(HttpServletRequest) - start"); //$NON-NLS-1$
		}

		boolean returnboolean = request.getContentType() != null && request.getContentType().startsWith(MULTIPART_CONTENT_TYPE);
		if (logger.isDebugEnabled()) {
			logger.debug("isMultipart(HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return returnboolean;
	}

	public MultipartHttpServletRequest resolveMultipart(
			HttpServletRequest request) throws MultipartException {
		if (logger.isDebugEnabled()) {
			logger.debug("resolveMultipart(HttpServletRequest) - start"); //$NON-NLS-1$
		}

		try {
			CosMultipartRequest multipartRequest = newMultipartRequest(request);
			if (logger.isDebugEnabled()) {
				Set<String> fileNames = multipartRequest.getFileNames();
				for (String fileName : fileNames) {
					File file = multipartRequest.getFile(fileName);
					logger.debug("Found multipart file '"
							+ fileName
							+ "' of size "
							+ (file != null ? file.length() : 0)
							+ " bytes with original filename ["
							+ multipartRequest.getOriginalFileName(fileName)
							+ "]"
							+ (file != null ? "stored at ["
									+ file.getAbsolutePath() + "]" : "empty"));
				}
			}
			MultipartHttpServletRequest returnMultipartHttpServletRequest = new CosMultipartHttpServletRequest(request, multipartRequest);
			if (logger.isDebugEnabled()) {
				logger.debug("resolveMultipart(HttpServletRequest) - end"); //$NON-NLS-1$
			}
			return returnMultipartHttpServletRequest;
		} catch (IOException ex) {
			logger.error("resolveMultipart(HttpServletRequest)", ex); //$NON-NLS-1$

			// Unfortunately, COS always throws an IOException,
			// so we need to check the error message here!
			if (ex.getMessage().indexOf("exceeds limit") != -1) {
				throw new MaxUploadSizeExceededException(this.maxUploadSize, ex);
			} else {
				throw new MultipartException(
						"Could not parse multipart request", ex);
			}
		}
	}

	/**
	 * Create a com.oreilly.servlet.MultipartRequest for the given HTTP request.
	 * Can be overridden to use a custom subclass, e.g. for testing purposes.
	 * 
	 * @param request
	 *            current HTTP request
	 * @return the new MultipartRequest
	 * @throws IOException
	 *             if thrown by the MultipartRequest constructor
	 */
	protected CosMultipartRequest newMultipartRequest(HttpServletRequest request)
			throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("newMultipartRequest(HttpServletRequest) - start"); //$NON-NLS-1$
		}

		String tempPath = this.uploadTempDir.getAbsolutePath();
		String enc = determineEncoding(request);
		CosMultipartRequest returnCosMultipartRequest = new CosMultipartRequest(request, tempPath, this.maxUploadSize, enc);
		if (logger.isDebugEnabled()) {
			logger.debug("newMultipartRequest(HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return returnCosMultipartRequest;
	}

	/**
	 * Determine the encoding for the given request. Can be overridden in
	 * subclasses.
	 * <p>
	 * The default implementation checks the request encoding, falling back to
	 * the default encoding specified for this resolver.
	 * 
	 * @param request
	 *            current HTTP request
	 * @return the encoding for the request (never <code>null</code>)
	 * @see javax.servlet.ServletRequest#getCharacterEncoding
	 * @see #setDefaultEncoding
	 */
	protected String determineEncoding(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("determineEncoding(HttpServletRequest) - start"); //$NON-NLS-1$
		}

		String enc = request.getCharacterEncoding();
		if (enc == null) {
			enc = this.defaultEncoding;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("determineEncoding(HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return enc;
	}

	public void cleanupMultipart(MultipartHttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("cleanupMultipart(MultipartHttpServletRequest) - start"); //$NON-NLS-1$
		}

		CosMultipartRequest multipartRequest = ((CosMultipartHttpServletRequest) request)
				.getMultipartRequest();
		Set<String> fileNames = multipartRequest.getFileNames();
		for (String fileName : fileNames) {
			File file = multipartRequest.getFile(fileName);
			if (file != null) {
				if (file.exists()) {
					if (file.delete()) {
						if (logger.isDebugEnabled()) {
							logger.debug("Cleaned up multipart file '"
									+ fileName
									+ "' with original filename ["
									+ multipartRequest
											.getOriginalFileName(fileName)
									+ "], stored at [" + file.getAbsolutePath()
									+ "]");
						}
					} else {
						logger.warn("Could not delete multipart file '"
								+ fileName
								+ "' with original filename ["
								+ multipartRequest
										.getOriginalFileName(fileName)
								+ "], stored at [" + file.getAbsolutePath()
								+ "]");
					}
				} else {
					if (logger.isDebugEnabled()) {
						logger
								.debug("Multipart file '"
										+ fileName
										+ "' with original filename ["
										+ multipartRequest
												.getOriginalFileName(fileName)
										+ "] has already been moved - no cleanup necessary");
					}
				}
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("cleanupMultipart(MultipartHttpServletRequest) - end"); //$NON-NLS-1$
		}
	}

}
