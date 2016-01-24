package com.ec.singleOut.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by jasshine_xxg on 2016/1/2.
 */
public class DateUtil {

    private final static String format = "yyyy-MM-dd HH:mm:ss";

    /**
     * @param dateTime
     * @return -1 means occur someException
     */
    public static long convertStringDate2LongDate(String dateTime)  {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
             date = simpleDateFormat.parse(dateTime);
             return date.getTime();
        } catch (Exception e) {
            return -1L;
        }
    }

    public static String convertLongDate2StringDate(long dateTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = new Date(dateTime);
        return simpleDateFormat.format(date);
    }

    public static String convertDate2String(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    /**
     *
     * @param dateTime
     * @return date if date == null, it means occur some exception
     */
    public static Date convertStringDate2Date(String dateTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateTime);
        } catch (Exception e) {
        }
        return date;
    }

    /**
     * @param dateTime  :时间
     * @param days：》0表示该时间后的多少天，《0 表示该时间前的天数
     * @return
     */
    public static String addDateOfDay(String dateTime,int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(convertStringDate2Date(dateTime));
        calendar.add(Calendar.DAY_OF_YEAR,days);
        Date d = calendar.getTime();
        return convertDate2String(d);
    }

    public static String addDateOfHour(String dateTime, int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(convertStringDate2Date(dateTime));
        calendar.add(Calendar.HOUR,hour);
        Date d = calendar.getTime();
        return convertDate2String(d);
    }

    public static String removeDateOfHour(String dateTime, int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(convertStringDate2Date(dateTime));
        calendar.add(Calendar.HOUR,-hour);
        Date d = calendar.getTime();
        return convertDate2String(d);
    }



    public static int convetDate2Int(Date date) {
        long time = date.getTime();
        String string2Time = time + "";
        String int2String = string2Time.substring(0, string2Time.length() - 3);
        return Integer.parseInt(int2String);
    }

    public static void main(String[] args) throws ParseException {
        System.out.println(convertStringDate2LongDate("2014-12-11 13:13:04"));
    }
}
