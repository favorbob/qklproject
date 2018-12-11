package cc.stbl.token.innerdisc.common.util;

import java.util.Calendar;

/**
 * 日期时间工具
 */
public class DateUtils {
    public static void main(String[] args) {
        System.out.println(DateUtils.getHourMinuSecStr());
    }

    /**
     * 简易获取时分秒字符串
     * @return 时分秒字符串
     */
    public static String getHourMinuSecStr(){
        Calendar cal = Calendar.getInstance();
//        int year = cal.get(Calendar.YEAR);//获取年份
//        int month=cal.get(Calendar.MONTH);//获取月份
//        int day=cal.get(Calendar.DATE);//获取日
        int hour=cal.get(Calendar.HOUR);//小时
        int minute=cal.get(Calendar.MINUTE);//分
        int second=cal.get(Calendar.SECOND);//秒
//        int WeekOfYear = cal.get(Calendar.DAY_OF_WEEK);//一周的第几天
        String timeStr = String.valueOf(hour)+String.valueOf(minute)+String.valueOf(second) ;
        return timeStr;
    }
}
