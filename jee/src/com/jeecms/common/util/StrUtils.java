package com.jeecms.common.util;

import org.apache.log4j.Logger;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.htmlparser.Node;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.util.ParserException;

/**
 * 字符串的帮助类，提供静态方法，不可以实例化。
 * 
 * @author liufang
 * 
 */
public class StrUtils {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(StrUtils.class);

	/**
	 * 禁止实例化
	 */
	private StrUtils() {
	}

	/**
	 * 处理url
	 * 
	 * url为null返回null，url为空串或以http://或https://开头，则加上http://，其他情况返回原参数。
	 * 
	 * @param url
	 * @return
	 */
	public static String handelUrl(String url) {
		if (logger.isDebugEnabled()) {
			logger.debug("handelUrl(String) - start"); //$NON-NLS-1$
		}

		if (url == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("handelUrl(String) - end"); //$NON-NLS-1$
			}
			return null;
		}
		url = url.trim();
		if (url.equals("") || url.startsWith("http://")
				|| url.startsWith("https://")) {
			if (logger.isDebugEnabled()) {
				logger.debug("handelUrl(String) - end"); //$NON-NLS-1$
			}
			return url;
		} else {
			String returnString = "http://" + url.trim();
			if (logger.isDebugEnabled()) {
				logger.debug("handelUrl(String) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
	}

	/**
	 * 分割并且去除空格
	 * 
	 * @param str
	 *            待分割字符串
	 * @param sep
	 *            分割符
	 * @param sep2
	 *            第二个分隔符
	 * @return 如果str为空，则返回null。
	 */
	public static String[] splitAndTrim(String str, String sep, String sep2) {
		if (logger.isDebugEnabled()) {
			logger.debug("splitAndTrim(String, String, String) - start"); //$NON-NLS-1$
		}

		if (StringUtils.isBlank(str)) {
			if (logger.isDebugEnabled()) {
				logger.debug("splitAndTrim(String, String, String) - end"); //$NON-NLS-1$
			}
			return null;
		}
		if (!StringUtils.isBlank(sep2)) {
			str = StringUtils.replace(str, sep2, sep);
		}
		String[] arr = StringUtils.split(str, sep);
		// trim
		for (int i = 0, len = arr.length; i < len; i++) {
			arr[i] = arr[i].trim();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("splitAndTrim(String, String, String) - end"); //$NON-NLS-1$
		}
		return arr;
	}

	/**
	 * 文本转html
	 * 
	 * @param txt
	 * @return
	 */
	public static String txt2htm(String txt) {
		if (logger.isDebugEnabled()) {
			logger.debug("txt2htm(String) - start"); //$NON-NLS-1$
		}

		if (StringUtils.isBlank(txt)) {
			if (logger.isDebugEnabled()) {
				logger.debug("txt2htm(String) - end"); //$NON-NLS-1$
			}
			return txt;
		}
		StringBuilder sb = new StringBuilder((int) (txt.length() * 1.2));
		char c;
		boolean doub = false;
		for (int i = 0; i < txt.length(); i++) {
			c = txt.charAt(i);
			if (c == ' ') {
				if (doub) {
					sb.append(' ');
					doub = false;
				} else {
					sb.append("&nbsp;");
					doub = true;
				}
			} else {
				doub = false;
				switch (c) {
				case '&':
					sb.append("&amp;");
					break;
				case '<':
					sb.append("&lt;");
					break;
				case '>':
					sb.append("&gt;");
					break;
				case '"':
					sb.append("&quot;");
					break;
				case '\n':
					sb.append("<br/>");
					break;
				default:
					sb.append(c);
					break;
				}
			}
		}
		String returnString = sb.toString();
		if (logger.isDebugEnabled()) {
			logger.debug("txt2htm(String) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * 剪切文本。如果进行了剪切，则在文本后加上"..."
	 * 
	 * @param s
	 *            剪切对象。
	 * @param len
	 *            编码小于256的作为一个字符，大于256的作为两个字符。
	 * @return
	 */
	public static String textCut(String s, int len, String append) {
		if (logger.isDebugEnabled()) {
			logger.debug("textCut(String, int, String) - start"); //$NON-NLS-1$
		}

		if (s == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("textCut(String, int, String) - end"); //$NON-NLS-1$
			}
			return null;
		}
		int slen = s.length();
		if (slen <= len) {
			if (logger.isDebugEnabled()) {
				logger.debug("textCut(String, int, String) - end"); //$NON-NLS-1$
			}
			return s;
		}
		// 最大计数（如果全是英文）
		int maxCount = len * 2;
		int count = 0;
		int i = 0;
		for (; count < maxCount && i < slen; i++) {
			if (s.codePointAt(i) < 256) {
				count++;
			} else {
				count += 2;
			}
		}
		if (i < slen) {
			if (count > maxCount) {
				i--;
			}
			if (!StringUtils.isBlank(append)) {
				if (s.codePointAt(i - 1) < 256) {
					i -= 2;
				} else {
					i--;
				}
				String returnString = s.substring(0, i) + append;
				if (logger.isDebugEnabled()) {
					logger.debug("textCut(String, int, String) - end"); //$NON-NLS-1$
				}
				return returnString;
			} else {
				String returnString = s.substring(0, i);
				if (logger.isDebugEnabled()) {
					logger.debug("textCut(String, int, String) - end"); //$NON-NLS-1$
				}
				return returnString;
			}
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("textCut(String, int, String) - end"); //$NON-NLS-1$
			}
			return s;
		}
	}

	public static String htmlCut(String s, int len, String append) {
		if (logger.isDebugEnabled()) {
			logger.debug("htmlCut(String, int, String) - start"); //$NON-NLS-1$
		}

		String text = html2Text(s, len * 2);
		String returnString = textCut(text, len, append);
		if (logger.isDebugEnabled()) {
			logger.debug("htmlCut(String, int, String) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	public static String html2Text(String html, int len) {
		if (logger.isDebugEnabled()) {
			logger.debug("html2Text(String, int) - start"); //$NON-NLS-1$
		}

		try {
			Lexer lexer = new Lexer(html);
			Node node;
			StringBuilder sb = new StringBuilder(html.length());
			while ((node = lexer.nextNode()) != null) {
				if (node instanceof TextNode) {
					sb.append(node.toHtml());
				}
				if (sb.length() > len) {
					break;
				}
			}
			String returnString = sb.toString();
			if (logger.isDebugEnabled()) {
				logger.debug("html2Text(String, int) - end"); //$NON-NLS-1$
			}
			return returnString;
		} catch (ParserException e) {
			logger.error("html2Text(String, int)", e); //$NON-NLS-1$

			throw new RuntimeException(e);
		}
	}

	/**
	 * 检查字符串中是否包含被搜索的字符串。被搜索的字符串可以使用通配符'*'。
	 * 
	 * @param str
	 * @param search
	 * @return
	 */
	public static boolean contains(String str, String search) {
		if (logger.isDebugEnabled()) {
			logger.debug("contains(String, String) - start"); //$NON-NLS-1$
		}

		if (StringUtils.isBlank(str) || StringUtils.isBlank(search)) {
			if (logger.isDebugEnabled()) {
				logger.debug("contains(String, String) - end"); //$NON-NLS-1$
			}
			return false;
		}
		String reg = StringUtils.replace(search, "*", ".*");
		Pattern p = Pattern.compile(reg);
		boolean returnboolean = p.matcher(str).matches();
		if (logger.isDebugEnabled()) {
			logger.debug("contains(String, String) - end"); //$NON-NLS-1$
		}
		return returnboolean;
	}
}
