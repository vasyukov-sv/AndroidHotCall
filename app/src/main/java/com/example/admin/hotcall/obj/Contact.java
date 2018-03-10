package com.example.admin.hotcall.obj;

/**
 * Created by sbt-vasyukov-sv on 22.06.2017 14:51 HotCall.
 * Contact POJO
 */
public class Contact {
    private final int id;
    private final int idContact;
    private final String name;
    private final String number;
//    private Bitmap photo;

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

//    public Bitmap getPhoto() {
//        return photo;
//    }
//
//    public Contact setPhoto(Bitmap photo) {
//        this.photo = photo;
//        return this;
//    }
}