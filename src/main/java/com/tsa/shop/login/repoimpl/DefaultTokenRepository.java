package com.tsa.shop.login.repoimpl;

import com.tsa.shop.login.repo.TokenRepository;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultTokenRepository implements TokenRepository {
    private final static String NULL_TOKEN_UUID = "null";
    private final Map<Integer, UUID> tokens = new ConcurrentHashMap<>();

    @Override
    public void add(UUID token) {
        tokens.put(token.hashCode(), token);
    }

    @Override
    public boolean isPresent(String token) {
        if (Objects.equals(token, NULL_TOKEN_UUID)) {
            return false;
        }
        UUID incomeUUID = UUID.fromString(token);
        return tokens.containsKey(incomeUUID.hashCode());
    }
}
