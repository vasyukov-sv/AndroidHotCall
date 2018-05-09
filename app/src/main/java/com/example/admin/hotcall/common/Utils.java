package com.example.admin.hotcall.common;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import java.util.Calendar;
import java.util.TimeZone;

public class Utils {
    public static final int PICK_CONTACT_REQUEST = 1;
    public static final int MY_PERMISSIONS_REQUEST = 2;

    static final int DATABASE_VERSION = 5;
    static final String T_CONTACTS = "tContacts";

    public static String getHumanPhone(String phone) {
        return phone == null ? "" : phone.replaceAll("\\+|-|\\s", "").replaceFirst("(\\d)(\\d{3})(\\d{3})(\\d+)", "$1 ($2) $3-$4");
    }

    static String getApplicationName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }

    public static long getFirstDayinCurrentMonth() {
        Calendar localCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        localCalendar.set(Calendar.HOUR_OF_DAY, 0);
        localCalendar.clear(Calendar.MINUTE);
        localCalendar.clear(Calendar.SECOND);
        localCalendar.clear(Calendar.MILLISECOND);
        localCalendar.set(Calendar.DAY_OF_MONTH, 1);
        return localCalendar.getTimeInMillis();
    }
}
