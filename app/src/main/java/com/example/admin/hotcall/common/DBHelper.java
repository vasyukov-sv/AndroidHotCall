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

    private SQLiteDatabase db;

    public DBHelper(Context context) {
        super(context, Utils.getApplicationName(context), null, Utils.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Utils.TABLE + "(id integer primary key,idcontact integer, name text,number text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Utils.TABLE);
        onCreate(db);
    }

    public void delete(int id) {
        db.delete(Utils.TABLE, "id=" + id, null);
    }

    public void insert(Contact contact) {
        delete(contact.getId());
        ContentValues cv = new ContentValues();
        cv.put("id", contact.getId());
        cv.put("idcontact", contact.getIdContact());
        cv.put("name", contact.getName());
        cv.put("number", contact.getNumber());
        db.insert(Utils.TABLE, null, cv);
    }

    public List<Contact> selectAll() {
        List<Contact> contacts = new ArrayList<>();
        Cursor cursor = db.query(Utils.TABLE, null, null, null, null, null, null);
        if (cursor != null) {
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
        }
        return contacts;
    }

    public void setDB(SQLiteDatabase db) {
        this.db = db;
    }

    public Contact getContact(int id) {
        Cursor cursor = db.query(Utils.TABLE, null, "id = " + id, null, null, null, null);
        if (cursor == null) {
            return null;
        }
        cursor.moveToFirst();
        Contact contact = new Contact(
                cursor.getInt(cursor.getColumnIndex("id")),
                cursor.getInt(cursor.getColumnIndex("idcontact")),
                cursor.getString(cursor.getColumnIndex("name")),
                cursor.getString(cursor.getColumnIndex("number"))
        );
        cursor.close();
        return contact;
    }
}