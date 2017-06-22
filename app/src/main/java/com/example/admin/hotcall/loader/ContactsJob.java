package com.example.admin.hotcall.loader;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import com.example.admin.hotcall.MainActivity;

/**
 * Created by sbt-vasyukov-sv on 21.06.2017 16:01 HotCall.
 * ContactsJob
 */
public class ContactsJob extends AsyncTask<Cursor, Object, String> {
    private static final String[] PROJECTION = new String[]{
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
    };
    private static final String SELECTION = "(" +
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + ContactsContract.Contacts._ID + " AND " +
            ContactsContract.Data.CONTACT_ID + " = " + ContactsContract.Contacts._ID + ")";

    private Uri contactUri;
    private ContentResolver contentResolver;
    private ContentResolver contentResolver;

    public ContactsJob(MainActivity mainActivity, ContentResolver contentResolver, Uri contactUri) {
        this.contentResolver = contentResolver;
        this.contactUri = contactUrid;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        functionCall(s);
    }

    @Override
    protected String doInBackground(Cursor... params) {
        Cursor cursor = contentResolver.query(contactUri, PROJECTION, SELECTION, null, null);
        cursor.moveToFirst();
        String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        cursor.close();
        return number;
    }


}