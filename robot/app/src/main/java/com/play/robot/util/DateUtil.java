package com.play.robot.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by llt on 2017/11/9.
 */

public class DateUtil {

    //date 转 String 年月日
    public static String dateToStr(Date dateDate) {
        return dateToStr(dateDate,"yyyy-MM-dd");
    }

    //date 转 String 年月日
    public static String dateToStr(Date dateDate, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    //date 转 String 年月日
    public static int dateToInt(Date dateDate, String format) {
        return Integer.valueOf(dateToStr(dateDate,format));
    }

    //String 转 date
    public static Date str2Date(String dateStr) {
        return str2Date(dateStr,"yyyy-MM-dd");
    }
    //String 转 date
    public static Date str2Date(String dateStr, String format) {
        SimpleDateFormat df;
        df = new SimpleDateFormat(format);
        try {
            return df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Date();
    }

    public static String longToStr(long dateLong) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long lt = new Long(dateLong);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static String longToStr(long dateLong,String format) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        long lt = new Long(dateLong);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }


    //获取当前date
    public static Date getNowDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    //获取
    public static Date getDate(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, day); //向后走一天
        return calendar.getTime();
    }


    public static Calendar getSelectedDate(String str) {
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.setTime(DateUtil.str2Date(str));
        return selectedDate;
    }

    public static Calendar getStartDate() {
        Calendar startDate = Calendar.getInstance();
        startDate.set(1949, 0, 1);
        return startDate;
    }

    public static Calendar getEndDate() {
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(DateUtil.getNowDate());
        return endDate;
    }


    // 根据年月日计算年龄,birthTimeString:"1994-11-14"
    public static int getAgeFromBirthTime(String birthTimeString) {
        Date date = str2Date(birthTimeString);
        // 得到当前时间的年、月、日
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            int yearNow = cal.get(Calendar.YEAR);
            int monthNow = cal.get(Calendar.MONTH) + 1;
            int dayNow = cal.get(Calendar.DATE);
            //得到输入时间的年，月，日
            cal.setTime(date);
            int selectYear = cal.get(Calendar.YEAR);
            int selectMonth = cal.get(Calendar.MONTH) + 1;
            int selectDay = cal.get(Calendar.DATE);
            // 用当前年月日减去生日年月日
            int yearMinus = yearNow - selectYear;
            int monthMinus = monthNow - selectMonth;
            int dayMinus = dayNow - selectDay;
            int age = yearMinus;// 先大致赋值
            if (yearMinus <= 0) {
                age = 0;
            }
            if (monthMinus < 0) {
                age = age - 1;
            } else if (monthMinus == 0) {
                if (dayMinus < 0) {
                    age = age - 1;
                }
            }
            return age;
        }
        return 0;
    }

    public static String getDatePlus0(int date){
        if(date < 10){
            return "0" + date;
        }
        return date+"";
    }


    public static String getWith0(int i){
        if(i < 10){
            return "0" + i;
        }
        return ""+i;
    }

    public static int getGapCount(String startTime,String endTime) {

        Date startDate = DateUtil.str2Date(startTime);
        Date endDate = DateUtil.str2Date(endTime);

        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(startDate);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(endDate);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);

        return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24)) + 1;
    }

    //计算time2减去time1的差值 差值只设置 几天 几个小时 或 几分钟 根据差值返回多长之间前或多长时间后

    public static int getDistanceTime(long time1, long time2) {
        long day;
        long diff;

        if (time1 < time2) {
            diff = time2 - time1;
        } else {
            diff = time1 - time2;
        }
        day = diff / (24 * 60 * 60 * 1000);

        return (int) day;
    }
}