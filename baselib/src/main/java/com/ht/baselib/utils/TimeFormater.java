package com.ht.baselib.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * <p>日期格式化类</p>
 *
 * @author 王多新
 * @version 1.0 (2015-10-19)
 */
public class TimeFormater {
    /**
     * NORMAL 日期+时间格式
     */
    public static final String TYPE_NORMAL = "yyyy-MM-dd HH:mm:ss";
    /**
     * 时间戳变换为秒时需要除以1000
     */
    public static final int CONSTANCE_1000 = 1000;

    /**时间格式*/
    public static final String YY_MM_DD = "yy-MM-dd";
    /**时间格式*/
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    /**时间格式*/
    public static final String YYYY_MM_DD_H = "yyyy年MM月dd日";
    /**时间格式*/
    public static final String HH_MM = "HH:mm";
    /**时间格式*/
    public static final String HH_MM_SS = "HH:mm:ss";
    /**时间格式*/
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    /**时间格式*/
    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    /**时间格式*/
    public static final String YYYY_MM_DD_HH_MM_SS_W = "yyyy-MM-dd-HH-mm-ss";
    /**时间格式*/
    public static final String YY_MM_DD_HH_MM_SS = "yy-MM-dd HH:mm:ss";
    /**时间格式*/
    public static final String YY_MM_DD_HH_MM = "yy-MM-dd HH:mm";

    /**
     * 一秒
     */
    public static final long ONE_SECOND = 1000;

    /**
     * 一分钟
     */
    public static final long ONE_MINUTE = 60 * ONE_SECOND;

    /**
     * 一小时
     */
    public static final long ONE_HOUR = 60 * ONE_MINUTE;
    /**
     * 一天
     */
    public static final long ONE_DAY = 12 * ONE_HOUR;

    /**
     * 取得当前时间戳（精确到秒）
     *
     * @return 当前时间戳
     */
    public static String getTimeStamp() {
        long time = System.currentTimeMillis();
        return String.valueOf(time / CONSTANCE_1000);
    }

