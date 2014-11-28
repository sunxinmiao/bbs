package com.jeecms.core.bbcode;

import org.apache.log4j.Logger;

import java.io.Serializable;

public class Bbcode implements Serializable {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(Bbcode.class);

	private static final long serialVersionUID = 1L;
	private String tagName = "";
	private String regex;
	private String replace;
	private boolean removQuotes;
	private boolean alwaysProcess;

	public Bbcode() {
	}

	/**
	 * BBCode class constructor
	 * 
	 * @param tagName
	 *            The tag name we are going to match
	 * @param regex
	 *            Regular expression relacted to the tag
	 * @param replace
	 *            The replacement string
	 */
	public Bbcode(String tagName, String regex, String replace) {
		this.tagName = tagName;
		this.regex = regex;
		this.replace = replace;
	}

	/**
	 * Gets the regex
	 * 
	 * @return String witht the regex
	 */
	public String getRegex() {
		return this.regex;
	}

	/**
	 * Gets the replacement string
	 * 
	 * @return string with the replacement data
	 */
	public String getReplace() {
		return this.replace;
	}

	/**
	 * Getst the tag name
	 * 
	 * @return The tag name
	 */
	public String getTagName() {
		return this.tagName;
	}

	public boolean removeQuotes() {
		return this.removQuotes;
	}

	/**
	 * Sets the regular expression associated to the tag
	 * 
	 * @param regex
	 *            Regular expression string
	 */
	public void setRegex(String regex) {
		this.regex = regex;
	}

	/**
	 * Sets the replacement string, to be aplyied when matching the code
	 * 
	 * @param replace
	 *            The replacement string data
	 */
	public void setReplace(String replace) {
		this.replace = replace;
	}

	/**
	 * Setst the tag name
	 * 
	 * @param tagName
	 *            The tag name
	 */
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public void enableAlwaysProcess() {
		if (logger.isDebugEnabled()) {
			logger.debug("enableAlwaysProcess() - start"); //$NON-NLS-1$
		}

		this.alwaysProcess = true;

		if (logger.isDebugEnabled()) {
			logger.debug("enableAlwaysProcess() - end"); //$NON-NLS-1$
		}
	}

	public boolean alwaysProcess() {
		return this.alwaysProcess;
	}

	public void enableRemoveQuotes() {
		if (logger.isDebugEnabled()) {
			logger.debug("enableRemoveQuotes() - start"); //$NON-NLS-1$
		}

		this.removQuotes = true;

		if (logger.isDebugEnabled()) {
			logger.debug("enableRemoveQuotes() - end"); //$NON-NLS-1$
		}
	}
}
