package com.example.admin.hotcall.loader;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.util.Log;
import com.example.admin.hotcall.mappers.ButtonMapper;
import com.example.admin.hotcall.obj.CallDuration;
import com.example.admin.hotcall.obj.Contact;

import java.io.IOException;
import java.io.InputStream;

import static android.content.ContentValues.TAG;

public class ContactsJob extends AsyncTask<ButtonMapper, Void, Contact> {

    private static final String[] PROJECTION = new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.HAS_PHONE_NUMBER};
    private static final String[] PROJECTION_PHONE = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};

    private final Uri contactUri;
    private final ContentResolver contentResolver;
    private final AsyncResponse delegate;

    private String msg;

    public ContactsJob(Uri contactUri, ContentResolver contentResolver, AsyncResponse delegate) {
        this.contactUri = contactUri;
        this.contentResolver = contentResolver;
        this.delegate = delegate;
    }

    @Override
    protected void onPostExecute(Contact contact) {
        super.onPostExecute(contact);
        delegate.processContacts(contact, msg);
    }

    @Override
    protected Contact doInBackground(ButtonMapper... buttonMappers) {
        msg = "";
        String name = null;
        String number = null;
        String contactID = null;
        Boolean hasPhoneNumber = null;

        Cursor cursor = contentResolver.query(contactUri, PROJECTION, null, null, null);
        if (cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            contactID = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            hasPhoneNumber = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0;
        }
        cursor.close();

        if (Boolean.FALSE.equals(hasPhoneNumber)) {
            msg = "У контакта нет телефона";
            return null;
        }
        Cursor cursorPhone = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PROJECTION_PHONE, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " + ContactsContract.CommonDataKinds.Phone.TYPE + " = " + ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                new String[]{contactID}, null);
        if (cursorPhone.moveToFirst()) {
            number = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }
        cursorPhone.close();

        Bitmap photo = retrieveContactPhoto(contactID);
        CallDuration duration = retrieveCallDuration(number);

        return new Contact(buttonMappers[0].getId(), Integer.valueOf(contactID), name, number, photo, duration);
    }

    private CallDuration retrieveCallDuration(String number) {

        String[] projection = new String[]{CallLog.Calls.CACHED_NAME, CallLog.Calls.NUMBER, CallLog.Calls.TYPE, CallLog.Calls.DATE};

        Cursor cursor = contentResolver.query(CallLog.Calls.CONTENT_URI, projection, null, null, null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(0);
            String phonenumber = cursor.getString(1);
            String type = cursor.getString(2); // https://developer.android.com/reference/android/provider/CallLog.Calls.html#TYPE
            String time = cursor.getString(3); // epoch time - https://developer.android.com/reference/java/text/DateFormat.html#parse(java.lang.String

            Log.i(TAG, "retrieveCallDuration: " + phonenumber);
            Log.i(TAG, "retrieveCallDuration: " + type);
            Log.i(TAG, "retrieveCallDuration: " + time);
        }
        cursor.close();

        return new CallDuration(456, 15640, 666, 555, 2);
    }

    private Bitmap retrieveContactPhoto(String contactID) {
        Bitmap photo = null;
        try {
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(contentResolver, ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.valueOf(contactID)));
            if (inputStream != null) {
                photo = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return photo;
    }
}