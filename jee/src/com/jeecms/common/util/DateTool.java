package com.jeecms.common.util;

/*
 * 创建日期 2008-09-26
 *
 * 功能   取日期时间工具
 *
 */

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/*******************************************************
 * 名称: 日期工具类
 * 功能: 提供日期处理的常用功能,如编获取系统时间等
 * 作者: 张德生
 * 日期: 2008-09-26 
 *******************************************************/
public class DateTool {
    
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_DAY_FORMAT = "yyyy-MM-dd";

	public DateTool() {
	}

	/**
	 * 取得字符串日期（格式为：yyyy-MM-dd HH:mm:ss）
	 * @param calendar
	 * @return
	 */
	public static String toStr(GregorianCalendar calendar) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        return dateFormat.format(calendar.getTime());
    }
	
	public static String syslogNowStr() {
	    String SYSLOG_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss:SSS";
	    SimpleDateFormat syslogDateFormat = new SimpleDateFormat(SYSLOG_DATE_FORMAT);
	    return syslogDateFormat.format(new GregorianCalendar().getTime());
	}

	/**
	 * 取得字符串日期（格式为：yyyy-MM-dd）
	 * @param calendar
	 * @return
	 */
    public static String toDateStr(GregorianCalendar calendar) {
        if (calendar == null) {
            return "";
        }
        SimpleDateFormat dateDayFormat = new SimpleDateFormat(
              DATE_DAY_FORMAT);
        return dateDayFormat.format(calendar.getTime());
    }

    /**
     * 将形如：yyyy-MM-dd HH:mm:ss的日期转换成GregorianCalendar类型
     * @param calendarStr
     * @return
     */
    public static GregorianCalendar toCalendar(String calendarStr) {
        if (calendarStr == null || "".equals(calendarStr)) {
            return null;
        }
        try {
        	SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            Date date = dateFormat.parse(calendarStr);
            GregorianCalendar rv = new GregorianCalendar();
            rv.setTime(date);
            return rv;
        } catch (ParseException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 将形如：yyyy-MM-dd的日期转换成GregorianCalendar类型
     * @param calendarStr
     * @return
     */
    public static GregorianCalendar toDateCalendar(String calendarStr) {
        if (calendarStr == null) {
            return null;
        }
        try {
        	SimpleDateFormat dateDayFormat = new SimpleDateFormat(
                    DATE_DAY_FORMAT);
            Date date = dateDayFormat.parse(calendarStr);
            GregorianCalendar rv = new GregorianCalendar();
            rv.setTime(date);
            return rv;
        } catch (ParseException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return null;
    }
	
    /**
     * 字符串类型时间转换成Date类型时间 
     * 
     * create by yangwm in Apr 9, 2009 11:28:15 PM
     * @param str
     * @return
     */
    public static Date toDate(String str) {
        if (str == null || "".equals(str)) {
            return null;
        }
        SimpleDateFormat dateFormatTemp = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        try {
            return dateFormatTemp.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
	/**
	 * @see 取得当前日期（格式为：yyyy-MM-dd）
	 * @return String
	 */
	public static String getDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String sDate = sdf.format(new Date());
		return sDate;
	}

	/**
	 * @see 取得当前时间（格式为：yyy-MM-dd HH:mm:ss）
	 * @return String
	 */
	public static String getDateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sDate = sdf.format(new Date());
		return sDate;
	}
	
	/**
	 * @see 取得当前时间（格式为：yyy-MM-dd HH:mm:ss）

	 * @return String
	 */
	public static String getDateTime4Second() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		String sDate = sdf.format(new Date());
		return sDate;
	}

	/**
	 * @see 按指定格式取得当前时间()
	 * @return String
	 */
	public static String getTimeFormat(String strFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(strFormat);
		String sDate = sdf.format(new Date());
		return sDate;
	}

	/**
	 * @see 取得指定时间的给定格式()
	 * @return String
	 * @throws ParseException
	 */
	public static String setDateFormat(String myDate, String strFormat)
			throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat(strFormat);
		String sDate = sdf.format(sdf.parse(myDate));

		return sDate;
	}

	public static String formatDateTime(String strDateTime, String strFormat) {
		String sDateTime = strDateTime;
		try {
			Calendar Cal = parseDateTime(strDateTime);
			SimpleDateFormat sdf = null;
			sdf = new SimpleDateFormat(strFormat);
			sDateTime = sdf.format(Cal.getTime());
		} catch (Exception e) {

		}
		return sDateTime;
	}

	public static Calendar parseDateTime(String baseDate) {
		Calendar cal = null;
		cal = new GregorianCalendar();
		int yy = Integer.parseInt(baseDate.substring(0, 4));
		int mm = Integer.parseInt(baseDate.substring(5, 7)) - 1;
		int dd = Integer.parseInt(baseDate.substring(8, 10));
		int hh = 0;
		int mi = 0;
		int ss = 0;
		if (baseDate.length() > 12) {
			hh = Integer.parseInt(baseDate.substring(11, 13));
			mi = Integer.parseInt(baseDate.substring(14, 16));
			ss = Integer.parseInt(baseDate.substring(17, 19));
		}
		cal.set(yy, mm, dd, hh, mi, ss);
		return cal;
	}

	public static int getDay(String strDate) {
		Calendar cal = parseDateTime(strDate);
		return cal.get(Calendar.DATE);
	}

	public static int getMonth(String strDate) {
		Calendar cal = parseDateTime(strDate);

		return cal.get(Calendar.MONTH) + 1;
	}

	public static int getWeekDay(String strDate) {
		Calendar cal = parseDateTime(strDate);
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	public static String getWeekDayName(String strDate) {
		String mName[] = { "日", "一", "二", "三", "四", "五", "六" };
		int iWeek = getWeekDay(strDate);
		iWeek = iWeek - 1;
		return "星期" + mName[iWeek];
	}

	public static int getYear(String strDate) {
		Calendar cal = parseDateTime(strDate);
		return cal.get(Calendar.YEAR);
	}
    
    /**
     * @method      getYears
     * @description 获取最近几年的年限：从今年开始往前数formYears年，往后数afterYears年
     * @author      wangda
     * @return      年限集合
     */
    public static String[] getYears(int formYears, int afterYears) {
        int n = formYears + 1 + afterYears;
        String[] years = new String[n];
        
        int thisYear = getYear(getDate());
        if (formYears != 0) {
            int formYears_ = -formYears;
            for (int i = formYears_; i < 0; i++) {
                String addYear = addYear2(new Date(), i);
                years[formYears + i] = addYear;
            }
        }
        years[formYears] = String.valueOf(thisYear);
        if (afterYears != 0) {
            for (int i = formYears + 1; i < n; i++) {
                String addYear = addYear2(new Date(), (i - (formYears)));
                years[i] = addYear;
            }
        }
        return years;
    }

	public static String dateAdd(String strDate, int iCount, int iType) {
		Calendar Cal = parseDateTime(strDate);
		int pType = 0;
		if (iType == 0) {
			pType = 1;
		} else if (iType == 1) {
			pType = 2;
		} else if (iType == 2) {
			pType = 5;
		} else if (iType == 3) {
			pType = 10;
		} else if (iType == 4) {
			pType = 12;
		} else if (iType == 5) {
			pType = 13;
		}
		Cal.add(pType, iCount);
		SimpleDateFormat sdf = null;
		if (iType <= 2)
			sdf = new SimpleDateFormat("yyyy-MM-dd");
		else
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sDate = sdf.format(Cal.getTime());
		return sDate;
	}

	public static String dateAdd(String strOption, int iDays, String strStartDate) {
		if (!strOption.equals("d"))
			;
		return strStartDate;
	}


    /**
     * @method addDay: 根据天数和小时数确定时间
     * @param dateField
     * @param days
     * @param hour
     * @return    xxxxxxx
     * @return GregorianCalendar    xxxxxxxx
     * @throws 
    */
    public static GregorianCalendar addDay(GregorianCalendar dateField, int days,int hours) {
        long dateTime = dateField.getTimeInMillis();
        if (days < 0)
            return dateField;
        long daysTime = days * 24 * 60 * 60 * 1000;
        long hoursTime = hours * 60 * 60 * 1000;
        long newDateTime = dateTime + daysTime + hoursTime;
        GregorianCalendar newDate = new GregorianCalendar();
        newDate.setTimeInMillis(newDateTime);
        return newDate;
    }
    
    /** 向前推前时间
     * @param dateField 时间参数
     * @param days 推前天数
     * @return 日期
     */
    public static GregorianCalendar delDay(GregorianCalendar dateField, int days) {
        long dateTime = dateField.getTimeInMillis();
        if (days < 0)
            return dateField;
        long daysTime = days * 24 * 60 * 60 * 1000;
        long newDateTime = dateTime - daysTime;
        GregorianCalendar newDate = new GregorianCalendar();
        newDate.setTimeInMillis(newDateTime);
        return newDate;
    }
    
    /**
     * @method addDay: 根据天数和小时数以及分钟确定时间
     * @param dateField
     * @param days
     * @param hour
     * @return    xxxxxxx
     * @return GregorianCalendar    xxxxxxxx
     * @throws 
    */
    public static GregorianCalendar addDay(GregorianCalendar dateField, int days,int hours,int minute) {
        long dateTime = dateField.getTimeInMillis();
        if (days < 0)
            return dateField;
        long daysTime = days * 24 * 60 * 60 * 1000;
        long hoursTime = hours * 60 * 60 * 1000;
        long minuteTime = minute * 60 * 1000;
        long newDateTime = dateTime + daysTime + hoursTime + minuteTime;
        GregorianCalendar newDate = new GregorianCalendar();
        newDate.setTimeInMillis(newDateTime);
        return newDate;
    }
    /**
     * @method addHour: 增加特定的小时数
     * @param dateField
     * @param hours
     * @param minute
     * @return    xxxxxxx
     * @return GregorianCalendar    xxxxxxxx
     * @throws 
    */
    public static GregorianCalendar addHour(GregorianCalendar dateField, int hours,int minute) {
    	System.out.println(dateField.getTime());
        long dateTime = dateField.getTimeInMillis();
        if (hours < 0)
            return dateField;
        long hoursTime = hours * 60 * 60 * 1000;
        long minuteTime = minute * 60 * 1000;
        long newDateTime = dateTime + hoursTime + minuteTime;
        GregorianCalendar newDate = new GregorianCalendar();
        newDate.setTimeInMillis(newDateTime);
        System.out.println("newDate:"+newDate.getTime());
        return newDate;
    }
    
    /**
     * @method intervalTwoCalendar: 获取两个时间对象之间的间隔毫秒数
     * @param beforDate
     * @param afterDate
     * @return    xxxxxxx
     * @return long    xxxxxxxx
     * @throws 
    */
    public static long getIntervalTwoCalendar(GregorianCalendar beforDate,GregorianCalendar afterDate){
        return afterDate.getTimeInMillis() - beforDate.getTimeInMillis();
    }
	/**
     * 小时的加法
     * 
     * @param dateField
     *            GregorianCalendar 基础时间
     * @param days
     *            int  要加的时间
     * @return GregorianCalendar
     */
    public static GregorianCalendar addDay(GregorianCalendar dateField, long days) {
        long dateTime = dateField.getTimeInMillis();
        if (days < 0)
            return dateField;
        long millis = 24 * 60 * 60 * 1000;
        long daysTime = days * millis;
        long newDateTime = dateTime + daysTime;
        GregorianCalendar newDate = new GregorianCalendar();
        newDate.setTimeInMillis(newDateTime);
        return newDate;
    }
    
	public static int dateDiff(String strDateBegin, String strDateEnd, int iType) {
		Calendar calBegin = parseDateTime(strDateBegin);
		Calendar calEnd = parseDateTime(strDateEnd);
		long lBegin = calBegin.getTimeInMillis();
		long lEnd = calEnd.getTimeInMillis();
		int ss = (int) ((lBegin - lEnd) / 1000L);
		int min = ss / 60;
		int hour = min / 60;
		int day = hour / 24;
		if (iType == 0)
			return hour;
		if (iType == 1)
			return min;
		if (iType == 2)
			return day;
		else
			return -1;
	}

	/***************************************************************************
	 * @功能 判断某年是否为闰年
	 * @return boolean
	 * @throws ParseException
	 **************************************************************************/
	public static boolean isLeapYear(int yearNum) {
		boolean isLeep = false;
		/** 判断是否为闰年，赋值给一标识符flag */
		if ((yearNum % 4 == 0) && (yearNum % 100 != 0)) {
			isLeep = true;
		} else if (yearNum % 400 == 0) {
			isLeep = true;
		} else {
			isLeep = false;
		}
		return isLeep;
	}

	/***************************************************************************
	 * @功能 计算当前日期某年的第几周
	 * @return interger
	 * @throws ParseException
	 **************************************************************************/
	public static int getWeekNumOfYear() {
		Calendar calendar = Calendar.getInstance();
		int iWeekNum = calendar.get(Calendar.WEEK_OF_YEAR);
		return iWeekNum;
	}

	/***************************************************************************
	 * @功能 计算指定日期某年的第几周
	 * @return interger
	 * @throws ParseException
	 **************************************************************************/
	public static int getWeekNumOfYearDay(String strDate) throws ParseException {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date curDate = format.parse(strDate);
		calendar.setTime(curDate);
		int iWeekNum = calendar.get(Calendar.WEEK_OF_YEAR);
		return iWeekNum;
	}

	/***************************************************************************
	 * @功能 计算某年某周的开始日期
	 * @return interger
	 * @throws ParseException
	 **************************************************************************/
	public static String getYearWeekFirstDay(int yearNum, int weekNum)
			throws ParseException {

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, yearNum);
		cal.set(Calendar.WEEK_OF_YEAR, weekNum);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		// 分别取得当前日期的年、月、日
		String tempYear = Integer.toString(yearNum);
		String tempMonth = Integer.toString(cal.get(Calendar.MONTH) + 1);
		String tempDay = Integer.toString(cal.get(Calendar.DATE));
		String tempDate = tempYear + "-" + tempMonth + "-" + tempDay;
		return setDateFormat(tempDate, "yyyy-MM-dd");

	}

	/***************************************************************************
	 * @功能 计算某年某周的结束日期
	 * @return interger
	 * @throws ParseException
	 **************************************************************************/
	public static String getYearWeekEndDay(int yearNum, int weekNum)
			throws ParseException {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, yearNum);
		cal.set(Calendar.WEEK_OF_YEAR, weekNum + 1);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		// 分别取得当前日期的年、月、日
		String tempYear = Integer.toString(yearNum);
		String tempMonth = Integer.toString(cal.get(Calendar.MONTH) + 1);
		String tempDay = Integer.toString(cal.get(Calendar.DATE));
		String tempDate = tempYear + "-" + tempMonth + "-" + tempDay;
		return setDateFormat(tempDate, "yyyy-MM-dd");
	}

	/***************************************************************************
	 * @功能 计算某年某月的开始日期
	 * @return interger
	 * @throws ParseException
	 ***************************************************************************
	 */
	public static String getYearMonthFirstDay(int yearNum, int monthNum)
			throws ParseException {

		// 分别取得当前日期的年、月、日
		String tempYear = Integer.toString(yearNum);
		String tempMonth = Integer.toString(monthNum);
		String tempDay = "1";
		String tempDate = tempYear + "-" + tempMonth + "-" + tempDay;
		return setDateFormat(tempDate, "yyyy-MM-dd");

	}

	/***************************************************************************
	 * @功能 计算某年某月的结束日期
	 * @return interger
	 * @throws ParseException
	 **************************************************************************/
	public static String getYearMonthEndDay(int yearNum, int monthNum)
			throws ParseException {

		// 分别取得当前日期的年、月、日
		String tempYear = Integer.toString(yearNum);
		String tempMonth = Integer.toString(monthNum);
		String tempDay = "31";
		if (tempMonth.equals("1") || tempMonth.equals("3")
				|| tempMonth.equals("5") || tempMonth.equals("7")
				|| tempMonth.equals("8") || tempMonth.equals("10")
				|| tempMonth.equals("12")) {
			tempDay = "31";
		}
		if (tempMonth.equals("4") || tempMonth.equals("6")
				|| tempMonth.equals("9") || tempMonth.equals("11")) {
			tempDay = "30";
		}
		if (tempMonth.equals("2")) {
			if (isLeapYear(yearNum)) {
				tempDay = "29";
			} else {
				tempDay = "28";
			}
		}
		String tempDate = tempYear + "-" + tempMonth + "-" + tempDay;
		return setDateFormat(tempDate, "yyyy-MM-dd");

	}
	
	/**
	 * 
	 * @method now: 返回当前时间
	 * @Date   2009/Sep 14, 2009
	 * @return 
	 */
	public synchronized static GregorianCalendar now() {
        return new GregorianCalendar();
    }

	/**
	 * 
	 * @method nowStr: 返回当前时间的字符串形式
	 * @Date   2009/Sep 14, 2009
	 * @return 
	 */
    public synchronized static String nowStr() {
        return toStr(new GregorianCalendar());
    }
    
    public static String longtoTime(long times) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date = format.parse("2000-01-01 00:00:00", new ParsePosition(0));

        date.setTime(date.getTime() + times);

        format.applyPattern("HH时mm分ss秒");

        return format.format(date);
    }    
    
    public static String addMonth(Date date ,int amount ){
        GregorianCalendar cal = new  GregorianCalendar();
        cal.setTime( date );
        cal.add(GregorianCalendar.MONTH, amount );
        date.setTime( cal.getTimeInMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        return dateFormat.format(date);
    }
    
    public static String addYear(Date date ,int amount ){
        GregorianCalendar cal = new  GregorianCalendar();
        cal.setTime( date );
        cal.add(GregorianCalendar.YEAR, amount );
        date.setTime(cal.getTimeInMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        return dateFormat.format(date);
    }
    
    /**
     * @method      addYear2
     * @description 增加年，返回的时候是4位
     * @author      wangda
     * @param       date
     * @param       amount
     * @return      增加后的年份
     */
    public static String addYear2(Date date ,int amount ){
        GregorianCalendar cal = new  GregorianCalendar();
        cal.setTime( date );
        cal.add(GregorianCalendar.YEAR, amount );
        date.setTime(cal.getTimeInMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        return dateFormat.format(date);
    }
	
	public static void main(String[] args) {
        
        /*int month = DateTool.getMonth(DateTool.getDate());
        // 第一季度
        if (month == 1 || month == 2 || month ==3) {
            System.out.println("今天属于第1季度");
        }*/
        
//        String[] years = DateTool.getYears(50, 50);
//        if (years == null || years.length == 0) {
//            return;
//        }
//        for (int i = 0; i < years.length; i++) {
//            String year = years[i];
//            System.out.println(year);
//        }
//        
		GregorianCalendar calendar = delDay(new GregorianCalendar(),5);
		System.out.println(toStr(calendar));
	}
	   /**
     * 按小时间隔增加时间
     * @param startTime
     * @return
     */
    public static String addHour(String startTime) {
        // 定义时间解析格式
        String SYSLOG_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat syslogDateFormat = new SimpleDateFormat(SYSLOG_DATE_FORMAT);
        GregorianCalendar cal=new GregorianCalendar();
        Date date=null;
        try {
            date = syslogDateFormat.parse(startTime);
            cal.setTime(syslogDateFormat.parse(startTime));
            cal.add(GregorianCalendar.HOUR, 1);
            date.setTime( cal.getTimeInMillis());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return syslogDateFormat.format(date);
    }
    /**
     * 按天间隔增加时间
     * @param startTime
     * @return
     */
    public static String addDay(String startTime) {
        // 定义时间解析格式
        String SYSLOG_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat syslogDateFormat = new SimpleDateFormat(SYSLOG_DATE_FORMAT);
        GregorianCalendar cal=new GregorianCalendar();
        Date date=null;
        try {
            date = syslogDateFormat.parse(startTime);
            cal.setTime(syslogDateFormat.parse(startTime));
            cal.add(GregorianCalendar.DAY_OF_YEAR, 1);
            date.setTime( cal.getTimeInMillis());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return syslogDateFormat.format(date);
    }
    /**
     * 按周间隔增加时间
     * @param startTime
     * @return
     */
    public static String addWeek(String startTime) {
        // 定义时间解析格式
        String SYSLOG_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat syslogDateFormat = new SimpleDateFormat(SYSLOG_DATE_FORMAT);
        GregorianCalendar cal=new GregorianCalendar();
        Date date=null;
        try {
            date = syslogDateFormat.parse(startTime);
            cal.setTime(syslogDateFormat.parse(startTime));
            cal.add(GregorianCalendar.WEEK_OF_YEAR, 1);
            date.setTime( cal.getTimeInMillis());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return syslogDateFormat.format(date);
    }
    /**
     * 按月间隔增加时间
     * @param startTime
     * @return
     */
    public static String addMonth(String startTime) {
        // 定义时间解析格式
        String SYSLOG_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat syslogDateFormat = new SimpleDateFormat(SYSLOG_DATE_FORMAT);
        GregorianCalendar cal=new GregorianCalendar();
        Date date=null;
        try {
            date = syslogDateFormat.parse(startTime);
            cal.setTime(syslogDateFormat.parse(startTime));
            cal.add(GregorianCalendar.MONTH, 1);
            date.setTime( cal.getTimeInMillis());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return syslogDateFormat.format(date);
    }
    /**
     * 按年间隔增加时间
     * @param startTime
     * @return
     */
    public static String addYear(String startTime) {
        // 定义时间解析格式
        String SYSLOG_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat syslogDateFormat = new SimpleDateFormat(SYSLOG_DATE_FORMAT);
        GregorianCalendar cal=new GregorianCalendar();
        Date date=null;
        try {
            date = syslogDateFormat.parse(startTime);
            cal.setTime(syslogDateFormat.parse(startTime));
            cal.add(GregorianCalendar.YEAR, 1);
            date.setTime( cal.getTimeInMillis());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return syslogDateFormat.format(date);
    }
    /**
     * 按固定间隔时间增加
     * @param startTime
     * @param duringTime
     * @return
     */
    public static String addFixedTime(String startTime, long duringTime){
        String SYSLOG_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat syslogDateFormat = new SimpleDateFormat(SYSLOG_DATE_FORMAT);
        GregorianCalendar cal=new GregorianCalendar();
        Date date=null;
        try {
            date = syslogDateFormat.parse(startTime);
            cal.setTime(syslogDateFormat.parse(startTime));
            if(duringTime<0){
                return syslogDateFormat.format(date);
            }
            date.setTime( cal.getTimeInMillis()+duringTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return syslogDateFormat.format(date);
    }
    /**
     * 
     * @method 计算当前时间的前一小时
     * @param endTime
     * @return
     */
    public static String calBeforeHour(String endTime){
        // 定义时间解析格式
        String SYSLOG_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat syslogDateFormat = new SimpleDateFormat(SYSLOG_DATE_FORMAT);
        GregorianCalendar cal=new GregorianCalendar();
        Date date=null;
        try {
            date = syslogDateFormat.parse(endTime);
            cal.setTime(syslogDateFormat.parse(endTime));
            cal.add(GregorianCalendar.HOUR, -1);
            date.setTime( cal.getTimeInMillis());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return syslogDateFormat.format(date);
    }
    /**
     * 
     * @method 计算当前时间的前一天
     * @param endTime
     * @return
     */
    public static String calBeforeDay(String endTime){
        // 定义时间解析格式
        String SYSLOG_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat syslogDateFormat = new SimpleDateFormat(SYSLOG_DATE_FORMAT);
        GregorianCalendar cal=new GregorianCalendar();
        Date date=null;
        try {
            date = syslogDateFormat.parse(endTime);
            cal.setTime(syslogDateFormat.parse(endTime));
            cal.add(GregorianCalendar.DAY_OF_YEAR, -1);
            date.setTime( cal.getTimeInMillis());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return syslogDateFormat.format(date);
    }
    /**
     * 
     * @method 计算当前时间的前一周
     * @param endTime
     * @return
     */
    public static String calBeforeWeek(String endTime){
        // 定义时间解析格式
        String SYSLOG_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat syslogDateFormat = new SimpleDateFormat(SYSLOG_DATE_FORMAT);
        GregorianCalendar cal=new GregorianCalendar();
        Date date=null;
        try {
            date = syslogDateFormat.parse(endTime);
            cal.setTime(syslogDateFormat.parse(endTime));
            cal.add(GregorianCalendar.WEEK_OF_YEAR, -1);
            date.setTime( cal.getTimeInMillis());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return syslogDateFormat.format(date);
    }
    
    /**
     * 
     * @method 计算当前时间的前一月
     * @param endTime
     * @return
     */
    public static String calBeforeMonth(String endTime){
        // 定义时间解析格式
        String SYSLOG_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat syslogDateFormat = new SimpleDateFormat(SYSLOG_DATE_FORMAT);
        GregorianCalendar cal=new GregorianCalendar();
        Date date=null;
        try {
            date = syslogDateFormat.parse(endTime);
            cal.setTime(syslogDateFormat.parse(endTime));
            cal.add(GregorianCalendar.MONTH, -1);
            date.setTime( cal.getTimeInMillis());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return syslogDateFormat.format(date);
    }
    
    /**
     * 
     * @method 计算当前时间的前一年
     * @param endTime
     * @return
     */
    public static String calBeforeYear(String endTime){
        // 定义时间解析格式
        String SYSLOG_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat syslogDateFormat = new SimpleDateFormat(SYSLOG_DATE_FORMAT);
        GregorianCalendar cal=new GregorianCalendar();
        Date date=null;
        try {
            date = syslogDateFormat.parse(endTime);
            cal.setTime(syslogDateFormat.parse(endTime));
            cal.add(GregorianCalendar.YEAR, -1);
            date.setTime( cal.getTimeInMillis());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return syslogDateFormat.format(date);
    }

}
