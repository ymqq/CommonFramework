package cn.ffcs.itbg.itpd.core.Utils;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @Desc: cn.ffcs.itbg.itpd.core.Utils.DateUtils.java 单元测试类
 *
 * @Author: Tyras on 2017/4/20 17:42.
 */
public class DateUtilsTest {

    @Test
    public void toDate() throws Exception {
        String format = "yyyyMMddHHmm";
        String time = "201704201550";
        Date date = DateUtils.toDate(time, format);

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar calendar = Calendar.getInstance();
        // 月份从0开始: 2017-04-20 15:50
        calendar.set(2017, 03, 20, 15, 50);
        assertEquals(sdf.format(calendar.getTime()), sdf.format(date));

        // 月份从0开始: 2017-05-20 15:50
        calendar.set(2017, 04, 20, 15, 50);
        assertNotEquals(sdf.format(calendar.getTime()), sdf.format(date));


        assertNull(DateUtils.toDate(null, format));

        assertNull(DateUtils.toDate(time, ""));

        assertNotNull(DateUtils.toDate(time, format));
    }

    @Test
    public void stringify() throws Exception {
        String format = "yyyyMMddHHmm";
        String time = "201704201550";

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar calendar = Calendar.getInstance();
        // 月份从0开始: 2017-04-20 15:50
        calendar.set(2017, 03, 20, 15, 50);
        assertEquals(time, DateUtils.stringify(calendar.getTime(), format));

        assertEquals("", DateUtils.stringify(null, format));
    }

    @Test
    public void getDateForDay() throws Exception {
        String format = "yyyyMMddHHmm";
        String time = "201704201550";

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar calendar = Calendar.getInstance();
        // 月份从0开始: 2017-04-26 15:50
        calendar.set(2017, 03, 26, 15, 50);
        Date actual = DateUtils.getDateForDay(calendar.getTime(), -6);
        assertEquals(time, sdf.format(actual));

        // 月份从0开始: 2017-04-14 15:50
        calendar.set(2017, 03, 14, 15, 50);
        actual = DateUtils.getDateForDay(calendar.getTime(), 6);
        assertEquals(time, sdf.format(actual));


        time = "201704301550";
        // 月份从0开始: 2017-05-01 15:50
        calendar.set(2017, 04, 01, 15, 50);
        actual = DateUtils.getDateForDay(calendar.getTime(), -1);
        assertEquals(time, sdf.format(actual));

        time = "201705011550";
        // 月份从0开始: 2017-04-30 15:50
        calendar.set(2017, 03, 30, 15, 50);
        actual = DateUtils.getDateForDay(calendar.getTime(), 1);
        assertEquals(time, sdf.format(actual));
    }

    @Test
    public void getDateForMonth() throws Exception {
        String format = "yyyyMMddHHmm";
        String time = "201704201550";

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar calendar = Calendar.getInstance();
        // 月份从0开始: 2017-05-20 15:50
        calendar.set(2017, 04, 20, 15, 50);
        Date actual = DateUtils.getDateForMonth(calendar.getTime(), -1);
        assertEquals(time, sdf.format(actual));

        // 月份从0开始: 2017-03-20 15:50
        calendar.set(2017, 02, 20, 15, 50);
        actual = DateUtils.getDateForMonth(calendar.getTime(), 1);
        assertEquals(time, sdf.format(actual));


        time = "201604201550";
        // 月份从0开始: 2017-04-20 15:50
        calendar.set(2017, 03, 20, 15, 50);
        actual = DateUtils.getDateForMonth(calendar.getTime(), -12);
        assertEquals(time, sdf.format(actual));

        time = "201704201550";
        // 月份从0开始: 2016-04-20 15:50
        calendar.set(2016, 03, 20, 15, 50);
        actual = DateUtils.getDateForMonth(calendar.getTime(), 12);
        assertEquals(time, sdf.format(actual));
    }

    @Test
    public void getDateForYear() throws Exception {
        String format = "yyyyMMddHHmm";
        String time = "201704201550";

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar calendar = Calendar.getInstance();
        // 月份从0开始: 2018-04-20 15:50
        calendar.set(2018, 03, 20, 15, 50);
        Date actual = DateUtils.getDateForYear(calendar.getTime(), -1);
        assertEquals(time, sdf.format(actual));

        // 月份从0开始: 2016-04-20 15:50
        calendar.set(2016, 03, 20, 15, 50);
        actual = DateUtils.getDateForYear(calendar.getTime(), 1);
        assertEquals(time, sdf.format(actual));
    }

    @Test
    public void sameDate() throws Exception {
        String format = "yyyyMMddHHmm";
        Calendar calendar = Calendar.getInstance();
        // 月份从0开始: 2017-04-20 15:50
        calendar.set(2017, 03, 20, 15, 50);
        Date date1 = calendar.getTime();
        assertTrue(DateUtils.sameDate(date1, date1, format));

        // 月份从0开始: 2017-05-20 15:50
        calendar.set(2017, 04, 20, 15, 50);
        Date date2 = calendar.getTime();
        assertFalse(DateUtils.sameDate(date1, date2, format));

        // 月份从0开始: 2017-04-20 18:50
        calendar.set(2017, 03, 20, 18, 50);
        date2 = calendar.getTime();
        assertFalse(DateUtils.sameDate(date1, date2, format));

        format = "yyyyMMdd";
        // 月份从0开始: 2017-04-20 18:50
        calendar.set(2017, 03, 20, 18, 50);
        date2 = calendar.getTime();
        assertTrue(DateUtils.sameDate(date1, date2, format));
    }

    @Test
    public void compareDate() throws Exception {
        Calendar calendar = Calendar.getInstance();
        // 月份从0开始: 2017-04-20 15:50
        calendar.set(2017, 03, 20, 15, 50);
        Date date1 = calendar.getTime();
        assertTrue(DateUtils.compareDate(date1, date1) == 0);

        // 月份从0开始: 2017-05-20 15:50
        calendar.set(2017, 04, 20, 15, 50);
        Date date2 = calendar.getTime();
        assertTrue(DateUtils.compareDate(date1, date2) == -1);

        // 月份从0开始: 2017-04-20 15:50
        calendar.set(2017, 03, 20, 18, 50);
        date2 = calendar.getTime();
        assertTrue(DateUtils.compareDate(date1, date2) == -1);
        assertTrue(DateUtils.compareDate(date2, date1) == 1);


        assertTrue(DateUtils.compareDate(null, null) == -2);
    }

}