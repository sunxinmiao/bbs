package com.jeecms.common.util;

import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtils {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DateUtils.class);

	/**
	 * 是否是今天。根据System.currentTimeMillis() / 1000 / 60 / 60 / 24计算。
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isToday(Date date) {
		if (logger.isDebugEnabled()) {
			logger.debug("isToday(Date) - start"); //$NON-NLS-1$
		}

		long day = date.getTime() / 1000 / 60 / 60 / 24;
		long currentDay = System.currentTimeMillis() / 1000 / 60 / 60 / 24;
		boolean returnboolean = day == currentDay;
		if (logger.isDebugEnabled()) {
			logger.debug("isToday(Date) - end"); //$NON-NLS-1$
		}
		return returnboolean;
	}

	/**
	 * 
	 * @param date
	 *            判断是否是本周，取出日期，依据日期取出该日所在周所有日期，在依据这些日期是否和本日相等
	 * @return
	 */
	public static boolean isThisWeek(Date date) {
		if (logger.isDebugEnabled()) {
			logger.debug("isThisWeek(Date) - start"); //$NON-NLS-1$
		}

		List<Date> dates = dateToWeek(date);
		Boolean flag = false;
		for (Date d : dates) {
			if (isToday(d)) {
				flag = true;
				break;
			} else {
				continue;
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("isThisWeek(Date) - end"); //$NON-NLS-1$
		}
		return flag;
	}

	/**
	 * 
	 * @param date
	 *            判断是否是本月的日期
	 * @return
	 */
	public static boolean isThisMonth(Date date) {
		if (logger.isDebugEnabled()) {
			logger.debug("isThisMonth(Date) - start"); //$NON-NLS-1$
		}

		long year = date.getYear();
		long month = date.getMonth();
		Calendar calendar = Calendar.getInstance();
		boolean returnboolean = calendar.getTime().getYear() == year && calendar.getTime().getMonth() == month;
		if (logger.isDebugEnabled()) {
			logger.debug("isThisMonth(Date) - end"); //$NON-NLS-1$
		}
		return returnboolean;
	}

	/**
	 * 
	 * @param date
	 *            判断是否是本年的日期
	 * @return
	 */
	public static boolean isThisYear(Date date) {
		if (logger.isDebugEnabled()) {
			logger.debug("isThisYear(Date) - start"); //$NON-NLS-1$
		}

		long year = date.getYear();
		Calendar calendar = Calendar.getInstance();
		boolean returnboolean = calendar.getTime().getYear() == year;
		if (logger.isDebugEnabled()) {
			logger.debug("isThisYear(Date) - end"); //$NON-NLS-1$
		}
		return returnboolean;
	}

	/**
	 * 
	 * @param mdate
	 *            取出改日的一周所有日期
	 * @return
	 */
	public static List<Date> dateToWeek(Date mdate) {
		if (logger.isDebugEnabled()) {
			logger.debug("dateToWeek(Date) - start"); //$NON-NLS-1$
		}

		int day = mdate.getDay();
		Date fdate;
		List<Date> list = new ArrayList();
		Long fTime = mdate.getTime() - day * 24 * 3600000;
		for (int i = 0; i < 7; i++) {
			fdate = new Date();
			fdate.setTime(fTime + (i * 24 * 3600000));
			list.add(i, fdate);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("dateToWeek(Date) - end"); //$NON-NLS-1$
		}
		return list;
	}

	public static Double diffTwoDate(Date begin, Date end) {
		if (logger.isDebugEnabled()) {
			logger.debug("diffTwoDate(Date, Date) - start"); //$NON-NLS-1$
		}

		Double returnDouble = (end.getTime() - begin.getTime()) / 1000 / 60.0;
		if (logger.isDebugEnabled()) {
			logger.debug("diffTwoDate(Date, Date) - end"); //$NON-NLS-1$
		}
		return returnDouble;
	}

	public static String parseDate(Date date, String format) {
		if (logger.isDebugEnabled()) {
			logger.debug("parseDate(Date, String) - start"); //$NON-NLS-1$
		}

		SimpleDateFormat formater = new SimpleDateFormat(format);
		String dateString;
		dateString = formater.format(date);

		if (logger.isDebugEnabled()) {
			logger.debug("parseDate(Date, String) - end"); //$NON-NLS-1$
		}
		return dateString;
	}

	public static Date afterDate(Date date, Integer after) {
		if (logger.isDebugEnabled()) {
			logger.debug("afterDate(Date, Integer) - start"); //$NON-NLS-1$
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + after);
		Date returnDate = calendar.getTime();
		if (logger.isDebugEnabled()) {
			logger.debug("afterDate(Date, Integer) - end"); //$NON-NLS-1$
		}
		return returnDate;
	}

	public static void main(String arg[]) {
		if (logger.isDebugEnabled()) {
			logger.debug("main(String[]) - start"); //$NON-NLS-1$
		}

		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 1);
		Double d=1/3.0;
		System.out.println(((double)Math.round(d*100))/100);
		System.out.println((double)Math.round(d*100)/100);
		System.out.println(35/100.0);
		System.out.println(afterDate(new Date(), 1));

		if (logger.isDebugEnabled()) {
			logger.debug("main(String[]) - end"); //$NON-NLS-1$
		}
	}
}
