package com.gsonkeno.hot.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by gaosong on 2017-03-31.
 */
public class DateUtils {
    public static final SimpleDateFormat standard_sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final SimpleDateFormat yyMMddHHmmss_sdf = new SimpleDateFormat("yyMMddHHmmss");

    public static final SimpleDateFormat yyyy_MM_dd_sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static final SimpleDateFormat HH_mm_ss_sdf = new SimpleDateFormat("HH:mm:ss");

    public static final SimpleDateFormat HHmmss_sdf = new SimpleDateFormat("HHmmss");

    public static final SimpleDateFormat yyMM_sdf = new SimpleDateFormat("yyMM");

    public static final SimpleDateFormat yy_MM_sdf = new SimpleDateFormat("yy_MM");


    public static final int SUNDAY = 1;

    public static final int MONDAY = 2;

    public static final int TUESDAY = 3;

    public static final int WEDNESDAY = 4;

    public static final int THURSDAY = 5;

    public static final int FRIDAY = 6;

    public static final int SATURDAY = 7;

    public static final int JANUARY = 1;

    public static final int FEBRUARY = 2;

    public static final int MARCH = 3;

    public static final int APRIL = 4;

    public static final int MAY = 5;

    public static final int JUNE = 6;

    public static final int JULY = 7;

    public static final int AUGUST = 8;

    public static final int SEPTEMBER = 9;

    public static final int OCTOBER = 10;

    public static final int NOVEMBER = 11;

    public static final int DECEMBER = 12;

    public static final long MILLISECONDS_IN_MINUTE = 60L * 1000L;

    public static final long MILLISECONDS_IN_HOUR = 60L * 60L * 1000l;
    public static final long SECONDS_IN_MOST_DAYS = 24L * 60L * 60L;

    public static final long MILLISECONDS_IN_DAY = SECONDS_IN_MOST_DAYS * 1000L;

