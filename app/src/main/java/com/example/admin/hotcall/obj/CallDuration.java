package com.example.admin.hotcall.obj;

import java.util.Locale;

public class CallDuration {
    private final long monthIncomingCall;
    private final long allTimeIncomingCall;
    private final long monthOutgoingCall;
    private final long allTimeOutgoingCall;
    private final long lastSuccess;

    public CallDuration(long monthIncomingCall, long allTimeIncomingCall, long monthOutgoingCall, long allTimeOutgoingCall, long lastSuccess) {
        this.monthIncomingCall = monthIncomingCall;
        this.allTimeIncomingCall = allTimeIncomingCall;
        this.monthOutgoingCall = monthOutgoingCall;
        this.allTimeOutgoingCall = allTimeOutgoingCall;
        this.lastSuccess = lastSuccess;

    }

    public long getLastSuccess() {
        return lastSuccess;
    }

    public long getMonthIncomingCall() {
        return monthIncomingCall;
    }

    public long getAllTimeIncomingCall() {
        return allTimeIncomingCall;
    }

    public long getMonthOutgoingCall() {
        return monthOutgoingCall;
    }

    public long getAllTimeOutgoingCall() {
        return allTimeOutgoingCall;
    }

    public String getOutgoingCall() {
        return getHMString(getMonthOutgoingCall(), getAllTimeOutgoingCall());
    }

    public String getIncomingCall() {
        return getHMString(getMonthIncomingCall(), getAllTimeIncomingCall());
    }

    private String getHMString(long durationMonth, long durationAlltime) {
        return String.format(Locale.getDefault(), "%d:%02d/ %d:%02d", durationMonth / 3600, durationMonth % 3600 / 60, durationAlltime / 3600, durationAlltime % 3600 / 60);
    }
}