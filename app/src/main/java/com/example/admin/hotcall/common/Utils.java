package com.example.admin.hotcall.common;

import android.content.Context;
import android.content.pm.ApplicationInfo;

/**
 * Created by sbt-vasyukov-sv on 20.06.2017 14:54 MyApplication7.
 * common Utils
 */
public class Utils {
    public static final int PICK_CONTACT_REQUEST = 1;
    static final int DATABASE_VERSION = 1;
    static final String TABLE = "tContacts";

    public static String getApplicationName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }
}
