package com.tsa.shop.login.interfaces;

import java.util.UUID;

public interface TokenRepository {
    void add(UUID token);

    boolean isPresent(String token);
}
