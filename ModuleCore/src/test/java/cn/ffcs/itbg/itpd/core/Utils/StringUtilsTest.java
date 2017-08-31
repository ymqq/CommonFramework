package cn.ffcs.itbg.itpd.core.Utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * @Desc: cn.ffcs.itbg.itpd.core.Utils.StringUtils.java 单元测试类
 *
 * @Author: Tyras on 2017/4/20 15:57.
 */
public class StringUtilsTest {

    @Test
    public void isEmpty() throws Exception {

        assertTrue(StringUtils.isEmpty(null));

        assertTrue(StringUtils.isEmpty(""));

        assertFalse(StringUtils.isEmpty("No Empty"));

    }

    @Test
    public void isNull() throws Exception {

        assertTrue(StringUtils.isNull(null));

        assertFalse(StringUtils.isNull(""));

        assertFalse(StringUtils.isNull("No Empty"));

    }

    @Test
    public void getString() throws Exception {

        // Test Default Value
        String excepted = "";
        String actual = "";
        assertEquals(excepted, StringUtils.getString(actual));

        excepted = "";
        actual = null;
        assertEquals(excepted, StringUtils.getString(actual));

        excepted = "default";
        actual = null;
        assertEquals(excepted, StringUtils.getString(actual, excepted));

        excepted = "default";
        actual = "";
        assertEquals(excepted, StringUtils.getString(actual, excepted));


        // Test actual value
        excepted = "No Empty";
        actual = "No Empty";
        assertEquals(excepted, StringUtils.getString(actual));

        excepted = "default";
        actual = "No Empty";
        assertNotEquals(excepted, StringUtils.getString(actual, excepted));

    }

    @Test
    public void equals() throws Exception {

        String s1 = null;
        String s2 = null;
        assertFalse(StringUtils.equals(s1, s2));

        s1 = null;
        s2 = "";
        assertFalse(StringUtils.equals(s1, s2));

        s1 = "";
        s2 = null;
        assertFalse(StringUtils.equals(s1, s2));

        s1 = "";
        s2 = "";
        assertTrue(StringUtils.equals(s1, s2));

        s1 = "No Empty";
        s2 = "";
        assertFalse(StringUtils.equals(s1, s2));

        s1 = "";
        s2 = "No Empty";
        assertFalse(StringUtils.equals(s1, s2));

        s1 = "No Empty";
        s2 = "No Empty";
        assertTrue(StringUtils.equals(s1, s2));

    }

}