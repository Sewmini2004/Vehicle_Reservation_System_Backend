package com.example.Vehicle_Reservation_System_Backend.utils;

import com.example.Vehicle_Reservation_System_Backend.exception.DateFormatException;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class DateFormatUtils {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    static {
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public static Date convertUtilToSqlDate(java.util.Date utilDate) {
        if (utilDate == null) {
            return null;
        }
        return new Date(utilDate.getTime());
    }
    public static java.util.Date convertSqlToUtilDate(Date sqlDate) {
        if (sqlDate == null) {
            return null;
        }
        return new java.util.Date(sqlDate.getTime());
    }
    public static java.util.Date toDate(String dateString) {
        try {
            return sdf.parse(dateString);
        } catch (ParseException ex) {
            System.out.println("LOG::Error::Date Parsing::Date format should be yyyy-MM-dd");
            throw new DateFormatException("Date format should be yyyy-MM-dd");
        }
    }
    public static String toDateString(java.util.Date date) {
        if (date == null) {
            return null;
        }
        return sdf.format(date);
    }
}
