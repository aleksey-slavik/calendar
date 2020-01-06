package com.itos.calendar.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.itos.calendar.R;
import com.itos.calendar.utils.DateUtils;
import com.itos.calendar.utils.IntentData;
import com.prolificinteractive.materialcalendarview.CalendarDay;

public class NotePreviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_preview);

        TextView note = findViewById(R.id.note_preview);

        Intent intent = getIntent();

        if (intent != null) {
            Object intentObject = intent.getParcelableExtra(IntentData.CALENDAR_DAY);

            if (intentObject instanceof CalendarDay) {
                CalendarDay day = (CalendarDay) intentObject;
                getSupportActionBar().setTitle(DateUtils.getFormattedDate(day));
            }
        }
    }
}
