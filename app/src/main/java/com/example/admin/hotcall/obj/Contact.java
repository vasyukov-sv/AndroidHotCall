package com.example.admin.hotcall.obj;

import android.graphics.Bitmap;

public class Contact {
    private final int id;
    private final int idContact;
    private final String name;
    private final String number;
    private Bitmap photo;

    public Contact(int id, int idContact, String name, String number, Bitmap photo) {
        this.id = id;
        this.idContact = idContact;
        this.name = name;
        this.number = number;
        this.photo = photo;
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

    public Bitmap getPhoto() {
        return photo;
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
}