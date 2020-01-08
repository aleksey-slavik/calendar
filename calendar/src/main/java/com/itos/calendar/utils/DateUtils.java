package com.itos.calendar.utils;


import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static final long MILLIS_PER_DAY = 86400000L;

    public static LocalDate getLocaleDate(long date) {
        Calendar calendar = Calendar.getInstance();
        date -= date % MILLIS_PER_DAY;
        calendar.setTime(new Date(date));

        return LocalDate.of(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH));
    }

    public static String getFormattedDate(CalendarDay day) {
        return day.getDate().format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));
    }

    public static long toLong(LocalDate localDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(localDate.getYear(), localDate.getMonthValue() - 1, localDate.getDayOfMonth());
        long timeInMillis = calendar.getTimeInMillis();
        timeInMillis -= timeInMillis % MILLIS_PER_DAY;
        return timeInMillis;
    }
}
