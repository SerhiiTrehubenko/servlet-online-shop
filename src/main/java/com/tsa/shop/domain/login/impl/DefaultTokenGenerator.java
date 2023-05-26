package com.tsa.shop.domain.login.impl;

import com.tsa.shop.domain.login.interfaces.TokenGenerator;

import java.util.UUID;

public class DefaultTokenGenerator implements TokenGenerator {
    @Override
    public UUID generate() {
        return UUID.randomUUID();
    }
}
