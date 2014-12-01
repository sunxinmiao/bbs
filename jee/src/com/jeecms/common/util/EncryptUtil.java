/**
 * 
 */
package com.jeecms.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.ultrapower.algorithm.Algorithm;
import com.ultrapower.encrypt.EncryptManager;
import com.ultrapower.encrypt.encryptkey.KeyGenerator;
import com.ultrapower.exception.EncryptException;

public class EncryptUtil {
	
	public static String isEncryptKey = "isEncrypt";
	private static List elemList = new ArrayList(); 
	/**
	 * 把一个byte类型的数转换成十六位的ASCII
	 * 
	 * @param ib
	 * @return
	 */
	private static String byte2HEX(byte ib) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
				'b', 'c', 'd', 'e', 'f' };
		char[] ob = new char[2];
		ob[0] = Digit[(ib >>> 4) & 0X0F];
		ob[1] = Digit[ib & 0X0F];
		String s = new String(ob);
		return s;
	}

	/**
	 * 将source进行SHA1加密
	 * 
	 * @param source
	 * @return
	 */
	public static String toSHA1(String source) {
		String newstr = "";
		MessageDigest sha;
		try {
			sha = MessageDigest.getInstance("SHA-1");
			sha.update(source.getBytes());
			newstr = new String(Base64.encode(sha.digest()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return newstr;
	}

	/**
	 * 校验LDAP密码(SSHA)与输入密码是否匹配
	 * @param ldapPwd
	 * @param oldPwd
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static boolean verifySSHA(String ldapPwd, String oldPwd)
			throws NoSuchAlgorithmException {

		// MessageDigest 提供了消息摘要算法，如 MD5 或 SHA，的功能，这里LDAP使用的是SHA-1
		MessageDigest md = MessageDigest.getInstance("SHA-1");

		// 取出加密字符
		if (ldapPwd.startsWith("{SSHA}")) {
			ldapPwd = ldapPwd.substring(6);
		} else if (ldapPwd.startsWith("{SHA}")) {
			ldapPwd = ldapPwd.substring(5);
		}

		// 解码BASE64
		byte[] ldappwbyte = Base64.decode(ldapPwd);
		byte[] shacode;
		byte[] salt;

		// 前20位是SHA-1加密段，20位后是最初加密时的随机明文

		if (ldappwbyte.length <= 20) {
			shacode = ldappwbyte;
			salt = new byte[0];
		} else {
			shacode = new byte[20];
			salt = new byte[ldappwbyte.length - 20];
			System.arraycopy(ldappwbyte, 0, shacode, 0, 20);
			System.arraycopy(ldappwbyte, 20, salt, 0, salt.length);
		}

		// 把用户输入的密码添加到摘要计算信息

		md.update(oldPwd.getBytes());
		// 把随机明文添加到摘要计算信息
		md.update(salt);

		// 按SSHA把当前用户密码进行计算

		byte[] inputpwbyte = md.digest();

		// 返回校验结果
		return MessageDigest.isEqual(shacode, inputpwbyte);
	}

	/**
	 * 校验一个MD5字符串和一个明文的字符串是否匹配
	 * 
	 * @param shaData
	 *            sha加密后的数据
	 * @param password
	 *            明文密码数据
	 * @return
	 */
	public static boolean verifyMD5(String md5Data, String password) {
		// 取出加密字符
		String passCode = toMD5(password);
		return StrUtil.equalsIgnoreCase(md5Data, passCode);
	}

	/**
	 * 校验一个SHA字符串和一个明文的字符串是否匹配
	 * 
	 * @param shaData
	 *            sha加密后的数据
	 * @param password
	 *            明文密码数据
	 * @return
	 */
	public static boolean verifySHA(String shaData, String password) {
		// 取出加密字符
		if (shaData.startsWith("{SSHA}")) {
			shaData = shaData.substring(6);
		} else if (shaData.startsWith("{SHA}")) {
			shaData = shaData.substring(5);
		} else if (shaData.startsWith("{SHA1}")) {
			shaData = shaData.substring(6);
		}
		String passCode = toSHA1(password);
		return StrUtil.equals(shaData, passCode);
	}

	/**
	 * 将source进行md5加密
	 * 
	 * @param source
	 * @return
	 */
	public static String toMD5(String source) {

		String newstr = "";
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] bs = md5.digest(source.getBytes());
			for (int i = 0; i < bs.length; i++) {
				newstr += byte2HEX(bs[i]);
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return newstr;
	}
	
	/**
	 * 获取一个用于3DES加密的Key
	 * 
	 * @param key
	 * @return
	 */
	public static String getKey(){
		//TODO
		return StrUtil.G_SPLIT;
	}
	
	/**
	 * 获取一个用于3DES加密的Key
	 * 
	 * @param key
	 * @return
	 */
	private static SecretKey get3DesKey(String key) {
		if (key == null) {
			key = "private key";
		}

		// 生成一个DESede密钥对象
		SecretKey theKey = null;

		try {
			byte[] encryptKey = new byte[25];
			byte[] tempKey = key.getBytes();

			if (tempKey.length < 24 && tempKey.length > 0) {
				int times = 24 / tempKey.length + 1;
				encryptKey = new byte[tempKey.length * times];
			}
			for (int i = 0, j = 0; i < encryptKey.length; i++, j++) {
				encryptKey[i] = tempKey[j];
				if (j == tempKey.length - 1) {
					j = -1;
				}
			}

			// 为上一个密钥指定一个DESede keyָ���� DESSede key
			DESedeKeySpec spec = new DESedeKeySpec(encryptKey);
			// �õ� DESSede keys
			SecretKeyFactory keyFactory = SecretKeyFactory
					.getInstance("DESede");
			theKey = keyFactory.generateSecret(spec);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return theKey;
	}

	/**
	 * 用指定的key加密字符串
	 * 
	 * 
	 * @param strPwd
	 * @param strKey
	 * @return
	 * @throws InvalidKeySpecException
	 */
	public static String DESEncrypt(String strPwd, String strKey) {
		if (strPwd == null || "".equals(strPwd)) {
			return "";
		}
		SecretKey theKey = get3DesKey(strKey);
		byte[] ciphertext = null;
		String cipherString = "";

		try {
			Cipher cipher = Cipher.getInstance("Desede/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, theKey);
			byte[] plaintext = strPwd.getBytes("UTF8");
			ciphertext = cipher.doFinal(plaintext);
			BASE64Encoder enc = new BASE64Encoder();
			cipherString = enc.encode(ciphertext);
		} catch (Exception e) {
			cipherString = strPwd;
			e.printStackTrace();
		}

		return cipherString;
	}

	/**
	 * 用指定的key加密字符串
	 * @param strPwd
	 * @param strKey
	 * @return
	 * @throws InvalidKeySpecException
	 */
	public static String DESEncrypt(String strPwd, String strKey, String target) {
		if (strPwd == null || "".equals(strPwd)) {
			return "";
		}
		SecretKey theKey = get3DesKey(strKey);
		byte[] ciphertext = null;
		String cipherString = "";

		try {
			Cipher cipher = Cipher.getInstance("Desede/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, theKey);
			byte[] plaintext = strPwd.getBytes("UTF8");
			ciphertext = cipher.doFinal(plaintext);
			BASE64Encoder enc = new BASE64Encoder();
			cipherString = enc.encode(ciphertext);
		} catch (Exception e) {
			cipherString = strPwd;
			e.printStackTrace();
		}

		return cipherString + target;
	}

	/**
	 * 用指定的key 解密指定的字符串
	 * @param strPwdCode
	 * @param strKey
	 * @return
	 * @throws InvalidKeyException
	 */
	public static String DESDencrypted(String strPwdCode, String strKey) {
		if (strPwdCode == null || "".equals(strPwdCode)) {
			return "";
		}
		SecretKey theKey = get3DesKey(strKey);

		String output = "";
		try {
			Cipher cipher = Cipher.getInstance("Desede/ECB/PKCS5Padding");
			BASE64Decoder dec = new BASE64Decoder();
			byte[] ciphertext = dec.decodeBuffer(strPwdCode);

			cipher.init(Cipher.DECRYPT_MODE, theKey);
			byte[] decryptedText = cipher.doFinal(ciphertext);
			output = new String(decryptedText, "UTF8");
		} catch (Exception e) {
			output = strPwdCode;

		}
		return output;
	}
	
	private static final byte[] keyBytes = { 0x13, 0x29, 0x4F, 0x58, 0x1B,
			0x4E, 0x38, 0x2D, 0x25, 0x79, 0x51, 0x5C, 0x68, 0x7A, 0x29, 0x74,
			0x33 };

	/**
	 * 获取4个随机索引数
	 * 
	 * @return int[4]
	 */
	private static int[] getRandomIndex() {
		int[] index = new int[4];
		int length = keyBytes.length;
		Random random = new Random();
		for (int i = 0; i < index.length; i++) {
			index[i] = getDiffNum(index[i == 0 ? 0 : i - 1], length, random);
		}
		return index;
	}

	/**
	 * 让获取的随机数与上一个随机数不同
	 * 
	 * @param lastNum
	 *            上一次的随机数
	 * @param length
	 *            随机数上限
	 * @param random
	 *            生成随机数类
	 * @return int
	 */
	private static int getDiffNum(int lastNum, int length, Random random) {
		int tempInt = random.nextInt(length);
		int i = 0;
		while (tempInt == lastNum) {
			if (i >= 3) {
				tempInt = (tempInt == length - 1) ? tempInt - 1 : tempInt + 1;
				break;
			}
			tempInt = random.nextInt(length);
			++i;
		}
		return tempInt;
	}

	/**
	 * 将字符加密
	 * 
	 * @param str
	 *            原文
	 * @return String 加密后的密文
	 */
	public static String StrEncrypt(String str) throws Exception {
		// 将输入的字符串进行换码处理，以支持中文和特殊字符的加密
		str = escape(str);
		int[] snIndex = getRandomIndex();
		int[] snNum = new int[str.length() + 4];
		char[] enChar = str.toCharArray();
		String result = "";
		String temp = "";

		for (int i = 0; i < snIndex.length; i++) {
			snNum[i] = snIndex[i];
		}

		int keyIndex = snIndex[0];
		for (int j = 0; j < enChar.length; j++) {
			snNum[j + 4] = enChar[j] ^ keyBytes[keyIndex];
		}

		for (int i = 1; i < snIndex.length; i++) {
			keyIndex = snIndex[i];
			for (int j = 0; j < snNum.length - 4; j++) {
				snNum[j + 4] = snNum[j + 4] ^ keyBytes[keyIndex];
			}
		}

		for (int k = 0; k < snNum.length; k++) {
			if (snNum[k] < 16) {
				temp = "0" + Integer.toHexString(snNum[k]);
			} else {
				temp = Integer.toHexString(snNum[k]);
			}
			result += temp;
		}
		return result;
	}

	/**
	 * 将密文解密
	 * 
	 * @param str
	 *            密文
	 * @return String 返回明文
	 */
	public static String StrDecryption(String str) throws Exception {
		int[] indexNum = new int[4];
		int[] enNum = new int[str.length() / 2 - 4];

		String strPerHex = "(\\p{XDigit}){2}";
		Pattern testHex = Pattern.compile(strPerHex);
		Matcher mHex = testHex.matcher(str);
		int i = 0;
		while (mHex.find()) {
			if (i < 4) {
				indexNum[i] = Integer.valueOf(mHex.group().toString(), 16);
			} else {
				enNum[i - 4] = Integer.valueOf(mHex.group().toString(), 16);
			}
			++i;
		}

		for (int j = indexNum.length - 1; j >= 0; j--) {
			int keyIndex = indexNum[j];
			for (int k = 0; k < enNum.length; k++) {
				enNum[k] = enNum[k] ^ keyBytes[keyIndex];
			}
		}

		String result = "";
		for (int j = 0; j < enNum.length; j++) {
			result += (char) enNum[j];
		}
		// 将解密的字符串进行反换码处理，将特殊编码格式转换为可识别的字符串
		result = unescape(result);
		return result;
	}

	// ===== Begin ========JavaScript escape/unescape 编码的 Java
	// 实现=====================
	private final static String[] hex = { "00", "01", "02", "03", "04", "05",
			"06", "07", "08", "09", "0A", "0B", "0C", "0D", "0E", "0F", "10",
			"11", "12", "13", "14", "15", "16", "17", "18", "19", "1A", "1B",
			"1C", "1D", "1E", "1F", "20", "21", "22", "23", "24", "25", "26",
			"27", "28", "29", "2A", "2B", "2C", "2D", "2E", "2F", "30", "31",
			"32", "33", "34", "35", "36", "37", "38", "39", "3A", "3B", "3C",
			"3D", "3E", "3F", "40", "41", "42", "43", "44", "45", "46", "47",
			"48", "49", "4A", "4B", "4C", "4D", "4E", "4F", "50", "51", "52",
			"53", "54", "55", "56", "57", "58", "59", "5A", "5B", "5C", "5D",
			"5E", "5F", "60", "61", "62", "63", "64", "65", "66", "67", "68",
			"69", "6A", "6B", "6C", "6D", "6E", "6F", "70", "71", "72", "73",
			"74", "75", "76", "77", "78", "79", "7A", "7B", "7C", "7D", "7E",
			"7F", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89",
			"8A", "8B", "8C", "8D", "8E", "8F", "90", "91", "92", "93", "94",
			"95", "96", "97", "98", "99", "9A", "9B", "9C", "9D", "9E", "9F",
			"A0", "A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8", "A9", "AA",
			"AB", "AC", "AD", "AE", "AF", "B0", "B1", "B2", "B3", "B4", "B5",
			"B6", "B7", "B8", "B9", "BA", "BB", "BC", "BD", "BE", "BF", "C0",
			"C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9", "CA", "CB",
			"CC", "CD", "CE", "CF", "D0", "D1", "D2", "D3", "D4", "D5", "D6",
			"D7", "D8", "D9", "DA", "DB", "DC", "DD", "DE", "DF", "E0", "E1",
			"E2", "E3", "E4", "E5", "E6", "E7", "E8", "E9", "EA", "EB", "EC",
			"ED", "EE", "EF", "F0", "F1", "F2", "F3", "F4", "F5", "F6", "F7",
			"F8", "F9", "FA", "FB", "FC", "FD", "FE", "FF" };

	private final static byte[] val = { 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x00, 0x01,
			0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F };

	/**
	 * 与Javascript相呼应的解码方法
	 * 
	 * @param s
	 *            密文
	 * @return String 明文
	 */
	public static String unescape(String s) {
		StringBuffer sbuf = new StringBuffer();
		int i = 0;
		int len = s.length();
		while (i < len) {
			int ch = s.charAt(i);
			if (ch == '+') { // space : map to ' '
				sbuf.append(' ');
			} else if ('A' <= ch && ch <= 'Z') {
				sbuf.append((char) ch);
			} else if ('a' <= ch && ch <= 'z') {
				sbuf.append((char) ch);
			} else if ('0' <= ch && ch <= '9') {
				sbuf.append((char) ch);
			} else if (ch == '-' || ch == '_' || ch == '.' || ch == '!'
					|| ch == '~' || ch == '*' || ch == '\'' || ch == '('
					|| ch == ')') {
				sbuf.append((char) ch);
			} else if (ch == '%') {
				int cint = 0;
				if ('u' != s.charAt(i + 1)) {
					cint = (cint << 4) | val[s.charAt(i + 1)];
					cint = (cint << 4) | val[s.charAt(i + 2)];
					i += 2;
				} else {
					cint = (cint << 4) | val[s.charAt(i + 2)];
					cint = (cint << 4) | val[s.charAt(i + 3)];
					cint = (cint << 4) | val[s.charAt(i + 4)];
					cint = (cint << 4) | val[s.charAt(i + 5)];
					i += 5;
				}
				sbuf.append((char) cint);
			} else {
				sbuf.append((char) ch);
			}
			i++;
		}
		return sbuf.toString();
	}

	/**
	 * 与Javascript相呼应的编码方法
	 * 
	 * @param s
	 *            原文
	 * @return String 编码后的密文
	 */
	public static String escape(String s) {
		StringBuffer sbuf = new StringBuffer();
		int len = s.length();
		for (int i = 0; i < len; i++) {
			int ch = s.charAt(i);
			if (ch == ' ') { // space : map to '+'
				sbuf.append('+');
			} else if ('A' <= ch && ch <= 'Z') { // 'A'..'Z' : as it was
				sbuf.append((char) ch);
			} else if ('a' <= ch && ch <= 'z') { // 'a'..'z' : as it was
				sbuf.append((char) ch);
			} else if ('0' <= ch && ch <= '9') { // '0'..'9' : as it was
				sbuf.append((char) ch);
			} else if (ch == '-'
					|| ch == '_' // unreserved : as it was
					|| ch == '.' || ch == '!' || ch == '~' || ch == '*'
					|| ch == '\'' || ch == '(' || ch == ')') {
				sbuf.append((char) ch);
			} else if (ch <= 0x007F) { // other ASCII : map to %XX
				sbuf.append('%');
				sbuf.append(hex[ch]);
			} else { // unicode : map to %uXXXX
				sbuf.append('%');
				sbuf.append('u');
				sbuf.append(hex[(ch >>> 8)]);
				sbuf.append(hex[(0x00FF & ch)]);
			}
		}
		return sbuf.toString();
	}

	// ===== End ========JavaScript escape/unescape 编码的 Java
	// 实现=====================
	
	/**
	 * 获得对配置文件加密时候使用的加密key
	 * @param fileName 加密文件名称
	 * @param encryAttribute 要加密的属性名
	 * @return
	 */
	public static String getEncryptFilekey(String fileName , String encryAttribute){
		
		if(fileName != null && encryAttribute != null){
			return fileName + "_" + encryAttribute;
		}
		return null;
		
	}
	
	/**
	 * 加载注册Property配置文件 对其进行加密等处理
	 * @param filePath	文件路径
	 * @param keys	要处理的文件属性key
	 * @return
	 */
	public static String regPropertyFile(String filePath, String[] keys) {

		String isEncrypt = null;
		try {
			InputStream in = new FileInputStream(filePath);
			SafeProperties safeProp = new SafeProperties();

			String fileName = "";
			int index = filePath.lastIndexOf("/") == -1 ? filePath
					.lastIndexOf("\\") : filePath.lastIndexOf("/");
			if (index > 0) {
				fileName = filePath.substring(index + 1);
			}
			safeProp.load(in);
			in.close();

			String obj = safeProp.getProperty(isEncryptKey);
			if (!StrUtil.isNullStr(obj)) {
				isEncrypt = new String(obj.getBytes("ISO8859-1"), "utf-8");
			}

			// 对elemText进行加密赋值给本项
			EncryptManager manager = EncryptManager.getInstance();

			// 获得生成加密key的实现类
			KeyGenerator keyGenerator = manager
					.getKeyGenerator("AMS");

			// 获得加密算法实现类
			Algorithm algorithm = manager
					.getAlgorithm("AMS");

			if ("true".equals(isEncrypt)) {
				for (int i = 0; i < keys.length; i++) {
					String value = new String(safeProp.getProperty(keys[i])
							.getBytes("ISO8859-1"), "utf-8");

					// 判断是否为空
					if (StrUtil.isNullStr(obj)) {
						throw new Exception(filePath + "key" + value + "null");
					}

					// 加密value保存到文件
					String confusePwd = null;
					String key = getEncryptFilekey(fileName,keys[i]);
					if (keyGenerator != null && algorithm != null) {

						// 获得加密种子
						String seed = keyGenerator.getEncryptSeed(key);

						// 获得加密盐
						String salt = keyGenerator.getEncryptSalt(key);

						// 加密
						confusePwd = algorithm.encrypt(value, seed, salt);

						safeProp.setProperty(keys[i], confusePwd);
					}

				}

				// 将is标识设置为false
				safeProp.setProperty(isEncryptKey, "false");
				FileOutputStream output = new FileOutputStream(filePath);
				safeProp.store(output, null);
				output.close();

			}else if ("false".equals(isEncrypt)) {

				for (int i = 0; i < keys.length; i++) {
					String value = new String(safeProp.getProperty(keys[i])
							.getBytes("ISO8859-1"), "utf-8");
					
					// 判断是否为空
					if (null != value && !"".equals(value)) {
						
						// 判断是否已经加密
						String key = getEncryptFilekey(fileName,keys[i]);
						
						// 获得解密时候使用的种子
						String decryptSeed = keyGenerator
								.revertSeed(value, key);

						// 获得解密时候使用的盐
						String decryptSalt = keyGenerator
								.revertSalt(value, key);

						// 解密
						try {
							algorithm.decrypt(value, decryptSeed, decryptSalt);
						} catch (EncryptException ee) {
							ee.printStackTrace();
							throw new Exception(filePath + "key" + value
									+ "not encrypted");

						}

					} else {
						throw new Exception(filePath + "key" + value + "null");
					}
				}

			} else {
				throw new Exception(filePath + " isEncryptKey Not found！");
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	
	/**
	 * 加载注册XML配置文件 对其进行加密等处理
	 * 
	 * @param filePath
	 *            文件路径
	 */
	public static void regXMLFile(String filePath){
	
		try {
			   SAXReader reader = new SAXReader();
			   File file = new File(filePath);
	           Document document = reader.read(file);
	           
	           // 得到根节点目录
	           Element root = document.getRootElement();
	           
	           //加密XML文件
			   encryptXML(file.getName(),root); 
			   
			   XMLWriter writer = new XMLWriter(new FileOutputStream(filePath));   
			   writer.write(document);  
	           writer.close();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EncryptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
	}
	
   
    /**
     * @param fileName 递归遍历所有节点进行加密
     * @param element
     * @throws EncryptException
     */
    public static void encryptXML(String fileName, Element element)
			throws EncryptException {
		List elements = element.elements();
		if (elements == null || elements.size() == 0) {
			return;
		}
		
		//有子元素 
		for (Iterator it = elements.iterator(); it.hasNext();) {
			Element elem = (Element) it.next();
			
			//递归遍历 
			encryptXML(fileName, elem);

			for (Iterator at = elem.attributeIterator(); at.hasNext();) {
				Attribute att = (Attribute) at.next();
				if(!isEncryptKey.equals(att.getName())){
					continue;
				}
				
				EncryptManager manager = EncryptManager.getInstance();

				//获得生成加密key的实现类
				KeyGenerator keyGenerator = manager
						.getKeyGenerator("AMS");

				//获得加密算法实现类
				Algorithm algorithm = manager
						.getAlgorithm("AMS");

				String key = getEncryptFilekey(fileName , elem.getName());
				String elemText = elem.getText();
				
				//如果需要加密
				if ("true".equals(att.getValue())) {

					//对elemText进行加密赋值给本项
					String confusePwd = null;
					
					if (keyGenerator != null && algorithm != null) {

						//获得加密种子
						String seed = keyGenerator.getEncryptSeed(key);

						//获得加密盐
						String salt = keyGenerator.getEncryptSalt(key);

						if (!StrUtil.isNullStr(elemText)) {
							
							//加密  
							confusePwd = algorithm
									.encrypt(elemText, seed, salt);
							elem.setText(confusePwd);
						} else {
							Attribute valueAtt = (Attribute) elem
									.attribute("value");
							
							//加密    
							confusePwd = algorithm.encrypt(valueAtt.getText(),
									seed, salt);
							valueAtt.setText(confusePwd);
						}
						
						//将当前标签的是否加密改为false
						att.setText("false");
					}

					
					//如果是已經加過密的  解密查看是否加密正常
				}else if("false".equals(att.getValue())){
					
					//加密内容
					elemText = elem.getText(); 
					if (StrUtil.isNullStr(elemText)) {
						elemText = ((Attribute) elem
						.attribute("value")).getText();
					}
					
					// 获得解密时候使用的种子
					String decryptSeed = keyGenerator
							.revertSeed(elemText, key);

					// 获得解密时候使用的盐
					String decryptSalt = keyGenerator
							.revertSalt(elemText, key);

					// 解密
					try {
						algorithm.decrypt(elemText, decryptSeed, decryptSalt);
					} catch (EncryptException ee) {
						ee.printStackTrace();
						throw new EncryptException(fileName + " key" + elem.getName()
								+ "not encrypted");

					}
					
				}
			}
		}

	}


	
	public static void main(String[] strs){
//		regXMLFile("G:/ftpConfig.xml");
//		String[] sts = {"test","zzzz"};
//		regPropertyFile("F:/MyEclipse/workspace/AMS2/src/job.properties",sts);
		String str = DESEncrypt("19860911",EncryptUtil.isEncryptKey);;
		System.out.println(str);
		
		System.out.println(DESDencrypted(str,EncryptUtil.isEncryptKey));
	}



} 
