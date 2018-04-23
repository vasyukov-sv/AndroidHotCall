package com.example.admin.hotcall.obj;

import android.graphics.Bitmap;

public class Contact {
    private final int id;
    private final long idContact;
    private final String name;
    private final String number;
    private Bitmap photo;
    private CallDuration duration;

    public Contact(int id, int idContact, String name, String number, Bitmap photo, CallDuration duration) {
        this.id = id;
        this.idContact = idContact;
        this.name = name;
        this.number = number;
        this.photo = photo;
        this.duration = duration;
    }

    public long getIdContact() {
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
        int result = getId();
        result = 31 * result + (int) (getIdContact() ^ (getIdContact() >>> 32));
        result = 31 * result + getName().hashCode();
        result = 31 * result + getNumber().hashCode();
        result = 31 * result + (getPhoto() != null ? getPhoto().hashCode() : 0);
        result = 31 * result + (getDuration() != null ? getDuration().hashCode() : 0);
        return result;
    }

    public CallDuration getDuration() {
        return duration;
    }

    public Contact setDuration(CallDuration duration) {
        this.duration = duration;
        return this;
    }
}