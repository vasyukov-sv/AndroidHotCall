package com.example.admin.hotcall.loader;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import com.example.admin.hotcall.obj.Contact;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by sbt-vasyukov-sv on 21.06.2017 16:01 HotCall.
 * ContactsJob
 */
public class ContactsJob extends AsyncTask<Integer, Void, Contact> {
    private static final String[] PROJECTION = new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};

    private final Uri contactUri;
    private final ContentResolver contentResolver;
    private final AsyncResponse delegate;

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
    protected Contact doInBackground(Integer... params) {
        Cursor cursor = contentResolver.query(contactUri, PROJECTION, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int idcontact = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            cursor.close();
            InputStream inputStream = openDisplayPhoto(idcontact);


            Bitmap photo = BitmapFactory.decodeStream(inputStream);
//            ImageView imageView = (ImageView) findViewById(R.id.img_contact);
//            imageView.setImageBitmap(photo);




            return new Contact(params[0], idcontact, name, number).setPhoto(photo);
        }
        return null;
    }


    private InputStream openDisplayPhoto(long contactId) {
        InputStream inputStream = null;
        try {
            inputStream = ContactsContract.Contacts.openContactPhotoInputStream(contentResolver, ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId));
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }
}