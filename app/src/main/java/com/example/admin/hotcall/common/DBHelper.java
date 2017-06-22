package com.example.admin.hotcall.common;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.example.admin.hotcall.obj.Contact;

/**
 * Created by sbt-vasyukov-sv on 20.06.2017 14:50 14:52 14:53
 * DBHelper utils
 */

public class DBHelper extends SQLiteOpenHelper {

    private final String LOG_TAG = this.getClass().getName();

    public DBHelper(Context context) {
        super(context, Utils.getApplicationName(context), null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(LOG_TAG, "--- onCreate database ---");
        db.execSQL("create table " + Utils.TABLE + "(id integer primary key,idcontact integer, name text,number text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //stub
    }

    public void insert(SQLiteDatabase db, Contact contact) {
        ContentValues cv = new ContentValues();
        cv.put("id", contact.getId());
        cv.put("idcontact", contact.getIdContact());
        cv.put("name", contact.getName());
        cv.put("number", contact.getNumber());
        db.insert(Utils.TABLE, null, cv);
    }
}