package com.tsa.shop.login.repoimpl;

import com.tsa.shop.domain.Session;
import com.tsa.shop.login.repo.TokenRepository;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultTokenRepository implements TokenRepository {
    private final static String NULL_TOKEN_UUID = "null";
    private final Map<Integer, Session> tokens = new ConcurrentHashMap<>();

    @Override
    public void add(Session session) {
        tokens.put(session.token().hashCode(), session);
    }

    @Override
    public boolean isPresent(String token) {
        if (Objects.equals(token, NULL_TOKEN_UUID)) {
            return false;
        }
        UUID incomeUUID = UUID.fromString(token);

        if (tokens.containsKey(incomeUUID.hashCode()) &&
        notExpired(incomeUUID)) {
            return true;
        } else if (tokens.containsKey(incomeUUID.hashCode())) {
            deleteToken(token);
        }
        return false;
    }

    private boolean notExpired(UUID incomeUUID) {
        Session session = tokens.get(incomeUUID.hashCode());
        return session.expireTime() - System.currentTimeMillis() > 0;
    }

    @Override
    public void deleteToken(String value) {
        UUID incomeUUID = UUID.fromString(value);
        tokens.remove(incomeUUID.hashCode());
    }

    @Override
    public Session getSession(String token) {
        UUID incomeUUID = UUID.fromString(token);
        return tokens.get(incomeUUID.hashCode());
    }
}
