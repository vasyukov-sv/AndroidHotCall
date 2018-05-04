package com.example.admin.hotcall.loader;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.CallLog;
import com.example.admin.hotcall.common.Utils;
import com.example.admin.hotcall.obj.CallDuration;
import com.example.admin.hotcall.obj.Contact;

public class CallDurationJob extends AsyncTask<Contact, Void, Contact> {
    private final ContentResolver contentResolver;
    private final AsyncResponse delegate;

    public CallDurationJob(ContentResolver contentResolver, AsyncResponse delegate) {
        this.contentResolver = contentResolver;
        this.delegate = delegate;
    }

    @Override
    protected void onPostExecute(Contact contact) {
        super.onPostExecute(contact);
        delegate.processContacts(contact);
    }

    @Override
    protected Contact doInBackground(Contact... contacts) {
        String number = contacts[0].getNumber();
        CallDuration callDuration = contacts[0].getDuration();
        long lastSuccessUpdate = callDuration.getLastSuccess();
        long firstDay = Utils.getFirstDayinCurrentMonth();

        long allTimeIncomingCall = callDuration.getAllTimeIncomingCall();
        long allTimeOutgoingCall = callDuration.getAllTimeOutgoingCall();
        long monthIncomingCall = lastSuccessUpdate > firstDay ? callDuration.getMonthIncomingCall() : 0;
        long monthOutgoingCall = lastSuccessUpdate > firstDay ? callDuration.getMonthOutgoingCall() : 0;

        String[] projection = new String[]{CallLog.Calls.DURATION, CallLog.Calls.TYPE, CallLog.Calls.DATE};
        String selectionClause = CallLog.Calls.NUMBER + " = ?";

        Cursor cursor = contentResolver.query(CallLog.Calls.CONTENT_URI, projection, selectionClause, new String[]{number}, null);
        while (cursor.moveToNext()) {
            long duration = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DURATION));
            int callType = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));

            switch (callType) {
                case CallLog.Calls.INCOMING_TYPE:
                    allTimeIncomingCall+= duration;
                    monthIncomingCall+= duration;
                    break;
                case CallLog.Calls.OUTGOING_TYPE:
                    allTimeOutgoingCall += duration;
                    monthOutgoingCall += duration;
                    break;
            }

        }
        cursor.close();
        contacts[0].setDuration(new CallDuration(monthIncomingCall, allTimeIncomingCall, monthOutgoingCall, allTimeOutgoingCall, System.currentTimeMillis()/1000));
        return contacts[0];
    }
}