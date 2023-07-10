package com.tsa.shop.domain;

import java.util.Objects;

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

    @Override
    public boolean equals(Object that) {
        if (this == that) return true;
        if (that == null) return false;
        if (!getClass().isAssignableFrom(that.getClass())) return false;
        User user = (User) that;

        return Objects.equals(email, user.email) &&
                Objects.equals(passwordMD5, user.passwordMD5) &&
                Objects.equals(sole, user.sole);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, passwordMD5, sole);
    }
}
