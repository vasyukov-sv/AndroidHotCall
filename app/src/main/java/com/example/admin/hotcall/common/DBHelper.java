package com.example.admin.hotcall.common;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.example.admin.hotcall.obj.CallDuration;
import com.example.admin.hotcall.obj.Contact;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

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
        db.execSQL("CREATE TABLE " + Utils.T_CONTACTS + "(id integer primary key,idcontact integer, name text,number text, photo blob, lastDateSync integer,monthIncomingCall integer,allTimeIncomingCall integer,monthOutgoingCall integer,allTimeOutgoingCall integer);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Utils.T_CONTACTS);
        onCreate(db);
    }

    public void delete(int id) {
        db.delete(Utils.T_CONTACTS, "id=" + id, null);
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

        db.insert(Utils.T_CONTACTS, null, cv);
    }

    public List<Contact> selectAll() {
        List<Contact> contacts = new ArrayList<>();
        byte[] bitmapdata;
        Cursor cursor = db.query(Utils.T_CONTACTS, null, null, null, null, null, "id");
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                bitmapdata = cursor.getBlob(cursor.getColumnIndex("photo"));
                CallDuration duration = new CallDuration(
                        cursor.getLong(cursor.getColumnIndex("monthIncomingCall")),
                        cursor.getLong(cursor.getColumnIndex("allTimeIncomingCall")),
                        cursor.getLong(cursor.getColumnIndex("monthOutgoingCall")),
                        cursor.getLong(cursor.getColumnIndex("allTimeOutgoingCall")),
                        cursor.getLong(cursor.getColumnIndex("lastDateSync")));
                Contact contact = new Contact(cursor.getInt(cursor.getColumnIndex("id")), cursor.getInt(cursor.getColumnIndex("idcontact")), cursor.getString(cursor.getColumnIndex("name")), cursor.getString(cursor.getColumnIndex("number")), BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length), duration);
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

    public void updateDuration(Contact contact) {
        CallDuration duration = contact.getDuration();
        ContentValues cv = new ContentValues();
        cv.put("lastDateSync", duration.getLastSuccess());
        cv.put("monthIncomingCall", duration.getMonthIncomingCall());
        cv.put("allTimeIncomingCall", duration.getAllTimeIncomingCall());
        cv.put("monthOutgoingCall", duration.getMonthOutgoingCall());
        cv.put("allTimeOutgoingCall", duration.getAllTimeOutgoingCall());

        db.update(Utils.T_CONTACTS, cv, "id = ?", new String[]{String.valueOf(contact.getId())});
    }
}