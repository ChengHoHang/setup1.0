package com.chh.setup.myutils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author chh
 * @date 2020/1/11 21:31
 */
public class DateUtils {

    /**
     * @param timestamp Long类型时间戳
     * @param pattern "yyyy-MM-dd HH:mm:ss".........
     * @return
     */
    public static String timestamp2Date(Long timestamp, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(new Date(timestamp));
    }

    public static Long Date2timestamp(String date, String pattern) throws ParseException {
        Date parse = new SimpleDateFormat(pattern).parse(date);
        return parse.getTime();
    }
}
