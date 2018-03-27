package com.example.admin.hotcall.obj;

import android.content.Context;
import android.view.View;

public interface MyIntent {
    void makeCall(final String number);

    void chooseContact(View view);

    void registerForContextMenu(View view);

    void unregisterForContextMenu(View view);

    Context getContext();
}