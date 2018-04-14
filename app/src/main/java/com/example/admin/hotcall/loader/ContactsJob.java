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
import com.example.admin.hotcall.obj.ButtonMapper;
import com.example.admin.hotcall.obj.CallDuration;
import com.example.admin.hotcall.obj.Contact;

import java.io.IOException;
import java.io.InputStream;

import static android.content.ContentValues.TAG;


public class ContactsJob extends AsyncTask<ButtonMapper, Void, Contact> {
//    private static final String[] PROJECTION = new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};

    private final Uri contactUri;
    private final ContentResolver contentResolver;
    private final AsyncResponse delegate;
    private String contactID;

    public ContactsJob(Uri contactUri, ContentResolver contentResolver, AsyncResponse delegate) {
        this.contactUri = contactUri;
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

//        Cursor cursor = contentResolver.query(contactUri, PROJECTION, null, null, null);
//        if (cursor != null && cursor.moveToFirst()) {
//            String idcontact = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
//
//
//            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//            String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//            cursor.close();

        String name = retrieveContactName();
        String number = retrieveContactNumber();


        Bitmap photo = retrieveContactPhoto();
        CallDuration duration = retrieveCallDuration(number);

//            InputStream inputStream = openDisplayPhoto(idcontact);


//            Bitmap photo = BitmapFactory.decodeStream(inputStream);
//            ImageView imageView = (ImageView) findViewById(R.id.img_contact);
//            imageView.setImageBitmap(photo);


//            buttonMappers[0].setContact(new Contact(buttonMappers[0].getId(), idcontact, name, number));

        return new Contact(buttonMappers[0].getId(), Integer.valueOf(contactID), name, number, photo, duration);
    }

    private CallDuration retrieveCallDuration(String number) {

        String[] projection = new String[] {
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.NUMBER,
                CallLog.Calls.TYPE,
                CallLog.Calls.DATE
        };


        Cursor cursor =  contentResolver.query(CallLog.Calls.CONTENT_URI, projection, null, null, null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(0);
            String phonenumber = cursor.getString(1);
            String type = cursor.getString(2); // https://developer.android.com/reference/android/provider/CallLog.Calls.html#TYPE
            String time = cursor.getString(3); // epoch time - https://developer.android.com/reference/java/text/DateFormat.html#parse(java.lang.String

            Log.i(TAG, "retrieveCallDuration: "+phonenumber);
            Log.i(TAG, "retrieveCallDuration: "+type);
            Log.i(TAG, "retrieveCallDuration: "+time);
        }
        cursor.close();

        return new CallDuration(456, 15640, 666, 555,2);
    }

    private Bitmap retrieveContactPhoto() {
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

    private String retrieveContactNumber() {

        String contactNumber = null;

        // getting contacts ID
        Cursor cursorID = contentResolver.query(contactUri, new String[]{ContactsContract.Contacts._ID}, null, null, null);

        if (cursorID.moveToFirst()) {

            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();

        Log.d(TAG, "Contact ID: " + contactID);

        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " + ContactsContract.CommonDataKinds.Phone.TYPE + " = " + ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                new String[]{contactID}, null);

        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }

        cursorPhone.close();

        Log.d(TAG, "Contact Phone Number: " + contactNumber);
        return contactNumber;
    }

    private String retrieveContactName() {

        String contactName = null;

        // querying contact data store
        Cursor cursor = contentResolver.query(contactUri, null, null, null, null);

        if (cursor.moveToFirst()) {

            // DISPLAY_NAME = The display name for the contact.
            // HAS_PHONE_NUMBER =   An indicator of whether this contact has at least one phone number.

            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }

        cursor.close();

        Log.d(TAG, "Contact Name: " + contactName);
        return contactName;
    }

}