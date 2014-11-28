package com.jeecms.common.web.cos;

import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;



import org.springframework.util.FileCopyUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.AbstractMultipartHttpServletRequest;

/**
 * MultipartHttpServletRequest implementation for Jason Hunter's COS. Wraps a
 * COS MultipartRequest with Spring MultipartFile instances.
 * 
 * <p>
 * Not intended for direct application usage. Application code can cast to this
 * implementation to access the underlying COS MultipartRequest, if it ever
 * needs to.
 * 
 * @author Juergen Hoeller
 * @since 06.10.2003
 * @see CosMultipartResolver
 * @see com.CosMultipartRequest.servlet.MultipartRequest
 */
public class CosMultipartHttpServletRequest extends
		AbstractMultipartHttpServletRequest {

	protected static final Logger logger = Logger.getLogger(CosMultipartHttpServletRequest.class);

	private final CosMultipartRequest multipartRequest;

	/**
	 * Wrap the given HttpServletRequest in a MultipartHttpServletRequest.
	 * 
	 * @param request
	 *            the servlet request to wrap
	 * @param multipartRequest
	 *            the COS multipart request to wrap
	 */
	protected CosMultipartHttpServletRequest(HttpServletRequest request,
			CosMultipartRequest multipartRequest) {
		super(request);
		this.multipartRequest = multipartRequest;
		setMultipartFiles(initFileMap(multipartRequest));
	}

	/**
	 * Return the underlying <code>com.oreilly.servlet.MultipartRequest</code>
	 * instance. There is hardly any need to access this.
	 */
	public CosMultipartRequest getMultipartRequest() {
		return multipartRequest;
	}

	/**
	 * Initialize a Map with file name Strings as keys and CosMultipartFile
	 * instances as values.
	 * 
	 * @param multipartRequest
	 *            the COS multipart request to get the multipart file data from
	 * @return the Map with CosMultipartFile values
	 */
	private MultiValueMap<String, MultipartFile> initFileMap(CosMultipartRequest multipartRequest) {
		if (logger.isDebugEnabled()) {
			logger.debug("initFileMap(CosMultipartRequest) - start"); //$NON-NLS-1$
		}

		MultiValueMap<String, MultipartFile> files = new LinkedMultiValueMap<String, MultipartFile>();
		Set<String> fileNames = multipartRequest.getFileNames();
		for (String fileName : fileNames) {
			files.add(fileName, new CosMultipartFile(fileName));
		}

		if (logger.isDebugEnabled()) {
			logger.debug("initFileMap(CosMultipartRequest) - end"); //$NON-NLS-1$
		}
		return files;
	}

	public Enumeration<String> getParameterNames() {
		if (logger.isDebugEnabled()) {
			logger.debug("getParameterNames() - start"); //$NON-NLS-1$
		}

		Enumeration<String> returnEnumeration = this.multipartRequest.getParameterNames();
		if (logger.isDebugEnabled()) {
			logger.debug("getParameterNames() - end"); //$NON-NLS-1$
		}
		return returnEnumeration;
	}

	public String getParameter(String name) {
		if (logger.isDebugEnabled()) {
			logger.debug("getParameter(String) - start"); //$NON-NLS-1$
		}

		String returnString = this.multipartRequest.getParameter(name);
		if (logger.isDebugEnabled()) {
			logger.debug("getParameter(String) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	public String[] getParameterValues(String name) {
		if (logger.isDebugEnabled()) {
			logger.debug("getParameterValues(String) - start"); //$NON-NLS-1$
		}

		String[] returnStringArray = this.multipartRequest.getParameterValues(name);
		if (logger.isDebugEnabled()) {
			logger.debug("getParameterValues(String) - end"); //$NON-NLS-1$
		}
		return returnStringArray;
	}

	public Map<String,String[]> getParameterMap() {
		if (logger.isDebugEnabled()) {
			logger.debug("getParameterMap() - start"); //$NON-NLS-1$
		}

		Map<String,String[]> params = new HashMap<String,String[]>();
		Enumeration<String> names = getParameterNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			params.put(name, getParameterValues(name));
		}
		Map<String, String[]> returnMap = Collections.unmodifiableMap(params);
		if (logger.isDebugEnabled()) {
			logger.debug("getParameterMap() - end"); //$NON-NLS-1$
		}
		return returnMap;
	}

	/**
	 * Implementation of Spring's MultipartFile interface on top of a COS
	 * MultipartRequest object.
	 */
	private class CosMultipartFile implements MultipartFile {

		private final String name;

		private final File file;

		private final long size;

		public CosMultipartFile(String name) {
			this.name = name;
			this.file = multipartRequest.getFile(this.name);
			this.size = (this.file != null ? this.file.length() : 0);
		}

		public String getName() {
			return name;
		}

		public String getOriginalFilename() {
			if (logger.isDebugEnabled()) {
				logger.debug("getOriginalFilename() - start"); //$NON-NLS-1$
			}

			String filename = multipartRequest.getOriginalFileName(this.name);
			String returnString = (filename != null ? filename : "");
			if (logger.isDebugEnabled()) {
				logger.debug("getOriginalFilename() - end"); //$NON-NLS-1$
			}
			return returnString;
		}

		public String getContentType() {
			if (logger.isDebugEnabled()) {
				logger.debug("getContentType() - start"); //$NON-NLS-1$
			}

			String returnString = multipartRequest.getContentType(this.name);
			if (logger.isDebugEnabled()) {
				logger.debug("getContentType() - end"); //$NON-NLS-1$
			}
			return returnString;
		}

		public boolean isEmpty() {
			if (logger.isDebugEnabled()) {
				logger.debug("isEmpty() - start"); //$NON-NLS-1$
			}

			boolean returnboolean = (this.size == 0);
			if (logger.isDebugEnabled()) {
				logger.debug("isEmpty() - end"); //$NON-NLS-1$
			}
			return returnboolean;
		}

		public long getSize() {
			return this.size;
		}

		public byte[] getBytes() throws IOException {
			if (logger.isDebugEnabled()) {
				logger.debug("getBytes() - start"); //$NON-NLS-1$
			}

			if (this.file != null && !this.file.exists()) {
				throw new IllegalStateException(
						"File has been moved - cannot be read again");
			}
			byte[] returnbyteArray = (this.size > 0 ? FileCopyUtils.copyToByteArray(this.file) : new byte[0]);
			if (logger.isDebugEnabled()) {
				logger.debug("getBytes() - end"); //$NON-NLS-1$
			}
			return returnbyteArray;
		}

		public InputStream getInputStream() throws IOException {
			if (logger.isDebugEnabled()) {
				logger.debug("getInputStream() - start"); //$NON-NLS-1$
			}

			if (this.file != null && !this.file.exists()) {
				throw new IllegalStateException(
						"File has been moved - cannot be read again");
			}
			if (this.size != 0) {
				InputStream returnInputStream = new FileInputStream(this.file);
				if (logger.isDebugEnabled()) {
					logger.debug("getInputStream() - end"); //$NON-NLS-1$
				}
				return returnInputStream;
			} else {
				InputStream returnInputStream = new ByteArrayInputStream(new byte[0]);
				if (logger.isDebugEnabled()) {
					logger.debug("getInputStream() - end"); //$NON-NLS-1$
				}
				return returnInputStream;
			}
		}

		public void transferTo(File dest) throws IOException,
				IllegalStateException {
			if (logger.isDebugEnabled()) {
				logger.debug("transferTo(File) - start"); //$NON-NLS-1$
			}

			if (this.file != null && !this.file.exists()) {
				throw new IllegalStateException(
						"File has already been moved - cannot be transferred again");
			}

			if (dest.exists() && !dest.delete()) {
				throw new IOException("Destination file ["
						+ dest.getAbsolutePath()
						+ "] already exists and could not be deleted");
			}

			boolean moved = false;
			if (this.file != null) {
				moved = this.file.renameTo(dest);
				if (!moved) {
					FileCopyUtils.copy(this.file, dest);
				}
			} else {
				dest.createNewFile();
			}

			if (logger.isDebugEnabled()) {
				logger.debug("Multipart file '"
						+ getName()
						+ "' with original filename ["
						+ getOriginalFilename()
						+ "], stored "
						+ (this.file != null ? "at ["
								+ this.file.getAbsolutePath() + "]"
								: "in memory") + ": "
						+ (moved ? "moved" : "copied") + " to ["
						+ dest.getAbsolutePath() + "]");
			}
		}
	}

}
