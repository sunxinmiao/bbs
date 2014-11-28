package com.jeecms.core.entity;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.SocketException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import com.jeecms.common.upload.UploadUtils;
import com.jeecms.core.entity.base.BaseFtp;

public class Ftp extends BaseFtp {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(Ftp.class);

	public String storeByExt(String path, String ext, InputStream in)
			throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("storeByExt(String, String, InputStream) - start"); //$NON-NLS-1$
		}

		String filename = UploadUtils.generateFilename(path, ext);
		store(filename, in);

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

		store(filename, in);

		if (logger.isDebugEnabled()) {
			logger.debug("storeByFilename(String, InputStream) - end"); //$NON-NLS-1$
		}
		return filename;
	}

	public File retrieve(String name) throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("retrieve(String) - start"); //$NON-NLS-1$
		}

		String path = System.getProperty("java.io.tmpdir");
		File file = new File(path, name);
		file = UploadUtils.getUniqueFile(file);

		FTPClient ftp = getClient();
		OutputStream output = new FileOutputStream(file);
		ftp.retrieveFile(getPath() + name, output);
		output.close();
		ftp.logout();
		ftp.disconnect();

		if (logger.isDebugEnabled()) {
			logger.debug("retrieve(String) - end"); //$NON-NLS-1$
		}
		return file;
	}

	public boolean restore(String name, File file) throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("restore(String, File) - start"); //$NON-NLS-1$
		}

		store(name, FileUtils.openInputStream(file));
		file.deleteOnExit();

		if (logger.isDebugEnabled()) {
			logger.debug("restore(String, File) - end"); //$NON-NLS-1$
		}
		return true;
	}

	private int store(String remote, InputStream in) {
		if (logger.isDebugEnabled()) {
			logger.debug("store(String, InputStream) - start"); //$NON-NLS-1$
		}

		try {
			FTPClient ftp = getClient();
			if (ftp != null) {
				String filename = getPath() + remote;
				String name = FilenameUtils.getName(filename);
				String path = FilenameUtils.getFullPath(filename);
				if (!ftp.changeWorkingDirectory(path)) {
					String[] ps = StringUtils.split(path, '/');
					String p = "/";
					ftp.changeWorkingDirectory(p);
					for (String s : ps) {
						p += s + "/";
						if (!ftp.changeWorkingDirectory(p)) {
							ftp.makeDirectory(s);
							ftp.changeWorkingDirectory(p);
						}
					}
				}
				ftp.storeFile(name, in);
				ftp.logout();
				ftp.disconnect();
			}
			in.close();

			if (logger.isDebugEnabled()) {
				logger.debug("store(String, InputStream) - end"); //$NON-NLS-1$
			}
			return 0;
		} catch (SocketException e) {
			logger.error("store(String, InputStream)", e); //$NON-NLS-1$

			logger.error("ftp store error", e);
			return 3;
		} catch (IOException e) {
			logger.error("store(String, InputStream)", e); //$NON-NLS-1$

			logger.error("ftp store error", e);
			return 4;
		}
	}

	private FTPClient getClient() throws SocketException, IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("getClient() - start"); //$NON-NLS-1$
		}

		FTPClient ftp = new FTPClient();
		ftp.addProtocolCommandListener(new PrintCommandListener(
				new PrintWriter(System.out)));
		ftp.setDefaultPort(getPort());
		ftp.connect(getIp());
		int reply = ftp.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			logger.warn("FTP server refused connection: " + getIp());
			ftp.disconnect();

			if (logger.isDebugEnabled()) {
				logger.debug("getClient() - end"); //$NON-NLS-1$
			}
			return null;
		}
		if (!ftp.login(getUsername(), getPassword())) {
			logger.warn("FTP server refused login: " + getIp() + ", user: "
					+ getUsername());
			ftp.logout();
			ftp.disconnect();

			if (logger.isDebugEnabled()) {
				logger.debug("getClient() - end"); //$NON-NLS-1$
			}
			return null;
		}
		ftp.setControlEncoding(getEncoding());
		ftp.setFileType(FTP.BINARY_FILE_TYPE);
		ftp.enterLocalPassiveMode();

		if (logger.isDebugEnabled()) {
			logger.debug("getClient() - end"); //$NON-NLS-1$
		}
		return ftp;
	}

	/* [CONSTRUCTOR MARKER BEGIN] */
	public Ftp() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Ftp(java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public Ftp(java.lang.Integer id, java.lang.String name,
			java.lang.String ip, java.lang.Integer port,
			java.lang.String encoding, java.lang.String url) {

		super(id, name, ip, port, encoding, url);
	}

	/* [CONSTRUCTOR MARKER END] */

}