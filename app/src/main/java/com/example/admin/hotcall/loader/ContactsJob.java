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
import com.example.admin.hotcall.mappers.ButtonMapper;
import com.example.admin.hotcall.obj.CallDuration;
import com.example.admin.hotcall.obj.Contact;

import java.io.IOException;
import java.io.InputStream;

public class ContactsJob extends AsyncTask<ButtonMapper, Void, Contact> {

    private static final String[] PROJECTION = new String[]{ContactsContract.CommonDataKinds.Phone.CONTACT_ID, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};

    private final Uri uri;
    private final ContentResolver contentResolver;
    private final AsyncResponse delegate;

    public ContactsJob(Uri uri, ContentResolver contentResolver, AsyncResponse delegate) {
        this.uri = uri;
        this.contentResolver = contentResolver;
        this.delegate = delegate;
    }

    @Override
    protected void onPostExecute(Contact contact) {
        super.onPostExecute(contact);
        delegate.processContacts(contact);
    }

    @Override
    protected Contact doInBackground(ButtonMapper... buttonMappers) {

        Cursor cursor = contentResolver.query(uri, PROJECTION, null, null, null);

        if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
        long contactID = cursor.getLong(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
        Bitmap photo = retrieveContactPhoto(contactID);
        CallDuration duration = retrieveCallDuration(number);
        cursor.close();

        return new Contact(buttonMappers[0].getId(), (int) contactID, name, number, photo, duration);
    }

    public CallDuration retrieveCallDuration(String number) {
        long allTimeIncomingCall = 0;
        long allTimeOutgoingCall = 0;

        String[] projection = new String[]{CallLog.Calls.DURATION, CallLog.Calls.TYPE, CallLog.Calls.DATE};
        String selectionClause = CallLog.Calls.NUMBER + " = ?";

        Cursor cursor = contentResolver.query(CallLog.Calls.CONTENT_URI, projection, selectionClause, new String[]{number}, null);
        while (cursor.moveToNext()) {
            long duration = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DURATION));
            int callType = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));

            switch (callType) {
                case CallLog.Calls.INCOMING_TYPE:
                    allTimeIncomingCall += duration;
                    break;
                case CallLog.Calls.OUTGOING_TYPE:
                    allTimeOutgoingCall += duration;
                    break;
            }

        }
        cursor.close();

        return new CallDuration(allTimeIncomingCall, allTimeIncomingCall, allTimeOutgoingCall, allTimeOutgoingCall, 2);
    }

    private Bitmap retrieveContactPhoto(long contactID) {
        Bitmap photo = null;
        try {
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(contentResolver, ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactID));
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