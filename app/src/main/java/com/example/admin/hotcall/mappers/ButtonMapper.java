package com.example.admin.hotcall.mappers;

import com.example.admin.hotcall.Button.RelativeLayoutButton;
import com.example.admin.hotcall.obj.Contact;

public class ButtonMapper {

    private final RelativeLayoutButton relativeLayoutButton;
    private Contact contact;
    private final int id;

    ButtonMapper(int id, RelativeLayoutButton relativeLayoutButton, Contact contact) {
        this.relativeLayoutButton = relativeLayoutButton;
        this.contact = contact;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    RelativeLayoutButton getRelativeLayoutButton() {
        return relativeLayoutButton;
    }

    public Contact getContact() {
        return contact;
    }

    ButtonMapper setContact(Contact contact) {
        this.contact = contact;
        return this;
    }
}
