package com.s33me.hamsandwich;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String date, String call, String name, String freq) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.DATE, date);
        contentValue.put(DatabaseHelper.CALL, call);
		contentValue.put(DatabaseHelper.NAME, name);
		contentValue.put(DatabaseHelper.FREQ, freq);
        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
    }
    public Cursor select(String c, String n) {
        String callArg = c;
        String nameArg = n;
        String selectQuery = "SELECT  _id, date, call, name, freq  FROM " + DatabaseHelper.TABLE_NAME + " WHERE call > ''";
        if (!callArg.equals("")) {
            callArg = callArg+"%";
            selectQuery = "SELECT  _id, date, call, name, freq  FROM " + DatabaseHelper.TABLE_NAME + " WHERE call like " + "'"+callArg+"'";
        } else if (!nameArg.equals("")) {
            nameArg = nameArg+"%";
            selectQuery = "SELECT  _id, date, call, name, freq  FROM " + DatabaseHelper.TABLE_NAME + " WHERE name like " + "'"+nameArg+"'";
        }


        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.DATE, DatabaseHelper.CALL, DatabaseHelper.NAME, DatabaseHelper.FREQ};
        Cursor cursor = database.rawQuery(selectQuery, null);
        //startManagingCursor(cursor);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    public Cursor fetch() {
        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.DATE, DatabaseHelper.CALL, DatabaseHelper.NAME, DatabaseHelper.FREQ};
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        //startManagingCursor(cursor);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id, String date, String call, String name, String freq) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.DATE, date);
        contentValues.put(DatabaseHelper.CALL, call);
		contentValues.put(DatabaseHelper.NAME, name);
		contentValues.put(DatabaseHelper.FREQ, freq);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + _id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + "=" + _id, null);
    }

}

