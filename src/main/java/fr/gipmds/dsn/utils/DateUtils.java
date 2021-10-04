package fr.gipmds.dsn.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static String format(Date date) {
        SimpleDateFormat formater = new SimpleDateFormat("yyyyMMddHHmmss");
        return formater.format(date);
    }

    public static String formatShort(Date date) {
        SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
        return formater.format(date);
    }

    public static Date parse(String date) throws ParseException {
        SimpleDateFormat formater = new SimpleDateFormat("yyyyMMddHHmmss");
        return formater.parse(date);
    }

    public static Date parseShort(String date) throws ParseException {
        if (date.length() != 8) {
            throw new ParseException("Not in format yyyyMMdd", date.length());
        }
        SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
        return formater.parse(date);
    }
}
