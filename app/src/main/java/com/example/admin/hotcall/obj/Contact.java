package com.example.admin.hotcall.obj;

/**
 * Created by sbt-vasyukov-sv on 22.06.2017 14:51 HotCall.
 * Contact POJO
 */
public class Contact {
    private int id;
    private int idContact;
    private String name;
    private String number;

    public Contact(int id, int idContact, String name, String number) {
        this.id = id;
        this.idContact = idContact;
        this.name = name;
        this.number = number;
    }

    public int getIdContact() {
        return idContact;
    }

    public void setIdContact(int idContact) {
        this.idContact = idContact;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}