package com.maciejprogramuje.facebook.bloodpressurenew.dbsql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.maciejprogramuje.facebook.bloodpressurenew.screens.main.OneMeasurement;

public class DbAdapter implements BaseColumns {
    public class DbEntry implements BaseColumns {
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_SYS = "sys";
        public static final String COLUMN_NAME_DIA = "dia";
        public static final String COLUMN_NAME_PULSE = "pulse";
    }

    static String sqlCreateTable() {
        return "CREATE TABLE " + DbHelper.TABLE_BLOOD_PRESSURE + " (" +
                DbAdapter.DbEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DbAdapter.DbEntry.COLUMN_NAME_DATE + " TEXT NOT NULL," +
                DbAdapter.DbEntry.COLUMN_NAME_SYS + " INTEGER," +
                DbAdapter.DbEntry.COLUMN_NAME_DIA + " INTEGER," +
                DbAdapter.DbEntry.COLUMN_NAME_PULSE + " INTEGER);";
    }

    static String sqlDeleteAllTable() {
        return "DROP TABLE IF EXIST " + DbHelper.TABLE_BLOOD_PRESSURE;
    }

    static public void deleteAllMesurementsFromDb(Context context) {
        openDbToWrite(context, new DbHelper(context)).delete(DbHelper.TABLE_BLOOD_PRESSURE, null, null);
    }

    public static Cursor getAllMeasurements(Context context) {
        return openDbToRead(context, new DbHelper(context)).query(
                DbHelper.TABLE_BLOOD_PRESSURE,
                null,
                null,
                null,
                null,
                null,
                DbEntry.COLUMN_NAME_DATE
        );
    }

    public static Cursor getOneMeasurementFromDb(Context context, long id) {
        return openDbToRead(context, new DbHelper(context)).query(
                DbHelper.TABLE_BLOOD_PRESSURE,
                null,
                DbAdapter.DbEntry._ID + " = ?",
                new String[]{id + ""},
                null,
                null,
                null
        );
    }

    static public void addOneMeasurementToDb(Context context, OneMeasurement oneMeasurement) {
        ContentValues cv = new ContentValues();
        cv.put(DbEntry.COLUMN_NAME_DATE, oneMeasurement.getDate());
        cv.put(DbEntry.COLUMN_NAME_SYS, oneMeasurement.getSys());
        cv.put(DbEntry.COLUMN_NAME_DIA, oneMeasurement.getDia());
        cv.put(DbEntry.COLUMN_NAME_PULSE, oneMeasurement.getPulse());

        openDbToWrite(context, new DbHelper(context)).insert(DbHelper.TABLE_BLOOD_PRESSURE, null, cv);
    }

    static public void deleteOneMeasurementFromDb(Context context, long id) {
        openDbToWrite(context, new DbHelper(context)).delete(DbHelper.TABLE_BLOOD_PRESSURE, DbEntry._ID + "=" + id, null);
    }

    private static SQLiteDatabase openDbToRead(Context context, SQLiteOpenHelper sqLiteOpenHelper) {
        return sqLiteOpenHelper.getReadableDatabase();
    }

    private static SQLiteDatabase openDbToWrite(Context context, SQLiteOpenHelper sqLiteOpenHelper) {
        return sqLiteOpenHelper.getWritableDatabase();
    }

    public static void closeDb(Context context, SQLiteOpenHelper sqLiteOpenHelper) {
        sqLiteOpenHelper.close();
    }
}
