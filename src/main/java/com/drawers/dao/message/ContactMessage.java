package com.drawers.dao.message;

import com.drawers.dao.utils.Singletons;

public class ContactMessage implements BaseMessage {
    String name;
    String phoneNumber;


    public ContactMessage(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public static ContactMessage fromString(String json) {
        return Singletons.singletonsInstance.gson.fromJson(json, ContactMessage.class);
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String toString() {
        return "MediaMessage [" + "name=" + name
                + ", phoneNumber=" + phoneNumber + "]";
    }

    @Override
    public String toJsonString() {
        return Singletons.singletonsInstance.gson.toJson(this);
    }
}
