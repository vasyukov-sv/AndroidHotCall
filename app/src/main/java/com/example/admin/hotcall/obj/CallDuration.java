package com.example.admin.hotcall.obj;

import java.util.Locale;

public class CallDuration {
    private long monthIncomingCall;
    private long allTimeIncomingCall;
    private long monthOutgoingCall;
    private long allTimeOutgoingCall;
    private long lastSuccess;

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

    public CallDuration setLastSuccess(long lastSuccess) {
        this.lastSuccess = lastSuccess;
        return this;
    }

    public long getMonthIncomingCall() {
        return monthIncomingCall;
    }

    public CallDuration setMonthIncomingCall(long monthIncomingCall) {
        this.monthIncomingCall = monthIncomingCall;
        return this;
    }

    public long getAllTimeIncomingCall() {
        return allTimeIncomingCall;
    }

    public CallDuration setAllTimeIncomingCall(long allTimeIncomingCall) {
        this.allTimeIncomingCall = allTimeIncomingCall;
        return this;
    }

    public long getMonthOutgoingCall() {
        return monthOutgoingCall;
    }

    public CallDuration setMonthOutgoingCall(long monthOutgoingCall) {
        this.monthOutgoingCall = monthOutgoingCall;
        return this;
    }

    public long getAllTimeOutgoingCall() {
        return allTimeOutgoingCall;
    }

    public CallDuration setAllTimeOutgoingCall(long allTimeOutgoingCall) {
        this.allTimeOutgoingCall = allTimeOutgoingCall;
        return this;
    }

    public String getOutgoingCall() {
        return "Исх." + getHMString(getMonthOutgoingCall(), getAllTimeOutgoingCall());
    }

    public String getIncomingCall() {
        return "Вх." + getHMString(getMonthIncomingCall(), getAllTimeIncomingCall());
    }

    private String getHMString(long durationMonth, long durationAlltime) {
        return String.format(Locale.getDefault(), "%d:%02d/ %d:%02d", durationMonth / 3600, durationMonth % 3600 / 60, durationAlltime / 3600, durationAlltime % 3600 / 60);
    }
}