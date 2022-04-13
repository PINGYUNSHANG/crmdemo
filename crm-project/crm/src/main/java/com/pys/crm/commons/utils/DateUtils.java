package com.pys.crm.commons.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 专门对date类型数据进行处理的工具类
 */
public class DateUtils {
    public static String formatDateTime(Date date) {
        //对指定的date对象进行格式化
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(date);
        return dateStr;
    }

    public static String formatDate(Date date) {
        //对指定的date对象进行格式化
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(date);
        return dateStr;
    }
    public static String formatTime(Date date) {
        //对指定的date对象进行格式化
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String dateStr = sdf.format(date);
        return dateStr;
    }
}
