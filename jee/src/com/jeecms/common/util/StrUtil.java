package com.jeecms.common.util;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Formatter;
import java.util.List;
import java.util.StringTokenizer;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * 
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class StrUtil {

    public static boolean DEBUG_MODE = true;

    public static String G_SPLIT = "G|T";

    public static String UNG_SPLIT = "G\\|T";
    
    public static String L_SPLIT = "L|G";

    public static String UNL_SPLIT = "L\\|G";
    
    public static String Z_SPLIT = "Z|G";

    public static String UNZ_SPLIT = "Z\\|G";
    
    public static String COMMA_SPLIT = ",";
    
    public static String TRUE = "true";
    
    public static String FALSE = "false";

    public StrUtil() {
        super();
    }

    public static int keyWordCount(String src, String key) {
        StringTokenizer st = new StringTokenizer(src, key);
        int count = 0;
        while (st.hasMoreTokens()) {
            st.nextToken();
            count++;
        }
        return count;
    }

    /**
     * @method getLogOperateNames: 将日志操作对象名称拼成字符串
     * @Date 2009/Sep 29, 2009
     * @return String 日志操作对象名称
     * @param operateNames
     *            日志操作对象名称List
     * @return
     */
    public static String getLogOperateNames(List operateNames) {
        if (operateNames == null) {
            return "";
        }
        int count = 0;
        StringBuffer InfoStr = new StringBuffer();
        for (Object name : operateNames) {
            ++count;
            if (count % 3 == 0 && count != operateNames.size()) {
                InfoStr.append((String) name).append('\n');
            } else if (count != operateNames.size()) {
                InfoStr.append((String) name).append(";");
            } else {
                InfoStr.append((String) name);
            }

        }
        return InfoStr.toString();
    }

    /**
     * @method getLogOperateDetail: 将日志详细信息拼成字符串
     * @Date 2009/Sep 29, 2009
     * @return String 日志详细信息
     * @param detail
     *            日志详细信息List
     * @return
     */
    public static String getLogOperateDetail(List detail) {
        if (detail == null) {
            return "";
        }
        StringBuffer InfoStr = new StringBuffer();
        for (Object name : detail) {
            InfoStr.append((String) name).append('\n');
        }
        return InfoStr.toString();
    }

    public static long ipStr2Long(String ip) {
        String[] ips = ip.split("\\.");
        return StrUtil.parseLong(ips[0]) * 256 * 256 * 256 + StrUtil.parseLong(ips[1]) * 256 * 256
                + StrUtil.parseLong(ips[2]) * 256 + StrUtil.parseLong(ips[3]);
    }

    public static String ipLong2Str(long ip) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 4; i++) {
            long temp = ip & 0xff;
            sb.insert(0, temp);
            if (i != 3)
                sb.insert(0, ".");
            ip = ip >> 8;
        }
        return sb.toString();
    }

    public static void printStr(String msg) {
        if (DEBUG_MODE) {
            System.out.print(msg + "\n");
        }
    }

    public static void printException(Exception exc) {
        if (DEBUG_MODE) {
            if (exc != null) {
                exc.printStackTrace();
            }
        }
    }

    public static int parseInt(String str) {
        if (str != null && !"".equals(str)) {
            try {
                return Integer.parseInt(str);
            } catch (Exception ex) {
                printException(ex);
            }
        }
        return 0;
    }

    public static int parseInt(String str, int val) {
        if (str != null && !"".equals(str)) {
            try {
                return Integer.parseInt(str, val);
            } catch (Exception ex) {
                printException(ex);
            }
        }
        return 0;
    }

    public static long parseLong(String str) {
        if (str != null && !"".equals(str)) {
            try {
                return Long.parseLong(str);
            } catch (Exception ex) {
                printException(ex);
            }
        }
        return 0;
    }

    public static double parseDouble(String str) {
        if (str != null && !"".equals(str)) {
            try {
                return Double.parseDouble(str);
            } catch (Exception ex) {
                printException(ex);
            }
        }
        return 0.0;
    }

    public static boolean isNum(String str) {
        boolean flag = true;
        char[] ch = str.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            if (!Character.isDigit(ch[i])) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    // ===== Begin ========JavaScript escape/unescape
    private final static String[] hex = { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "0A", "0B", "0C",
            "0D", "0E", "0F", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "1A", "1B", "1C", "1D", "1E",
            "1F", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "2A", "2B", "2C", "2D", "2E", "2F", "30",
            "31", "32", "33", "34", "35", "36", "37", "38", "39", "3A", "3B", "3C", "3D", "3E", "3F", "40", "41", "42",
            "43", "44", "45", "46", "47", "48", "49", "4A", "4B", "4C", "4D", "4E", "4F", "50", "51", "52", "53", "54",
            "55", "56", "57", "58", "59", "5A", "5B", "5C", "5D", "5E", "5F", "60", "61", "62", "63", "64", "65", "66",
            "67", "68", "69", "6A", "6B", "6C", "6D", "6E", "6F", "70", "71", "72", "73", "74", "75", "76", "77", "78",
            "79", "7A", "7B", "7C", "7D", "7E", "7F", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "8A",
            "8B", "8C", "8D", "8E", "8F", "90", "91", "92", "93", "94", "95", "96", "97", "98", "99", "9A", "9B", "9C",
            "9D", "9E", "9F", "A0", "A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8", "A9", "AA", "AB", "AC", "AD", "AE",
            "AF", "B0", "B1", "B2", "B3", "B4", "B5", "B6", "B7", "B8", "B9", "BA", "BB", "BC", "BD", "BE", "BF", "C0",
            "C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9", "CA", "CB", "CC", "CD", "CE", "CF", "D0", "D1", "D2",
            "D3", "D4", "D5", "D6", "D7", "D8", "D9", "DA", "DB", "DC", "DD", "DE", "DF", "E0", "E1", "E2", "E3", "E4",
            "E5", "E6", "E7", "E8", "E9", "EA", "EB", "EC", "ED", "EE", "EF", "F0", "F1", "F2", "F3", "F4", "F5", "F6",
            "F7", "F8", "F9", "FA", "FB", "FC", "FD", "FE", "FF" };

    private final static byte[] val = { 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
            0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
            0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x00,
            0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x0A, 0x0B,
            0x0C, 0x0D, 0x0E, 0x0F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
            0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F,
            0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
            0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
            0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
            0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
            0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
            0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
            0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
            0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
            0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F };

    public static String unescape(String s) {
        StringBuffer sbuf = new StringBuffer();
        int i = 0;
        int len = s.length();
        while (i < len) {
            int ch = s.charAt(i);
            if ('A' <= ch && ch <= 'Z') {
                sbuf.append((char) ch);
            } else if ('a' <= ch && ch <= 'z') {
                sbuf.append((char) ch);
            } else if ('0' <= ch && ch <= '9') {
                sbuf.append((char) ch);
            } else if (ch == '-' || ch == '_' || ch == '.' || ch == '!' || ch == '~' || ch == '*' || ch == '\''
                    || ch == '(' || ch == ')') {
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
            } else if (ch == '-' || ch == '_' // unreserved : as it was
                    || ch == '.' || ch == '!' || ch == '~' || ch == '*' || ch == '\'' || ch == '(' || ch == ')') {
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

    // ===== End ========JavaScript escape/unescape Java

    /**
     * 如果是null转化为空串，否则返回原字符串 create by yangwm in Nov 4, 2008 9:15:00 PM
     * 
     * @param str
     * @return
     */
    public static final String nullToEmptyString(String str) {
        return str == null ? "" : str;
    }

    /**
     * 如果是null（包括"null"）转化为空串，否则返回原字符串
     * 
     * create by yangwm in Nov 4, 2008 9:15:00 PM
     * 
     * @param str
     * @return
     */
    public static final String nullStringToEmptyString(String str) {
        return str == null || "null".equalsIgnoreCase(str) ? "" : str;
    }

    /**
     * 比较两个字符串是否相同，如果两个字符串为null或""均视为相同
     * 
     * create by yangwm in Nov 4, 2008 8:52:57 PM
     * 
     * @param str1
     * @param str2
     * @return
     */
    public static boolean equals(String str1, String str2) {
        str1 = nullToEmptyString(str1);
        str2 = nullToEmptyString(str2);
        return str1.equals(str2);
    }

    /**
     * �Ƚ�比较两个字符串是否相同，如果两个字符串为null或""均视为相同且忽略大小写
     * 
     * create by yangwm in Nov 4, 2008 8:52:57 PM
     * 
     * @param str1
     * @param str2
     * @return
     */
    public static boolean equalsIgnoreCase(String str1, String str2) {
        str1 = nullToEmptyString(str1);
        str2 = nullToEmptyString(str2);
        return str1.equalsIgnoreCase(str2);
    }

    /**
     * 比较两个字符串是否相同，如果两个字符串为null或""或"null"均视为相同 create by yangwm in Nov 4, 2008
     * 8:52:57 PM
     * 
     * @param str1
     * @param str2
     * @return ��ͬΪtrue������false
     */
    public static boolean equalsNullIgnoreCase(String str1, String str2) {
        str1 = nullStringToEmptyString(str1);
        str2 = nullStringToEmptyString(str2);
        return str1.equals(str2);
    }

    /**
     * �ַ字符串是否为空， null或""均视为空��
     * 
     * @param value
     * @return
     */
    public static boolean isNullStr(String value) {
        return value == null || value.trim().equals("");
    }

    /**
     * �ַ字符串是否为空, null或""或"null"均视为空 create by yangwm in Nov 5, 2008 3:59:24 PM
     * 
     * @param value
     * @return
     */
    public static boolean isEquateNullStr(String value) {
        return equalsNullIgnoreCase(null, value);
    }

    // ---------------------- main test ---------------------------
    public static void main(String[] args) {
        // String str = "bid=a,bid=b,bid=c";
        // System.out.println(keyWordCount(str, ","));
        String stest = "1234 abcd[]()<+>,.~\\";
        System.out.println(stest);

        // test nullToEmptyString and test nullStringToEmptyString
        System.out.println("------------test nullToEmptyString and nullStringToEmptyString ------------");
        System.out.println(nullToEmptyString(null));
        System.out.println(nullToEmptyString(""));
        System.out.println(nullToEmptyString("null"));
        System.out.println(nullStringToEmptyString(null));
        System.out.println(nullStringToEmptyString(""));
        System.out.println(nullStringToEmptyString("null"));

        // test equate
        System.out.println("------------test equate and equalsIgnoreCase ------------");
        System.out.println(equals(null, null));
        System.out.println(equals("", ""));
        System.out.println(equals(null, ""));
        System.out.println(equals("", null));
        System.out.println(equals(null, "abc"));
        System.out.println(equals("abc", "ABC"));
        System.out.println(equalsIgnoreCase("abc", "ABC"));

    }

    public static String createFortPwd(String pwd) {
        return EncryptUtil.DESEncrypt(pwd, "root");
    }

    public static String toString(String[] str) {
        if (str != null) {
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < str.length; i++) {
                buffer.append(str[i] + ";");
            }
            return buffer.toString();
        }
        return "";
    }
    
    /**
     * 将字符串每4位截取劈开成数组
     * create by hujin
     * @param str
     *
     * @return 字符串数组
     */
    public static String[] splitGroupNode(String str) {
    	String[] nodes = null;
    	if(str!=null && "".equals(str)==false && str.length()%4==0){
    		int len = str.length();
            int count = len/4;
            nodes = new String[count];
            for(int i=0;i<count;i++){
            	nodes[i] = str.substring(4*i, 4*i+4);
            }            
    	}
    	return nodes;
    }
    
    public static String removeZero(String str){
    	return str.replaceFirst("^0*", "");
    }
    
    public static String stringFilter(String str){
          String[][] FilterChars={{"<","&lt;"},{">","&gt;"},{" ","&nbsp;"},{"\"","&quot;"},{"&","&amp;"},
            {"/","&#47;"},{"\\","&#92;"},{"\n","<br>"},{"\r","<br>"}};
          String[] str_arr=stringSpilit(str,"");
          for(int i=0;i<str_arr.length;i++){
            for(int j=0;j<FilterChars.length;j++){
              if(FilterChars[j][0].equals(str_arr[i]))
                str_arr[i]=FilterChars[j][1];
            }
          }
          return (stringConnect(str_arr,"")).trim();
    }
    
    public static String[] stringSpilit(String str,String spilit_sign){
        String[] spilit_string=str.split(spilit_sign);
        if(spilit_string[0].equals(""))
        {
          String[] new_string=new String[spilit_string.length-1];
          for(int i=1;i<spilit_string.length;i++)
            new_string[i-1]=spilit_string[i];
            return new_string;
        }
        else
          return spilit_string;
      }
    
    public static String stringConnect(String[] strings,String spilit_sign){
        String str="";
        if (strings == null || strings.length  == 0) {
            return str;
        }
        for(int i=0;i<strings.length;i++){
          str+=strings[i]+spilit_sign;
        }
        return str;
      }

    /**
     * 将字符串str格式化为 “00002345”的形式。如果str为null或者“”，或者无法格式化为数字，则返回null。
     * 
     * @param str
     * @return
     */
    public static String formatOrderStr(String str) {
        if (StrUtil.isNullStr(str)) {
            return null;
        }
        
        Long result = null;
        try {
            result = Long.valueOf(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
        Formatter formatter = new Formatter();
        
        return formatter.format("%08d", result).toString();
    }
    
    /**
     * 判断字符串是否是全部由‘0’组成。如果str为null或者“”，则返回false
     * @param str
     * @return
     */
    public static boolean isAllZero(String str) {
        if (StrUtil.isNullStr(str)) {
            return false;
        }
        
        char[] arrChar = str.toCharArray();
        for (int i = 0; i < arrChar.length; i ++) {
            if ('0' != arrChar[i]) {
                return false;
            }
        }
        return true;
    }
    
    /**
     *将String字符串前边填充位数的无意义的“0”字符去除
     * @Date May 31, 2011
     * @author duyonggang
     * @param String 原字符串形式如:"00000130"
     * @return String 去除前边填充的“0”后的字符串形式如:"130"
     */
    public static String removeNumber0(String str){
		Integer number = Integer.parseInt(str);
		String rm0Str = number.toString();		
    	return rm0Str;
    }
} 
