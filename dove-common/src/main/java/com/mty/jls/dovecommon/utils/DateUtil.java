package com.mty.jls.dovecommon.utils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * @author jiangpeng
 * @date 2020/10/2117:01
 */
public class DateUtil {

    /**
     * @param str
     * @return
     * @Title
     * @Description 将带有纳秒的时间字符串转换成LocalDateTime
     */
    public static LocalDateTime strToLocalDateTime(String str) {
        return LocalDateTime.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static String timeStamp2Date(Long seconds){
     return timeStamp2Date(seconds,null);
    }
    /**
     * 时间戳转换成日期格式字符串
     *
     * @param seconds 精确到秒的时间戳
     * @param formatter
     * @return
     */
    public static String timeStamp2Date(Long seconds, DateTimeFormatter formatter) {
        if (formatter == null) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        }
        final LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(seconds, 0, ZoneOffset.ofHours(8));
        return localDateTime.format(formatter);
    }

    /**
     * 日期格式为 UTC (格林威治时间 0时区)   并且还带有  ***T...Z 格式的东西
     * @param seconds
     * @return
     */
    public static String timeStamp2UTC(Long seconds) {
        final LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(seconds, 0, ZoneOffset.ofHours(8));
        return localDateTime.toInstant(ZoneOffset.UTC).toString();
    }

}
