package com.tsa.shop.login.repo;

import com.tsa.shop.domain.Session;

public interface TokenRepository {
    void add(Session session);

    boolean isPresent(String token);

    void deleteToken(String value);

    Session getSession(String token);
}
