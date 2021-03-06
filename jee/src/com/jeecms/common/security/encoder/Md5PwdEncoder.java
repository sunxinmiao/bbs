package com.jeecms.common.security.encoder;

import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

/**
 * MD5密码加密
 * 
 * @author liufang
 * 
 */
public class Md5PwdEncoder implements PwdEncoder {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(Md5PwdEncoder.class);

	public String encodePassword(String rawPass) {
		if (logger.isDebugEnabled()) {
			logger.debug("encodePassword(String) - start"); //$NON-NLS-1$
		}

		String returnString = encodePassword(rawPass, defaultSalt);
		if (logger.isDebugEnabled()) {
			logger.debug("encodePassword(String) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	public String encodePassword(String rawPass, String salt) {
		if (logger.isDebugEnabled()) {
			logger.debug("encodePassword(String, String) - start"); //$NON-NLS-1$
		}

		String saltedPass = mergePasswordAndSalt(rawPass, salt, false);
		MessageDigest messageDigest = getMessageDigest();
		byte[] digest;
		try {
			digest = messageDigest.digest(saltedPass.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			logger.error("encodePassword(String, String)", e); //$NON-NLS-1$

			throw new IllegalStateException("UTF-8 not supported!");
		}
		String returnString = new String(Hex.encodeHex(digest));
		if (logger.isDebugEnabled()) {
			logger.debug("encodePassword(String, String) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	public boolean isPasswordValid(String encPass, String rawPass) {
		if (logger.isDebugEnabled()) {
			logger.debug("isPasswordValid(String, String) - start"); //$NON-NLS-1$
		}

		boolean returnboolean = isPasswordValid(encPass, rawPass, defaultSalt);
		if (logger.isDebugEnabled()) {
			logger.debug("isPasswordValid(String, String) - end"); //$NON-NLS-1$
		}
		return returnboolean;
	}

	public boolean isPasswordValid(String encPass, String rawPass, String salt) {
		if (logger.isDebugEnabled()) {
			logger.debug("isPasswordValid(String, String, String) - start"); //$NON-NLS-1$
		}

		if (encPass == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("isPasswordValid(String, String, String) - end"); //$NON-NLS-1$
			}
			return false;
		}
		String pass2 = encodePassword(rawPass, salt);
		boolean returnboolean = encPass.equals(pass2);
		if (logger.isDebugEnabled()) {
			logger.debug("isPasswordValid(String, String, String) - end"); //$NON-NLS-1$
		}
		return returnboolean;
	}

	protected final MessageDigest getMessageDigest() {
		if (logger.isDebugEnabled()) {
			logger.debug("getMessageDigest() - start"); //$NON-NLS-1$
		}

		String algorithm = "MD5";
		try {
			MessageDigest returnMessageDigest = MessageDigest.getInstance(algorithm);
			if (logger.isDebugEnabled()) {
				logger.debug("getMessageDigest() - end"); //$NON-NLS-1$
			}
			return returnMessageDigest;
		} catch (NoSuchAlgorithmException e) {
			logger.error("getMessageDigest()", e); //$NON-NLS-1$

			throw new IllegalArgumentException("No such algorithm ["
					+ algorithm + "]");
		}
	}

	/**
	 * Used by subclasses to extract the password and salt from a merged
	 * <code>String</code> created using
	 * {@link #mergePasswordAndSalt(String,Object,boolean)}.
	 * <p>
	 * The first element in the returned array is the password. The second
	 * element is the salt. The salt array element will always be present, even
	 * if no salt was found in the <code>mergedPasswordSalt</code> argument.
	 * </p>
	 * 
	 * @param mergedPasswordSalt
	 *            as generated by <code>mergePasswordAndSalt</code>
	 * 
	 * @return an array, in which the first element is the password and the
	 *         second the salt
	 * 
	 * @throws IllegalArgumentException
	 *             if mergedPasswordSalt is null or empty.
	 */
	protected String mergePasswordAndSalt(String password, Object salt,
			boolean strict) {
		if (logger.isDebugEnabled()) {
			logger.debug("mergePasswordAndSalt(String, Object, boolean) - start"); //$NON-NLS-1$
		}

		if (password == null) {
			password = "";
		}
		if (strict && (salt != null)) {
			if ((salt.toString().lastIndexOf("{") != -1)
					|| (salt.toString().lastIndexOf("}") != -1)) {
				throw new IllegalArgumentException(
						"Cannot use { or } in salt.toString()");
			}
		}
		if ((salt == null) || "".equals(salt)) {
			if (logger.isDebugEnabled()) {
				logger.debug("mergePasswordAndSalt(String, Object, boolean) - end"); //$NON-NLS-1$
			}
			return password;
		} else {
			String returnString = password + "{" + salt.toString() + "}";
			if (logger.isDebugEnabled()) {
				logger.debug("mergePasswordAndSalt(String, Object, boolean) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
	}

	/**
	 * 混淆码。防止破解。
	 */
	private String defaultSalt;

	/**
	 * 获得混淆码
	 * 
	 * @return
	 */
	public String getDefaultSalt() {
		return defaultSalt;
	}

	/**
	 * 设置混淆码
	 * 
	 * @param salt
	 */
	public void setDefaultSalt(String defaultSalt) {
		this.defaultSalt = defaultSalt;
	}
}
