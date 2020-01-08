package com.itos.calendar.view;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.itos.calendar.R;
import com.itos.calendar.db.CalendarDatabaseHelper;
import com.itos.calendar.entity.DayRecord;
import com.itos.calendar.entity.DayType;
import com.itos.calendar.event.CalendarEventDecorator;
import com.itos.calendar.utils.DateUtils;
import com.itos.calendar.utils.Constants;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class CalendarViewActivity extends AppCompatActivity {

    private MaterialCalendarView calendarView;

    private FloatingActionButton addEventButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view);

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
        intent.putExtra(Constants.CALENDAR_DAY_KEY, day);
        startActivity(intent);
    }

    private void showEventDays() {
        CalendarDatabaseHelper databaseHelper = CalendarDatabaseHelper.getInstance(this);
        List<DayRecord> dayRecords = databaseHelper.getAllDayRecords();
        List<CalendarDay> markDays = new ArrayList<>();

        for (DayRecord currentDate : dayRecords) {
            if (currentDate.getDayType() == DayType.CRITICAL) {
                LocalDate current = DateUtils.getLocaleDate(currentDate.getDate());
                markDays.add(CalendarDay.from(current));
            }
        }

        decorate(markDays, R.drawable.ic_day_marker);

    }

    private void decorate(List<CalendarDay> days, int drawable) {
        calendarView.addDecorators(
                new CalendarEventDecorator(CalendarViewActivity.this, drawable, days));
    }
}
