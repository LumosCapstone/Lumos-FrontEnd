package com.lumoscapstone.lumos.api;

import com.google.gson.annotations.SerializedName;

public class User {
    // TODO: may need to change, based on backend's 20230310-create-init-schema.sql as April 4th, 2023

    // Member Variables
    private int id;
    private String name;
    private String email;
    private String phoneNumber;
    private String passwordHash;

    // JSON object as a string
    @SerializedName("body")
    private String text;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getText() {
        return text;
    }
}
