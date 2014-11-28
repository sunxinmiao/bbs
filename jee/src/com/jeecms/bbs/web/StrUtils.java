package com.jeecms.bbs.web;

import org.apache.log4j.Logger;

import org.apache.commons.lang.StringUtils;

public class StrUtils {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(StrUtils.class);

	/**
	 * 是否有中文字符
	 * 
	 * @param s
	 * @return
	 */
	public static boolean hasCn(String s) {
		if (logger.isDebugEnabled()) {
			logger.debug("hasCn(String) - start"); //$NON-NLS-1$
		}

		if (s == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("hasCn(String) - end"); //$NON-NLS-1$
			}
			return false;
		}
		boolean returnboolean = countCn(s) > s.length();
		if (logger.isDebugEnabled()) {
			logger.debug("hasCn(String) - end"); //$NON-NLS-1$
		}
		return returnboolean;
	}

	/**
	 * 获得字符。符合中文习惯。
	 * 
	 * @param s
	 * @param length
	 * @return
	 */
	public static String getCn(String s, int len) {
		if (logger.isDebugEnabled()) {
			logger.debug("getCn(String, int) - start"); //$NON-NLS-1$
		}

		if (s == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("getCn(String, int) - end"); //$NON-NLS-1$
			}
			return s;
		}
		int sl = s.length();
		if (sl <= len) {
			if (logger.isDebugEnabled()) {
				logger.debug("getCn(String, int) - end"); //$NON-NLS-1$
			}
			return s;
		}
		// 留出一个位置用于…
		len -= 1;
		int maxCount = len * 2;
		int count = 0;
		int i = 0;
		while (count < maxCount && i < sl) {
			if (s.codePointAt(i) < 256) {
				count++;
			} else {
				count += 2;
			}
			i++;
		}
		if (count > maxCount) {
			i--;
		}
		String returnString = s.substring(0, i) + "…";
		if (logger.isDebugEnabled()) {
			logger.debug("getCn(String, int) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * 计算GBK编码的字符串的字节数
	 * 
	 * @param s
	 * @return
	 */
	public static int countCn(String s) {
		if (logger.isDebugEnabled()) {
			logger.debug("countCn(String) - start"); //$NON-NLS-1$
		}

		if (s == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("countCn(String) - end"); //$NON-NLS-1$
			}
			return 0;
		}
		int count = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.codePointAt(i) < 256) {
				count++;
			} else {
				count += 2;
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("countCn(String) - end"); //$NON-NLS-1$
		}
		return count;
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
		for (int i = 0; i < txt.length(); i++) {
			c = txt.charAt(i);
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
			case ' ':
				sb.append("&nbsp;");
				break;
			case '\n':
				sb.append("<br/>");
				break;
			default:
				sb.append(c);
				break;
			}
		}
		String returnString = sb.toString();
		if (logger.isDebugEnabled()) {
			logger.debug("txt2htm(String) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * html转文本
	 * 
	 * @param htm
	 * @return
	 */
	public static String htm2txt(String htm) {
		if (logger.isDebugEnabled()) {
			logger.debug("htm2txt(String) - start"); //$NON-NLS-1$
		}

		if (htm == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("htm2txt(String) - end"); //$NON-NLS-1$
			}
			return htm;
		}
		htm = htm.replace("&amp;", "&");
		htm = htm.replace("&lt;", "<");
		htm = htm.replace("&gt;", ">");
		htm = htm.replace("&quot;", "\"");
		htm = htm.replace("&nbsp;", " ");
		htm = htm.replace("<br/>", "\n");

		if (logger.isDebugEnabled()) {
			logger.debug("htm2txt(String) - end"); //$NON-NLS-1$
		}
		return htm;
	}

	/**
	 * 替换字符串
	 * 
	 * @param sb
	 * @param what
	 * @param with
	 * @return
	 */
	public static StringBuilder replace(StringBuilder sb, String what,
			String with) {
		if (logger.isDebugEnabled()) {
			logger.debug("replace(StringBuilder, String, String) - start"); //$NON-NLS-1$
		}

		int pos = sb.indexOf(what);
		while (pos > -1) {
			sb.replace(pos, pos + what.length(), with);
			pos = sb.indexOf(what);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("replace(StringBuilder, String, String) - end"); //$NON-NLS-1$
		}
		return sb;
	}

	/**
	 * 替换字符串
	 * 
	 * @param s
	 * @param what
	 * @param with
	 * @return
	 */
	public static String replace(String s, String what, String with) {
		if (logger.isDebugEnabled()) {
			logger.debug("replace(String, String, String) - start"); //$NON-NLS-1$
		}

		String returnString = replace(new StringBuilder(s), what, with).toString();
		if (logger.isDebugEnabled()) {
			logger.debug("replace(String, String, String) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * 全角-->半角
	 * 
	 * @param qjStr
	 * @return
	 */
	public String Q2B(String qjStr) {
		if (logger.isDebugEnabled()) {
			logger.debug("Q2B(String) - start"); //$NON-NLS-1$
		}

		String outStr = "";
		String Tstr = "";
		byte[] b = null;
		for (int i = 0; i < qjStr.length(); i++) {
			try {
				Tstr = qjStr.substring(i, i + 1);
				b = Tstr.getBytes("unicode");
			} catch (java.io.UnsupportedEncodingException e) {
				logger.error("Q2B(String)", e); //$NON-NLS-1$

				e.printStackTrace();
			}
			if (b[3] == -1) {
				b[2] = (byte) (b[2] + 32);
				b[3] = 0;
				try {
					outStr = outStr + new String(b, "unicode");
				} catch (java.io.UnsupportedEncodingException e) {
					logger.error("Q2B(String)", e); //$NON-NLS-1$

					e.printStackTrace();
				}
			} else
				outStr = outStr + Tstr;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Q2B(String) - end"); //$NON-NLS-1$
		}
		return outStr;
	}

	public static final char[] N62_CHARS = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
			'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
			'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
			'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
			'X', 'Y', 'Z' };
	public static final char[] N36_CHARS = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
			'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
			'x', 'y', 'z' };

	private static StringBuilder longToNBuf(long l, char[] chars) {
		if (logger.isDebugEnabled()) {
			logger.debug("longToNBuf(long, char[]) - start"); //$NON-NLS-1$
		}

		int upgrade = chars.length;
		StringBuilder result = new StringBuilder();
		int last;
		while (l > 0) {
			last = (int) (l % upgrade);
			result.append(chars[last]);
			l /= upgrade;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("longToNBuf(long, char[]) - end"); //$NON-NLS-1$
		}
		return result;
	}

	/**
	 * 长整数转换成N62
	 * 
	 * @param l
	 * @return
	 */
	public static String longToN62(long l) {
		if (logger.isDebugEnabled()) {
			logger.debug("longToN62(long) - start"); //$NON-NLS-1$
		}

		String returnString = longToNBuf(l, N62_CHARS).reverse().toString();
		if (logger.isDebugEnabled()) {
			logger.debug("longToN62(long) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	public static String longToN36(long l) {
		if (logger.isDebugEnabled()) {
			logger.debug("longToN36(long) - start"); //$NON-NLS-1$
		}

		String returnString = longToNBuf(l, N36_CHARS).reverse().toString();
		if (logger.isDebugEnabled()) {
			logger.debug("longToN36(long) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * 长整数转换成N62
	 * 
	 * @param l
	 * @param length
	 *            如N62不足length长度，则补足0。
	 * @return
	 */
	public static String longToN62(long l, int length) {
		if (logger.isDebugEnabled()) {
			logger.debug("longToN62(long, int) - start"); //$NON-NLS-1$
		}

		StringBuilder sb = longToNBuf(l, N62_CHARS);
		for (int i = sb.length(); i < length; i++) {
			sb.append('0');
		}
		String returnString = sb.reverse().toString();
		if (logger.isDebugEnabled()) {
			logger.debug("longToN62(long, int) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	public static String longToN36(long l, int length) {
		if (logger.isDebugEnabled()) {
			logger.debug("longToN36(long, int) - start"); //$NON-NLS-1$
		}

		StringBuilder sb = longToNBuf(l, N36_CHARS);
		for (int i = sb.length(); i < length; i++) {
			sb.append('0');
		}
		String returnString = sb.reverse().toString();
		if (logger.isDebugEnabled()) {
			logger.debug("longToN36(long, int) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	/**
	 * N62转换成整数
	 * 
	 * @param n62
	 * @return
	 */
	public static long n62ToLong(String n62) {
		if (logger.isDebugEnabled()) {
			logger.debug("n62ToLong(String) - start"); //$NON-NLS-1$
		}

		long returnlong = nToLong(n62, N62_CHARS);
		if (logger.isDebugEnabled()) {
			logger.debug("n62ToLong(String) - end"); //$NON-NLS-1$
		}
		return returnlong;
	}

	public static long n36ToLong(String n36) {
		if (logger.isDebugEnabled()) {
			logger.debug("n36ToLong(String) - start"); //$NON-NLS-1$
		}

		long returnlong = nToLong(n36, N36_CHARS);
		if (logger.isDebugEnabled()) {
			logger.debug("n36ToLong(String) - end"); //$NON-NLS-1$
		}
		return returnlong;
	}

	private static long nToLong(String s, char[] chars) {
		if (logger.isDebugEnabled()) {
			logger.debug("nToLong(String, char[]) - start"); //$NON-NLS-1$
		}

		char[] nc = s.toCharArray();
		long result = 0;
		long pow = 1;
		for (int i = nc.length - 1; i >= 0; i--, pow *= chars.length) {
			int n = findNIndex(nc[i], chars);
			result += n * pow;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("nToLong(String, char[]) - end"); //$NON-NLS-1$
		}
		return result;
	}

	private static int findNIndex(char c, char[] chars) {
		if (logger.isDebugEnabled()) {
			logger.debug("findNIndex(char, char[]) - start"); //$NON-NLS-1$
		}

		for (int i = 0; i < chars.length; i++) {
			if (c == chars[i]) {
				if (logger.isDebugEnabled()) {
					logger.debug("findNIndex(char, char[]) - end"); //$NON-NLS-1$
				}
				return i;
			}
		}
		throw new RuntimeException("N62(N36)非法字符：" + c);
	}

	public static void main(String[] args) {
	}
}
