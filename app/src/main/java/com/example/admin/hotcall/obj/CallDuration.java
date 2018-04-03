package com.example.admin.hotcall.obj;

import java.util.Locale;

public class CallDuration {
    private int monthIncomingCall;
    private int allTimeIncomingCall;
    private int monthOutgoingCall;
    private int allTimeOutgoingCall;

    public CallDuration(int monthIncomingCall, int allTimeIncomingCall, int monthOutgoingCall, int allTimeOutgoingCall) {
        this.monthIncomingCall = monthIncomingCall;
        this.allTimeIncomingCall = allTimeIncomingCall;
        this.monthOutgoingCall = monthOutgoingCall;
        this.allTimeOutgoingCall = allTimeOutgoingCall;
    }

    private int getMonthIncomingCall() {
        return monthIncomingCall;
    }

    public CallDuration setMonthIncomingCall(int monthIncomingCall) {
        this.monthIncomingCall = monthIncomingCall;
        return this;
    }

    private int getAllTimeIncomingCall() {
        return allTimeIncomingCall;
    }

    public CallDuration setAllTimeIncomingCall(int allTimeIncomingCall) {
        this.allTimeIncomingCall = allTimeIncomingCall;
        return this;
    }

    private int getMonthOutgoingCall() {
        return monthOutgoingCall;
    }

    public CallDuration setMonthOutgoingCall(int monthOutgoingCall) {
        this.monthOutgoingCall = monthOutgoingCall;
        return this;
    }

    private int getAllTimeOutgoingCall() {
        return allTimeOutgoingCall;
    }

    public CallDuration setAllTimeOutgoingCall(int allTimeOutgoingCall) {
        this.allTimeOutgoingCall = allTimeOutgoingCall;
        return this;
    }

    public String getOutgoingCall() {
        return getHMString(getMonthOutgoingCall(), getAllTimeOutgoingCall());
    }

    public String getIncomingCall() {
        return getHMString(getMonthIncomingCall(), getAllTimeIncomingCall());
    }

    private String getHMString(int durationMonth, int durationAlltime) {
        return String.format(Locale.getDefault(), "%dh, %d/ %dh, %d", durationMonth / 3600, durationMonth / 60, durationAlltime / 3600, durationAlltime / 60);
    }
}