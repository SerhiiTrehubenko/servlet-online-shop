package com.tsa.shop.login.repo;

import java.util.UUID;

public interface TokenRepository {
    void add(UUID token);

    boolean isPresent(String token);
}
