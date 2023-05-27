package com.tsa.shop.login.impl;

import com.tsa.shop.login.interfaces.TokenGenerator;

import java.util.UUID;

public class DefaultTokenGenerator implements TokenGenerator {
    @Override
    public UUID generate() {
        return UUID.randomUUID();
    }
}
