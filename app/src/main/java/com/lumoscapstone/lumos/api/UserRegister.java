package com.lumoscapstone.lumos.api;

public class UserRegister {
    private String name;
    private String email;
    private String phoneNumber;
    private String password;

    public UserRegister(String name, String email, String phoneNumber, String password) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }
}
