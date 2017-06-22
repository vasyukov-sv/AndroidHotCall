package com.example.admin.hotcall.loader;

import com.example.admin.hotcall.obj.Contact;

/**
 * Created by sbt-vasyukov-sv on 22.06.2017 12:44 HotCall.
 * Interface 4 Response async job
 */
public interface AsyncResponse {
    void processContacts(Contact contact);
}
