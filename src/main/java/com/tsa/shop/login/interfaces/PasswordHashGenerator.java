package com.tsa.shop.login.interfaces;

public interface PasswordHashGenerator {
    String generateMD5(String password, String sole);
}
