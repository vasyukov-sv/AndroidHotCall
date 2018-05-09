package com.example.admin.hotcall.loader;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.CallLog;
import com.example.admin.hotcall.common.Utils;
import com.example.admin.hotcall.obj.CallDuration;
import com.example.admin.hotcall.obj.Contact;

public class CallDurationJob extends AsyncTask<Contact, Void, Contact> {
    private static final String[] PROJECTION = new String[]{CallLog.Calls.DURATION, CallLog.Calls.TYPE, CallLog.Calls.DATE};
    private static final String SELECTIONCLAUSE = CallLog.Calls.NUMBER + " = ? AND " + CallLog.Calls.DATE + "> ?";

    private final ContentResolver contentResolver;
    private final AsyncResponse delegate;

    public CallDurationJob(ContentResolver contentResolver, AsyncResponse delegate) {
        this.contentResolver = contentResolver;
        this.delegate = delegate;
    }

    @Override
    protected void onPostExecute(Contact contact) {
        super.onPostExecute(contact);
        delegate.processContactDurationCall(contact);
    }

    @Override
    protected Contact doInBackground(Contact... contacts) {
        String number = contacts[0].getNumber();
        CallDuration callDuration = contacts[0].getDuration();
        long firstDay = Utils.getFirstDayinCurrentMonth();

        long lastSuccessUpdate = 0;
        long allTimeIncomingCall = 0;
        long allTimeOutgoingCall = 0;
        long monthIncomingCall = 0;
        long monthOutgoingCall = 0;

        if (callDuration != null) {
            lastSuccessUpdate = callDuration.getLastSuccess();
            allTimeIncomingCall = callDuration.getAllTimeIncomingCall();
            allTimeOutgoingCall = callDuration.getAllTimeOutgoingCall();
            monthIncomingCall = lastSuccessUpdate > firstDay ? callDuration.getMonthIncomingCall() : 0;
            monthOutgoingCall = lastSuccessUpdate > firstDay ? callDuration.getMonthOutgoingCall() : 0;
        }

        Cursor cursor = contentResolver.query(CallLog.Calls.CONTENT_URI, PROJECTION, SELECTIONCLAUSE, new String[]{number, String.valueOf(lastSuccessUpdate)}, null);
        while (cursor.moveToNext()) {
            long duration = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DURATION));
            int callType = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));

            switch (callType) {
                case CallLog.Calls.INCOMING_TYPE:
                    allTimeIncomingCall += duration;
                    monthIncomingCall += duration;
                    break;
                case CallLog.Calls.OUTGOING_TYPE:
                    allTimeOutgoingCall += duration;
                    monthOutgoingCall += duration;
                    break;
            }
        }
        cursor.close();
        contacts[0].setDuration(new CallDuration(monthIncomingCall, allTimeIncomingCall, monthOutgoingCall, allTimeOutgoingCall, System.currentTimeMillis()));
        return contacts[0];
    }
}