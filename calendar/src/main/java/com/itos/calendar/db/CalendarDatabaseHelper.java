package com.itos.calendar.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.itos.calendar.entity.DayRecord;
import com.itos.calendar.entity.DayType;
import com.itos.calendar.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class CalendarDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "CalendarDatabaseHelper";

    private static final String CREATE_DATE_TABLE =
            String.format("CREATE TABLE %s (%s LONG PRIMARY KEY, %s TEXT, %s INTEGER, %s INTEGER);",
                    Constants.TABLE_DAY,
                    Constants.KEY_DAY_DATE,
                    Constants.KEY_DAY_NOTE,
                    Constants.KEY_DAY_TYPE,
                    Constants.KEY_DAY_VALUE);

    private static final String GET_DAY_RECORD_BY_DATE =
            String.format("SELECT * FROM %s WHERE %s = ?",
                    Constants.TABLE_DAY,
                    Constants.KEY_DAY_DATE);

    private static final String GET_ALL_DAY_RECORDS =
            String.format("SELECT * FROM %s",
                    Constants.TABLE_DAY);

    private static CalendarDatabaseHelper instance;

    public static synchronized CalendarDatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new CalendarDatabaseHelper(context.getApplicationContext());
        }

        return instance;
    }

    private CalendarDatabaseHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_DATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_DAY);
            onCreate(sqLiteDatabase);
        }
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(false);
    }

    public DayRecord getDayRecordByDate(long dateValue) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(GET_DAY_RECORD_BY_DATE, new String[]{String.valueOf(dateValue)});
        DayRecord dayRecord = null;

        try {
            if (cursor.moveToFirst()) {
                dayRecord = new DayRecord();
                dayRecord.setDate(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DAY_DATE)));
                dayRecord.setNote(cursor.getString(cursor.getColumnIndex(Constants.KEY_DAY_NOTE)));
                dayRecord.setDayType(DayType.values()[cursor.getInt(cursor.getColumnIndex(Constants.KEY_DAY_TYPE))]);
                dayRecord.setValue(cursor.getInt(cursor.getColumnIndex(Constants.KEY_DAY_VALUE)));
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return dayRecord;
    }

    public List<DayRecord> getAllDayRecords() {
        List<DayRecord> dayRecords = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(GET_ALL_DAY_RECORDS, null);
        DayRecord dayRecord;

        try {
            if (cursor.moveToFirst()) {
                do {
                    dayRecord = new DayRecord();
                    dayRecord.setDate(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DAY_DATE)));
                    dayRecord.setNote(cursor.getString(cursor.getColumnIndex(Constants.KEY_DAY_NOTE)));
                    dayRecord.setDayType(DayType.values()[cursor.getInt(cursor.getColumnIndex(Constants.KEY_DAY_TYPE))]);
                    dayRecord.setValue(cursor.getInt(cursor.getColumnIndex(Constants.KEY_DAY_VALUE)));
                    dayRecords.add(dayRecord);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return dayRecords;
    }

    public long createOrUpdateDayRecord(DayRecord dayRecord) {
        SQLiteDatabase db = this.getWritableDatabase();
        long dayId = -1L;
        db.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            values.put(Constants.KEY_DAY_DATE, dayRecord.getDate());
            values.put(Constants.KEY_DAY_NOTE, dayRecord.getNote());
            values.put(Constants.KEY_DAY_TYPE, dayRecord.getDayType().ordinal());
            values.put(Constants.KEY_DAY_VALUE, dayRecord.getValue());

            int rows = db.update(
                    Constants.TABLE_DAY,
                    values,
                    Constants.KEY_DAY_DATE + " = ?",
                    new String[]{
                            String.valueOf(
                                    dayRecord.getDate())});
            if (rows == 1) {
                Cursor cursor = db.rawQuery(
                        GET_DAY_RECORD_BY_DATE,
                        new String[]{
                                String.valueOf(
                                        dayRecord.getDate())});

                try {
                    if (cursor.moveToFirst()) {
                        dayId = cursor.getLong(0);
                        db.setTransactionSuccessful();
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            } else {
                db.insertOrThrow(Constants.TABLE_DAY, null, values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            db.endTransaction();
        }

        return dayId;
    }
}
