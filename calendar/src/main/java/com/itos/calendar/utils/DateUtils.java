package com.itos.calendar.utils;


import org.threeten.bp.LocalDate;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static final long MILLIS_PER_DAY = 86400000L;

    public static boolean isSameDay(long date1, long date2) {
        date1 /= MILLIS_PER_DAY;
        date2 /= MILLIS_PER_DAY;
        return date1 == date2;
    }

    public static long addDay(long date, int count) {
        return date + count * MILLIS_PER_DAY;
    }

    public static LocalDate getLocaleDate(long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(date));

        return LocalDate.of(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH));
    }
}
