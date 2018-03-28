package com.example.admin.hotcall.common;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.example.admin.hotcall.obj.Contact;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sbt-vasyukov-sv on 20.06.2017 14:50 14:52 14:53
 * DBHelper utils
 */

public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper sInstance;


    private SQLiteDatabase db;

    private DBHelper(Context context) {
        super(context, Utils.getApplicationName(context), null, Utils.DATABASE_VERSION);
    }

    public static synchronized DBHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DBHelper(context.getApplicationContext());
        }
        sInstance.setDB(sInstance.getWritableDatabase());
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Utils.TABLE + "(id integer primary key,idcontact integer, name text,number text, photo blob);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Utils.TABLE);
        onCreate(db);
    }

    public void delete(int id) {
        db.delete(Utils.TABLE, "id=" + id, null);
    }

    @SuppressWarnings("unused")
    public void deleteAll() {
        db.delete(Utils.TABLE, null, null);
    }

    public void insert(Contact contact) {
        delete(contact.getId());

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap bitmap = contact.getPhoto();
        if (bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        }

        ContentValues cv = new ContentValues();
        cv.put("id", contact.getId());
        cv.put("idcontact", contact.getIdContact());
        cv.put("name", contact.getName());
        cv.put("number", contact.getNumber());
        cv.put("photo", stream.toByteArray());
        db.insert(Utils.TABLE, null, cv);
    }

    public List<Contact> selectAll() {
        List<Contact> contacts = new ArrayList<>();
        byte[] bitmapdata;
        Cursor cursor = db.query(Utils.TABLE, null, null, null, null, null, "id");
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                bitmapdata = cursor.getBlob(cursor.getColumnIndex("photo"));
                Contact contact = new Contact(cursor.getInt(cursor.getColumnIndex("id")), cursor.getInt(cursor.getColumnIndex("idcontact")), cursor.getString(cursor.getColumnIndex("name")), cursor.getString(cursor.getColumnIndex("number")), BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length));
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

    @SuppressWarnings("unused")
    public Contact getContact(int id) {
        Cursor cursor = db.query(Utils.TABLE, null, "id = " + id, null, null, null, null);
        if (cursor == null) {
            return null;
        }
        cursor.moveToFirst();
        byte[] bitmapdata = cursor.getBlob(cursor.getColumnIndex("photo"));
        Contact contact = new Contact(cursor.getInt(cursor.getColumnIndex("id")), cursor.getInt(cursor.getColumnIndex("idcontact")), cursor.getString(cursor.getColumnIndex("name")), cursor.getString(cursor.getColumnIndex("number")), BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length));
        cursor.close();
        return contact;
    }
}