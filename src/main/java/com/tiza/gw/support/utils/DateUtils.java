package com.tiza.gw.support.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2014-07-01 15:21)
 * Version: 1.0
 * Updated:
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    private static Logger logger = LoggerFactory.getLogger(DateUtils.class);
    public final static String DAY_SHORT_FORMAT = "yyyyMMdd";
    public final static String MONTH_SHORT_FORMAT = "yyyyMM";
    public final static String DAY_SHORT_2_FORMAT = "yyyy-MM-dd";
    public final static String All_DAY_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public final static String All_DAY_2_FORMAT = "yyyyMMddHHmmss";

    public static String formatDate(Date date, String format) {
        SimpleDateFormat aSimpleDateFormat = new SimpleDateFormat(format);
        return aSimpleDateFormat.format(date);
    }

    public static Date parseDate(String dateStr, String format) {
        SimpleDateFormat aSimpleDateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = aSimpleDateFormat.parse(dateStr);
        } catch (Exception e) {
            logger.error("parseDate", e);
        }
        return date;
    }

    public static String getFirstDateOfLastWeek() {
        String firstDate = null;

        GregorianCalendar cal = new GregorianCalendar();
        int week = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (week == 0)
            week = 7;
        cal.add(Calendar.DATE, -(week + 6));
        firstDate = formatDate(cal.getTime(), DAY_SHORT_FORMAT);
        return firstDate;
    }

    public static String getLastDateOfLastWeek() {
        String lastDate = null;
        GregorianCalendar cal = new GregorianCalendar();
        int week = cal.get(Calendar.DAY_OF_WEEK) - 1;

        if (week == 0)
            week = 7;
        cal.add(Calendar.DATE, -week);
        lastDate = formatDate(cal.getTime(), DAY_SHORT_FORMAT);
        return lastDate;
    }

    public static String getCurDateTime(String format) {
        try {
            Calendar now = Calendar.getInstance(TimeZone.getDefault());
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            sdf.setTimeZone(TimeZone.getDefault());
            return (sdf.format(now.getTime()));
        } catch (Exception e) {
            logger.error("getCurDateTime exception", e);
            return null;
        }
    }

    public static String afterNMonthDate(Date theDate, Integer nMonthNum, String format) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(theDate);
            int targetMonth = cal.get(Calendar.MONTH) + nMonthNum;
            cal.set(Calendar.MONTH, targetMonth);
            return formatDate(cal.getTime(), format);
        } catch (Exception e) {
            return null;
        }
    }

    public static String afterNDaysDate(Date theDate, Integer nDayNum, String format) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(theDate);
            cal.add(Calendar.DAY_OF_YEAR, nDayNum);
            return formatDate(cal.getTime(), format);
        } catch (Exception e) {
            return null;
        }
    }

    public static String afterNDaysDate(String theDate, Integer nDayNum, String format) {
        try {
            Date dd1 = parseDate(theDate, format);
            Calendar cal = Calendar.getInstance();
            cal.setTime(dd1);
            cal.add(Calendar.DAY_OF_YEAR, nDayNum);
            return formatDate(cal.getTime(), format);
        } catch (Exception e) {
            logger.error("afterNDaysDate exception:" + e);
            return null;
        }
    }


    public static Integer getWeekOfYear() {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            return cal.get(Calendar.WEEK_OF_YEAR);
        } catch (Exception e) {
            logger.error("获取周失败：" + e);
            return null;
        }
    }

    public static Integer getWeekOfMonth() {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            return cal.get(Calendar.WEEK_OF_MONTH);
        } catch (Exception e) {
            logger.error("获取周失败：" + e);
            return null;
        }
    }

    public static Integer getMonth() {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            return cal.get(Calendar.MONTH) + 1;
        } catch (Exception e) {
            logger.error("获取当前月失败：" + e);
            return null;
        }
    }

    public static Integer getDayOfMonth(Date date) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal.get(Calendar.DAY_OF_MONTH);
        } catch (Exception e) {
            logger.error("获取当前月失败：" + e);
            return null;
        }
    }

    public static Integer getDayOfMonth() {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            return cal.get(Calendar.DAY_OF_MONTH);
        } catch (Exception e) {
            logger.error("获取当前月失败：" + e);
            return null;
        }
    }

    public static Integer getYear() {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            return cal.get(Calendar.YEAR);
        } catch (Exception e) {
            logger.error("获取周失败：" + e);
            return null;
        }
    }


    public static Integer getWeekOfYear(Date date) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal.get(Calendar.WEEK_OF_YEAR);
        } catch (Exception e) {
            logger.error("获取周失败：" + e);
            return null;
        }
    }

    public static Integer getWeekOfMonth(Date date) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal.get(Calendar.WEEK_OF_MONTH);
        } catch (Exception e) {
            logger.error("获取周失败：" + e);
            return null;
        }
    }

    public static Integer getMonth(Date date) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal.get(Calendar.MONTH) + 1;
        } catch (Exception e) {
            logger.error("获取当前月失败：" + e);
            return null;
        }
    }

    public static Integer getYear(Date date) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal.get(Calendar.YEAR);
        } catch (Exception e) {
            logger.error("获取周失败：" + e);
            return null;
        }
    }

    public static Integer getDateOfDays(Date date) {
        Long quot = System.currentTimeMillis() - date.getTime();
        quot = quot / 1000 / 60 / 60 / 24;
        return quot.intValue();
    }

    /**
     * 得到几天前的时间
     */

    public static Date getDateBefore(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        return now.getTime();
    }

    /**
     * 得到几天后的时间
     */

    public static Date getDateAfter(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }

    /**
     * 给定时间的开始时间
     *
     * @param d   给定时间
     * @param day 差的天数
     * @return 比如 给定时间为 2014-12-12 22：22：22 差2天 则返回 2014-12-10 00：00：00
     */
    public static Date getDateStart(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }

    /**
     * 同 getDateStart 方法，返回为结束时间 23：59：59
     *
     * @param d
     * @param day
     * @return
     */
    public static Date getDateEnd(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.HOUR_OF_DAY, 23);
        now.set(Calendar.MINUTE, 59);
        now.set(Calendar.SECOND, 59);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }

    public static Boolean compareToDay(Date date) {
        String date1 = DateUtils.formatDate(date, DateUtils.DAY_SHORT_FORMAT);
        String date2 = DateUtils.formatDate(DateUtils.getDateBefore(new Date(), 1), DateUtils.DAY_SHORT_FORMAT);
        String date3 = DateUtils.formatDate(new Date(), DateUtils.DAY_SHORT_FORMAT);
        return date1.equals(date2) || date1.equals(date3);
    }

    public static boolean areSameDay(Date dateA, Date dateB) {
        Calendar calDateA = Calendar.getInstance();
        calDateA.setTime(dateA);

        Calendar calDateB = Calendar.getInstance();
        calDateB.setTime(dateB);

        return calDateA.get(Calendar.YEAR) == calDateB.get(Calendar.YEAR)
                && calDateA.get(Calendar.MONTH) == calDateB.get(Calendar.MONTH)
                && calDateA.get(Calendar.DAY_OF_MONTH) == calDateB.get(Calendar.DAY_OF_MONTH);
    }
}
