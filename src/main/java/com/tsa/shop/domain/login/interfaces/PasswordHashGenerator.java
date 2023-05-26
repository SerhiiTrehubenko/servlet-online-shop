package com.tsa.shop.domain.login.interfaces;

public interface PasswordHashGenerator {
    String generateMD5(String password, String sole);
}
