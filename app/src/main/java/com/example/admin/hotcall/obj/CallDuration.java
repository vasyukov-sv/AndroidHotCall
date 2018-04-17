package com.example.admin.hotcall.obj;

import java.util.Locale;

public class CallDuration {
    private int monthIncomingCall;
    private int allTimeIncomingCall;
    private int monthOutgoingCall;
    private int allTimeOutgoingCall;
    private long lastSuccess;

    public CallDuration(int monthIncomingCall, int allTimeIncomingCall, int monthOutgoingCall, int allTimeOutgoingCall, long lastSuccess) {
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

    public int getMonthIncomingCall() {
        return monthIncomingCall;
    }

    public CallDuration setMonthIncomingCall(int monthIncomingCall) {
        this.monthIncomingCall = monthIncomingCall;
        return this;
    }

    public int getAllTimeIncomingCall() {
        return allTimeIncomingCall;
    }

    public CallDuration setAllTimeIncomingCall(int allTimeIncomingCall) {
        this.allTimeIncomingCall = allTimeIncomingCall;
        return this;
    }

    public int getMonthOutgoingCall() {
        return monthOutgoingCall;
    }

    public CallDuration setMonthOutgoingCall(int monthOutgoingCall) {
        this.monthOutgoingCall = monthOutgoingCall;
        return this;
    }

    public int getAllTimeOutgoingCall() {
        return allTimeOutgoingCall;
    }

    public CallDuration setAllTimeOutgoingCall(int allTimeOutgoingCall) {
        this.allTimeOutgoingCall = allTimeOutgoingCall;
        return this;
    }

    public String getOutgoingCall() {
        return "Исх." + getHMString(getMonthOutgoingCall(), getAllTimeOutgoingCall());
    }

    public String getIncomingCall() {
        return "Вх." + getHMString(getMonthIncomingCall(), getAllTimeIncomingCall());
    }

    private String getHMString(int durationMonth, int durationAlltime) {
        return String.format(Locale.getDefault(), "%d:%02d/ %d:%02d", durationMonth / 3600, durationMonth % 3600 / 60, durationAlltime / 3600, durationAlltime % 3600 / 60);
    }
}