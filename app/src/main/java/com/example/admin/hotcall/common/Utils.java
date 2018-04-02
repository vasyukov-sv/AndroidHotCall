package com.example.admin.hotcall.common;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import java.util.List;

public class Utils {
    public static final int PICK_CONTACT_REQUEST = 1;
    public static final int PERMISSION_REQUEST_CALL = 0;

    static final int DATABASE_VERSION = 3;
    static final String TABLE = "tContacts";

    public static <T> T getItemByIndex(List<T> list, int index) {
        return list.size() > index ? list.get(index) : null;
    }

    public static String getHumanPhone(String phone) {
        return phone.replaceAll("\\+|-|\\s", "")
                .replaceFirst("(\\d)(\\d{3})(\\d{3})(\\d+)", "$1 ($2) $3-$4");
    }

    static String getApplicationName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }
}
