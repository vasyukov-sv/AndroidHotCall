package com.example.admin.hotcall.loader;

import com.example.admin.hotcall.obj.Contact;

public interface AsyncResponse {
    void processContacts(Contact contact);

    void processContactDurationCall(Contact contact);
}
