package com.jeecms.core.bbcode;

import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import com.jeecms.bbs.web.StrUtils;

public class BbcodeHandler extends DefaultHandler implements InitializingBean {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(BbcodeHandler.class);

	private Map<String, Bbcode> bbMap = new LinkedHashMap<String, Bbcode>();
	private Map<String, Bbcode> alwaysProcessMap = new LinkedHashMap<String, Bbcode>();
	private String tagName = "";
	private StringBuffer sb;
	private Bbcode bb;

	public BbcodeHandler() {
	}

	public String bbcode2html(String s) {
		if (logger.isDebugEnabled()) {
			logger.debug("bbcode2html(String) - start"); //$NON-NLS-1$
		}

		if (StringUtils.isBlank(s)) {
			if (logger.isDebugEnabled()) {
				logger.debug("bbcode2html(String) - end"); //$NON-NLS-1$
			}
			return s;
		}
		s = StrUtils.txt2htm(s);
		String returnString = processText(s);
		if (logger.isDebugEnabled()) {
			logger.debug("bbcode2html(String) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	public String processText(String s) {
		if (logger.isDebugEnabled()) {
			logger.debug("processText(String) - start"); //$NON-NLS-1$
		}

		int codeIndex = s.indexOf("[code");
		int codeEndIndex = codeIndex > -1 ? s.indexOf("[/code]") : -1;

		if (codeIndex == -1 || codeEndIndex == -1 || codeEndIndex < codeIndex) {
			String returnString = bbcode2htmlExceptCodeTag(s);
			if (logger.isDebugEnabled()) {
				logger.debug("processText(String) - end"); //$NON-NLS-1$
			}
			return returnString;
		} else {
			int nextStartPos = 0;
			StringBuilder result = new StringBuilder(s.length());

			while (codeIndex > -1 && codeEndIndex > -1
					&& codeEndIndex > codeIndex) {
				codeEndIndex += "[/code]".length();
				String nonCodeResult = bbcode2htmlExceptCodeTag(s.substring(
						nextStartPos, codeIndex));
				String codeResult = parseCode(s.substring(codeIndex,
						codeEndIndex));
				result.append(nonCodeResult).append(codeResult);
				nextStartPos = codeEndIndex;
				codeIndex = s.indexOf("[code", codeEndIndex);
				codeEndIndex = codeIndex > -1 ? s.indexOf("[/code]", codeIndex)
						: -1;
			}

			if (nextStartPos > -1) {
				String nonCodeResult = bbcode2htmlExceptCodeTag(s
						.substring(nextStartPos));
				result.append(nonCodeResult);
			}
			String returnString = result.toString();
			if (logger.isDebugEnabled()) {
				logger.debug("processText(String) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
	}

	private String parseCode(String text) {
		if (logger.isDebugEnabled()) {
			logger.debug("parseCode(String) - start"); //$NON-NLS-1$
		}

		for (Iterator<Bbcode> iter = getBbList().iterator(); iter.hasNext();) {
			Bbcode bb = iter.next();
			if (bb.getTagName().startsWith("code")) {
				Matcher matcher = Pattern.compile(bb.getRegex()).matcher(text);
				StringBuffer sb = new StringBuffer(text);
				while (matcher.find()) {
					StringBuilder lang = null;
					StringBuilder contents = null;
					if ("code".equals(bb.getTagName())) {
						contents = new StringBuilder(matcher.group(1));
					} else {
						lang = new StringBuilder(matcher.group(1));
						contents = new StringBuilder(matcher.group(2));
					}
					StrUtils.replace(contents, "<br /> ", "\n");
					// XML-like tags
					StrUtils.replace(contents, "<", "&lt;");
					StrUtils.replace(contents, ">", "&gt;");
					// Note: there is no replacing for spaces and tabs as
					// we are relying on the Javascript SyntaxHighlighter
					// library
					// to do it for us
					StringBuffer replace = new StringBuffer(bb.getReplace());
					int index = replace.indexOf("$1");
					if ("code".equals(bb.getTagName())) {
						if (index > -1) {
							replace.replace(index, index + 2, contents
									.toString());
						}
						index = sb.indexOf("[code]");
					} else {
						if (index > -1) {
							replace.replace(index, index + 2, lang.toString());
						}
						index = replace.indexOf("$2");
						if (index > -1) {
							replace.replace(index, index + 2, contents
									.toString());
						}
						index = sb.indexOf("[code=");
					}
					int lastIndex = sb.indexOf("[/code]", index)
							+ "[/code]".length();

					if (lastIndex > index) {
						sb.replace(index, lastIndex, replace.toString());
					}
				}
				text = sb.toString();
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("parseCode(String) - end"); //$NON-NLS-1$
		}
		return text;
	}

	public String bbcode2htmlExceptCodeTag(String text) {
		if (logger.isDebugEnabled()) {
			logger.debug("bbcode2htmlExceptCodeTag(String) - start"); //$NON-NLS-1$
		}

		if (text == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("bbcode2htmlExceptCodeTag(String) - end"); //$NON-NLS-1$
			}
			return text;
		}

		if (text.indexOf('[') > -1 && text.indexOf(']') > -1) {
			for (Iterator<Bbcode> iter = getBbList().iterator(); iter.hasNext();) {
				Bbcode bb = iter.next();
				if (!bb.getTagName().startsWith("code")) {
					text = text.replaceAll(bb.getRegex(), bb.getReplace());
				}
			}
		}

		text = parseDefaultRequiredBBCode(text);

		if (logger.isDebugEnabled()) {
			logger.debug("bbcode2htmlExceptCodeTag(String) - end"); //$NON-NLS-1$
		}
		return text;
	}

	public String parseDefaultRequiredBBCode(String text) {
		if (logger.isDebugEnabled()) {
			logger.debug("parseDefaultRequiredBBCode(String) - start"); //$NON-NLS-1$
		}

		Collection<Bbcode> list = getAlwaysProcessList();

		for (Iterator<Bbcode> iter = list.iterator(); iter.hasNext();) {
			Bbcode bb = iter.next();
			text = text.replaceAll(bb.getRegex(), bb.getReplace());
		}

		if (logger.isDebugEnabled()) {
			logger.debug("parseDefaultRequiredBBCode(String) - end"); //$NON-NLS-1$
		}
		return text;
	}

	public void addBb(Bbcode bb) {
		if (logger.isDebugEnabled()) {
			logger.debug("addBb(Bbcode) - start"); //$NON-NLS-1$
		}

		if (bb.alwaysProcess()) {
			this.alwaysProcessMap.put(bb.getTagName(), bb);
		} else {
			this.bbMap.put(bb.getTagName(), bb);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("addBb(Bbcode) - end"); //$NON-NLS-1$
		}
	}

	public Collection<Bbcode> getBbList() {
		if (logger.isDebugEnabled()) {
			logger.debug("getBbList() - start"); //$NON-NLS-1$
		}

		Collection<Bbcode> returnCollection = this.bbMap.values();
		if (logger.isDebugEnabled()) {
			logger.debug("getBbList() - end"); //$NON-NLS-1$
		}
		return returnCollection;
	}

	public Collection<Bbcode> getAlwaysProcessList() {
		if (logger.isDebugEnabled()) {
			logger.debug("getAlwaysProcessList() - start"); //$NON-NLS-1$
		}

		Collection<Bbcode> returnCollection = this.alwaysProcessMap.values();
		if (logger.isDebugEnabled()) {
			logger.debug("getAlwaysProcessList() - end"); //$NON-NLS-1$
		}
		return returnCollection;
	}

	public Bbcode findByName(String tagName) {
		if (logger.isDebugEnabled()) {
			logger.debug("findByName(String) - start"); //$NON-NLS-1$
		}

		Bbcode returnBbcode = (Bbcode) this.bbMap.get(tagName);
		if (logger.isDebugEnabled()) {
			logger.debug("findByName(String) - end"); //$NON-NLS-1$
		}
		return returnBbcode;
	}

	public void startElement(String uri, String localName, String tag,
			Attributes attrs) {
		if (logger.isDebugEnabled()) {
			logger.debug("startElement(String, String, String, Attributes) - start"); //$NON-NLS-1$
		}

		if (tag.equals("match")) {
			this.sb = new StringBuffer();
			this.bb = new Bbcode();

			String tagName = attrs.getValue("name");
			if (tagName != null) {
				this.bb.setTagName(tagName);
			}

			// Shall we remove the infamous quotes?
			String removeQuotes = attrs.getValue("removeQuotes");
			if (removeQuotes != null && removeQuotes.equals("true")) {
				this.bb.enableRemoveQuotes();
			}

			String alwaysProcess = attrs.getValue("alwaysProcess");
			if (alwaysProcess != null && "true".equals(alwaysProcess)) {
				this.bb.enableAlwaysProcess();
			}
		}

		this.tagName = tag;

		if (logger.isDebugEnabled()) {
			logger.debug("startElement(String, String, String, Attributes) - end"); //$NON-NLS-1$
		}
	}

	public void endElement(String uri, String localName, String tag) {
		if (logger.isDebugEnabled()) {
			logger.debug("endElement(String, String, String) - start"); //$NON-NLS-1$
		}

		if (tag.equals("match")) {
			this.addBb(this.bb);
		} else if (this.tagName.equals("replace")) {
			this.bb.setReplace(this.sb.toString().trim());
			this.sb.delete(0, this.sb.length());
		} else if (this.tagName.equals("regex")) {
			this.bb.setRegex(this.sb.toString().trim());
			this.sb.delete(0, this.sb.length());
		}

		this.tagName = "";

		if (logger.isDebugEnabled()) {
			logger.debug("endElement(String, String, String) - end"); //$NON-NLS-1$
		}
	}

	public void characters(char ch[], int start, int length) {
		if (logger.isDebugEnabled()) {
			logger.debug("characters(char[], int, int) - start"); //$NON-NLS-1$
		}

		if (this.tagName.equals("replace") || this.tagName.equals("regex"))
			this.sb.append(ch, start, length);

		if (logger.isDebugEnabled()) {
			logger.debug("characters(char[], int, int) - end"); //$NON-NLS-1$
		}
	}

	public void error(SAXParseException exception) throws SAXException {
		if (logger.isDebugEnabled()) {
			logger.debug("error(SAXParseException) - start"); //$NON-NLS-1$
		}

		throw exception;
	}

	public void afterPropertiesSet() throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("afterPropertiesSet() - start"); //$NON-NLS-1$
		}

		SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
		parser.parse(configLocation.getInputStream(), this);
		handler = this;

		if (logger.isDebugEnabled()) {
			logger.debug("afterPropertiesSet() - end"); //$NON-NLS-1$
		}
	}

	private static BbcodeHandler handler;

	public static String toHtml(String s) {
		if (logger.isDebugEnabled()) {
			logger.debug("toHtml(String) - start"); //$NON-NLS-1$
		}

		if (handler == null) {
			throw new RuntimeException("BbcodeHandler not prepared!");
		}
		String returnString = handler.bbcode2html(s);
		if (logger.isDebugEnabled()) {
			logger.debug("toHtml(String) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	private Resource configLocation;

	public Resource getConfigLocation() {
		return configLocation;
	}

	public void setConfigLocation(Resource configLocation) {
		this.configLocation = configLocation;
	}
}
