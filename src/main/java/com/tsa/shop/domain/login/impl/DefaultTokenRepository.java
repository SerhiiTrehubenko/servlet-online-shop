package com.tsa.shop.domain.login.impl;

import com.tsa.shop.domain.login.interfaces.TokenRepository;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultTokenRepository implements TokenRepository {

    private final Map<Integer, UUID> tokens = new ConcurrentHashMap<>();

    @Override
    public void add(UUID token) {
        tokens.put(token.hashCode(), token);
    }

    @Override
    public boolean isPresent(String token) {
        UUID incomeUUID = UUID.fromString(token);
        return tokens.containsKey(incomeUUID.hashCode());
    }
}
