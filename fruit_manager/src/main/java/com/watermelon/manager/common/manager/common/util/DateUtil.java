package com.watermelon.manager.common.manager.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DateUtil {
	private static final Log log = LogFactory.getLog(DateUtil.class);
	public static final String DATE_PATTERN_YYYY_MM = "yyyy-MM";
	public static final String DATE_PATTERN_YYYY_MM_DD = "yyyy-MM-dd";
	public static final String DATE_PATTERN_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
	public static final String DATE_PATTERN_YYYYMMDDHHMM = "yyyyMMddHHmm";
	public static final String DATE_PATTERN_YYYY_MM_DDHHMMSS = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_PATTERN_YYYYdotMMdotDDHHMMSS = "yyyy.MM.dd HH:mm:ss";
	public static final String DATE_PATTERN_YYYY_MM_DDHHMM = "yyyy-MM-dd HH:mm";
	public static final String DATE_PATTERN_YYYY_MM_DDHHMMSS2 = "yyyy/MM/dd HH:mm:ss";
	private static final DateFormat FROMATYYYY_MM_DDHHMM = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private static final DateFormat FROMATYYYY_MM_DDHHMMSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final DateFormat FROMATYYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");

	public static String date2String(Date theDate, String datePattern) {
		if (theDate == null) {
			return "";
		} else {
			SimpleDateFormat format = new SimpleDateFormat(datePattern);

			try {
				return format.format(theDate);
			} catch (Exception arg3) {
				return "";
			}
		}
	}

	public static String date2String(Date theDate) {
		if (theDate == null) {
			return "";
		} else {
			try {
				return FROMATYYYY_MM_DD.format(theDate);
			} catch (Exception arg1) {
				return "";
			}
		}
	}

	public static Date string2Date(String dateString, String datePattern) {
		if (dateString != null && dateString.trim().length() != 0) {
			SimpleDateFormat format = new SimpleDateFormat(datePattern);

			try {
				return format.parse(dateString);
			} catch (ParseException arg3) {
				log.error("ParseException in Converting String to date: " + arg3.getMessage());
				return null;
			}
		} else {
			return null;
		}
	}

	public static Date changeDateTime(Date theDate, int addDays, int hour, int minute, int second) {
		if (theDate == null) {
			return null;
		} else {
			Calendar cal = Calendar.getInstance();
			cal.setTime(theDate);
			cal.add(5, addDays);
			if (hour >= 0 && hour <= 24) {
				cal.set(11, hour);
			}

			if (minute >= 0 && minute <= 60) {
				cal.set(12, minute);
			}

			if (second >= 0 && second <= 60) {
				cal.set(13, second);
			}

			return cal.getTime();
		}
	}

	public static Date getFirstDayOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(1, year);
		cal.set(2, month - 1);
		cal.set(11, 0);
		cal.set(12, 0);
		cal.set(13, 0);
		cal.set(5, 1);
		return cal.getTime();
	}

	public static Date getLastDayOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(1, year);
		cal.set(2, month - 1);
		cal.set(11, 23);
		cal.set(12, 59);
		cal.set(13, 59);
		cal.set(5, 1);
		cal.add(2, 1);
		cal.add(5, -1);
		return cal.getTime();
	}

	public static Date getFirstDayOfMonth(Date theDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(theDate);
		cal.set(11, 0);
		cal.set(12, 0);
		cal.set(13, 0);
		cal.set(5, 1);
		return cal.getTime();
	}

	public static Date getNextMonth(Date theDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(theDate);
		cal.add(2, 1);
		return cal.getTime();
	}

	public static Date getLastMonth(Date theDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(theDate);
		cal.add(2, -1);
		return cal.getTime();
	}

	public static Date getLastDate() {
		return changeDateTime(getTodayDate(), -1, 0, 0, 0);
	}

	public static Date getLastDate(Date date) {
		return changeDateTime(date, -1, 0, 0, 0);
	}

	public static Date getNextDate(Date date) {
		return changeDateTime(date, 1, 0, 0, 0);
	}

	public static Date getTodayDate() {
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return (new GregorianCalendar(cal.get(1), cal.get(2), cal.get(5))).getTime();
	}

	public static Date getDate(Date date) {
		if (date == null) {
			return null;
		} else {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			return (new GregorianCalendar(cal.get(1), cal.get(2), cal.get(5))).getTime();
		}
	}

	public static Date string2Date(String dateString) {
		if (dateString != null && dateString.trim().length() != 0) {
			try {
				return FROMATYYYY_MM_DD.parse(dateString);
			} catch (ParseException arg1) {
				log.error("ParseException in Converting String to date: " + arg1.getMessage());
				return null;
			}
		} else {
			return null;
		}
	}

	public static Date string2Time(String dateString) {
		if (dateString != null && dateString.trim().length() != 0) {
			try {
				return FROMATYYYY_MM_DDHHMM.parse(dateString);
			} catch (ParseException arg1) {
				log.error("ParseException in Converting String to date: " + arg1.getMessage());
				return null;
			}
		} else {
			return null;
		}
	}

	public static Date string2Times(String dateString) {
		if (dateString != null && dateString.trim().length() != 0) {
			try {
				return FROMATYYYY_MM_DDHHMMSS.parse(dateString);
			} catch (ParseException arg1) {
				log.error("ParseException in Converting String to date: " + arg1.getMessage());
				return null;
			}
		} else {
			return null;
		}
	}

	public static Date addDateTime(Date theDate, int addDays, int addHour, int addMinute, int addSecond) {
		Date result = new Date();
		result.setTime(theDate.getTime());
		long addTime = (long) (addDays * 3600 * 24 + addHour * 3600 + addMinute * 60 + addSecond) * 1000L;
		result.setTime(result.getTime() + addTime);
		return result;
	}

	public static long getCurrentTime() {
		return (new Date()).getTime();
	}

	public static int subDateDay(Date date1, Date date2) {
		return (int) (date1.getTime() - date2.getTime()) / 86400000;
	}

	public static String string2DateString(String dateString) {
		if (dateString != null && !dateString.equals("")) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = string2Date(dateString);
			return simpleDateFormat.format(date);
		} else {
			return "";
		}
	}
}