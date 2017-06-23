package com.example.admin.hotcall.common;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.admin.hotcall.obj.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sbt-vasyukov-sv on 20.06.2017 14:50 14:52 14:53
 * DBHelper utils
 */

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, Utils.getApplicationName(context), null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + Utils.TABLE + "(id integer primary key,idcontact integer, name text,number text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //stub
    }

    public void delete(SQLiteDatabase db, Contact contact) {
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

    public List<Contact> selectAll(SQLiteDatabase db) {
        List<Contact> contacts = new ArrayList<>();
        Cursor cursor = db.query(Utils.TABLE, null, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Contact contact = new Contact(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getInt(cursor.getColumnIndex("idcontact")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getString(cursor.getColumnIndex("number"))
            );
            contacts.add(contact);
            cursor.moveToNext();
        }
        cursor.close();
        return contacts;
    }
}