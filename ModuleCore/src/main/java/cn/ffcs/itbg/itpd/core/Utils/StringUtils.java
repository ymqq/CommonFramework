package cn.ffcs.itbg.itpd.core.Utils;

/**
 * @Desc: 字符串工具类
 *
 * 对应编写了测试类：test/cn.ffcs.itbg.itpd.core.Utils.StringUtilsTest.java
 *
 * 快捷定位测试类：
 *  1）鼠标右键->Go To -> Test -> StringUtilsTest
 *  2）Ctrl + Shift + T -> StringUtilsTest
 *
 * 新增方法请同时编写对应的测试方法：快速定位后，现在Create New Test...，然后选择新增方法，更新测试类
 *
 *
 * @Author: Tyras on 2017/4/20 15:20.
 */

public class StringUtils {

    /**
     * 判断字符串是否为空，包括：null 与 ""
     *
     * @param s
     * @return
     */
    public static boolean isEmpty(String s) {
        return s == null || "".equals(s);
    }

    /**
     * 判断字符串是否为null
     *
     * @param s
     * @return
     */
    public static boolean isNull(String s) {
        return s == null;
    }

    /**
     * 获取合法的字符串展示使用，避免展示null
     *
     * @param s
     * @return
     */
    public static String getString(String s) {
        return getString(s, "");
    }

    /**
     * 获取合法的字符串用于展示，同时设定默认值。
     *
     * @param s
     * @param defaultValue
     * @return
     */
    public static String getString(String s, String defaultValue) {
        return isEmpty(s) ? defaultValue : s;
    }

    /**
     * 判断两个字符串内容是否相等
     * null时始终不等
     *
     * @param s1
     * @param s2
     * @return
     */
    public static boolean equals(String s1, String s2) {
        if (s1 == null || s2 == null) {
            return false;
        } else {
            return s1.equals(s2);
        }
    }
}
