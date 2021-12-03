package com.example.mybill.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 使用示例
 * String pattern = "yyyy-MM-dd";
 * long timeate = 1637666333000L;
 * <p>
 * FormatDate formatDate = new FormatDate();
 * System.out.println("当前时间戳： " + formatDate.getCurTimeLong());
 * System.out.println(formatDate.getCurDate(pattern ));
 * System.out.println(formatDate.getDateToString(timeate,pattern));
 * System.out.println(formatDate.getStringToDate("2018-11-02",pattern));
 */


public class FormatDate {

    String pattern = "yyyy-MM-dd";

    /**
     * 获取系统时间戳
     *
     * @return
     */
    public long getCurTimeLong() {
        long time = System.currentTimeMillis();
        return time;
    }

    /**
     * 获取当前时间
     *
     * @param pattern
     * @return
     */
    public static String getCurDate(String pattern) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(pattern);
        return sDateFormat.format(new Date());
    }

    /**
     * 时间戳转换成字符窜
     *
     * @param milSecond
     * @param pattern
     * @return
     */
    public String getDateToString(long milSecond, String pattern) {

        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    /**
     * 将字符串转为时间戳
     *
     * @param dateString
     * @param pattern
     * @return
     */
    public static long getStringToDate(String dateString, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = new Date();
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }
}