package com.example.admin.hotcall.common;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import java.util.List;

public class Utils {
    public static final int PICK_CONTACT_REQUEST = 1;
    static final int DATABASE_VERSION = 2;
    static final String TABLE = "tContacts";

    public static <T> T getItemByIndex(List<T> list, int index) {
        return list.size() > index ? list.get(index) : null;
    }

    static String getApplicationName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }
}
