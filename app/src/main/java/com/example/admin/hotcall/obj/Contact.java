package com.example.admin.hotcall.obj;

import android.graphics.drawable.Drawable;

/**
 * Created by sbt-vasyukov-sv on 22.06.2017 14:51 HotCall.
 * Contact POJO
 */
public class Contact {
    private int id;
    private int idContact;
    private String name;
    private String number;
    private Drawable photo;

    public Contact(int id, int idContact, String name, String number) {
        this.id = id;
        this.idContact = idContact;
        this.name = name;
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return idContact == contact.idContact && name.equals(contact.name) && number.equals(contact.number);
    }

    @Override
    public int hashCode() {
        int result = idContact;
        result = 31 * result + name.hashCode();
        result = 31 * result + number.hashCode();
        return result;
    }

    public int getIdContact() {
        return idContact;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public Drawable getPhoto() {
        return photo;
    }

    public Contact setPhoto(Drawable photo) {
        this.photo = photo;
        return this;
    }
}