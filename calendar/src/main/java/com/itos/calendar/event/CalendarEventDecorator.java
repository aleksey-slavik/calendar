package com.itos.calendar.event;

import android.content.Context;
import android.graphics.Color;
import android.text.style.ForegroundColorSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CalendarEventDecorator implements DayViewDecorator {

    private Context context;

    private int drawable;

    private Set<CalendarDay> dates;

    public CalendarEventDecorator(Context context, int drawable, Collection<CalendarDay> dates) {
        this.context = context;
        this.drawable = drawable;
        this.dates = new HashSet<>(dates);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setSelectionDrawable(context.getDrawable(drawable));
    }
}
