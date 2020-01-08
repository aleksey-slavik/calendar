package com.itos.calendar.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.itos.calendar.R;
import com.itos.calendar.db.CalendarDatabaseHelper;
import com.itos.calendar.entity.DayRecord;
import com.itos.calendar.entity.DayType;
import com.itos.calendar.utils.DateUtils;
import com.itos.calendar.utils.Constants;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.threeten.bp.LocalDate;

public class NotePreviewActivity extends AppCompatActivity {

    private Switch criticalSwitch;

    private SeekBar criticalValue;

    private EditText note;

    private AppCompatButton editNoteButton;

    private LocalDate date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_preview);

        criticalSwitch = findViewById(R.id.critical_switch);
        criticalValue = findViewById(R.id.critical_value);
        note = findViewById(R.id.note_preview);
        editNoteButton = findViewById(R.id.editNoteButton);

        Intent intent = getIntent();

        if (intent != null) {
            Object intentObject = intent.getParcelableExtra(Constants.CALENDAR_DAY_KEY);

            if (intentObject instanceof CalendarDay) {
                CalendarDay day = (CalendarDay) intentObject;
                getSupportActionBar().setTitle(DateUtils.getFormattedDate(day));
                date = day.getDate();
            }
        }

        loadDayData();

        criticalSwitch.setOnCheckedChangeListener((CompoundButton button, boolean isChecked) -> showCriticalValue(isChecked));
        editNoteButton.setOnClickListener((View v) -> saveOrUpdateNote());
    }

    private void loadDayData() {
        CalendarDatabaseHelper databaseHelper = CalendarDatabaseHelper.getInstance(this);
        DayRecord dayRecord = databaseHelper.getDayRecordByDate(DateUtils.toLong(date));
        if (dayRecord != null) {
            note.setText(dayRecord.getNote(), TextView.BufferType.EDITABLE);

            if (dayRecord.getDayType() == DayType.CRITICAL) {
                criticalSwitch.setChecked(true);
                criticalValue.setVisibility(View.VISIBLE);
                criticalValue.setProgress(dayRecord.getValue());
            } else {
                criticalSwitch.setChecked(false);
                criticalValue.setVisibility(View.GONE);
            }
        }
    }

    private void showCriticalValue(boolean isChecked) {
        criticalValue.setVisibility(isChecked ? View.VISIBLE : View.GONE);
    }

    private void saveOrUpdateNote() {
        CalendarDatabaseHelper databaseHelper = CalendarDatabaseHelper.getInstance(this);

        DayRecord dayRecord = new DayRecord();
        dayRecord.setDate(DateUtils.toLong(date));
        dayRecord.setNote(note.getText().toString());
        dayRecord.setDayType(criticalSwitch.isChecked() ? DayType.CRITICAL : DayType.STANDARD);
        dayRecord.setValue(criticalSwitch.isChecked() ? criticalValue.getProgress() : 0);

        databaseHelper.createOrUpdateDayRecord(dayRecord);

        Intent intent = new Intent(this, CalendarViewActivity.class);
        startActivity(intent);
    }
}
