package com.maciejprogramuje.facebook.bloodpressurenew.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DbAdapter implements BaseColumns {
    public class DbEntry implements BaseColumns {
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_SYS = "sys";
        public static final String COLUMN_NAME_DIA = "dia";
        public static final String COLUMN_NAME_PULSE = "pulse";
    }

    static String sqlCreateTable(String tableName) {
        return "CREATE TABLE " + tableName + " (" +
                DbAdapter.DbEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DbAdapter.DbEntry.COLUMN_NAME_DATE + " TEXT NOT NULL," +
                DbAdapter.DbEntry.COLUMN_NAME_SYS + " INTEGER," +
                DbAdapter.DbEntry.COLUMN_NAME_DIA + " INTEGER," +
                DbAdapter.DbEntry.COLUMN_NAME_PULSE + " INTEGER);";
    }

    static String sqlDeleteAllTable(String tableName) {
        return "DROP TABLE IF EXIST " + tableName;
    }

    static public void deleteAllMesurementsFromDb(Context context, String tableName) {
        openDbToWrite(context, new DbHelper(context)).delete(tableName, null, null);
    }

    public static Cursor getAllMeasurements(Context context, String tableName) {
        return openDbToRead(context, new DbHelper(context)).query(
                tableName,
                null,
                null,
                null,
                null,
                null,
                DbEntry.COLUMN_NAME_DATE
        );
    }

    public static Cursor getOneMeasurementFromDb(Context context, long id, String tableName) {
        return openDbToRead(context, new DbHelper(context)).query(
                tableName,
                null,
                DbAdapter.DbEntry._ID + " = ?",
                new String[]{id + ""},
                null,
                null,
                null
        );
    }

    static public void addOneMeasurementToDb(Context context, String tableName, String date, int sys, int dia, int pulse) {
        ContentValues cv = new ContentValues();
        cv.put(DbEntry.COLUMN_NAME_DATE, date);
        cv.put(DbEntry.COLUMN_NAME_SYS, sys);
        cv.put(DbEntry.COLUMN_NAME_DIA, dia);
        cv.put(DbEntry.COLUMN_NAME_PULSE, pulse);

        openDbToWrite(context, new DbHelper(context)).insert(tableName, null, cv);
    }

    static public void deleteOneMeasurementFromDb(Context context, String tableName, long id) {
        openDbToWrite(context, new DbHelper(context)).delete(tableName, DbEntry._ID + "=" + id, null);
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
