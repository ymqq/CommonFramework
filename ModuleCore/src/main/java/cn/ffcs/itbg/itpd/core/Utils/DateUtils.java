package cn.ffcs.itbg.itpd.core.Utils;


import android.support.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Desc:字符串工具类
 *
 * 对应编写了测试类：test/cn.ffcs.itbg.itpd.core.Utils.DateUtilsTest.java
 *
 * 快捷定位测试类：
 *  1）鼠标右键->Go To -> Test -> DateUtilsTest
 *  2）Ctrl + Shift + T -> DateUtilsTest
 *
 * 新增方法请同时编写对应的测试方法：快速定位后，现在Create New Test...，然后选择新增方法，更新测试类
 *
 *
 * @Author: Tyras on 2017/4/20 15:27.
 */

public class DateUtils {

    /**
     * 将日期字符串按照格式化规则转换为日期实例
     *
     * @param date
     * @param format
     * @return
     */
    public static Date toDate(@Nullable String date, @Nullable String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将日期实例对象按照格式化规则序列化为字符串
     *
     * @param date
     * @param format
     * @return
     */
    public static String stringify(@Nullable Date date, @Nullable String format) {
        if (date == null) {
            return "";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(date);
        }
    }

    /**
     * 获取差异dValue天数后的日期实例对象
     *
     * @param date
     * @param dValue
     * @return
     */
    public static Date getDateForDay(@Nullable Date date, int dValue) {
        if (date == null) {
            return null;
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + dValue);
            return calendar.getTime();
        }
    }

    /**
     * 获取差异dValue月份后的日期实例对象
     *
     * @param date
     * @param dValue
     * @return
     */
    public static Date getDateForMonth(@Nullable Date date, int dValue) {
        if (date == null) {
            return null;
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + dValue);
            return calendar.getTime();
        }
    }

    /**
     * 获取差异dValue年数后的日期实例对象
     *
     * @param date
     * @param dValue
     * @return
     */
    public static Date getDateForYear(@Nullable Date date, int dValue) {
        if (date == null) {
            return null;
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + dValue);
            return calendar.getTime();
        }
    }

    /**
     * 比较两个日期是否相等，按照格式化规则，可以指定比对的具体日期部位
     *
     * @param date1
     * @param date2
     * @param format
     * @return
     */
    public static boolean sameDate(@Nullable Date date1, @Nullable Date date2, @Nullable String format) {
        if (date1 == null || date2 == null) {
            return false;
        } else {
            return stringify(date1, format).equals(stringify(date2, format));
        }
    }

    /**
     * 比较两个日期的先后
     * date1 < date2 返回 -1
     * date1 == date2 返回 0
     * date1 > date2 返回 1
     * 异常 返回 -2
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int compareDate(@Nullable Date date1, @Nullable Date date2) {
        if (date1 == null || date2 == null) {
            return -2;
        } else {
            long dValue = date1.getTime() - date2.getTime();
            if (dValue > 0) {
                return 1;
            } else if (dValue < 0) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}
