package com.example.admin.myapplication.Common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by sbt-vasyukov-sv on 20.06.2017 14:50 14:52 14:53 MyApplication7.
 * DBHelper utils
 */

public class DBHelper extends SQLiteOpenHelper {

    private final String LOG_TAG = this.getClass().getName();

    public DBHelper(Context context) {
        super(context, Utils.getApplicationName(context), null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "--- onCreate database ---");
        db.execSQL("create table " + Utils.TABLE + "(id integer primary key,name text,number text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //stub
    }
}