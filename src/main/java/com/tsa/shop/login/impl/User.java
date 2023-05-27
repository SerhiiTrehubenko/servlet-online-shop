package com.tsa.shop.login.impl;

public class User {
    private final String email;
    private final String passwordMD5;
    private final String sole;

    public User(String email, String passwordMD5, String sole) {
        this.email = email;
        this.passwordMD5 = passwordMD5;
        this.sole = sole;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordMD5() {
        return passwordMD5;
    }

    public String getSole() {
        return sole;
    }
}
