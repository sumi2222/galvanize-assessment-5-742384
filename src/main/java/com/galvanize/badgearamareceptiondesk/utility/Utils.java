package com.galvanize.badgearamareceptiondesk.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class Utils {
   /* private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat tf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public static Date getDate(String date) throws ParseException {
        return new Date(df.parse(date).getTime());
    }

    public static String currentDate() {
        return df.format(Calendar.getInstance().getTimeInMillis());
    }

    public static Date currBeginDay() throws ParseException {
        return getDate(currentDate());
    }

    public static Date currentTime() {
        return new Date(Calendar.getInstance().getTimeInMillis());
    }

    public static Date atStartOfDay(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return localDateTimeToDate(startOfDay);
    }

    public static Date atEndOfDay(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return localDateTimeToDate(endOfDay);
    }

    private static LocalDateTime dateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    private static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
*/
}