    /**
     * 日期格式字符串转换成时间戳
     *
     * @param dateStr 字符串日期
     * @param format  如：yyyy-MM-dd HH:mm:ss
     * @return 转换后的时间戳
     */
    public static String date2TimeStamp(String dateStr, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return String.valueOf(sdf.parse(dateStr).getTime() / CONSTANCE_1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 日期格式字符串转换成时间戳
     *
     * @param dateStr 字符串日期
     * @param format  如：yyyy-MM-dd HH:mm:ss
     * @return 转换后的时间戳 单位秒
     */
    public static long date2TimeStampLong(String dateStr, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(dateStr).getTime() / CONSTANCE_1000;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 时间戳转换成日期格式字符串
     *
     * @param seconds 精确到秒的字符串
     * @param format  日期格式
     * @return 转换后的日期
     */
    public static String timeStamp2Date(String seconds, String format) {
        String result = "";
        try {
            if (seconds == null || "".equals(seconds) || seconds.equals("null")) {
                return "";
            }
            if (seconds.contains(".")) {
                seconds = seconds.substring(0, seconds.indexOf("."));
            }
            if (format == null || "".equals(format)) {
                format = TYPE_NORMAL;
            }

            SimpleDateFormat sdf = new SimpleDateFormat(format);
            if (seconds.contains(".")) {
                seconds = seconds.split("[.]")[0];
            }
            if (seconds.contains(":")) {
                seconds = seconds.split(":")[0];
            }
            result = sdf.format(new Date(Long.valueOf(seconds) * CONSTANCE_1000));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 时间戳转换成日期格式字符串
     *
     * @param seconds 精确到秒
     * @param format  日期格式
     * @return 转换后的日期
     */
    public static String timeStamp2Date(long seconds, String format) {
        String result = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            result = sdf.format(new Date(seconds * CONSTANCE_1000));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取当前日期是星期几
     *
     * @param dt 需要转换的Date
     * @return 当前日期是星期几
     */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }

    /**
     * 获取当天开始日期的时间戳
     * 格式：2011-04-06 00:00:00.0
     *
     * @return 时间戳
     */
    public static String getCurrStartTimestamp() {
        try {
            Calendar ca = Calendar.getInstance();
            //获取年份
            int year = ca.get(Calendar.YEAR);
            //获取月份
            int month = ca.get(Calendar.MONTH) + 1;
            //获取日
            int day = ca.get(Calendar.DATE);

            String dateString = year + "-" + month + "-" + day + " 00:00:00";
            return date2TimeStamp(dateString, "yyyy-MM-dd HH:mm:ss");

        } catch (Exception ex) {
            return "0";
        }
    }

    /**
     * 时间转为指定格式的字符串
     * @param date 时间
     * @param style 格式
     * @return
     */
    public static String parseToString(Date date, String style) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        simpleDateFormat.applyPattern(style);
        String str = "";
        if (date == null) {
            return str;
        }
        str = simpleDateFormat.format(date);
        return str;
    }

    /**
     * 格式化时间字符串为日期对象
     * @param s 字符串
     * @param style 格式
     * @return
     */
    public static Date parseToDate(String s, String style) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        simpleDateFormat.applyPattern(style);
        Date date = null;
        if (s == null || s.length() < 6) {
            return null;
        }
        try {
            date = simpleDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * Description：格式化时间戳字符串为指定日期格式
     * @param time 时间戳，字符串 类型
     * @param style 格式
     * @return { String }
     */
    public static String parseTimeStrToDate(String time, String style){
        if (!StringUtils.isBlank(time)){
            Long t = Long.parseLong(time);
            return parseTimeStrToDate(t, style);
        }else{
            return "";
        }
    }
    /**
     * Description：格式化时间戳为指定日期格式
     * @param time 时间戳，long 类型
     * @param style 格式
     * @return { String }
     * @throws
     */
    public static String parseTimeStrToDate(Long time, String style){
        SimpleDateFormat format = new SimpleDateFormat(style);
        String d = format.format(time);
        return d;
    }

    /**
     * 格式化一个特定时间，封装为一个Map
     * @param objDate 时间
     * @return
     */
    public static Map<String,String> getCurrentMap(Date objDate){
        if(null==objDate){ return null;}
        Calendar cal = Calendar.getInstance();
        cal.setTime(objDate);
        Integer day=cal.get(Calendar.DAY_OF_MONTH);
        String  sToday =String.valueOf(day);
        if(sToday.length()==1){
            sToday="0"+sToday;
        }

        cal.setFirstDayOfWeek(Calendar.MONDAY);
        Integer week=cal.get(Calendar.WEEK_OF_YEAR);
        String  sWeek =String.valueOf(week);
        if(sWeek.length()==1){
            sWeek="0"+sWeek;
        }


        Integer month=cal.get(Calendar.MONTH)+1;
        String sMonth = String.valueOf(month);
        if(sMonth.length()==1){
            sMonth="0"+sMonth;
        }
        Integer year= cal.get(Calendar.YEAR);
        String sYear = String .valueOf(year);
        Map<String,String>  m =  new HashMap<String,String>();
        m.put("year", sYear);
        m.put("month", sMonth);
        m.put("week", sWeek);
        m.put("day", sToday);
        return m;
    }

    /**
     * 计算两个时间差
     * @param dateStart 开始时间
     * @param dateEnd 结束时间
     * @return
     */
    public static long subtractDate(Date dateStart,Date dateEnd){
        long obj = dateEnd.getTime() - dateStart.getTime();
        return obj;
    }

    /**
     * 得到几天后的时间
     * @param d 时间
     * @param day 多少天后
     * @return
     */
    public static Date getDateAfter(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }

    /**
     * 将CST时间字符串格式化
     * @param dateStr 待格式化CST时间字符串
     * @param style 格式化格式
     * @return
     */
    public static String parseCST2DateStr(String dateStr, String style) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        Date date = null;
        if (dateStr == null) {
            return null;
        }
        try {
            date = (Date) sdf.parse(dateStr);
            return parseToString(date, style);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将时间字符串格式化
     * @param dateStr 待格式化时间字符串
     * @param style 格式化格式
     * @return
     */
    public static String parseDateStr2DateStr(String dateStr, String style) {
        Date date = null;
        if (dateStr == null || "".equals(dateStr.trim())) {
            return null;
        }else{
            date = parseToDate(dateStr, YYYY_MM_DD_HH_MM_SS);
            return parseToString(date, style);
        }
    }

    /**
     * Description：返回指定格式的Date
     * String
     *
     * @param date 时间
     * @param style 格式化
     * @return
     * @throws
     */
    public static String getFormatTime(Date date, String style) {
        return (new SimpleDateFormat(style)).format(date);
    }

    /**
     * Description：处理类似微博过去时间显示效果
     * @param time 时间戳，字符串 类型
     * @return { String }
     * @throws
     */
    public static String getInterval(String time){
        if (!StringUtils.isBlank(time)){
            Long t = Long.parseLong(time);
            return getInterval(t);
        }else{
            return "";
        }
    }
    /**
     * Description：处理类似微博过去时间显示效果
     * @param time 时间戳，long 类型
     * @return { String }
     * @throws
     */
    public static String getInterval(Long time){
        SimpleDateFormat format = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
        String d = format.format(time);
        Date date = null;
        try {
            date = format.parse(d);
        } catch (ParseException e1) {
            e1.printStackTrace();
            return "";
        }
        return getInterval(date);
    }

    /**
     * Description：处理类似微博过去时间显示效果
     * @param createAt
     *            Data 类型
     * @return { String }
     * @throws
     */
    public static String getInterval(Date createAt) {
        //定义最终返回的结果字符串。
        String interval = null;
        long millisecond = new Date().getTime() - createAt.getTime();
        long second = millisecond/1000;

        if (second <= 0) {
            second = 0;
        }

        if (second == 0) {
            interval = "刚刚";
        } else if (second < 30) {
            interval = second + "秒以前";
        } else if (second >= 30 && second < 60) {
            interval = "半分钟前";
        } else if (second >= 60 && second < 60 * 60) {
            long minute = second / 60;
            interval = minute + "分钟前";
        } else if (second >= 60 * 60 && second < 60 * 60 * 24) {
            long hour = (second / 60) / 60;
            interval = hour + "小时前";
        } else if (second >= 60 * 60 * 24 && second <= 60 * 60 * 24 * 2) {
            interval = "昨天" + getFormatTime(createAt, "HH:mm");
        } else if (second >= 60 * 60 * 24 * 2 && second <= 60 * 60 * 24 * 7) {
            long day = ((second / 60) / 60) / 24;
            interval = day + "天前";
        } else if (second >= 60 * 60 * 24 * 7) {
            interval = getFormatTime(createAt, "MM-dd HH:mm");
        } else if (second >= 60 * 60 * 24 * 365) {
            interval = getFormatTime(createAt, YYYY_MM_DD_HH_MM);
        } else {
            interval = "";
        }

        return interval;
    }

    /**
     * 通过日期来确定星座
     *
     * @param dateStr "****-**-**"形式的字符串日期
     * @return
     */
    public static String getStarSeat(String dateStr) {
        Date date = parseToDate(dateStr,YYYY_MM_DD);
        if (null != date){
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return getStarSeat(cal.get(Calendar.MONTH)+1,cal.get(Calendar.DAY_OF_MONTH));
        }else{
            return dateStr;
        }
    }

    /**
     * 通过日期来确定星座
     * @param date 日期
     * @return
     */
    public static String getStarSeat(Date date) {
        if (null != date){
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return getStarSeat(cal.get(Calendar.MONTH)+1,cal.get(Calendar.DAY_OF_MONTH));
        }else{
            return "";
        }
    }

    /**
     * 通过日期来确定星座
     *
     * @param month 月份
     * @param day 日期
     * @return
     */
    public static String getStarSeat(int month, int day) {
        String starSeat;
        if ((month == 3 && day >= 21) || (month == 4 && day <= 19)) {
            starSeat = "白羊座";
        } else if ((month == 4 && day >= 20) || (month == 5 && day <= 20)) {
            starSeat = "金牛座";
        } else if ((month == 5 && day >= 21) || (month == 6 && day <= 21)) {
            starSeat = "双子座";
        } else if ((month == 6 && day >= 22) || (month == 7 && day <= 22)) {
            starSeat = "巨蟹座";
        } else if ((month == 7 && day >= 23) || (month == 8 && day <= 22)) {
            starSeat = "狮子座";
        } else if ((month == 8 && day >= 23) || (month == 9 && day <= 22)) {
            starSeat = "处女座";
        } else if ((month == 9 && day >= 23) || (month == 10 && day <= 23)) {
            starSeat = "天秤座";
        } else if ((month == 10 && day >= 24) || (month == 11 && day <= 22)) {
            starSeat = "天蝎座";
        } else if ((month == 11 && day >= 23) || (month == 12 && day <= 21)) {
            starSeat = "射手座";
        } else if ((month == 12 && day >= 22) || (month == 1 && day <= 19)) {
            starSeat = "摩羯座";
        } else if ((month == 1 && day >= 20) || (month == 2 && day <= 18)) {
            starSeat = "水瓶座";
        } else {
            starSeat = "双鱼座";
        }
        return starSeat;
    }
}