    /**
     * 转换时间字符串
     * @param time 原时间字符串
     * @param fromPattern 原时间字符串格式
     * @param toPattern  目标字符串格式
     * @return 目标格式的时间字符串<br>如果原时间字符串不满足原格式，返回 {@code null}
     */
    public static String convertTimeByPattern(String time, SimpleDateFormat fromPattern, SimpleDateFormat toPattern){
        Date dateTime = null;
        try {
            dateTime = fromPattern.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        String  toTime = toPattern.format(dateTime);
        return toTime;
    }

    /**
     * Date对象转时间字符串
     * @param date 时间对象
     * @param pattern  待返回的时间字符串格式
     * @return 对应格式 sdf 下的时间字符串
     */
    public static String dateToString(Date date, SimpleDateFormat pattern){
        String time = pattern.format(date);
        return time;
    }

    /**
     * Date对象转标准格式时间字符串,<br> 实际调用{@link #dateToString(Date, SimpleDateFormat)}
     * @param date 时间对象
     * @return  标准格式 {@link #standard_sdf} 时间字符串
     */
    public  static  String dateToString(Date date){
        return dateToString(date,standard_sdf);
    }

    /**
     * 时间字符串转时间类型
     * @param time 时间字符串
     * @param pattern 时间字符串格式
     * @return     时间字符串相对应的时间对象<br>如果时间字符串不满足格式，返回 {@code null}
     */
    public static Date stringToDate(String time, SimpleDateFormat pattern){
        Date date = null;
        try {
            date = pattern.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取时间字符串的对应偏移后的时间
     * @param time 原时间字符串
     * @param pattern  原时间字符串格式
     * @param field 偏移量单位
     * @param offset 偏移量
     * @return 偏移后的时间字符串
     */
    public static String getTimeOff( String time, SimpleDateFormat pattern, int field, int offset){
        Date date = stringToDate(time, pattern);

        if (date == null) return null;

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(field,offset);

        String offsetTime = pattern.format(c.getTime());

        return offsetTime;
    }

    /**
     * 时间字符串转时间类型，实际调用 {@link #stringToDate(String, SimpleDateFormat)}
     * @param time 时间字符串
     * @return 标准格式时间字符串 {@link #standard_sdf} 的 <code>Date</code> 对象
     */
    public static Date stringToDate(String time){
        return stringToDate(time,standard_sdf);
    }

    /**
     * 获取时间范围
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @param field 递增字段
     * @param amount 递增值
     * @param pattern 返回时间格式
     * @return 指定开始结束时间、指定时间格式、指定递增字段和递增值 的时间范围
     */
    public static List<String> getRangeTime(Date beginTime, Date endTime, int field,int amount, SimpleDateFormat pattern){
        Calendar c = Calendar.getInstance();

        List<String> rangeTimeList = new ArrayList<String>();

        while (beginTime.compareTo(endTime)<=0) {
            c.setTime(beginTime);
            c.add(field, amount);
            beginTime = c.getTime();
            rangeTimeList.add(dateToString(beginTime, pattern));
        }
        return rangeTimeList;
    }

    /**
     * <p>
     * Returns a date that is rounded to the next even minute after the current time.
     * </p>
     *
     * <p>
     * For example a current time of 08:13:54 would result in a date
     * with the time of 08:14:00. If the date's time is in the 59th minute,
     * then the hour (and possibly the day) will be promoted.
     * </p>
     *
     * @return the new rounded date
     */
    public static Date evenMinuteDateAfterNow() {
        return evenMinuteDate(null);
    }

    /**
     * <p>
     * Returns a date that is rounded to the next even minute above the given
     * date.
     * </p>
     *
     * <p>
     * For example an input date with a time of 08:13:54 would result in a date
     * with the time of 08:14:00. If the date's time is in the 59th minute,
     * then the hour (and possibly the day) will be promoted.
     * </p>
     *
     * @param date
     *          the Date to round, if <code>null</code> the current time will
     *          be used
     * @return the new rounded date
     */
    public static Date evenMinuteDate(Date date) {
        if (date == null) {
            date = new Date();
        }

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.setLenient(true);

        c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + 1);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        return c.getTime();
    }

    /**
     * <p>
     * Get a <code>Date</code> object that represents the given time, on
     * tomorrow's date.
     * </p>
     *
     * @param second
     *          The value (0-59) to give the seconds field of the date
     * @param minute
     *          The value (0-59) to give the minutes field of the date
     * @param hour
     *          The value (0-23) to give the hours field of the date
     * @return the new date
     */
    public static Date tomorrowAt(int hour, int minute, int second) {
        validateSecond(second);
        validateMinute(minute);
        validateHour(hour);

        Date date = new Date();

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.setLenient(true);

        // advance one day
        c.add(Calendar.DAY_OF_YEAR, 1);

        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, second);
        c.set(Calendar.MILLISECOND, 0);

        return c.getTime();
    }

    public static void validateHour(int hour) {
        if (hour < 0 || hour > 23) {
            throw new IllegalArgumentException(
                    "Invalid hour (must be >= 0 and <= 23).");
        }
    }

    public static void validateMinute(int minute) {
        if (minute < 0 || minute > 59) {
            throw new IllegalArgumentException(
                    "Invalid minute (must be >= 0 and <= 59).");
        }
    }

    public static void validateSecond(int second) {
        if (second < 0 || second > 59) {
            throw new IllegalArgumentException(
                    "Invalid second (must be >= 0 and <= 59).");
        }
    }

    public static void validateDayOfMonth(int day) {
        if (day < 1 || day > 31) {
            throw new IllegalArgumentException("Invalid day of month.");
        }
    }

    public static void validateMonth(int month) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException(
                    "Invalid month (must be >= 1 and <= 12.");
        }
    }

    private static final int MAX_YEAR = Calendar.getInstance().get(Calendar.YEAR) + 100;
    public static void validateYear(int year) {
        if (year < 0 || year > MAX_YEAR) {
            throw new IllegalArgumentException(
                    "Invalid year (must be >= 0 and <= " + MAX_YEAR);
        }
    }

    /**
     * <p>
     * Get a <code>Date</code> object that represents the given time, on
     * today's date (equivalent to {@link #dateOf(int, int, int)}).
     * </p>
     *
     * @param second
     *          The value (0-59) to give the seconds field of the date
     * @param minute
     *          The value (0-59) to give the minutes field of the date
     * @param hour
     *          The value (0-23) to give the hours field of the date
     * @return the new date
     */
    public static Date todayAt(int hour, int minute, int second) {
        return dateOf(hour, minute, second);
    }

    /**
     * <p>
     * Get a <code>Date</code> object that represents the given time, on
     * today's date  (equivalent to {@link #todayAt(int, int, int)}).
     * </p>
     *
     * @param second
     *          The value (0-59) to give the seconds field of the date
     * @param minute
     *          The value (0-59) to give the minutes field of the date
     * @param hour
     *          The value (0-23) to give the hours field of the date
     * @return the new date
     */
    public static Date dateOf(int hour, int minute, int second) {
        validateSecond(second);
        validateMinute(minute);
        validateHour(hour);

        Date date = new Date();

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.setLenient(true);

        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, second);
        c.set(Calendar.MILLISECOND, 0);

        return c.getTime();
    }

    /**
     * <p>
     * Get a <code>Date</code> object that represents the given time, on the
     * given date.
     * </p>
     *
     * @param second
     *          The value (0-59) to give the seconds field of the date
     * @param minute
     *          The value (0-59) to give the minutes field of the date
     * @param hour
     *          The value (0-23) to give the hours field of the date
     * @param dayOfMonth
     *          The value (1-31) to give the day of month field of the date
     * @param month
     *          The value (1-12) to give the month field of the date
     * @return the new date
     */
    public static Date dateOf(int hour, int minute, int second,
                              int dayOfMonth, int month) {
        validateSecond(second);
        validateMinute(minute);
        validateHour(hour);
        validateDayOfMonth(dayOfMonth);
        validateMonth(month);

        Date date = new Date();

        Calendar c = Calendar.getInstance();
        c.setTime(date);

        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, second);
        c.set(Calendar.MILLISECOND, 0);

        return c.getTime();
    }

    /**
     * <p>
     * Get a <code>Date</code> object that represents the given time, on the
     * given date.
     * </p>
     *
     * @param second
     *          The value (0-59) to give the seconds field of the date
     * @param minute
     *          The value (0-59) to give the minutes field of the date
     * @param hour
     *          The value (0-23) to give the hours field of the date
     * @param dayOfMonth
     *          The value (1-31) to give the day of month field of the date
     * @param month
     *          The value (1-12) to give the month field of the date
     * @param year
     *          The value (1970-2099) to give the year field of the date
     * @return the new date
     */
    public static Date dateOf(int hour, int minute, int second,
                              int dayOfMonth, int month, int year) {
        validateSecond(second);
        validateMinute(minute);
        validateHour(hour);
        validateDayOfMonth(dayOfMonth);
        validateMonth(month);
        validateYear(year);

        Date date = new Date();

        Calendar c = Calendar.getInstance();
        c.setTime(date);

        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, second);
        c.set(Calendar.MILLISECOND, 0);

        return c.getTime();
    }

    public static void main(String[] args) {
        System.out.println(convertTimeByPattern("2017-03-24", yyyy_MM_dd_sdf,standard_sdf));
        System.out.println(getTimeOff("2017-03-24",yyyy_MM_dd_sdf,Calendar.DAY_OF_MONTH,10));

        Date beginDate  = stringToDate("2017-03-24", yyyy_MM_dd_sdf);
        Date endDate = stringToDate("2017-04-08", yyyy_MM_dd_sdf);
        System.out.println(getRangeTime(beginDate,endDate,Calendar.DAY_OF_MONTH, 1,yyMMddHHmmss_sdf));
        System.out.println(MILLISECONDS_IN_MINUTE);
    }
}
