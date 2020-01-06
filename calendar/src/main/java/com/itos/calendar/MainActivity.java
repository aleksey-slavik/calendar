package com.itos.calendar;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.itos.calendar.event.CalendarEventDecorator;
import com.itos.calendar.utils.DateUtils;
import com.itos.calendar.utils.IntentData;
import com.itos.calendar.view.NotePreviewActivity;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MaterialCalendarView calendarView;

    private FloatingActionButton addEventButton;

    final List<Long> dateList = Arrays.asList(
            1577836800000L,
            1578009600000L,
            1578096000000L,
            1578182400000L,
            1578268800000L);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find views
        calendarView = findViewById(R.id.calendarView);
        addEventButton = findViewById(R.id.addEvent);

        calendarView.setShowOtherDates(MaterialCalendarView.SHOW_ALL);
        calendarView.setOnDateChangedListener((MaterialCalendarView widget, CalendarDay day, boolean selected) -> showNotePreview(widget, day));
        addEventButton.setOnClickListener(this::addEventRange);

        // add saved events
        showEventDays();
    }

    private void addEventRange(View view) {
        Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    private void showNotePreview(View view, CalendarDay day) {
        Intent intent = new Intent(this, NotePreviewActivity.class);
        intent.putExtra(IntentData.CALENDAR_DAY, day);
        startActivity(intent);
    }

    private void showEventDays() {
        List<CalendarDay> leftDays = new ArrayList<>();
        List<CalendarDay> rightDays = new ArrayList<>();
        List<CalendarDay> innerDays = new ArrayList<>();

        for (Long currentDate : dateList) {
            LocalDate current = DateUtils.getLocaleDate(currentDate);
            boolean right = false;
            boolean left = false;

            for (Long otherDate : dateList) {
                if (DateUtils.isSameDay(
                        currentDate,
                        DateUtils.addDay(otherDate, 1))) {
                    left = true;
                }

                if (DateUtils.isSameDay(
                        DateUtils.addDay(currentDate, 1),
                        otherDate)) {
                    right = true;
                }
            }

            if (left && right) {
                innerDays.add(CalendarDay.from(current));
            } else if (left) {
                leftDays.add(CalendarDay.from(current));
            } else if (right) {
                rightDays.add(CalendarDay.from(current));
            }
        }

        decorate(innerDays, R.drawable.ic_range_inner);
        decorate(leftDays, R.drawable.ic_range_left_border);
        decorate(rightDays, R.drawable.ic_range_right_border);

    }

    private void decorate(List<CalendarDay> days, int drawable) {
        calendarView.addDecorators(
                new CalendarEventDecorator(MainActivity.this, drawable, days));
    }
}
